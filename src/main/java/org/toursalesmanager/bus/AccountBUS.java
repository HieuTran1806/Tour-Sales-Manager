package org.toursalesmanager.bus;

import org.toursalesmanager.config.AppConfig;
import org.toursalesmanager.dao.AccountDAO;
import org.toursalesmanager.dto.AccountDTO;
import org.toursalesmanager.enums.Role;
import org.toursalesmanager.exception.DataAccessException;
import org.toursalesmanager.exception.ValidationException;
import org.toursalesmanager.gui.helper.PasswordUtil;
import org.toursalesmanager.service.MailService;

import jakarta.mail.MessagingException;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountBUS {
    private final AccountDAO accountDAO;
    private final Map<String, PasswordResetCode> passwordResetCodes = new ConcurrentHashMap<>();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ACTIVE_STATUS = "ACTIVE";
    private final Map<String, Instant> passwordResetLastSentAt = new ConcurrentHashMap<>();

    public AccountBUS() {
        this.accountDAO = new AccountDAO();
    }

    public AccountBUS(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public AccountDTO login(
            String username,
            String password
    ) {
        if (username == null || username.isBlank()) {
            throw new ValidationException(
                    "Tên đăng nhập không được để trống"
            );
        }

        if (password == null || password.isBlank()) {
            throw new ValidationException(
                    "Mật khẩu không được để trống"
            );
        }

        AccountDTO account = accountDAO.loginAccount(username.trim());
        if (account == null) {
            return null;
        }

        String storedPassword = account.getPassword();
        if (PasswordUtil.matches(password, storedPassword)) {
            requireActive(account);
            return account;
        }

        if (!PasswordUtil.isBCryptHash(storedPassword) && password.equals(storedPassword)) {
            requireActive(account);
            String hashedPassword = PasswordUtil.hash(password);
            if (accountDAO.updatePassword(account.getUsername(), hashedPassword)) {
                account.setPassword(hashedPassword);
            }
            return account;
        }
        return null;
    }

    // Chỉ kiểm tra sau khi mật khẩu đúng, để không lộ trạng thái tài khoản cho người đoán mật khẩu.
    // status NULL (tài khoản cũ) vẫn được xem là đang hoạt động.
    private void requireActive(AccountDTO account) {
        String status = account.getStatus();
        if (status != null && !status.trim().equalsIgnoreCase(ACTIVE_STATUS)) {
            throw new ValidationException(
                    "Tài khoản đã bị khóa hoặc ngừng hoạt động. Vui lòng liên hệ quản trị viên."
            );
        }
    }

    // Tạo tài khoản cho nhân viên đã có hồ sơ; trả về mật khẩu ban đầu để báo cho nhân viên
    public String createStaffAccount(
            String idStaff,
            Role role
    ) {
        if (idStaff == null || idStaff.isBlank()) {
            throw new ValidationException(
                    "Mã nhân viên không hợp lệ"
            );
        }

        validateStaffRole(role);

        if (accountDAO.existsByStaffId(idStaff.trim())) {
            throw new ValidationException(
                    "Nhân viên này đã có tài khoản"
            );
        }

        String initialPassword = PasswordUtil.newStaffPassword();
        if (!accountDAO.createStaffAccount(idStaff.trim(), role, initialPassword)) {
            throw new ValidationException("Không thể tạo tài khoản nhân viên");
        }
        return initialPassword;
    }

    public boolean updateStaffRole(
            String idStaff,
            Role role
    ) {
        if (idStaff == null || idStaff.isBlank()) {
            throw new ValidationException(
                    "Mã nhân viên không hợp lệ"
            );
        }

        validateStaffRole(role);

        return accountDAO.updateStaffRole(
                idStaff.trim(),
                role
        );
    }

    private void validateStaffRole(Role role) {
        if (role == null) {
            throw new ValidationException(
                    "Vai trò không được để trống"
            );
        }

        if (role == Role.CUSTOMER) {
            throw new ValidationException(
                    "Không thể gán vai trò CUSTOMER cho nhân viên"
            );
        }
    }

    public boolean registerCustomer(String username, String password, String firstName,
            String lastName, String address, String phoneNumber,
            LocalDate dob, String confirmPassword, String email) {

        String normalizedUsername = username == null ? null : username.trim();
        String normalizedFirstName = firstName == null ? null : firstName.trim();
        String normalizedLastName = lastName == null ? null : lastName.trim();
        String normalizedAddress = address == null ? null : address.trim();
        String normalizedPhone = phoneNumber == null ? null : phoneNumber.trim();
        String normalizedEmail = normalizeEmail(email);

        validateCustomerInfo(normalizedUsername, password, normalizedFirstName, normalizedLastName,
                normalizedAddress, normalizedPhone, dob, confirmPassword, normalizedEmail);

        Role role = Role.CUSTOMER;

        return accountDAO.registerCustomer(
                normalizedUsername,
                password,
                normalizedFirstName,
                normalizedLastName,
                normalizedAddress,
                normalizedPhone,
                dob,
                role,
                normalizedEmail
        );
    }

    public synchronized boolean sendPasswordResetCode(String email) throws MessagingException {
        String normalizedEmail = normalizeEmail(email);
        validateEmail(normalizedEmail);
        if (!accountDAO.customerEmailExists(normalizedEmail)) {
            return false;
        }

        Instant lastSentAt = passwordResetLastSentAt.get(normalizedEmail);
        if (lastSentAt != null
                && Instant.now().isBefore(lastSentAt.plusSeconds(AppConfig.otpResendSeconds()))) {
            long secondsRemaining = AppConfig.otpResendSeconds()
                    - java.time.Duration.between(lastSentAt, Instant.now()).getSeconds();
            throw new IllegalArgumentException("Vui lòng đợi " + secondsRemaining + " giây trước khi gửi lại OTP");
        }

        String code = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
        MailService.sendOtp(normalizedEmail, code);
        passwordResetCodes.put(normalizedEmail, new PasswordResetCode(
                code,
                Instant.now().plusSeconds(AppConfig.otpLifetimeMinutes() * 60L),
                0
        ));
        passwordResetLastSentAt.put(normalizedEmail, Instant.now());
        return true;
    }

    public void verifyPasswordResetCode(String email, String code) {
        String normalizedEmail = normalizeEmail(email);
        validateEmail(normalizedEmail);
        getValidPasswordResetCode(normalizedEmail, code);
    }

    public boolean resetCustomerPassword(String email, String code, String newPassword, String confirmPassword) {
        String normalizedEmail = normalizeEmail(email);
        validateEmail(normalizedEmail);
        validateNewPassword(newPassword, confirmPassword);

        PasswordResetCode resetCode = getValidPasswordResetCode(normalizedEmail, code);
        boolean updated = accountDAO.updateCustomerPasswordByEmail(
                normalizedEmail,
                PasswordUtil.hash(newPassword)
        );
        if (updated) {
            passwordResetCodes.remove(normalizedEmail, resetCode);
            passwordResetLastSentAt.remove(normalizedEmail);
        }
        return updated;
    }

    private PasswordResetCode getValidPasswordResetCode(String normalizedEmail, String code) {
        PasswordResetCode resetCode = passwordResetCodes.get(normalizedEmail);
        if (resetCode == null) {
            throw new IllegalArgumentException("Mã xác nhận không hợp lệ hoặc đã hết hạn");
        }
        if (Instant.now().isAfter(resetCode.expiresAt())) {
            passwordResetCodes.remove(normalizedEmail, resetCode);
            throw new IllegalArgumentException("Mã xác nhận đã hết hạn. Vui lòng yêu cầu mã mới");
        }
        if (code == null || !resetCode.code().equals(code.trim())) {
            if (resetCode.failedAttempts() + 1 >= AppConfig.otpMaxAttempts()) {
                passwordResetCodes.remove(normalizedEmail, resetCode);
                throw new IllegalArgumentException("Bạn đã nhập sai mã quá nhiều lần. Vui lòng yêu cầu mã mới");
            }
            passwordResetCodes.replace(normalizedEmail, resetCode,
                    new PasswordResetCode(resetCode.code(), resetCode.expiresAt(), resetCode.failedAttempts() + 1));
            throw new IllegalArgumentException("Mã xác nhận không chính xác");
        }
        return resetCode;
    }

    private void validateCustomerInfo(String username, String password, String firstName, String lastName,
                                      String address, String phoneNumber, LocalDate dob, String confirmPassword,
                                      String email) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Tên tài khoản không được để trống");
        }
        if (username.length() > 30) {
            throw new ValidationException("Tên tài khoản không được dài quá 30 ký tự");
        }

        if (accountDAO.existsByUsername(username)) {
            throw new ValidationException(
                    "Tên tài khoản đã tồn tại"
            );
        }
        validateEmail(email);
        if (accountDAO.emailExists(email)) {
            throw new ValidationException("Email này đã được sử dụng");
        }

        if (password == null || password.isBlank()) {
            throw new ValidationException("Mật khẩu không được để trống");
        }

        if (confirmPassword == null || !password.equals(confirmPassword)) {
            throw new ValidationException(
                    "Mật khẩu xác nhận không khớp"
            );
        }
        if (password.length() < 6) {
            throw new ValidationException(
                    "Mật khẩu phải có ít nhất 6 ký tự"
            );
        }
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ValidationException("Mật khẩu không được vượt quá 72 byte UTF-8");
        }

        if (firstName == null || firstName.isBlank()) {
            throw new ValidationException("Tên không được để trống");
        }
        if (firstName.length() > 45) {
            throw new ValidationException("Tên không được dài quá 45 ký tự");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new ValidationException("Họ không được để trống");
        }
        if (lastName.length() > 45) {
            throw new ValidationException("Họ không được dài quá 45 ký tự");
        }

        if (address == null || address.isBlank()) {
            throw new ValidationException("Địa chỉ không được để trống");
        }
        if (address.length() > 255) {
            throw new ValidationException("Địa chỉ không được dài quá 255 ký tự");
        }

        if (phoneNumber == null || !phoneNumber.matches("^0\\d{9}$")) {
            throw new ValidationException("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0");
        }
        if (isPhoneExisted(phoneNumber)) {
            throw new ValidationException("Số điện thoại này đã được sử dụng");
        }

        if (dob == null) {
            throw new ValidationException("Ngày sinh không được để trống");
        }
        if (dob.isAfter(LocalDate.now())) {
            throw new ValidationException("Ngày sinh không được sau ngày hiện tại");
        }
    }

    private void validateNewPassword(String password, String confirmPassword) {
        if (password == null || password.isBlank()) {
            throw new ValidationException("Mật khẩu không được để trống");
        }
        if (confirmPassword == null || !password.equals(confirmPassword)) {
            throw new ValidationException("Mật khẩu xác nhận không khớp");
        }
        if (password.length() < 6) {
            throw new ValidationException("Mật khẩu phải có ít nhất 6 ký tự");
        }
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
               throw new ValidationException("Mật khẩu không được vượt quá 72 byte UTF-8");      }
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    private void validateEmail(String email) {
        if (email == null || email.length() > 254 || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new ValidationException("Email không hợp lệ");
        }
    }

    private record PasswordResetCode(String code, Instant expiresAt, int failedAttempts) {
    }

    private boolean isPhoneExisted(String phoneNumber) {
        return accountDAO.isPhoneExisted(phoneNumber);
    }

}
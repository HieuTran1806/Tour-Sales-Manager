package org.toursalesmanager.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Nơi duy nhất đọc config.properties. File được đọc một lần (UTF-8) khi dùng lần đầu,
 * sửa file thì phải mở lại ứng dụng.
 *
 * Thứ tự tìm config.properties:
 * 1. Cạnh file jar (khi chạy bằng java -jar TourSalesManager.jar)
 * 2. Thư mục đang chạy lệnh
 * 3. Trong classpath, tức src/main/resources (khi chạy từ IntelliJ)
 *
 * Mỗi key đều có giá trị mặc định ở đây, nên thiếu key thì ứng dụng vẫn chạy như cũ
 * (trừ db.url / db.username là bắt buộc). Danh sách key xem config.properties.example.
 */
public final class AppConfig {
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String CONFIG_RESOURCE = "/" + CONFIG_FILE_NAME;

    private static volatile Properties properties;

    private AppConfig() {
    }

    // ===================== Ứng dụng / đơn vị =====================

    public static String appName() {
        return get("app.name", "Quản lý Tour du lịch");
    }

    public static String companyHotline() {
        return get("company.hotline", "");
    }

    public static String companyEmail() {
        return get("company.email", "");
    }

    // Dòng liên hệ in cuối email gửi khách, rỗng nếu chưa cấu hình
    public static String contactLine() {
        List<String> parts = new ArrayList<>();
        if (!companyHotline().isEmpty()) {
            parts.add("Hotline: " + companyHotline());
        }
        if (!companyEmail().isEmpty()) {
            parts.add("Email: " + companyEmail());
        }
        return String.join(" | ", parts);
    }

    // ===================== CSDL =====================

    public static String dbUrl() {
        return require("db.url");
    }

    public static String dbUsername() {
        return require("db.username");
    }

    public static String dbPassword() {
        return secret("db.password", "DB_PASSWORD");
    }

    // ===================== Email =====================

    public static String mailHost() {
        return get("mail.smtp.host", "smtp.gmail.com");
    }

    public static int mailPort() {
        return getPositiveInt("mail.smtp.port", 587);
    }

    public static boolean mailStartTls() {
        return getBoolean("mail.smtp.starttls", true);
    }

    public static int mailTimeoutMillis() {
        return getPositiveInt("mail.timeout.ms", 10_000);
    }

    // Biến môi trường được ưu tiên để không phải ghi mật khẩu vào file
    public static String mailUsername() {
        return secret("mail.username", "MAIL_USERNAME");
    }

    public static String mailAppPassword() {
        return secret("mail.app.password", "MAIL_APP_PASSWORD");
    }

    // ===================== OTP đặt lại mật khẩu =====================

    public static int otpLifetimeMinutes() {
        return getPositiveInt("otp.lifetime.minutes", 5);
    }

    public static int otpResendSeconds() {
        return getPositiveInt("otp.resend.seconds", 60);
    }

    public static int otpMaxAttempts() {
        return getPositiveInt("otp.max.attempts", 5);
    }

    // ===================== Tài khoản / bảo mật =====================

    // Rỗng nghĩa là mỗi tài khoản nhân viên mới được sinh mật khẩu ngẫu nhiên
    public static String defaultStaffPassword() {
        return get("account.default.password", "");
    }

    // BCrypt chỉ nhận 4..31; càng lớn càng an toàn nhưng đăng nhập càng chậm
    public static int bcryptRounds() {
        int rounds = getPositiveInt("security.bcrypt.rounds", 10);
        return Math.max(4, Math.min(rounds, 31));
    }

    // ===================== Thanh toán =====================

    public static String bankBin() {
        return get("payment.bank.bin", "");
    }

    public static String bankName() {
        return get("payment.bank.name", "");
    }

    public static String bankAccountNo() {
        return get("payment.bank.accountNo", "");
    }

    public static String bankAccountName() {
        return get("payment.bank.accountName", "");
    }

    // Nội dung chuyển khoản tối đa 25 ký tự, nên tiền tố chỉ nhận chữ/số không dấu, tối đa 6 ký tự
    public static String paymentTransferPrefix() {
        String prefix = get("payment.transfer.prefix", "TSM").toUpperCase();
        return prefix.matches("[A-Z0-9]{1,6}") ? prefix : "TSM";
    }

    public static int paymentQrExpireMinutes() {
        return getPositiveInt("payment.qr.expire.minutes", 15);
    }

    public static int paymentDeadlineHours() {
        return getPositiveInt("payment.deadline.hours", 48);
    }

    // ===================== Đọc giá trị chung =====================

    public static String get(String key, String defaultValue) {
        String value = load().getProperty(key);
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }

    public static String require(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new IllegalStateException("Thiếu thuộc tính " + key + " trong config.properties");
        }
        return value;
    }

    public static int getPositiveInt(String key, int defaultValue) {
        String value = get(key, null);
        if (value == null) {
            return defaultValue;
        }
        try {
            int number = Integer.parseInt(value);
            if (number > 0) {
                return number;
            }
        } catch (NumberFormatException ignored) {
            // rơi xuống cảnh báo bên dưới
        }
        System.err.println("config.properties: " + key + "=" + value
                + " không phải số nguyên dương, dùng mặc định " + defaultValue
                + " (lưu ý: file .properties không hỗ trợ comment cùng dòng)");
        return defaultValue;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key, null);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    // Giá trị bí mật: biến môi trường (nếu có) ưu tiên hơn file config
    public static String secret(String key, String environmentVariable) {
        String fromEnv = System.getenv(environmentVariable);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv.trim();
        }
        return get(key, "");
    }

    // Bản sao toàn bộ cấu hình, cho code cũ cần đọc trực tiếp
    public static Properties properties() {
        Properties copy = new Properties();
        copy.putAll(load());
        return copy;
    }

    private static Properties load() {
        Properties loaded = properties;
        if (loaded == null) {
            synchronized (AppConfig.class) {
                loaded = properties;
                if (loaded == null) {
                    loaded = readConfig();
                    properties = loaded;
                }
            }
        }
        return loaded;
    }

    private static Properties readConfig() {
        Properties config = new Properties();

        Path externalConfig = findExternalConfig();
        if (externalConfig != null) {
            try (Reader reader = Files.newBufferedReader(externalConfig, StandardCharsets.UTF_8)) {
                config.load(reader);
                return config;
            } catch (IOException e) {
                throw new RuntimeException("Không thể đọc " + externalConfig.toAbsolutePath(), e);
            }
        }

        try (InputStream input = AppConfig.class.getResourceAsStream(CONFIG_RESOURCE)) {
            if (input == null) {
                throw new IllegalStateException(
                        "Không tìm thấy config.properties. Khi chạy từ IDE: sao chép "
                                + "config.properties.example thành config.properties trong "
                                + "src/main/resources. Khi chạy file jar: đặt config.properties "
                                + "cùng thư mục với file jar."
                );
            }
            config.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            return config;
        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc config.properties", e);
        }
    }

    private static Path findExternalConfig() {
        List<Path> candidates = new ArrayList<>();

        try {
            Path codeSource = Paths.get(
                    AppConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            // Chạy từ IDE thì codeSource là thư mục target/classes, chỉ xét khi đang chạy từ file jar
            if (Files.isRegularFile(codeSource)) {
                candidates.add(codeSource.resolveSibling(CONFIG_FILE_NAME));
            }
        } catch (URISyntaxException | SecurityException | NullPointerException e) {
            // Không xác định được vị trí jar, bỏ qua
        }
        candidates.add(Paths.get(CONFIG_FILE_NAME));

        for (Path candidate : candidates) {
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        return null;
    }
}

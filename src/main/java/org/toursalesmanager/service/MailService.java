package org.toursalesmanager.service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.toursalesmanager.config.AppConfig;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class MailService {
    public static void sendOtp(String to, String otp) throws MessagingException {
        send(to, AppConfig.appName() + " - Mã OTP đặt lại mật khẩu",
                "Mã OTP đặt lại mật khẩu của bạn là: " + otp
                        + "\nMã có hiệu lực trong " + AppConfig.otpLifetimeMinutes() + " phút.");
    }

    // Gửi email; tự thêm dòng liên hệ của đơn vị (company.hotline / company.email) ở cuối nếu có
    public static void send(String to, String subject, String body) throws MessagingException {
        String from = requireValue(AppConfig.mailUsername(), "MAIL_USERNAME", "mail.username");
        String appPassword = requireValue(AppConfig.mailAppPassword(), "MAIL_APP_PASSWORD", "mail.app.password");
        String timeout = String.valueOf(AppConfig.mailTimeoutMillis());

        Properties p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", String.valueOf(AppConfig.mailStartTls()));
        p.put("mail.smtp.host", AppConfig.mailHost());
        p.put("mail.smtp.port", String.valueOf(AppConfig.mailPort()));
        p.put("mail.smtp.connectiontimeout", timeout);
        p.put("mail.smtp.timeout", timeout);
        p.put("mail.smtp.writetimeout", timeout);

        Session session = Session.getInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, appPassword);
            }
        });

        String contact = AppConfig.contactLine();
        String fullBody = contact.isEmpty() ? body : body + "\n\n--\n" + AppConfig.appName() + "\n" + contact;

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject(subject, StandardCharsets.UTF_8.name());
        msg.setText(fullBody, StandardCharsets.UTF_8.name());
        Transport.send(msg);
    }

    private static String requireValue(String value, String environmentVariable, String configKey) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Chưa cấu hình biến môi trường " + environmentVariable
                    + " hoặc " + configKey + " trong config.properties");
        }
        return value;
    }
}

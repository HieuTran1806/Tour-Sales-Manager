package org.toursalesmanager.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Tạo mã QR chuyển khoản theo chuẩn VietQR (NAPAS, dựa trên EMVCo).
 * Ứng dụng ngân hàng quét mã sẽ điền sẵn tài khoản nhận, số tiền và nội dung chuyển khoản.
 */
public final class VietQrService {
    private static final String NAPAS_GUID = "A000000727";
    private static final String SERVICE_TRANSFER_TO_ACCOUNT = "QRIBFTTA";
    private static final String CURRENCY_VND = "704";
    private static final String COUNTRY_VN = "VN";

    private VietQrService() {
    }

    public static String buildPayload(String bankBin, String accountNo, BigDecimal amount, String content) {
        if (bankBin == null || !bankBin.matches("\\d{6}")) {
            throw new IllegalArgumentException("Mã BIN ngân hàng phải gồm 6 chữ số");
        }
        if (accountNo == null || !accountNo.matches("[0-9A-Za-z]{1,19}")) {
            throw new IllegalArgumentException("Số tài khoản nhận thanh toán không hợp lệ");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0");
        }

        String beneficiary = tlv("00", bankBin) + tlv("01", accountNo);
        String merchantInfo = tlv("00", NAPAS_GUID) + tlv("01", beneficiary) + tlv("02", SERVICE_TRANSFER_TO_ACCOUNT);

        StringBuilder payload = new StringBuilder()
                .append(tlv("00", "01"))
                .append(tlv("01", "12"))            // QR động, dùng một lần cho một giao dịch
                .append(tlv("38", merchantInfo))
                .append(tlv("53", CURRENCY_VND))
                .append(tlv("54", amount.setScale(0, java.math.RoundingMode.UNNECESSARY).toPlainString()))
                .append(tlv("58", COUNTRY_VN));
        if (content != null && !content.isBlank()) {
            payload.append(tlv("62", tlv("08", toAscii(content))));
        }
        payload.append("6304");
        return payload.append(crc16(payload.toString())).toString();
    }

    public static BufferedImage toImage(String payload, int size) {
        try {
            BitMatrix matrix = new QRCodeWriter().encode(payload, BarcodeFormat.QR_CODE, size, size, Map.of(
                    EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M,
                    EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name(),
                    EncodeHintType.MARGIN, 2
            ));
            BufferedImage image = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < matrix.getWidth(); x++) {
                for (int y = 0; y < matrix.getHeight(); y++) {
                    image.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }
            return image;
        } catch (WriterException e) {
            throw new IllegalStateException("Không tạo được ảnh mã QR: " + e.getMessage(), e);
        }
    }

    private static String tlv(String id, String value) {
        if (value.length() > 99) {
            throw new IllegalArgumentException("Giá trị trường " + id + " của mã QR quá dài");
        }
        return id + String.format("%02d", value.length()) + value;
    }

    // Nội dung chuyển khoản chỉ nên chứa chữ không dấu, số và khoảng trắng để ngân hàng không cắt bỏ
    private static String toAscii(String text) {
        String normalized = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace('đ', 'd').replace('Đ', 'D')
                .replaceAll("[^A-Za-z0-9 ]", "");
        return normalized.length() > 25 ? normalized.substring(0, 25) : normalized;
    }

    // CRC-16/CCITT-FALSE (poly 0x1021, init 0xFFFF) theo chuẩn EMVCo
    static String crc16(String data) {
        int crc = 0xFFFF;
        for (byte b : data.getBytes(StandardCharsets.US_ASCII)) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                crc = (crc & 0x8000) != 0 ? (crc << 1) ^ 0x1021 : crc << 1;
                crc &= 0xFFFF;
            }
        }
        return String.format("%04X", crc);
    }
}

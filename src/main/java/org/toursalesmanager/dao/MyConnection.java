package org.toursalesmanager.dao;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MyConnection {
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String CONFIG_RESOURCE = "/" + CONFIG_FILE_NAME;

    public static Connection getConnection() {
        Properties config = loadConfig();
        String url = requireProperty(config, "db.url");
        String username = requireProperty(config, "db.username");
        String password = config.getProperty("db.password", "");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Không thể kết nối cơ sở dữ liệu", e);
        }
    }

    /**
     * Thứ tự tìm config.properties:
     * 1. Cạnh file jar (khi chạy bằng java -jar TourSalesManager.jar)
     * 2. Thư mục đang chạy lệnh
     * 3. Trong classpath, tức src/main/resources (khi chạy từ IntelliJ)
     */
    private static Properties loadConfig() {
        Properties config = new Properties();

        Path externalConfig = findExternalConfig();
        if (externalConfig != null) {
            try (InputStream input = Files.newInputStream(externalConfig)) {
                config.load(input);
                return config;
            } catch (IOException e) {
                throw new RuntimeException("Không thể đọc " + externalConfig.toAbsolutePath(), e);
            }
        }

        try (InputStream input = MyConnection.class.getResourceAsStream(CONFIG_RESOURCE)) {
            if (input == null) {
                throw new IllegalStateException(
                        "Không tìm thấy config.properties. Khi chạy từ IDE: sao chép "
                                + "config.properties.example thành config.properties trong "
                                + "src/main/resources. Khi chạy file jar: đặt config.properties "
                                + "cùng thư mục với file jar."
                );
            }
            config.load(input);
            return config;
        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc config.properties", e);
        }
    }

    private static Path findExternalConfig() {
        List<Path> candidates = new ArrayList<>();

        try {
            Path codeSource = Paths.get(
                    MyConnection.class.getProtectionDomain().getCodeSource().getLocation().toURI()
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

    private static String requireProperty(Properties config, String key) {
        String value = config.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Thiếu thuộc tính " + key + " trong config.properties"
            );
        }
        return value.trim();
    }

    public static void closeConnection(Connection c){
        try {
            if(c != null)
                c.close();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

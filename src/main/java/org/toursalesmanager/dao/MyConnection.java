package org.toursalesmanager.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyConnection {
    private static final String CONFIG_RESOURCE = "/config.properties";

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

    private static Properties loadConfig() {
        Properties config = new Properties();
        try (InputStream input = MyConnection.class.getResourceAsStream(CONFIG_RESOURCE)) {
            if (input == null) {
                throw new IllegalStateException(
                        "Không tìm thấy config.properties. Hãy sao chép "
                                + "config.properties.example thành config.properties "
                                + "trong src/main/resources và điền thông tin kết nối."
                );
            }
            config.load(input);
            return config;
        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc config.properties", e);
        }
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

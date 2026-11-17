package org.toursalesmanager.dao;

import org.toursalesmanager.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyConnection {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(AppConfig.dbUrl(), AppConfig.dbUsername(), AppConfig.dbPassword());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Không thể kết nối cơ sở dữ liệu", e);
        }
    }

    /**
     * Toàn bộ config.properties. Code mới nên dùng các hàm có kiểu trong {@link AppConfig}
     * (ví dụ AppConfig.bankBin()) thay vì tự đọc key.
     */
    public static Properties getConfig() {
        return AppConfig.properties();
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

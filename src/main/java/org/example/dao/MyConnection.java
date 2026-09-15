package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    public static Connection getConnection(){
        Connection c = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mySQL://localhost:3306/tour";
            String username = "root";
            String password = "root123";

            c = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return c;
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

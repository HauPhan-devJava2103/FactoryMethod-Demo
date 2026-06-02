package vn.com.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/factory_method";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin123";


    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Thực hiện kết nối
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Kết nối Database thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy Driver: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối Database: " + e.getMessage());
        }
        return connection;
    }
}
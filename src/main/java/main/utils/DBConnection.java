package main.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    private static String dbName = "skillbox";
    private static String dbUser = "root";
    private static String dbPass = "Wshulck1!";

    private String sql;

    public DBConnection(String sql) {
        this.sql = sql;
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/" + dbName +
                                "?user=" + dbUser + "&password=" + dbPass);
                connection.createStatement().execute("DROP TABLE IF EXISTS page");
                connection.createStatement().execute("CREATE TABLE page(" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "path TEXT NOT NULL, " +
                        "code INT NOT NULL, " +
                        "content MEDIUMTEXT NOT NULL, " +
//                        "PRIMARY KEY(id))");
                        "PRIMARY KEY(id), UNIQUE KEY(path(200)))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static Connection getConnectionAfter() throws SQLException {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/" + dbName +
                                "?user=" + dbUser + "&password=" + dbPass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void insertQuery(String sqlQuery) {
        String sql = "INSERT INTO page(path, code, content) " +
                "VALUES" + sqlQuery + " ON DUPLICATE KEY UPDATE id = id";
        try {
            DBConnection.getConnectionAfter().createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

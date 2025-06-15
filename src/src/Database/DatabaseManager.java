/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.*;

/**
 *
 * @author alkim
 */

public final class DatabaseManager {

    private static final String DB_URL = "jdbc:derby:universityDB;create=true";


    private Connection conn;

    public DatabaseManager() throws SQLException {
        loadDriver();
        establishConnection();
        initializeSchema();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void loadDriver() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Derby Embedded Driver not found!", e);
        }
    }

    public void establishConnection() throws SQLException {
        this.conn = DriverManager.getConnection(DB_URL);
    }

    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public ResultSet queryDB(String sql) {
        try (Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public void updateDB(String sql) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void dropTableIfExists(String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(null, null, tableName.toUpperCase(), null)) {
            if (rs.next()) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("DROP TABLE " + tableName);
                }
            }
        }
    }

    private void initializeSchema() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                "CREATE TABLE USERS (" +
                "  id INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
                "  firstName VARCHAR(100), " +
                "  lastName VARCHAR(100), " +
                "  password VARCHAR(100), " +
                "  role VARCHAR(20), " +
                "  major VARCHAR(100), " +
                "  pending VARCHAR(1000), " +
                "  approved VARCHAR(1000)" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE Majors (" +
                "name VARCHAR(100) PRIMARY KEY)"
            );

            stmt.executeUpdate(
                "CREATE TABLE Courses (" +
                "name VARCHAR(100), " +
                "major_name VARCHAR(100) REFERENCES Majors(name))"
            );
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                e.printStackTrace();
            }
        }
    }
}

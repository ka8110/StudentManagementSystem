/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alkim
 */
public class DatabaseManagerTest {
    
    private DatabaseManager dbManager;

    @Before
    public void setUp() throws SQLException {
        dbManager = new DatabaseManager();
    }

    @After
    public void tearDown() {
        dbManager.closeConnections();
    }

    /**
     * Test of getConnection method, of class DatabaseManager.
     */
    @Test
    public void testGetConnection() throws SQLException {
        Connection conn = dbManager.getConnection();
        assertNotNull("Connection should not be null", conn);
        assertFalse("Connection should be open", conn.isClosed());
        conn.close();
    }

    /**
     * Test of establishConnection method, of class DatabaseManager.
     */
    @Test
    public void testEstablishConnection() throws SQLException {
        dbManager.establishConnection();
        assertNotNull("Connection should not be null after establishConnection", dbManager.getConnection());
    }

    /**
     * Test of closeConnections method, of class DatabaseManager.
     */
    @Test
    public void testCloseConnections() throws SQLException {
        dbManager.establishConnection();
        dbManager.closeConnections();
        // Cannot directly test private conn field, but calling again getConnection returns new conn
        Connection conn = dbManager.getConnection();
        assertNotNull(conn);
        conn.close();
    }
    /**
     * Test of queryDB method, of class DatabaseManager.
     */
    @Test
    public void testQueryDB() throws SQLException {
        // Insert a test user
        dbManager.updateDB("INSERT INTO USERS (firstName, lastName, password, role, major, pending, approved) " +
                           "VALUES ('Test', 'User', 'pass', 'student', 'CS', '', '')");
        ResultSet rs = dbManager.queryDB("SELECT * FROM USERS WHERE firstName='Test'");
        assertNotNull(rs);
        assertTrue(rs.next());
        assertEquals("Test", rs.getString("firstName"));
    }

    /**
     * Test of updateDB method, of class DatabaseManager.
     */
    @Test
    public void testUpdateDB() throws SQLException {
        // Insert new user
        dbManager.updateDB("INSERT INTO USERS (firstName, lastName, password, role, major, pending, approved) " +
                           "VALUES ('Update', 'Test', 'pass', 'student', 'CS', '', '')");
        // Update user lastName
        dbManager.updateDB("UPDATE USERS SET lastName='Changed' WHERE firstName='Update'");
        ResultSet rs = dbManager.queryDB("SELECT lastName FROM USERS WHERE firstName='Update'");
        assertTrue(rs.next());
        assertEquals("Changed", rs.getString("lastName"));
    }

    /**
     * Test of dropTableIfExists method, of class DatabaseManager.
     */
    @Test
    public void testDropTableIfExists() throws SQLException {
        // Create a temporary table
        dbManager.updateDB("CREATE TABLE TempTable (id INT)");

        // Ensure it exists
        ResultSet rs1 = dbManager.queryDB("SELECT * FROM SYS.SYSTABLES WHERE TABLENAME='TEMPTABLE'");
        assertTrue(rs1.next());

        // Drop the table
        dbManager.dropTableIfExists("TempTable");

        // Check it no longer exists
        ResultSet rs2 = dbManager.queryDB("SELECT * FROM SYS.SYSTABLES WHERE TABLENAME='TEMPTABLE'");
        assertFalse(rs2.next());
    }
    
}

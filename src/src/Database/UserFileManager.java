/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

/**
 *
 * @author alkim
 */
import Model.Course;
import Model.Lecturer;
import Model.Major;
import Model.Student;
import Model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserFileManager {
    private static final String TBL = "USERS";
    private final DatabaseManager dbm;
    
    /** Constructor injection of your DatabaseManager
     * @param dbm */
    public UserFileManager(DatabaseManager dbm) {
        this.dbm = dbm;
    }

    /**
     * Load all users from the database, mapping result-set rows directly to domain objects.
     * @return 
     */
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM " + TBL + " ORDER BY id";

        try (Connection conn = dbm.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id       = rs.getInt("id");
                String first = rs.getString("firstName");
                String last  = rs.getString("lastName");
                String pass  = rs.getString("password");
                String role  = rs.getString("role");

                if ("STUDENT".equalsIgnoreCase(role)) {
                    String majorName = rs.getString("major");
                    Major major = (majorName != null && !majorName.isBlank()) ? new Major(majorName, new ArrayList<>()): null;
                    Student student = new Student(id, first, last, pass, major);
                    populateCourseList(rs.getString("pending"), student.getPendingCourses());
                    populateCourseList(rs.getString("approved"), student.getApprovedCourses());
                    users.add(student);

                } else if ("LECTURER".equalsIgnoreCase(role)) {
                    users.add(new Lecturer(id,first, last, pass));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    /**
     * Insert a new User into the database.
     * For students, their ID will be set on the object after insertion.
     */
    public void appendUser(User u) {
        String sql = "INSERT INTO " + TBL + " (firstName, lastName, password, role, major, pending, approved) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getPassword());

            if (u instanceof Student s) {
                ps.setString(4, "STUDENT");
                ps.setString(5, s.getMajor() != null ? s.getMajor().getName() : "");
                ps.setString(6, joinCourses(s.getPendingCourses()));
                ps.setString(7, joinCourses(s.getApprovedCourses()));
            } else {
                ps.setString(4, "LECTURER");
                ps.setString(5, "");
                ps.setString(6, "");
                ps.setString(7, "");
            }

            ps.executeUpdate();

            // Retrieve and set generated ID
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    u.setId(generatedId);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }

    /**
     * Update an existing User's data in the database.
     */
    public void updateUser(User u) {
        String sql = "UPDATE " + TBL + " SET firstName=?, lastName=?, password=?, role=?, major=?, pending=?, approved=? WHERE id=?";
        try (Connection conn = dbm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getPassword());

            if (u instanceof Student s) {
                ps.setString(4, "STUDENT");
                ps.setString(5, s.getMajor() != null ? s.getMajor().getName() : "");
                ps.setString(6, joinCourses(s.getPendingCourses()));
                ps.setString(7, joinCourses(s.getApprovedCourses()));
            } else {
                ps.setString(4, "LECTURER");
                ps.setString(5, "");
                ps.setString(6, "");
                ps.setString(7, "");
            }

            ps.setInt(8, u.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Replace all Users in the database with the provided list.
     */
    public void saveAllUsers(List<User> users) {
        String delete = "DELETE FROM " + TBL;
        try (Connection conn = dbm.getConnection();
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);
            stmt.executeUpdate(delete);

            String insert = "INSERT INTO " + TBL 
                          + "(firstName, lastName, password, role, major, pending, approved)"
                          + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                for (User u : users) {
                    ps.setString(1, u.getFirstName());
                    ps.setString(2, u.getLastName());
                    ps.setString(3, u.getPassword());

                    if (u instanceof Student s) {
                        ps.setString(4, "STUDENT");
                        ps.setString(5, s.getMajor() != null ? s.getMajor().getName() : "");
                        ps.setString(6, joinCourses(s.getPendingCourses()));
                        ps.setString(7, joinCourses(s.getApprovedCourses()));
                    } else {
                        ps.setString(4, "LECTURER");
                        ps.setString(5, "");
                        ps.setString(6, "");
                        ps.setString(7, "");
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void seedDefaultLecturer() {
        String checkSql = "SELECT COUNT(*) FROM USERS WHERE role = 'LECTURER'";
        String insertSql = "INSERT INTO USERS " +
            "(firstName, lastName, password, role, major, pending, approved) " +
            "VALUES ('Admin','Admin','123','LECTURER','','','')";

        try (Connection conn = dbm.getConnection();
             PreparedStatement check = conn.prepareStatement(checkSql);
             ResultSet rs = check.executeQuery()) {

            
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                    insert.executeUpdate();
                    System.out.println("Default lecturer added.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    /** Parse semicolon-delimited course names into Course objects. */
    private void populateCourseList(String raw, List<Course> list) {
        list.clear();
        if (raw == null || raw.isBlank()) return;

        Set<Course> seen = new HashSet<>();
        for (String name : raw.split(";")) {
            String trimmed = name.trim();
            if (!trimmed.isEmpty()) {
                Course course = new Course(trimmed);
                if (seen.add(course)) {
                    list.add(course);  // avoid duplicates
                }
            }
        }
    }

    /** Convert a list of Course objects into semicolon-delimited course names. */
    private String joinCourses(List<Course> courses) {
        StringBuilder sb = new StringBuilder();
        for (Course c : courses) {
            String name = c.getName().trim();
            if (!name.isEmpty()) {
                if (sb.length() > 0) sb.append(';');
                sb.append(name);
            }
        }
        return sb.toString();
    }
}

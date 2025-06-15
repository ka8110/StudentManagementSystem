/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;


import Model.Course;
import Model.Major;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Database.DatabaseManager;

/**
 *
 * @author alkim
 */
public class MajorRepository implements IMajorRepository {
private final DatabaseManager dbm;

    // Constructor injection
    public MajorRepository(DatabaseManager dbm) {
        this.dbm = dbm;
    }

    /**
     * Fetch all majors from the database, including their courses.
     */
    @Override
    public List<Major> findAll() {
        List<Major> majors = new ArrayList<>();
        String sqlMajors = "SELECT name FROM Majors ORDER BY name";

        try (Connection conn = dbm.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlMajors)) {

            while (rs.next()) {
                String majorName = rs.getString("name");
                Major major = new Major(majorName);

                // load associated courses
                String sqlCourses = "SELECT name FROM Courses WHERE major_name = ? ORDER BY name";
                try (PreparedStatement ps = conn.prepareStatement(sqlCourses)) {
                    ps.setString(1, majorName);
                    try (ResultSet crs = ps.executeQuery()) {
                        while (crs.next()) {
                            major.addCourse(new Course(crs.getString("name")));
                        }
                    }
                }

                majors.add(major);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return majors;
    }

    /**
     * Insert or update a major and its courses in the database.
     */
    @Override
    public void updateMajor(Major major) {
        String name = major.getName();
        List<Course> courses = major.getCourses();

        try (Connection conn = dbm.getConnection()) {
            conn.setAutoCommit(false);

            // Check if major exists
            String checkSql = "SELECT COUNT(*) FROM Majors WHERE name = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, name);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) == 0) {
                        // Insert new major
                        String insertSql = "INSERT INTO Majors(name) VALUES(?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setString(1, name);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }

            // refresh courses
            try (PreparedStatement del = conn.prepareStatement(
                     "DELETE FROM Courses WHERE major_name = ?")) {
                del.setString(1, name);
                del.executeUpdate();
            }

            String insCourse = "INSERT INTO Courses(name, major_name) VALUES(?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insCourse)) {
                for (Course c : courses) {
                    ps.setString(1, c.getName());
                    ps.setString(2, name);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Seed the Majors and Courses tables from file-format lines.
     * Safe to run multiple times (idempotent).
     */
    public void seedFromFileLines(List<String> lines) {
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            String[] parts = line.split(";", 2);
            String majorName = parts[0].trim();
            Major major = new Major(majorName);

            if (parts.length > 1 && !parts[1].isBlank()) {
                // courses separated by comma, each entry is name as in "101:Intro..."
                for (String courseDef : parts[1].split(",")) {
                    major.addCourse(new Course(courseDef.trim()));
                }
            }
            updateMajor(major);
        }
    }
}

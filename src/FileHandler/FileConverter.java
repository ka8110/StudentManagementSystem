/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileHandler;

import Model.Course;
import Model.Lecturer;
import Model.Major;
import Model.Student;
import Model.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alkim
 */
public class FileConverter {
    /**
     * Serialize a User (Student or Lecturer) into a CSV line:
     * id,firstName,lastName,password,role,major
     * @param user
     * @return 
     */
    public static String toCSV(User user) {
        if (user instanceof Student student) {
            String pending = joinCourses(student.getPendingCourses());
            String approved = joinCourses(student.getApprovedCourses());
            String major = student.getMajor() != null ? student.getMajor().getName() : "";
            return String.join(",", 
                String.valueOf(student.getId()),
                student.getFirstName(),
                student.getLastName(),
                student.getPassword(),
                "STUDENT",
                major,
                pending,
                approved
            );
        } else if (user instanceof Lecturer) {
            return String.join(",", 
                String.valueOf(user.getId()),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                "LECTURER"
            );
        }
        return "";
    }

    public static User fromCSV(String line) {
        String[] parts = line.split(",", -1);
        int id = Integer.parseInt(parts[0]);
        String first = parts[1];
        String last = parts[2];
        String pass = parts[3];
        String role = parts[4];

        if ("STUDENT".equals(role)) {
            String majorName = parts.length > 5 ? parts[5] : null;
            String pendingRaw = parts.length > 6 ? parts[6] : "";
            String approvedRaw = parts.length > 7 ? parts[7] : "";

            Major major = majorName != null && !majorName.isBlank() ? new Major(majorName, new ArrayList<>()) : null;

            Student student = new Student(id, first, last, pass, major);
            student.getPendingCourses().addAll(splitCourses(pendingRaw));
            student.getApprovedCourses().addAll(splitCourses(approvedRaw));
            return student;

        } else if ("LECTURER".equals(role)) {
            return new Lecturer(id, first, last, pass);
        }

        return null;
    }

    private static String joinCourses(List<Course> courses) {
        List<String> names = new ArrayList<>();
        for (Course c : courses) names.add(c.getName());
        return String.join(";", names);
    }

    private static List<Course> splitCourses(String raw) {
        List<Course> list = new ArrayList<>();
        if (raw == null || raw.isBlank()) return list;

        for (String name : raw.split(";")) {
            if (!name.isBlank()) list.add(new Course(name.trim()));
        }
        return list;
    }
}

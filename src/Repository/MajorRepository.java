/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import FileHandler.FileRead;
import FileHandler.FileWrite;
import Model.Course;
import Model.Major;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alkim
 */
public class MajorRepository implements IMajorRepository {
    private static final String FILE_NAME = "majors.txt";

    @Override
    public List<Major> findAll() {
        List<Major> majors = new ArrayList<>();
        List<String> lines = FileRead.readLines(FILE_NAME);

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(";", 2);
            String majorName = parts[0].trim();
            Major major = new Major(majorName);

            if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                String[] courseNames = parts[1].split(",");
                for (String courseName : courseNames) {
                    major.addCourse(new Course(courseName.trim()));
                }
            }

            majors.add(major);
        }

        return majors;
    }

    @Override
    public void updateMajor(Major major) {
        List<String> lines = FileRead.readLines(FILE_NAME);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        String updatedLine = buildLineFromMajor(major);

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(";", 2);
            String nameInFile = parts[0].trim();
            if (nameInFile.equalsIgnoreCase(major.getName())) {
                updatedLines.add(updatedLine);
                found = true;
            } else {
                updatedLines.add(line);
            }
        }

        if (!found) {
            updatedLines.add(updatedLine);
        }

        FileWrite.writeLines(FILE_NAME, updatedLines);
    }

    private String buildLineFromMajor(Major major) {
        StringBuilder sb = new StringBuilder();
        sb.append(major.getName()).append(";");

        List<Course> courses = major.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            sb.append(courses.get(i).getName());
            if (i < courses.size() - 1) sb.append(",");
        }

        return sb.toString();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author alkim
 */

import Model.Course;
import Model.Major;
import Model.Student;
import Service.MajorService;
import Service.StudentService;

import java.util.List;
import java.util.Scanner;

public class StudentController implements IStudentController {
    private final MajorService majorService;
    private final StudentService studentService;
    private final Scanner scanner = new Scanner(System.in);

    public StudentController(MajorService majorService, StudentService studentService) {
        this.majorService = majorService;
        this.studentService = studentService;
    }

    /** Main menu for a logged-in student
     * @param student */
    @Override
    public void start(Student student) {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. Select Major & Request Courses");
            System.out.println("2. View Pending Requests");
            System.out.println("3. View Approved Courses");
            System.out.println("4. Logout");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> selectMajorAndRequest(student);
                case "2" -> viewPending(student);
                case "3" -> viewApproved(student);
                case "4" -> {
                    System.out.println("Goodbye, " + student.getFirstName() + "!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void selectMajorAndRequest(Student student) {
        List<Major> majors = majorService.getAllMajors();
        if (majors.isEmpty()) {
            System.out.println("No majors available.");
            return;
        }

        // 1) Choose Major
        System.out.println("Available Majors:");
        for (int i = 0; i < majors.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, majors.get(i).getName());
        }
        System.out.print("Select a major (0 to cancel): ");
        int majorChoice = readIntInRange(majors.size());
        if (majorChoice == 0) {
            System.out.println("Cancelled.");
            return;
        }

        Major selectedMajor = majors.get(majorChoice - 1);
        student.setMajor(selectedMajor);

        // Clear old requests/approvals when changing major
        student.getPendingCourses().clear();
        student.getApprovedCourses().clear();
        studentService.updateStudent(student);

        System.out.println("Chosen Major: " + selectedMajor.getName());

        // 2) Enroll in Courses for that Major
        List<Course> courses = selectedMajor.getCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available for this major yet.");
            return;
        }

        while (true) {
            System.out.println("\nCourses in " + selectedMajor.getName() + ":");
            for (int i = 0; i < courses.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, courses.get(i).getName());
            }
            System.out.print("Request a course (0 to finish): ");
            int courseChoice = readIntInRange(courses.size());
            if (courseChoice == 0) break;

            Course course = courses.get(courseChoice - 1);
            if (student.getPendingCourses().contains(course)) {
                System.out.println("Already requested: " + course.getName());
            } else if (student.getApprovedCourses().contains(course)) {
                System.out.println("Already approved: " + course.getName());
            } else {
                student.requestCourse(course);
                studentService.updateStudent(student);
                System.out.println("Requested: " + course.getName());
            }
        }
    }

    private void viewPending(Student student) {
        List<Course> pending = student.getPendingCourses();
        if (pending.isEmpty()) {
            System.out.println("No pending requests.");
        } else {
            System.out.println("Pending Courses:");
            for (Course c : pending) {
                System.out.println(" - " + c.getName());
            }
        }
    }

    private void viewApproved(Student student) {
        List<Course> approved = student.getApprovedCourses();
        if (approved.isEmpty()) {
            System.out.println("No approved courses.");
        } else {
            System.out.println("Approved Courses:");
            for (Course c : approved) {
                System.out.println(" - " + c.getName());
            }
        }
    }

    // Helper to read an int between 0 and max (inclusive), reprompting on invalid input
    private int readIntInRange(int max) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                int n = Integer.parseInt(line);
                if (n >= 0 && n <= max) return n;
            } catch (NumberFormatException ignored) { }
            System.out.print("Please enter a number between 0 and " + max + ": ");
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import Model.Course;
import Model.Lecturer;
import Model.Student;
import Service.StudentService;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author alkim
 */
public class LecturerController implements ILecturerController{
    private final Scanner scanner = new Scanner(System.in);
    private final StudentService studentService;

    public LecturerController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void start(Lecturer lecturer) {
        while (true) {
            System.out.println("\n--- Lecturer Menu ---");
            System.out.println("1. Review Pending Course Requests");
            System.out.println("2. Logout");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> reviewAllPending();
                case "2" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    @Override
    public void reviewAllPending() {
        List<Student> students = studentService.getAllStudents();
        boolean found = false;

        for (Student s : students) {
            List<Course> pending = s.getPendingCourses();

            if (!pending.isEmpty()) {
                found = true;
                boolean reviewing = true;

                while (reviewing && !pending.isEmpty()) {
                    System.out.println("\nStudent: " + s.getFullName() + " (ID: " + s.getId() + ")");
                    for (int i = 0; i < pending.size(); i++) {
                        System.out.printf("%d. %s%n", i + 1, pending.get(i).getName());
                    }

                    System.out.print("Enter course number to approve, negative number to decline, or 0 to skip to next student: ");

                    try {
                        int sel = Integer.parseInt(scanner.nextLine());

                        if (sel == 0) {
                            reviewing = false;
                        } else if (sel > 0 && sel <= pending.size()) {
                            Course c = pending.get(sel - 1);
                            s.approveCourse(c);
                            System.out.println("Approved: " + c.getName());
                        } else if (sel < 0 && -sel <= pending.size()) {
                            Course c = pending.get(-sel - 1);
                            s.declineCourse(c);
                            System.out.println("Declined: " + c.getName());
                        } else {
                            System.out.println("Invalid selection.");
                        }

                        studentService.updateStudent(s); // Persist after each change
                        pending = s.getPendingCourses(); // Refresh pending list
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
            }
        }

        if (!found) {
            System.out.println("No pending requests");
        }
    }
}

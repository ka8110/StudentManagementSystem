/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Lecturer;
import Model.Student;
import Model.User;
import Service.LoginService;
import Service.StudentService;
import java.util.Scanner;

/**
 *
 * @author alkim
 */
public class LoginController implements ILoginController {
    private final LoginService loginService;
    private StudentService studentService;
    private final Scanner scanner = new Scanner(System.in);

    public LoginController(LoginService loginService, StudentService studentService) {
        this.loginService = loginService;
        this.studentService = studentService;
    }
    
    @Override
    public Student loginAsStudent() {
        System.out.print("Enter your Student ID (or 0 to register): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("You must enter a valid Student ID.");
            return null;
        }

        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a numeric ID.");
            return null;
        }

        if (id == 0) {
            // Register new student
            System.out.print("First Name: ");
            String firstName = scanner.nextLine().trim();
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine().trim();
            System.out.print("New Password: ");
            String newPassword = scanner.nextLine().trim();

            Student student = studentService.registerStudent(firstName, lastName, newPassword);
            System.out.println("Registered successfully! Your Student ID is: " + student.getId());
            return student;
        }

        // Attempt login for existing student
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = loginService.login(id, password, "Student");
        if (user instanceof Student student) {
            System.out.println("Login successful.");
            return student;
        } else {
            System.out.println("Invalid ID or Password");
            return null;
        }
    }
    
    /**
     * Prompts the user to log in by entering an ID and role.
     * Returns the matching User (Student or Lecturer), or null if invalid.
     * @return 
     */
    @Override
    public Lecturer loginAsLecturer() {
        System.out.print("Enter your Lecturer ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        Lecturer l = (Lecturer) loginService.login(id, password, "Lecturer");
        if (l == null) {
            System.out.println("Invalid ID or Password");
        } else {
            System.out.println("Login successful.");
        }
        return l;
    }
}

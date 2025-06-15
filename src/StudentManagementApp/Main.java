/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package StudentManagementApp;

import Controller.LecturerController;
import Controller.LoginController;
import Controller.StudentController;
import FileHandler.UserFileManager;
import Model.Lecturer;
import Model.Student;
import Model.User;
import Repository.LoginRepository;
import Repository.MajorRepository;
import Repository.StudentRepository;
import Repository.UserRepository;
import Service.LoginService;
import Service.MajorService;
import Service.StudentService;
import java.util.Scanner;

/**
 *
 * @author alkim
 */
public class Main {
    public static void main(String[] args) {
        MajorRepository majorRepository = new MajorRepository();
        MajorService majorService = new MajorService(majorRepository);
        
        UserFileManager userFileManager = new UserFileManager();
        UserRepository userRepository = new UserRepository(userFileManager);
        
        StudentRepository studentRepository = new StudentRepository(userFileManager, userRepository);
        StudentService studentService = new StudentService(studentRepository);
        StudentController studentController = new StudentController(majorService, studentService);
        
        LecturerController lecturerController = new LecturerController(studentService);
        
        LoginRepository loginRepository = new LoginRepository(userFileManager);
        LoginService loginService = new LoginService(loginRepository);
        LoginController loginController = new LoginController(loginService, studentService);

        User loggedIn = null;
        
        Scanner scanner = new Scanner(System.in);
        while (loggedIn == null) {
            System.out.println("\n--- Login as: ---");
            System.out.println("1. Student");
            System.out.println("2. Lecturer");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> loggedIn = loginController.loginAsStudent();  // Handles registration if ID is 0
                case "2" -> loggedIn = loginController.loginAsLecturer();
                default -> System.out.println("Invalid selection. Please enter 1 or 2.");
            }
        }

        // Role‐based main menu
        switch (loggedIn) {
            case Lecturer lec -> // Lecturer only needs to review enrollments
                lecturerController.start(lec);
            case Student stu -> // Student registers (if not already) and enrolls
                studentController.start(stu);
            default -> {
                System.out.println("Goodbye!");
            }
        }  
   }
}

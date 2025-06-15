/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package StudentManagementApp;

import Controller.LecturerController;
import Controller.LoginController;
import Controller.StudentController;
import Database.DatabaseManager;
import Database.UserFileManager;
import Model.Lecturer;
import Model.Student;
import Model.User;
import Repository.LoginRepository;
import Repository.MajorRepository;
import Repository.StudentRepository;
import Service.LoginService;
import Service.MajorService;
import Service.StudentService;
import java.sql.SQLException;
import javax.swing.SwingUtilities;

/**
 *
 * @author alkim
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        try {
            // Initialize the embedded database and schema
            DatabaseManager dbm = new DatabaseManager();

            // Create DAOs
            UserFileManager userFileManager = new UserFileManager(dbm);
            userFileManager.seedDefaultLecturer();
            
            // Create repositories
            MajorRepository majorRepo      = new MajorRepository(dbm);
            StudentRepository studentRepo  = new StudentRepository(userFileManager);
            LoginRepository loginRepo      = new LoginRepository(userFileManager);

            // Create services
            MajorService majorService      = new MajorService(majorRepo);
            StudentService studentService  = new StudentService(studentRepo);
            LoginService loginService      = new LoginService(loginRepo);

            // Boot and seed majors (optional)
            majorRepo.seedFromFileLines(
                java.util.List.of(
                    "Computer Science;Intro to Programming,Data Structures",
                    "Mathematics;Calculus,Algebra"
                )
            );
            
            SwingUtilities.invokeLater(() -> {
                new LoginController(loginService, studentService) {
                    // Override the student-login callback to open the student panel
                    @Override
                    public Student loginAsStudent() {
                        Student student = super.loginAsStudent();
                        if (student != null) {
                            this.dispose();
                            // Open the StudentController with all needed services
                            new StudentController(majorService, studentService, student);

                        }
                        return student;
                    }

                    @Override
                    public Lecturer loginAsLecturer() {
                        Lecturer lecturer = super.loginAsLecturer();
                        if (lecturer != null) {
                            this.dispose();
                            new LecturerController(studentService, lecturer);
                        }
                        return lecturer;
                    }
                };
            });
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
   }
}

package Controller;

import Model.Lecturer;
import Model.Student;
import Model.User;
import Service.LoginService;
import Service.StudentService;

import javax.swing.*;
import java.awt.*;

public class LoginController extends JFrame implements ILoginController {
    private final LoginService loginService;
    private final StudentService studentService;

    private JTextField studentIdField;
    private JPasswordField studentPasswordField;

    private JTextField lecturerIdField;
    private JPasswordField lecturerPasswordField;

    public LoginController(LoginService loginService, StudentService studentService) {
        this.loginService = loginService;
        this.studentService = studentService;

        setTitle("Login Portal");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setupTabs();
        setVisible(true);
    }

    private void setupTabs() {
        JTabbedPane tabs = new JTabbedPane();

        JPanel studentPanel = createStudentLoginPanel();
        JPanel lecturerPanel = createLecturerLoginPanel();

        tabs.add("Student Login", studentPanel);
        tabs.add("Lecturer Login", lecturerPanel);

        add(tabs);
    }

    private JPanel createStudentLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        studentIdField = new JTextField();
        studentPasswordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> loginAsStudent());
        registerButton.addActionListener(e -> registerNewStudent());

        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdField);
        panel.add(new JLabel("Password:"));
        panel.add(studentPasswordField);
        panel.add(loginButton);
        panel.add(registerButton);

        return panel;
    }

    private JPanel createLecturerLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        lecturerIdField = new JTextField();
        lecturerPasswordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> loginAsLecturer());

        panel.add(new JLabel("Lecturer ID:"));
        panel.add(lecturerIdField);
        panel.add(new JLabel("Password:"));
        panel.add(lecturerPasswordField);
        panel.add(loginButton);

        return panel;
    }

    @Override
    public Student loginAsStudent() {
        String idText = studentIdField.getText().trim();
        String password = new String(studentPasswordField.getPassword());

        if (idText.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return null;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID.");
            return null;
        }

        User user = loginService.login(id, password, "Student");
        if (user instanceof Student student) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            this.dispose();
            return student;
        } else {
            JOptionPane.showMessageDialog(this, "Invalid ID or password.");
            return null;
        }
    }

    private void registerNewStudent() {
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] fields = {
            "First Name:", firstNameField,
            "Last Name:", lastNameField,
            "New Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Register New Student", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
                return;
            }

            Student student = studentService.registerStudent(firstName, lastName, password);
            JOptionPane.showMessageDialog(this, "Registered successfully! Your Student ID is: " + student.getId());
        }
    }

    @Override
    public Lecturer loginAsLecturer() {
        String idText = lecturerIdField.getText().trim();
        String password = new String(lecturerPasswordField.getPassword());

        if (idText.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return null;
        }

        int id;
        
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Lecturer ID.");
            return null;
        }

        User user = loginService.login(id, password, "Lecturer");
        if (user instanceof Lecturer lecturer) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            this.dispose();
            return lecturer;
        } else {
            JOptionPane.showMessageDialog(this, "Invalid ID or password.");
            return null;
        }
    }
}

package Controller;

import Model.Course;
import Model.Lecturer;
import Model.Student;
import Service.StudentService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LecturerController extends JFrame implements ILecturerController {
    private final StudentService studentService;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final Lecturer lecturer;
    
    public LecturerController(StudentService studentService, Lecturer lecturer) {
        this.studentService = studentService;
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);
        this.lecturer = lecturer;
        
        setTitle("Lecturer Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        setupMainMenu();
        setupReviewPanel();

        add(mainPanel);
        setVisible(true);
    }

    private void setupMainMenu() {
        JPanel menuPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton reviewBtn = new JButton("Review Pending Course Requests");
        JButton logoutBtn = new JButton("Logout");

        reviewBtn.addActionListener(e -> {
            updateReviewPanel();
            cardLayout.show(mainPanel, "REVIEW");
        });

        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Goodbye!");
            System.exit(0);
        });
        
        menuPanel.add(reviewBtn);
        menuPanel.add(logoutBtn);

        mainPanel.add(menuPanel, "MENU");
    }

    private JPanel reviewPanel;
    private void setupReviewPanel() {
        reviewPanel = new JPanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(reviewPanel);
        mainPanel.add(scrollPane, "REVIEW");
    }

    private void updateReviewPanel() {
        reviewPanel.removeAll();
        List<Student> students = studentService.getAllStudents();
        boolean found = false;

        for (Student s : students) {
            List<Course> pending = s.getPendingCourses();
            if (!pending.isEmpty()) {
                found = true;

                JPanel studentPanel = new JPanel();
                studentPanel.setLayout(new BoxLayout(studentPanel, BoxLayout.Y_AXIS));
                studentPanel.setBorder(BorderFactory.createTitledBorder(s.getFullName() + " (ID: " + s.getId() + ")"));

                for (Course course : pending) {
                    JPanel coursePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel courseLabel = new JLabel(course.getName());
                    JButton approveBtn = new JButton("Approve");
                    JButton declineBtn = new JButton("Decline");

                    approveBtn.addActionListener(e -> {
                        s.approveCourse(course);
                        studentService.updateStudent(s);
                        updateReviewPanel();
                    });

                    declineBtn.addActionListener(e -> {
                        s.declineCourse(course);
                        studentService.updateStudent(s);
                        updateReviewPanel();
                    });

                    coursePanel.add(courseLabel);
                    coursePanel.add(approveBtn);
                    coursePanel.add(declineBtn);
                    studentPanel.add(coursePanel);
                }

                reviewPanel.add(studentPanel);
            }
        }

        if (!found) {
            reviewPanel.add(new JLabel("No pending course requests."));
        }

        reviewPanel.revalidate();
        reviewPanel.repaint();
    }

    @Override
    public void start(Lecturer lecturer) {
        // Entry point already handled in constructor
        cardLayout.show(mainPanel, "MENU");
    }

    @Override
    public void reviewAllPending() {
        updateReviewPanel();
        cardLayout.show(mainPanel, "REVIEW");
    }
}
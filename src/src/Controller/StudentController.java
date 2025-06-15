package Controller;

import Model.Course;
import Model.Major;
import Model.Student;
import Service.MajorService;
import Service.StudentService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentController extends JFrame implements IStudentController {
    private final MajorService majorService;
    private final StudentService studentService;
    private final Student student;

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public StudentController (MajorService majorService,StudentService studentService,Student student) {
        this.majorService   = majorService;
        this.studentService = studentService;
        this.student        = student;

        setTitle("Student Panel – " + student.getFirstName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        setupMenuPanel();
        setupSelectMajorPanel();
        setupPendingPanel();
        setupApprovedPanel();

        add(mainPanel);
        setVisible(true);
        start(student);
    }

    private void setupMenuPanel() {
        JPanel menu = new JPanel(new GridLayout(5,1,10,10));
        menu.setBorder(BorderFactory.createEmptyBorder(30,100,30,100));

        JButton btnSelectMajor = new JButton("1. Select Major & Request Courses");
        JButton btnViewPending = new JButton("2. View Pending Requests");
        JButton btnViewApproved= new JButton("3. View Approved Courses");
        JButton btnLogout      = new JButton("4. Logout");

        btnSelectMajor.addActionListener(e -> {
            refreshSelectMajor();
            cardLayout.show(mainPanel, "MAJOR");
        });
        btnViewPending.addActionListener(e -> {
            refreshPending();
            cardLayout.show(mainPanel, "PENDING");
        });
        btnViewApproved.addActionListener(e -> {
            refreshApproved();
            cardLayout.show(mainPanel, "APPROVED");
        });
        btnLogout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Goodbye, " + student.getFirstName() + "!");
            dispose();
        });

        menu.add(new JLabel("Welcome, " + student.getFullName(), SwingConstants.CENTER));
        menu.add(btnSelectMajor);
        menu.add(btnViewPending);
        menu.add(btnViewApproved);
        menu.add(btnLogout);

        mainPanel.add(menu, "MENU");
    }

    // ——— Major Selection & Request Panel ———
    private JPanel majorPanel;
    private JComboBox<String> majorCombo;
    private JList<String>    courseList;
    private DefaultListModel<String> courseListModel;

    private void setupSelectMajorPanel() {
        majorPanel = new JPanel(new BorderLayout(10,10));
        majorPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Top: choose major
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Choose Major:"));
        majorCombo = new JComboBox<>();
        top.add(majorCombo);
        JButton btnSetMajor = new JButton("Set Major");
        top.add(btnSetMajor);
        btnSetMajor.addActionListener(e -> doSetMajor());

        // Center: courses list + request button
        courseListModel = new DefaultListModel<>();
        courseList      = new JList<>(courseListModel);
        JScrollPane scroll = new JScrollPane(courseList);

        JButton btnRequest = new JButton("Request Selected Course");
        btnRequest.addActionListener(e -> doRequestCourse());

        majorPanel.add(top, BorderLayout.NORTH);
        majorPanel.add(scroll, BorderLayout.CENTER);
        majorPanel.add(btnRequest, BorderLayout.SOUTH);

        mainPanel.add(majorPanel, "MAJOR");
    }

    // ——— Pending Requests Panel ———
    private JPanel pendingPanel;
    private JTextArea pendingArea;

    private void setupPendingPanel() {
        pendingPanel = new JPanel(new BorderLayout(5,5));
        pendingPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pendingArea  = new JTextArea();
        pendingArea.setEditable(false);
        pendingPanel.add(new JLabel("Pending Courses:"), BorderLayout.NORTH);
        pendingPanel.add(new JScrollPane(pendingArea), BorderLayout.CENTER);
        JButton back1 = new JButton("Back to Menu");
        back1.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        pendingPanel.add(back1, BorderLayout.SOUTH);
        mainPanel.add(pendingPanel, "PENDING");
    }

    // ——— Approved Courses Panel ———
    private JPanel approvedPanel;
    private JTextArea approvedArea;

    private void setupApprovedPanel() {
        approvedPanel = new JPanel(new BorderLayout(5,5));
        approvedPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        approvedArea  = new JTextArea();
        approvedArea.setEditable(false);
        approvedPanel.add(new JLabel("Approved Courses:"), BorderLayout.NORTH);
        approvedPanel.add(new JScrollPane(approvedArea), BorderLayout.CENTER);
        JButton back2 = new JButton("Back to Menu");
        back2.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        approvedPanel.add(back2, BorderLayout.SOUTH);
        mainPanel.add(approvedPanel, "APPROVED");
    }

    @Override
    public void start(Student student) {
        cardLayout.show(mainPanel, "MENU");
    }

    // ——— Helpers ———

    private void refreshSelectMajor() {
        majorCombo.removeAllItems();
        for (Major m : majorService.getAllMajors()) {
            majorCombo.addItem(m.getName());
        }
        courseListModel.clear();
    }

    private void doSetMajor() {
        int idx = majorCombo.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "No major selected.");
            return;
        }

        Major chosen = majorService.getAllMajors().get(idx);
        student.setMajor(chosen);
        student.getPendingCourses().clear();
        student.getApprovedCourses().clear();
        studentService.updateStudent(student);

        // populate course list
        courseListModel.clear();
        for (Course c : chosen.getCourses()) {
            courseListModel.addElement(c.getName());
        }
    }

    private void doRequestCourse() {
        int idx = courseList.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Select a course to request.");
            return;
        }

        Major   maj = student.getMajor();
        Course  c   = maj.getCourses().get(idx);
        if (student.getPendingCourses().contains(c)) {
            JOptionPane.showMessageDialog(this, "Already requested.");
        } else if (student.getApprovedCourses().contains(c)) {
            JOptionPane.showMessageDialog(this, "Already approved.");
        } else {
            student.requestCourse(c);
            studentService.updateStudent(student);
            JOptionPane.showMessageDialog(this, "Requested: " + c.getName());
        }
    }

    private void refreshPending() {
        List<Course> pending = student.getPendingCourses();
        pendingArea.setText("");
        if (pending.isEmpty()) {
            pendingArea.setText("No pending requests.");
        } else {
            for (Course c : pending) {
                pendingArea.append("• " + c.getName() + "\n");
            }
        }
    }

    private void refreshApproved() {
        List<Course> approved = student.getApprovedCourses();
        approvedArea.setText("");
        if (approved.isEmpty()) {
            approvedArea.setText("No approved courses.");
        } else {
            for (Course c : approved) {
                approvedArea.append("• " + c.getName() + "\n");
            }
        }
    }
}

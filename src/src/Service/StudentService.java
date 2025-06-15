/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.Student;
import Repository.StudentRepository;
import java.util.List;

/**
 *
 * @author alkim
 */
public class StudentService implements IStudentService{
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }
    // COnstructor for registering a student
    @Override
    public Student registerStudent(String firstName, String lastName, String password) {
        Student student = new Student(firstName, lastName, password);
        repository.addStudent(student);
        return student;
    }
    
    /**
     * Retrieve the full roster of students.
     * Used by LecturerController to find pending requests.
     * @return list of all Students
     */
    @Override
    public List<Student> getAllStudents() {
        return repository.findAll();
    }
    
    // Update student
    @Override
    public void updateStudent(Student student) {
        repository.update(student);
    }
}

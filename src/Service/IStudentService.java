/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.Student;
import java.util.List;

/**
 *
 * @author alkim
 */
public interface IStudentService {
    Student registerStudent(String firstName, String lastName, String password);
    List<Student> getAllStudents();
    void updateStudent(Student student);
}

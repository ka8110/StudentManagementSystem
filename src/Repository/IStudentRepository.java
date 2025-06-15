/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.Student;
import java.util.List;

/**
 *
 * @author alkim
 */
public interface IStudentRepository {
    List<Student> findAll();
    Student addStudent(Student student);
    boolean update(Student updatedStudent);
    Student findById(int id);
}

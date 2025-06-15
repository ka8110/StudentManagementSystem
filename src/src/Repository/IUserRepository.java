/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.Lecturer;
import Model.Student;
import Model.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author alkim
 */
public interface IUserRepository {
    List<User> findAll();
    List<Student> findAllStudents();
    List<Lecturer> findAllLecturers();
    Optional<User> findById(int id);
    Optional<User> findByIdAndPassword(int id, String password);
    void addUser(User user);
    boolean updateUser(User updatedUser);
}

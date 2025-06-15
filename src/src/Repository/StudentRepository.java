/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

/**
 *
 * @author alkim
 */
import Database.UserFileManager;
import Model.Student;
import Model.User;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements IStudentRepository {
    private final UserFileManager userDao;

    public StudentRepository(UserFileManager userDao) {
        this.userDao = userDao;
    }
    
    /**
     * Load every Student from the database.
     * @return 
     */
    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        for (User u : userDao.loadUsers()) {
            if (u instanceof Student s) {
                students.add(s);
            }
        }
        return students;
    }
    
    /**
     * Register a brand-new student in the database.
     * @param student
     * @return 
     */
    @Override
    public Student addStudent(Student student) {
        userDao.appendUser(student);
        return student;
    }

    /**
     * Update an existing student in the database.
     * @param updatedStudent
     * @return 
     */
    @Override
    public boolean update(Student updatedStudent) {
        // Optionally check existence first
        Student existing = findById(updatedStudent.getId());
        if (existing == null) {
            return false;
        }
        userDao.updateUser(updatedStudent);
        return true;
    }

    /**
     * Find a single student by ID.
     * @param id
     * @return 
     */
    @Override
    public Student findById(int id) {
        for (User u : userDao.loadUsers()) {
            if (u instanceof Student s && s.getId() == id) {
                return s;
            }
        }
        return null;
    }
}

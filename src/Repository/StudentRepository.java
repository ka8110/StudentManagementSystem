/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

/**
 *
 * @author alkim
 */
import FileHandler.UserFileManager;
import Model.Student;
import Model.User;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements IStudentRepository {
    private final UserFileManager fileHandler;
    private final UserRepository  users;  // in‐memory + append logic

    public StudentRepository(UserFileManager fileHandler, UserRepository users) {
        this.fileHandler = fileHandler;
        this.users       = users;
    }
    
    /** Load every Student from disk (via fileHandler)
     * @return  */
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        for (User u : fileHandler.loadUsers()) {
            if (u instanceof Student s) {
                students.add(s);
            }
        }
        return students;
    }
    
    /**
     * Register a brand‐new student.
     * Uses UserRepository.addUser → file‐append under the hood.
     * @param student
     * @return 
     */
    public Student addStudent(Student student) {
        users.addUser(student);   // append one line to users.txt
        return student;
    }

    /**
     * Update an existing student.
     * Loads entire file, replaces one student object, then overwrites file.
     * @param updatedStudent
     * @return 
     */
    public boolean update(Student updatedStudent) {
        List<User> all = fileHandler.loadUsers();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            User u = all.get(i);
            if (u instanceof Student s && s.getId() == updatedStudent.getId()) {
                all.set(i, updatedStudent);
                found = true;
                break;
            }
        }
        if (found) {
            fileHandler.saveAllUsers(all);
        }
        return found;
    }

    /** Helper for other layers: find a single student in the current file
     * @param id
     * @return  */
    public Student findById(int id) {
        for (User u : fileHandler.loadUsers()) {
            if (u instanceof Student s && s.getId() == id) {
                return s;
            }
        }
        return null;
    }
}
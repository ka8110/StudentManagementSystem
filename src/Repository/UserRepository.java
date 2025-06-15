/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import FileHandler.UserFileManager;
import Model.Lecturer;
import Model.Student;
import Model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author alkim
 */
public class UserRepository implements IUserRepository {
    private final UserFileManager fileManager;
    private final List<User> users;

    public UserRepository(UserFileManager fileManager) {
        this.fileManager = fileManager;
        this.users = new ArrayList<>(fileManager.loadUsers());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users); // Return a copy to prevent external modification
    }

    @Override
    public List<Student> findAllStudents() {
        List<Student> students = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Student s) {
                students.add(s);
            }
        }
        return students;
    }

    @Override
    public List<Lecturer> findAllLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Lecturer l) {
                lecturers.add(l);
            }
        }
        return lecturers;
    }

    @Override
    public Optional<User> findById(int id) {
        return users.stream()
            .filter(u -> u.getId() == id)
            .findFirst();
    }

    @Override
    public Optional<User> findByIdAndPassword(int id, String password) {
        return users.stream()
            .filter(u -> u.getId() == id && u.getPassword().equals(password))
            .findFirst();
    }

    public void addUser(User user) {
        users.add(user);
        fileManager.saveAllUsers(users);
    }

    @Override
    public boolean updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                fileManager.saveAllUsers(users);
                return true;
            }
        }
        return false;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Database.UserFileManager;
import Model.Lecturer;
import Model.Student;
import Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IUserRepository {
    private final UserFileManager userDao;

    public UserRepository(UserFileManager userDao) {
        this.userDao = userDao;
    }

    /**
     * Return all users from the database.
     */
    @Override
    public List<User> findAll() {
        return new ArrayList<>(userDao.loadUsers());
    }

    /**
     * Return all students.
     */
    @Override
    public List<Student> findAllStudents() {
        List<Student> students = new ArrayList<>();
        for (User u : userDao.loadUsers()) {
            if (u instanceof Student s) {
                students.add(s);
            }
        }
        return students;
    }

    /**
     * Return all lecturers.
     */
    @Override
    public List<Lecturer> findAllLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        for (User u : userDao.loadUsers()) {
            if (u instanceof Lecturer l) {
                lecturers.add(l);
            }
        }
        return lecturers;
    }

    /**
     * Find a user by ID.
     */
    @Override
    public Optional<User> findById(int id) {
        return userDao.loadUsers().stream()
            .filter(u -> u.getId() == id)
            .findFirst();
    }

    /**
     * Find a user by ID & password.
     */
    @Override
    public Optional<User> findByIdAndPassword(int id, String password) {
        return userDao.loadUsers().stream()
            .filter(u -> u.getId() == id && u.getPassword().equals(password))
            .findFirst();
    }

    /**
     * Add a new user to the database.
     */
    @Override
    public void addUser(User user) {
        userDao.appendUser(user);
    }

    /**
     * Update an existing user.
     */
    @Override
    public boolean updateUser(User updatedUser) {
        Optional<User> existing = findById(updatedUser.getId());
        if (existing.isPresent()) {
            userDao.updateUser(updatedUser);
            return true;
        }
        return false;
    }
}

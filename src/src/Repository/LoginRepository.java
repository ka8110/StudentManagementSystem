/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Database.UserFileManager;
import Model.User;
import java.util.Optional;

/**
 *
 * @author alkim
 */
public class LoginRepository implements ILoginRepository {
    private final UserFileManager userDao;

    public LoginRepository(UserFileManager userDao) {
        this.userDao = userDao;
    }

    /**
     * Look up a user by ID, role, and password.
     */
    @Override
    public User findByIdAndPassword(int id, String password, String role) {
        Optional<User> found = userDao.loadUsers().stream()
            .filter(u -> u.getId() == id)
            .filter(u -> u.getRole().equalsIgnoreCase(role))
            .filter(u -> u.getPassword().equals(password))
            .findFirst();
        return found.orElse(null);
    }
}

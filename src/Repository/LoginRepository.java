/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import FileHandler.UserFileManager;
import Model.User;

/**
 *
 * @author alkim
 */
public class LoginRepository implements ILoginRepository {
    private final UserFileManager fileHandler;

    public LoginRepository(UserFileManager fileHandler) {
        this.fileHandler = fileHandler;
    }

    /**
     * Look up a user by ID and role.
     * @param id the user’s numeric ID
     * @param password
     * @param role the expected role ("Student" or "Lecturer")
     * @return the matching User, or null if none found
     */
    @Override
    public User findByIdAndPassword(int id, String password, String role) {
        for (User u : fileHandler.loadUsers()) {
            if (u.getId() == id
             && u.getRole().equalsIgnoreCase(role)
             && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}

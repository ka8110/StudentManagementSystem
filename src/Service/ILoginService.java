/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.User;

/**
 *
 * @author alkim
 */
public interface ILoginService {
    /**
     * Attempts to log in a user with the given ID, password, and role.
     * @param id the user’s ID
     * @param password the user’s password
     * @param role the user’s role ("Student" or "Lecturer")
     * @return the authenticated User, or null if credentials are invalid
     */
    User login(int id, String password, String role);
}

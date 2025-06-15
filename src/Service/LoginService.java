/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.User;
import Repository.LoginRepository;

/**
 *
 * @author alkim
 */
public class LoginService implements ILoginService{
    private final LoginRepository repository;

    public LoginService(LoginRepository repository) {
        this.repository = repository;
    }

    /**
     * Attempts to log in a user with the given ID and password role.
     * @param id the user’s ID
     * @param password
     * @param role the user’s role ("Student" or "Lecturer")
     * @return the authenticated User, or null if credentials are invalid
     */
    public User login(int id, String password, String role) {
        return repository.findByIdAndPassword(id, password, role);
    }
    
    
}

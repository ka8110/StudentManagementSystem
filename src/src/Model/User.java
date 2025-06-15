
package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 *
 * @author alkim
 */

public abstract class User {
    
    private Integer id;
    private String firstName;
    private String lastName;
    private String password;
    private final String role;

    /**
     * Constructor for loading an existing user (with known ID).
     * @param id
     * @param firstName
     * @param lastName
     * @param password
     * @param role
     */
    public User(Integer id, String firstName, String lastName, String password, String role) {
        this.id        = id;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.password  = password;
        this.role      = role;
    }

    /**
     * Constructor for registering a new user (ID will be assigned later).
     * @param firstName
     * @param lastName
     * @param password
     * @param role
     */
    public User(String firstName, String lastName, String password, String role) {
        this(null, firstName, lastName, password, role);
    }

    /** Set the user ID (e.g. after insertion into the database).
     * @param id */
    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    /** Convenience: firstName + " " + lastName
     * @return  */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}

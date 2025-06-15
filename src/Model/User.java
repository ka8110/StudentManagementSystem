
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
    
    private final int id;
    private String firstName;
    private String lastName;
    private String password;
    private final String role;
    private static final HashSet<Integer> usedIds = new HashSet<>();
    public static int nextId = getLastUsedId("users.txt") + 1;

    // Constructor login info
    public User(int id, String firstName, String lastName, String password, String role) {
        this.id        = id;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.password  = password;
        this.role      = role;
    }
    
    // Constructor for registration
    public User(String firstName, String lastName, String password, String role) {
        this.id        = generateId();
        this.firstName = firstName;
        this.lastName  = lastName;
        this.password  = password;
        this.role      = role;
    }

    private int generateId() {
        while (usedIds.contains(nextId)) {
            nextId++;
        }
        usedIds.add(nextId);
        return nextId++;
    }
    
    public static int getLastUsedId(String FILE_NAME) {
        int maxId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;           // skip blank lines

                String[] parts = line.split(",", -1);
                String idText = parts[0].trim();
                if (idText.isEmpty()) continue;         // skip lines with no ID

                try {
                    int id = Integer.parseInt(idText);
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException e) {
                    // skip lines where the first token isn't a valid integer
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxId;
    }
    
    public int getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    public String getRole() { 
        return this.role;       
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getFullName() {
        return (this.firstName + " " + this.lastName);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileHandler;

/**
 *
 * @author alkim
 */
import Model.User;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserFileManager {
    
    private static final String USER_FILE = "users.txt";

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        for (String rawLine : FileRead.readLines(USER_FILE)) {
            if (rawLine == null) continue;
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;   // skip blank lines
            }                

            String[] parts = line.split(",", -1);
            // If there's no ID field, or it's empty, skip this line
            if (parts.length == 0 || parts[0].trim().isEmpty()) {
                System.err.println("Skipping malformed user line (no ID): " + rawLine);
                continue;
            }

            try {
                User u = FileConverter.fromCSV(line);
                if (u != null) users.add(u);
            } catch (NumberFormatException e) {
                // Skip lines where we can't parse the ID
                System.err.println("Skipping malformed user line (bad ID): " + rawLine);
            }
        }
        return users;
    }
    
    public void appendUser(User user) {
        String line = FileConverter.toCSV(user);
        FileWrite.appendLine("users.txt", List.of(line)); // This will add to the end
    }
    
    public void updateUser(User updatedUser) {
        List<User> users = loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                break;
            }
        }
        saveAllUsers(users);
    }
    
    public void saveAllUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User u : users) {
                writer.write(FileConverter.toCSV(u));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

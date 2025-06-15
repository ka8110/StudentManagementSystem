/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alkim
 */
public class FileRead {
    public static List<String> readLines(String path) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (!l.isBlank()) lines.add(l);
            }
        } catch (IOException e) { 
            e.printStackTrace();
        }
        return lines;
    }
}

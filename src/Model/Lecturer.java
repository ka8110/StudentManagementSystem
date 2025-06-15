/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author alkim
 */
public class Lecturer extends User { 
    public Lecturer (int id, String firstName, String lastName, String password) {
        super(id, firstName, lastName, password, "LECTURER");
    }
}

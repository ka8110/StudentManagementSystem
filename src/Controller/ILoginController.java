/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Lecturer;
import Model.Student;

/**
 *
 * @author alkim
 */
public interface ILoginController {
    Student loginAsStudent();
    Lecturer loginAsLecturer();
}

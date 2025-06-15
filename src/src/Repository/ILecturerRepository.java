/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.Lecturer;
import java.util.List;

/**
 *
 * @author alkim
 */
public interface ILecturerRepository {
    Lecturer addLecturer(Lecturer lecturer);
    List<Lecturer> findAll();
    Lecturer findById(int id);
}

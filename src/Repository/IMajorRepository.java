/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.Major;
import java.util.List;

/**
 *
 * @author alkim
 */
public interface IMajorRepository {
    List<Major> findAll();
    void updateMajor(Major major);
}

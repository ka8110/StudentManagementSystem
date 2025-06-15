/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.Major;
import java.util.List;

/**
 *
 * @author alkim
 */
public interface IMajorService {
    List<Major> getAllMajors();
    void updateMajor(Major major);
    Major getMajorByName(String name);
}

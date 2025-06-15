/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Model.User;

/**
 *
 * @author alkim
 */
public interface ILoginRepository {
    User findByIdAndPassword(int id, String password, String role);
}

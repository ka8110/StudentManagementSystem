/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

/**
 *
 * @author alkim
 */
import Model.Lecturer;
import Model.User;

import java.util.ArrayList;
import java.util.List;

public class LecturerRepository implements ILecturerRepository {
    private final UserRepository users;

    public LecturerRepository(UserRepository users) {
        this.users = users;
    }

    @Override
    public Lecturer addLecturer(Lecturer lecturer) {
        users.addUser(lecturer); // Append new lecturer
        return lecturer;
    }

    @Override
    public List<Lecturer> findAll() {
        List<Lecturer> lecturers = new ArrayList<>();
        for (User u : users.findAll()) {
            if (u instanceof Lecturer l) {
                lecturers.add(l);
            }
        }
        return lecturers;
    }

    @Override
    public Lecturer findById(int id) {
        for (Lecturer l : findAll()) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }

}

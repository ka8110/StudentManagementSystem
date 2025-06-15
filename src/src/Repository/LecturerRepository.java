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
import java.util.Optional;
import java.util.List;

public class LecturerRepository implements ILecturerRepository {
    private final IUserRepository userRepo;

    public LecturerRepository(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Add a new Lecturer to the database.
     * @param lecturer
     * @return 
     */
    @Override
    public Lecturer addLecturer(Lecturer lecturer) {
        userRepo.addUser(lecturer);
        return lecturer;
    }

    /**
     * Return all Lecturers.
     * @return 
     */
    @Override
    public List<Lecturer> findAll() {
        return userRepo.findAllLecturers();
    }

    /**
     * Find a Lecturer by ID.
     * @param id
     * @return 
     */
    @Override
    public Lecturer findById(int id) {
        Optional<Lecturer> found = userRepo.findById(id)
            .filter(u -> u instanceof Lecturer)
            .map(u -> (Lecturer) u);
        return found.orElse(null);
    }
}


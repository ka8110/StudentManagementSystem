/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.Major;
import Repository.MajorRepository;
import java.util.List;

/**
 *
 * @author alkim
 */
public class MajorService implements IMajorService{
    private final MajorRepository repository;

    public MajorService(MajorRepository repository) {
        this.repository = repository;
    }

    /** Load every major (with its courses)
     * @return  */
    @Override
    public List<Major> getAllMajors() {
        return repository.findAll();
    }
    
    @Override
    public void updateMajor(Major major) {
        repository.updateMajor(major);
    }
    
    /** Find a single Major by its name, or return null if none matches.
     * @param name
     * @return  */
    @Override
    public Major getMajorByName(String name) {
        for (Major m : repository.findAll()) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
}

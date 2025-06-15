package Model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alkim
 */
public class Student extends User{
    private Major major;
    private final List<Course> pendingCourses = new ArrayList<>();
    private final List<Course> approvedCourses = new ArrayList<>();
    
    // Registering constructor
    public Student (String firstName, String lastName, String password) {
        super(firstName, lastName, password, "STUDENT"); 
    }
    
    // Loading constructor
    public Student (int id, String firstName, String lastName, String password, Major major) {
        super(id, firstName, lastName, password, "STUDENT");
        this.major = major;
    }
    
    public void setMajor(Major major) {
        this.major = major;
    }
    public Major getMajor() {
        return major;
    }

    /** Called by student when they pick a course to enroll in
     * @param course */
    public void requestCourse(Course course) {
        if (!pendingCourses.contains(course) && !approvedCourses.contains(course)) {
            pendingCourses.add(course);
        }
    }

    /** Lecturer calls this to approve one pending course
     * @param course */
    public void approveCourse(Course course) {
        if (pendingCourses.remove(course)) {
            approvedCourses.add(course);
        }
    }

    /** Lecturer calls this to decline/remove a pending request
     * @param course */
    public void declineCourse(Course course) {
        pendingCourses.remove(course);
    }

    public List<Course> getPendingCourses() {
        return pendingCourses;
    }
    public List<Course> getApprovedCourses() {
        return approvedCourses;
    }
    
}

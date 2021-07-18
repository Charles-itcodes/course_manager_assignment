package se.lexicon.course_manager_assignment.data.dao;




import se.lexicon.course_manager_assignment.model.Course;

import java.time.LocalDate;
import java.util.Collection;

public interface CourseDao {

    Course createCourse(String courseName, LocalDate startDate, int weekDuration);
    Course findById(int id);
    Collection<Course> findByNameContains(String name);
    Collection<Course> findByDateBefore(LocalDate end);
    Collection<Course> findByDateAfter(LocalDate start);
    Collection<Course> findAll();
    Collection<Course> findByStudentId(int studentId);
    boolean removeCourse(Course course);
    void clear();

}

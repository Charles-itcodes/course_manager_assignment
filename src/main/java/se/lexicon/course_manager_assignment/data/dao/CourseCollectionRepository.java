package se.lexicon.course_manager_assignment.data.dao;



import se.lexicon.course_manager_assignment.data.sequencers.CourseSequencer;
import se.lexicon.course_manager_assignment.model.Course;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class CourseCollectionRepository implements CourseDao{

    private Collection<Course> courses;
    private Course course;


    public CourseCollectionRepository(Collection<Course> courses) {
        this.courses = courses;
    }

    @Override
    public Course createCourse(String courseName, LocalDate startDate, int weekDuration) {

        Course course = new Course(CourseSequencer.nextCourseId());
        course.setCourseName(courseName);
        course.setStartDate(startDate);
        course.setWeekDuration(weekDuration);
        courses.add(course);
        return course;
    }

    @Override
    public Course findById(int id) {
        LinkedList<Course> list = new LinkedList<Course>();
        for (Course mCourse : courses) {
            if (mCourse.getId() == id) {
                list.add(mCourse);
            }
        }
        if (list.size() != 0) {
            return list.get(0);

        } else
            return null;
    }

    @Override
    public Collection<Course> findByNameContains(String name) {
        Collection<Course> mCourses = new ArrayList<Course>();
        for (Course course : courses) {
            if (course.getCourseName().equalsIgnoreCase(name)) {
                mCourses.add(course);
            }
        }
        return mCourses;
    }

    @Override
    public Collection<Course> findByDateBefore(LocalDate end) {
        Collection<Course> mCourses = new ArrayList<Course>();

        for (Course course : courses) {
            if (course.getStartDate().isBefore(end)) {
                mCourses.add(course);
            }
        }

        return mCourses;
    }

    @Override
    public Collection<Course> findByDateAfter(LocalDate start) {
        Collection<Course> mCourses = new ArrayList<Course>();
        for (Course course : courses) {
            if (course.getStartDate().isAfter(start)) {
                mCourses.add(course);
            }
        }
        return mCourses;
    }

    @Override
    public Collection<Course> findAll() {
        return  courses;
    }

    @Override
    public Collection<Course> findByStudentId(int studentId) {
        return null;
    }

    @Override
    public boolean removeCourse(Course course) {

        for (Course mCourse : courses) {
            if (course.getCourseName().equalsIgnoreCase(mCourse.getCourseName())) {
                courses.remove(course);
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        this.courses = new HashSet<>();
    }
}

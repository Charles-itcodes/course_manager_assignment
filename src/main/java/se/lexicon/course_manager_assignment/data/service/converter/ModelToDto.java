package se.lexicon.course_manager_assignment.data.service.converter;

import org.springframework.stereotype.Component;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ModelToDto implements Converters {
    @Override
    public StudentView studentToStudentView(Student student) {

        return new StudentView(student.getId(), student.getName(), student.getEmail(), student.getAddress());
    }

    @Override
    public CourseView courseToCourseView(Course course) {
        if (course == null) {
            return null;

        } else {
            return new CourseView(course.getId(), course.getCourseName(), course.getStartDate(),
                    course.getWeekDuration(), studentsToStudentViews(course.getStudents()));
        }
    }

    @Override
    public List<CourseView> coursesToCourseViews(Collection<Course> courses) {

        List<CourseView> _mCourseView = new ArrayList<>();
        for (Course mCourse : courses) {
            _mCourseView.add(new CourseView(mCourse.getId(), mCourse.getCourseName(), mCourse.getStartDate(),
                    mCourse.getWeekDuration(), studentsToStudentViews(mCourse.getStudents())));
        }
        return _mCourseView;
    }

    @Override
    public List<StudentView> studentsToStudentViews(Collection<Student> students) {

        List<StudentView> _myStudentView = new ArrayList<>();
        if (students == null) {
            return _myStudentView;
        } else {
            for (Student student : students) {
                _myStudentView.add(
                        new StudentView(student.getId(), student.getName(), student.getEmail(), student.getAddress()));
            }
            return _myStudentView;

        }
    }
}

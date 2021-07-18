package se.lexicon.course_manager_assignment.data.service.converter;


import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;

import java.util.Collection;
import java.util.List;

public interface Converters {

    StudentView studentToStudentView(Student student);

    CourseView courseToCourseView(Course course);

    List<CourseView> coursesToCourseViews(Collection<Course> courses);

    List<StudentView> studentsToStudentViews(Collection<Student> students);
}

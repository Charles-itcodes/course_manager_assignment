package se.lexicon.course_manager_assignment.data.service.student;



import se.lexicon.course_manager_assignment.dto.forms.CreateStudentForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateStudentForm;
import se.lexicon.course_manager_assignment.dto.views.StudentView;

import java.util.List;

public interface StudentService {
    StudentView create(CreateStudentForm form);

    StudentView update(UpdateStudentForm form);

    StudentView findById(int id);

    StudentView searchByEmail(String email);

    List<StudentView> searchByName(String name);

    List<StudentView> findAll();

    boolean deleteStudent(int id);
}

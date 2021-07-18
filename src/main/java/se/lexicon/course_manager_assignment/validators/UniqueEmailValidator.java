package se.lexicon.course_manager_assignment.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lexicon.course_manager_assignment.data.dao.StudentDao;
import se.lexicon.course_manager_assignment.model.Student;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private StudentDao studentDao;

    @Autowired
    public UniqueEmailValidator(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Student student = null;
        student = studentDao.findByEmailIgnoreCase(value);
        return student == null;
    }
}

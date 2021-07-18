package se.lexicon.course_manager_assignment.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "A student with this email already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

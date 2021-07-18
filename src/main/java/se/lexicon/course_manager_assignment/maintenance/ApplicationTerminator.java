package se.lexicon.course_manager_assignment.maintenance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.lexicon.course_manager_assignment.data.dao.CourseDao;
import se.lexicon.course_manager_assignment.data.dao.StudentDao;
import se.lexicon.course_manager_assignment.data.sequencers.CourseSequencer;
import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;


import javax.annotation.PreDestroy;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

import static se.lexicon.course_manager_assignment.maintenance.StaticResources.*;


@Component
public class ApplicationTerminator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTerminator.class);


    private final CourseDao courseDao;
    private final StudentDao studentDao;
    private final ObjectMapper objectMapper;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    public ApplicationTerminator(CourseDao courseDao, StudentDao studentDao, ObjectMapper objectMapper) {
        this.courseDao = courseDao;
        this.studentDao = studentDao;
        this.objectMapper = objectMapper;
    }

    @PreDestroy
    public void destroy(){
        if(!activeProfile.equals("test")){

            Collection<Student> tempResultStudents = studentDao.findAll();
            Collection<Course> tempResultCourses = courseDao.findAll();

            Collection<Student> students = tempResultStudents == null ? new HashSet<>() : new HashSet<>(tempResultStudents);
            Collection<Course>  courses = tempResultCourses == null ? new HashSet<>() : new HashSet<>(tempResultCourses);

            saveStudents(students);
            saveCourses(courses);

            Properties properties = new Properties();
            properties.setProperty("currentStudentId", String.valueOf(StudentSequencer.getStudentSequencer()));
            properties.setProperty("currentCourseId", String.valueOf(CourseSequencer.getCourseSequencer()));
            try(FileWriter writer = new FileWriter(SEQUENCERS_FILE)){
                properties.store(writer, "Latest sequencer values");
                LOGGER.info("Latest sequencer values stored");
            }catch (IOException ex){
                LOGGER.error(ex.getMessage());
            }
        }
    }

    private void saveCourses(Collection<Course> courses) {
        try{
            objectMapper.writeValue(COURSE_FILE, courses);
            LOGGER.info("Courses were successfully persisted on termination");
        }catch (IOException ex){
            LOGGER.error(ex.getMessage());
        }
    }

    private void saveStudents(Collection<Student> students){
        try{
            objectMapper.writeValue(STUDENT_FILE, students);
            LOGGER.info("Students were successfully persisted on termination");
        }catch (IOException ex){
            LOGGER.error(ex.getMessage());
        }
    }


}

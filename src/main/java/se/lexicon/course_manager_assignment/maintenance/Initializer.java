package se.lexicon.course_manager_assignment.maintenance;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import se.lexicon.course_manager_assignment.data.dao.CourseCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.StudentCollectionRepository;
import se.lexicon.course_manager_assignment.data.sequencers.CourseSequencer;
import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;


import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

import static se.lexicon.course_manager_assignment.maintenance.StaticResources.*;


@Configuration
public class Initializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Initializer.class);

    private final ObjectMapper objectMapper;
    private Collection<Student> students = new HashSet<>();
    private Collection<Course> courses = new HashSet<>();
    private Properties sequencerData;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    @Primary
    public StudentCollectionRepository studentDaoCollection(){
        return new StudentCollectionRepository(students);
    }

    @Bean
    @Primary
    public CourseCollectionRepository courseDaoCollection(){
        return new CourseCollectionRepository(courses);
    }

    @Autowired
    public Initializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init(){
        sequencerData = new Properties();
        if(!activeProfile.equals("test")){
            students = readStudents();
            courses = readCourses();
            String currentCourseId = "0";
            String currentStudentId = "0";


            try(FileReader reader = new FileReader(SEQUENCERS_FILE)){
                sequencerData.load(reader);

            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }

            currentStudentId = sequencerData.getProperty("currentStudentId");
            currentCourseId = sequencerData.getProperty("currentCourseId");

            StudentSequencer.setStudentSequencer(Integer.parseInt(currentStudentId));
            CourseSequencer.setCourseSequencer(Integer.parseInt(currentCourseId));
        }
    }

    private Collection<Student> readStudents(){
        Collection<Student> students = new HashSet<>();
        try{
            students = objectMapper.readValue(STUDENT_FILE, new TypeReference<Collection<Student>>() {});
            LOGGER.info("Student data from file successfully loaded");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return students;
    }

    private Collection<Course> readCourses(){
        Collection<Course> courses = new HashSet<>();
        try{
            courses = objectMapper.readValue(COURSE_FILE, new TypeReference<Collection<Course>>() {});
            LOGGER.info("Course data from file successfully loaded");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return courses;
    }
}

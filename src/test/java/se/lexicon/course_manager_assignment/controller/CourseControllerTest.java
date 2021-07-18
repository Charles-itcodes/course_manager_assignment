package se.lexicon.course_manager_assignment.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import se.lexicon.course_manager_assignment.data.service.course.CourseService;
import se.lexicon.course_manager_assignment.data.service.student.StudentService;
import se.lexicon.course_manager_assignment.dto.forms.CreateCourseForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateCourseForm;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.dto.views.StudentView;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {StudentController.class, CourseController.class})
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private StudentService studentService;

    public static final StudentView STUDENT_VIEW = new StudentView(1, "Test Testsson", "test@test.com", "Test street 1");
    public static final StudentView STUDENT_VIEW1 = new StudentView(2, "Test2 Testsson2", "test2@test.com", "Test street 2");
    public static final StudentView STUDENT_VIEW2 = new StudentView(3, "Test3 Testsson3", "test3@test.com", "Test street 3");

    public static final List<StudentView> STUDENT_VIEWS = Arrays.asList(
            STUDENT_VIEW, STUDENT_VIEW1,STUDENT_VIEW2
    );
    public static final CourseView JAVA = new CourseView(1, "Java SE8", LocalDate.parse("2020-09-11"), 12, Arrays.asList(STUDENT_VIEW, STUDENT_VIEW1));
    public static final CourseView C_SHARP = new CourseView(2, "C# .NET CORE", LocalDate.parse("2020-08-11"), 12, Arrays.asList(STUDENT_VIEW2, STUDENT_VIEW));
    public static final CourseView C_PLUS_PLUS = new CourseView(3, "C++ beginners", LocalDate.parse("2020-10-11"), 20, STUDENT_VIEWS);


    @Test
    void find_type_name_and_value_C_return_success() throws Exception {
        String type = "name", value = "C";

        when(courseService.searchByCourseName(value)).thenReturn(Arrays.asList(C_SHARP, C_PLUS_PLUS));

        mockMvc.perform(get("/courses")
                    .param("type", type)
                    .param("value", value))
                .andExpect(status().isOk())
                .andExpect(model().attribute("courses", Arrays.asList(C_SHARP, C_PLUS_PLUS)))
                .andExpect(view().name("/course/courses_view"));
    }

    @Test
    void find_type_courseStartBefore_and_value_20201011_return_success() throws Exception {
        String type = "courseStartBefore", value = "2020-10-11";

        when(courseService.searchByDateBefore(LocalDate.parse(value))).thenReturn(Arrays.asList(JAVA, C_SHARP));

        mockMvc.perform(get("/courses")
                .param("type", type)
                .param("value", value))
                .andExpect(status().isOk())
                .andExpect(model().attribute("courses", Arrays.asList(JAVA, C_SHARP)))
                .andExpect(view().name("/course/courses_view"));
    }

    @Test
    void find_type_courseStartBefore_and_value_null_return_message() throws Exception {
        String type = "courseStartBefore";

        mockMvc.perform(get("/courses")
                .param("type", type))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("/course/courses_view"));
    }

    @Test
    void find_type_courseStartAfter_and_value_20200811_return_success() throws Exception {
        String type = "courseStartAfter", value = "2020-08-11";

        when(courseService.searchByDateAfter(LocalDate.parse(value))).thenReturn(Arrays.asList(JAVA, C_PLUS_PLUS));

        mockMvc.perform(get("/courses")
                .param("type", type)
                .param("value", value))
                .andExpect(status().isOk())
                .andExpect(model().attribute("courses", Arrays.asList(JAVA, C_PLUS_PLUS)))
                .andExpect(view().name("/course/courses_view"));
    }

    @Test
    void find_type_courseStartAfter_and_value_null_return_message() throws Exception {
        String type = "courseStartAfter";

        mockMvc.perform(get("/courses")
                .param("type", type))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("/course/courses_view"));
    }

    @Test
    void find_no_params_return_all_courses() throws Exception {
        when(courseService.findAll()).thenReturn(Arrays.asList(JAVA, C_SHARP, C_PLUS_PLUS));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("courses", Arrays.asList(JAVA, C_SHARP, C_PLUS_PLUS)));
    }

    @Test
    void showCreateForm_success() throws Exception {
        mockMvc.perform(get("/courses/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("form"))
                .andExpect(view().name("/course/course_form"));

    }

    @Test
    void processForm_with_valid_form_success() throws Exception {
        CourseView courseView = new CourseView(4, "PHP Advanced", LocalDate.parse("2020-12-11"),6, new ArrayList<>());
        when(courseService.create(any(CreateCourseForm.class))).thenReturn(courseView);

        mockMvc.perform(post("/courses/process")
                    .param("id", (String) null)
                    .param("courseName", "PHP Advanced")
                    .param("startDate", LocalDate.now().toString())
                    .param("weekDuration", "6"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void processForm_with_invalid_form_failure() throws Exception {

        mockMvc.perform(post("/courses/process")
                    .param("id", (String) null)
                    .param("courseName", "1")
                    .param("startDate", "2020-06-15")
                    .param("weekDuration", "-1"))
                .andExpect(model().hasErrors())
                .andExpect(view().name("/course/course_form"));
    }

    @Test
    void getCourse_return_courseView() throws Exception {
        Integer pathVariable = 3;
        CourseView expected = new CourseView(3, "C++ beginners", LocalDate.parse("2020-10-11"), 20, STUDENT_VIEWS);

        when(courseService.findById(pathVariable)).thenReturn(C_PLUS_PLUS);

        mockMvc.perform(get("/courses/{id}", pathVariable))
                .andExpect(status().isOk())
                .andExpect(model().attribute("course", expected))
                .andExpect(view().name("course/course_view"));
    }

    @Test
    void getCourse_return_4xClientError() throws Exception {
        Integer pathVariable = 4;


        when(courseService.findById(pathVariable)).thenReturn(null);

        mockMvc.perform(get("/courses/{id}", pathVariable))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUpdateForm_success() throws Exception{
        Integer pathVariable = 1;
        when(courseService.findById(pathVariable)).thenReturn(JAVA);

        mockMvc.perform(get("/courses/{id}/update", pathVariable))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("form"))
                .andExpect(view().name("course/course_update_form"));
    }

    @Test
    void getUpdateForm_failure() throws Exception{
        Integer pathVariable = 4;
        when(courseService.findById(pathVariable)).thenReturn(null);

        mockMvc.perform(get("/courses/{id}/update", pathVariable))
                .andExpect(status().is4xxClientError());
    }

    //CourseView JAVA = new CourseView(1, "Java SE8", LocalDate.parse("2020-09-11"), 12, Arrays.asList(STUDENT_VIEW, STUDENT_VIEW1));
    @Test
    void updateCourse_success() throws Exception {
        Integer pathVariable = 1;

        when(courseService.update(any(UpdateCourseForm.class))).thenReturn(new CourseView(1, "Java 11", LocalDate.now().plusWeeks(1), 14, Arrays.asList(STUDENT_VIEW,STUDENT_VIEW1)));

        mockMvc.perform(post("/courses/{id}/update", pathVariable)
                        .param("id", "1")
                        .param("courseName","Java 11")
                        .param("startDate", LocalDate.now().plusWeeks(1).toString())
                        .param("weekDuration", "14"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/1"));
    }

    @Test
    void updateCourse_validation_errors() throws Exception {
        Integer pathVariable = 0;

        mockMvc.perform(post("/courses/{id}/update", pathVariable)
                .param("id", "0")
                .param("courseName","J")
                .param("startDate", LocalDate.now().minusDays(1).toString())
                .param("weekDuration", "-1"))
                .andExpect(model().hasErrors())
                .andExpect(view().name("course/course_update_form"));
    }

    @Test
    void updateCourse_failure_pathVariable_not_equals_form_id() throws Exception {
        Integer pathVariable = 2;

        mockMvc.perform(post("/courses/{id}/update", pathVariable)
                .param("id", "1")
                .param("courseName","Java 11")
                .param("startDate", LocalDate.now().plusWeeks(1).toString())
                .param("weekDuration", "14"))
                .andExpect(status().is4xxClientError());

    }
}

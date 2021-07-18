package se.lexicon.course_manager_assignment.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import se.lexicon.course_manager_assignment.data.dao.StudentDao;
import se.lexicon.course_manager_assignment.data.service.course.CourseService;
import se.lexicon.course_manager_assignment.data.service.student.StudentService;
import se.lexicon.course_manager_assignment.dto.forms.CreateStudentForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateStudentForm;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.model.Student;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {StudentController.class, CourseController.class})
public class StudentControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private StudentDao studentDao;

    @MockBean
    private CourseService courseService;

    public static final List<StudentView> STUDENT_VIEW_LIST = Arrays.asList(
            new StudentView(
                    1,
                    "Test Testsson",
                    "test@test.com",
                    "Test street 1"
            )
    );

    public static final StudentView STUDENT_VIEW = new StudentView(
            1,
            "Test Testsson",
            "test@test.com",
            "Test street 1"
    );

    @Test
    void find_param_full_name_return_success() throws Exception {
        String query = "Test Testsson";

        when(studentService.searchByName(query)).thenReturn(STUDENT_VIEW_LIST);

        mockMvc.perform(get("/students")
                    .param("type", "full_name")
                    .param("value", query))
                .andExpect(status().isOk())
                .andExpect(model().attribute("students", STUDENT_VIEW_LIST));
    }

    @Test
    void find_param_full_name_return_empty_List_with_message() throws Exception {
        String query = "C3PO";

        when(studentService.searchByName(query)).thenReturn(null);

        mockMvc.perform(get("/students")
                .param("type", "full_name")
                .param("value", query))
                .andExpect(status().isOk())
                .andExpect(model().attribute("students", new ArrayList<>()))
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("student/students_view"));

    }

    @Test
    void find_return_all_students() throws Exception {

        when(studentService.findAll()).thenReturn(STUDENT_VIEW_LIST);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("students", STUDENT_VIEW_LIST))
                .andExpect(view().name("student/students_view"));
    }

    @Test
    void find_param_email_return_redirect() throws Exception {
        String value = "test@test.com";
        when(studentService.searchByEmail(value)).thenReturn(STUDENT_VIEW);

        mockMvc.perform(get("/students")
                        .param("type", "email")
                        .param("value", value))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    void find_param_email_return_null_return_allStudents_with_message() throws Exception {
        String value = "foo@bar.com";
        when(studentService.searchByEmail(value)).thenReturn(null);
        when(studentService.findAll()).thenReturn(STUDENT_VIEW_LIST);


        mockMvc.perform(get("/students")
                .param("type", "email")
                .param("value", value))
                .andExpect(status().isOk())
                .andExpect(view().name("student/students_view"))
                .andExpect(model().attributeExists("message"));
    }

    @Test
    void showCreateForm_success() throws Exception {

        mockMvc.perform(get("/students/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/student_form"));
    }

    @Test
    void processForm_success() throws Exception {
        when(studentDao.findByEmailIgnoreCase("test@test.com")).thenReturn(null);
        when(studentService.create(any(CreateStudentForm.class))).thenReturn(STUDENT_VIEW);

        mockMvc.perform(post("/students/process")
                    .param("id", (String) null)
                    .param("name", "Test Testsson")
                    .param("email", "test@test.com")
                    .param("address", "Test street 1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void process_form_failure() throws Exception {
        when(studentDao.findByEmailIgnoreCase("test@test.com")).thenReturn(any(Student.class));

        mockMvc.perform(post("/students/process")
                    .param("id", (String) null)
                    .param("name", "E S")
                    .param("email", "test@test.com")
                    .param("address", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("student/student_form"));
    }

    @Test
    void getStudent_return_expected_data() throws Exception {
        Integer id = 1;
        when(studentService.findById(id)).thenReturn(STUDENT_VIEW);
        when(courseService.findByStudentId(id)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", STUDENT_VIEW))
                .andExpect(model().attribute("courses", new ArrayList<>()))
                .andExpect(view().name("student/student_view"));
    }

    @Test
    void getStudent_return_null_return_4xxClientError() throws Exception {
        Integer id = 2;
        when(studentService.findById(id)).thenReturn(null);


        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("errors/error_response"))
                .andExpect(model().attributeExists("errorMap"));
    }

    @Test
    void showUpdateForm_successfully_populate_form() throws Exception {
        Integer id = 1;
        when(studentService.findById(id)).thenReturn(STUDENT_VIEW);

        mockMvc.perform(get("/students/{id}/update", id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("form"))
                .andExpect(view().name("student/student_update_form"));
    }

    @Test
    void showUpdateForm_invalid_id_return_4xxClientError() throws Exception {
        Integer id = 2;
        when(studentService.findById(id)).thenReturn(null);

        mockMvc.perform(get("/students/{id}/update", id))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void processUpdateForm_success() throws Exception {
        Integer id = 1;
        when(studentService.searchByEmail(anyString())).thenReturn(STUDENT_VIEW);
        when(studentService.update(new UpdateStudentForm(1, "Testy Testsson", "test@test.com", "Test street 2")))
                .thenReturn(new StudentView(1, "Testy Testsson", "test@test.com", "Test street 2"));

        mockMvc.perform(post("/students/{id}/update", id)
                        .param("id", String.valueOf(1))
                        .param("name", "Testy Testsson")
                        .param("email", "test@test.com")
                        .param("address", "Test street 2"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void processUpdateForm_failure() throws Exception {
        Integer id = 2;
        when(studentService.searchByEmail(anyString())).thenReturn(STUDENT_VIEW);

        mockMvc.perform(post("/students/{id}/update", id)
                .param("id", String.valueOf(id))
                .param("name", "Nisse Nys")
                .param("email", "nisse@test.com")
                .param("address", "Test street 3"))
                .andExpect(model().hasErrors())
                .andExpect(view().name("student/student_update_form"));
    }

    @Test
    void processUpdateForm_failure_IllegalArgumentException() throws Exception {
        Integer id = 2;

        mockMvc.perform(post("/students/{id}/update", id)
                .param("id", String.valueOf(1))
                .param("name", "Nisse Nys")
                .param("email", "nisse@test.com")
                .param("address", "Test street 3"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void delete_success() throws Exception {
        Integer id = 1;
        when(studentService.deleteStudent(id)).thenReturn(true);
        when(courseService.findByStudentId(id)).thenReturn(new ArrayList<>());
        when(studentService.findById(id)).thenReturn(null);

        mockMvc.perform(post("/students/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeDoesNotExist("message"));
    }

    @Test
    void delete_failure_model_redirects_with_message() throws Exception {
        Integer id = 1;
        when(studentService.deleteStudent(id)).thenReturn(true);
        when(courseService.findByStudentId(id))
                .thenReturn(Arrays.asList(
                        new CourseView(1, "Java advanced", LocalDate.parse("2020-05-15"), 12, Arrays.asList(STUDENT_VIEW)
                        )));

        when(studentService.findById(id)).thenReturn(null);

        mockMvc.perform(post("/students/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"));

    }
}

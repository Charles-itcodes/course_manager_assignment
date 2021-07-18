package se.lexicon.course_manager_assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.lexicon.course_manager_assignment.data.service.course.CourseService;
import se.lexicon.course_manager_assignment.data.service.student.StudentService;
import se.lexicon.course_manager_assignment.dto.forms.CreateStudentForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateStudentForm;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.exception.ResourceNotFoundException;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentController {

    private StudentService studentService;
    private CourseService courseService;


    @Autowired
    public StudentController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/students")
    public String find(@RequestParam(name = "type", defaultValue = "all") String type,
                       @RequestParam(name = "value", required = false) String value,
                       Model model){

        if(value == null){
            value = "";
        }

        String message = "Couldn't find any result that matches " + value;

        switch(type){
            case "full_name":
                List<StudentView> studentViews = studentService.searchByName(value);
                if(studentViews == null) studentViews = new ArrayList<>();
                if(studentViews.isEmpty()){
                    model.addAttribute("message", message);
                }
                model.addAttribute("students", studentViews);
                break;
            case "email":
                StudentView student = studentService.searchByEmail(value);
                if(student != null){
                    return "redirect:/students/"+student.getId();
                }else {
                    model.addAttribute("message", message);
                }
                break;
            default:
                model.addAttribute("students", studentService.findAll());
        }
        return "student/students_view";
    }

    @GetMapping("/students/create")
    public String showCreateForm(Model model){
        if(!model.containsAttribute("form")){
            CreateStudentForm form = new CreateStudentForm();
            model.addAttribute("form", form);
        }
        return "student/student_form";
    }

    @PostMapping("/students/process")
    public String processForm(@Valid @ModelAttribute(name = "form") CreateStudentForm form, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "student/student_form";
        }

        studentService.create(form);
        return "redirect:/students";
    }

    @GetMapping("/students/{id}")
    public String getStudent(@PathVariable("id") Integer id, Model model){
        StudentView studentView = studentService.findById(id);
        if(studentView == null) throw new ResourceNotFoundException("Could'nt find Student with id: " + id);

        List<CourseView> courses = courseService.findByStudentId(id);
        model.addAttribute("student", studentView);
        model.addAttribute("courses", courses);
        return "student/student_view";
    }

    @GetMapping("/students/{id}/update")
    public String showUpdateForm(@PathVariable(name = "id") Integer id, Model model){
        StudentView student = studentService.findById(id);
        if(student == null) throw new ResourceNotFoundException("Student with id: " + id + " could not be found");

        model.addAttribute("form", new UpdateStudentForm(student.getId(), student.getName(), student.getEmail(), student.getAddress()));
        return "student/student_update_form";
    }

    @PostMapping("/students/{id}/update")
    public String processUpdateForm(@PathVariable(name = "id") Integer id, @Valid @ModelAttribute(name = "form") UpdateStudentForm form, BindingResult bindingResult){
        if(!id.equals(form.getId())){
            throw new IllegalArgumentException("PathVariable id did not match form.id");
        }
        StudentView student = studentService.searchByEmail(form.getEmail());
        if(student != null){
            if(!Integer.valueOf(student.getId()).equals(id)){
                FieldError error = new FieldError("form", "email", "A student with this email already exists");
                bindingResult.addError(error);
            }
        }

        if(bindingResult.hasErrors()){
            return "student/student_update_form";
        }

        studentService.update(form);

        return "redirect:/students/"+id;
    }

    @PostMapping("/students/{id}/delete")
    public ModelAndView delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:/students");
        boolean deleted = studentService.deleteStudent(id);

        if(deleted){
            List<CourseView> courseViews = courseService.findByStudentId(id);
            StudentView studentView = studentService.findById(id);
            deleted = (studentView == null) && ((courseViews == null) || (courseViews.isEmpty()));
        }
        if(!deleted){
            redirectAttributes.addFlashAttribute("message", "Warning: delete command may have failed. You need to remove the student from all courses");
        }
        return model;
    }
}

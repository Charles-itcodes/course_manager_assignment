package se.lexicon.course_manager_assignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.lexicon.course_manager_assignment.data.service.course.CourseService;
import se.lexicon.course_manager_assignment.data.service.student.StudentService;
import se.lexicon.course_manager_assignment.dto.forms.CreateCourseForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateCourseForm;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.exception.ResourceNotFoundException;


import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CourseController {
    private CourseService courseService;
    private StudentService studentService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    public CourseController(CourseService courseService, StudentService studentService) {
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @GetMapping("/courses")
    public String find(
            @RequestParam(name = "type", defaultValue = "all") String type,
            @RequestParam(name = "value", required = false) String value,
            Model model){
        if (value == null){
            value = "";
        }

        LocalDate date = null;
        if(type.equals("courseStartBefore") || type.equals("courseStartAfter")){
            try{
                date = LocalDate.parse(value);
            }catch (DateTimeParseException ex){
                LOGGER.warn(ex.toString());
            }

        }


        List<CourseView> courseViews = new ArrayList<>();
        String message = "Couldn't find any result that matches " + value;

        switch (type){
            case "name":
                courseViews = courseService.searchByCourseName(value);
                courseViews = courseViews == null ? new ArrayList<>() : courseViews;
                model.addAttribute("courses", courseViews);
                if(courseViews == null || courseViews.isEmpty()) model.addAttribute("message", message);
                break;
            case "courseStartBefore":
                if(date == null){
                    model.addAttribute("message", message);
                }else {
                    courseViews = courseService.searchByDateBefore(date);
                    if(courseViews == null || courseViews.isEmpty()) model.addAttribute("message", message);
                }
                model.addAttribute("courses", courseViews);
                break;
            case "courseStartAfter":
                if(date == null){
                    model.addAttribute("message", message);
                }else {
                    courseViews = courseService.searchByDateAfter(date);
                    if(courseViews == null || courseViews.isEmpty()) model.addAttribute("message", message);
                }
                model.addAttribute("courses", courseViews);
                break;
            default:
                model.addAttribute("courses", courseService.findAll());
        }
        return "/course/courses_view";
    }

    @GetMapping("/courses/create")
    public String showCreateForm(Model model){
        if(!model.containsAttribute("form")){
            model.addAttribute("form", new CreateCourseForm());
        }
        return "/course/course_form";
    }

    @PostMapping("/courses/process")
    public String processForm(@Valid @ModelAttribute(name = "form") CreateCourseForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/course/course_form";
        }
        courseService.create(form);
        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}")
    public String getCourse(@PathVariable("id") Integer id, Model model){
        CourseView courseView = courseService.findById(id);
        if(courseView == null) throw new ResourceNotFoundException("Could not find course with id: " + id);
        model.addAttribute("course", courseView);
        return "course/course_view";
    }

    @GetMapping("/courses/{id}/update")
    public String getUpdateForm(@PathVariable("id") Integer id, Model model){
        CourseView courseView = courseService.findById(id);
        if(courseView == null) throw new ResourceNotFoundException("Update aborted could'nt find course with id: " + id);
        model.addAttribute("form", new UpdateCourseForm(courseView.getId(),courseView.getCourseName(),courseView.getStartDate(),courseView.getWeekDuration()));
        return "course/course_update_form";
    }

    @PostMapping("/courses/{id}/update")
    public String updateCourse(@PathVariable("id") Integer id, @Valid @ModelAttribute(name = "form")UpdateCourseForm form, BindingResult bindingResult){
        if(!id.equals(form.getId())){
            throw new IllegalArgumentException("PathVariable id didn't match form.id");
        }
        if(bindingResult.hasErrors()){
            return "course/course_update_form";
        }

        CourseView courseView = courseService.update(form);
        if(courseView == null) throw new IllegalArgumentException("Update failed CourseView returned was null");
        return "redirect:/courses/"+courseView.getId();
    }

    @PostMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable("id") Integer id){
        boolean deleted = courseService.deleteCourse(id);
        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}/enrollment")
    public String getAvailableStudents(@PathVariable("id") Integer id, Model model){
        CourseView courseView = courseService.findById(id);
        if(courseView == null) throw new ResourceNotFoundException("Could'nt find Course with id: " + id);

        List<StudentView> studentViews = studentService.findAll();
        if(studentViews == null) studentViews = new ArrayList<>();

        List<StudentView> availableStudents = studentViews.stream()
                .filter(student -> !courseView.getStudents().contains(student))
                .collect(Collectors.toList());

        model.addAttribute("available_students", availableStudents);
        model.addAttribute("course", courseView);
        return "course/enroll_student";
    }

    @PostMapping("/courses/{id}/unregister")
    public String unregisterStudent(
            @PathVariable("id") Integer id,
            @RequestParam("studentId") Integer studentId,
            @RequestParam(name = "origin", required = false) String origin){
        if(origin == null) origin = "";

        courseService.removeStudentFromCourse(id, studentId);
        return origin.equals("student") ? "redirect:/students/"+studentId : "redirect:/courses/"+id+"/enrollment";
    }

    @PostMapping("/courses/{id}/register")
    public String registerStudent(@PathVariable("id") Integer id, @RequestParam("studentId") Integer studentId){
        courseService.addStudentToCourse(id,studentId);
        return "redirect:/courses/"+id+"/enrollment";
    }
}
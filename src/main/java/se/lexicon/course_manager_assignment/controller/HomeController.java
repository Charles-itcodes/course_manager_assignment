package se.lexicon.course_manager_assignment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = {"/", "/index", ""})
    public String getIndexPage(){
        return "index";
    }

}

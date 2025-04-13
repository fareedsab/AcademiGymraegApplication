package com.WebBased.AcademiGymraeg.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/Home")
    public String Home(Model model)
    {
        model.addAttribute("message","value");
        return "Home";
    }
    @GetMapping("/login")
    public String login(Model model)
    {
        model.addAttribute("message","value");
        return "login";
    }
    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }
//
    @GetMapping("/instructor-dashboard")
    public String instructorDashboard() {
        return "instructor-dashboard";
    }
//
//    @GetMapping("/student-dashboard")
//    public String studentDashboard() {
//        return "student-dashboard";
//    }
//
//    @GetMapping("/dashboard")
//    public String defaultDashboard() {
//        return "dashboard";
//    }
}

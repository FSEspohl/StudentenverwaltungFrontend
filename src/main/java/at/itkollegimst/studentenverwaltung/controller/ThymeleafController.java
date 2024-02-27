package at.itkollegimst.studentenverwaltung.controller;

import at.itkollegimst.studentenverwaltung.services.StudentenService;
import lombok.AllArgsConstructor;
import at.itkollegimst.studentenverwaltung.domain.Student;
import at.itkollegimst.studentenverwaltung.exceptions.StudentNichtGefunden;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class ThymeleafController {

    private final StudentenService studentService;

    @GetMapping("/web/students")
    public ModelAndView allStudents() {
        return new ModelAndView("allstudents", "studenten", studentService.alleStudenten());
    }

    @GetMapping("/web/insertstudentform")
    public ModelAndView insertStudentForm() {
        return new ModelAndView("insertstudentform", "mystudent", new Student());
    }

    @GetMapping("/web/deletestudent/{id}")
    public String deleteStudent(@PathVariable Long id, Model model) {
        try {
            studentService.studentLoeschenMitId(id);
            return "redirect:/web/students";
        } catch (StudentNichtGefunden e) {
            model.addAttribute("errortitle", "Student-Löschen schlägt fehl!");
            model.addAttribute("errormessage", e.getMessage());
            return "myerrorspage";
        }
    }

    @PostMapping("/web/insertstudent")
    public String insertStudent(@Valid @ModelAttribute("mystudent") Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "insertstudentform";
        }
        this.studentService.studentEinfuegen(student);
        return "redirect:/web/students";
    }
}
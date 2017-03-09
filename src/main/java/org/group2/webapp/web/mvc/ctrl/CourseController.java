package org.group2.webapp.web.mvc.ctrl;

import org.group2.webapp.entity.Course;
import org.group2.webapp.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/course")
public class CourseController {

    private final Logger log = LoggerFactory.getLogger(CourseController.class);

    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "admin/course/courses";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam String code, Model model) {
        Course course = courseService.findOne(code);
        if (course == null)
            return index(model);
        model.addAttribute("course", course);
        return "admin/course/detail";
    }

    @GetMapping("/new")
    public String newCourse(Model model) {
        model.addAttribute("course", new Course());
        return "admin/course/add";
    }

    @PostMapping("/new")
    public String newCourse(@Valid @RequestBody Course course, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "admin/course/add";
        else
            return index(model);
    }

    @GetMapping("/edit")
    public String editCourse(@RequestParam String code, Model model) {
        Course course = courseService.findOne(code);
        if (course == null)
            return index(model);

        model.addAttribute("course", course);
        return "admin/course/edit";
    }

    @PostMapping("/edit")
    public String editCourse(@Valid @RequestBody Course course, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "admin/course/edit";
        else
            return index(model);
    }


    @PostMapping("/delete")
    public String deleteCourse(@RequestParam String code, Model model) {
        courseService.delete(code);
        return index(model);
    }
}

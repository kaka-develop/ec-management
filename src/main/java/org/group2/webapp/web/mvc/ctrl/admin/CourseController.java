package org.group2.webapp.web.mvc.ctrl.admin;

<<<<<<< HEAD
import javax.validation.Valid;

import org.group2.webapp.entity.Assessment;
=======
import org.apache.commons.lang3.StringUtils;
import org.group2.webapp.entity.Item;
import org.group2.webapp.entity.Item;
>>>>>>> branch 'master' of https://github.com/EnterpriseWebSoftwareDevelopmentSEM7-G2/ec-management
import org.group2.webapp.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/course")
public class CourseController {

    private final Logger log = LoggerFactory.getLogger(CourseController.class);

    public static final String REDIRECT_INDEX = "redirect:/admin/course";

    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "admin/course/courses";
    }

    @GetMapping("/detail/{code}")
    public String detail(@PathVariable String code, Model model) {
        Item course = courseService.findOne(code);
        if (course == null)
            return REDIRECT_INDEX;
        model.addAttribute("course", course);
        return "admin/course/detail";
    }

    @GetMapping("/new")
    public String newCourse(Model model) {
        model.addAttribute("course", new Item());
        return "admin/course/add";
    }

    @PostMapping("/new")
    public String newCourse(@Valid @ModelAttribute Item course, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/course/add";
        else
            courseService.create(course);
        return REDIRECT_INDEX;
    }

    @GetMapping("/edit/{code}")
    public String editCourse(@PathVariable String code, Model model) {
        Item course = courseService.findOne(code);
        if (course == null)
            return REDIRECT_INDEX;

        model.addAttribute("course", course);
        return "admin/course/edit";
    }

    @PostMapping("/edit")
    public String editCourse(@Valid @ModelAttribute Item course, BindingResult bindingResult) {
        log.debug("");
        if (bindingResult.hasErrors())
            return "admin/course/edit";
        else
            courseService.update(course);
        return REDIRECT_INDEX;
    }


    @PostMapping("/delete/{code}")
    public String deleteCourse(Model model,@PathVariable String code) {
        try{
            courseService.delete(code);
        }catch (Exception e){
            model.addAttribute("error","Cannot delete this course! Had assessment");
        }
        model.addAttribute("courses", courseService.findAll());
        return "admin/course/courses";
    }
}

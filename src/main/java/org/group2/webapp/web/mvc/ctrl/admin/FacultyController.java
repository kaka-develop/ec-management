package org.group2.webapp.web.mvc.ctrl.admin;

import org.apache.commons.lang3.StringUtils;
import org.group2.webapp.entity.Faculty;
import org.group2.webapp.service.FacultyService;
import org.group2.webapp.util.ConvertUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/faculty")
public class FacultyController {

    private final Logger log = LoggerFactory.getLogger(FacultyController.class);

    public static final String REDIRECT_INDEX = "redirect:/admin/faculty";

    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        model.addAttribute("faculties", facultyService.findAll());
        return "admin/faculty/faculties";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable String id, Model model) {
        Faculty faculty = facultyService.findOne(ConvertUntil.covertStringToLong(id));
        if (faculty == null)
            return REDIRECT_INDEX;
        model.addAttribute("faculty", faculty);
        return "admin/faculty/detail";
    }

    @GetMapping("/new")
    public String newFaculty(Model model) {
        model.addAttribute("faculty", new Faculty());
        return "admin/faculty/add";
    }

    @PostMapping("/new")
    public String newFaculty(@Valid @ModelAttribute Faculty faculty, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/faculty/add";
        else
            facultyService.create(faculty);
        return REDIRECT_INDEX;
    }

    @GetMapping("/edit/{id}")
    public String editFaculty(@PathVariable String id, Model model) {
        Faculty faculty = facultyService.findOne(ConvertUntil.covertStringToLong(id));
        if (faculty == null)
            return REDIRECT_INDEX;

        model.addAttribute("faculty", faculty);
        return "admin/faculty/edit";
    }

    @PostMapping("/edit")
    public String editFaculty(@Valid @ModelAttribute Faculty faculty, BindingResult bindingResult) {
        log.debug("");
        if (bindingResult.hasErrors())
            return "admin/faculty/edit";
        else
            facultyService.update(faculty);
        return REDIRECT_INDEX;
    }


    @PostMapping("/delete/{id}")
    public String deleteFaculty(@PathVariable String id) {
        facultyService.delete(ConvertUntil.covertStringToLong(id));
        return REDIRECT_INDEX;
    }
}

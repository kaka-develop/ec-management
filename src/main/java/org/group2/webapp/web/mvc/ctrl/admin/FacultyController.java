package org.group2.webapp.web.mvc.ctrl.admin;

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

@Controller
@RequestMapping("/admin/faculty")
public class FacultyController {

    private final Logger log = LoggerFactory.getLogger(FacultyController.class);

    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        model.addAttribute("faculties", facultyService.findAll());
        return "admin/faculty/faculties";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam String id, Model model) {
        Faculty faculty = facultyService.findOne(ConvertUntil.covertStringToLong(id));
        if (faculty == null)
            return index(model);
        model.addAttribute("faculty", faculty);
        return "admin/faculty/detail";
    }

    @GetMapping("/new")
    public String newFaculty(Model model) {
        model.addAttribute("faculty", new Faculty());
        return "admin/faculty/add";
    }

    @PostMapping("/new")
    public String newFaculty(@Valid @RequestBody Faculty faculty, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "admin/faculty/add";
        else
            return index(model);
    }

    @GetMapping("/edit")
    public String editFaculty(@RequestParam String id, Model model) {
        Faculty faculty = facultyService.findOne(ConvertUntil.covertStringToLong(id));
        if (faculty == null)
            return index(model);

        model.addAttribute("faculty", faculty);
        return "admin/faculty/edit";
    }

    @PostMapping("/edit")
    public String editFaculty(@Valid @RequestBody Faculty faculty, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "admin/faculty/edit";
        else
            return index(model);
    }


    @PostMapping("/delete")
    public String deleteFaculty(@RequestParam String id, Model model) {
        facultyService.delete(ConvertUntil.covertStringToLong(id));
        return index(model);
    }
}

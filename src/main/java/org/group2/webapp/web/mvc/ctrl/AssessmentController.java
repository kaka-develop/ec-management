package org.group2.webapp.web.mvc.ctrl;

import org.group2.webapp.entity.Assessment;
import org.group2.webapp.service.AssessmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/assessment")
public class AssessmentController {

    private final Logger log = LoggerFactory.getLogger(AssessmentController.class);

    private AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        model.addAttribute("assessments", assessmentService.findAll());
        return "admin/assessment/assessments";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam String crn, Model model) {
        Assessment assessment = assessmentService.findOne(crn);
        if (assessment == null)
            return index(model);
        model.addAttribute("assessment", assessment);
        return "admin/assessment/detail";
    }

    @GetMapping("/new")
    public String newAssessment(Model model) {
        model.addAttribute("assessment", new Assessment());
        return "admin/assessment/add";
    }

    @PostMapping("/new")
    public String newAssessment(@Valid @RequestBody Assessment assessment, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "admin/assessment/add";
        else
            return index(model);
    }

    @GetMapping("/edit")
    public String editAssessment(@RequestParam String crn, Model model) {
        Assessment assessment = assessmentService.findOne(crn);
        if (assessment == null)
            return index(model);

        model.addAttribute("assessment", assessment);
        return "admin/assessment/edit";
    }

    @PostMapping("/edit")
    public String editAssessment(@Valid @RequestBody Assessment assessment, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "admin/assessment/edit";
        else
            return index(model);
    }


    @PostMapping("/delete")
    public String deleteAssessment(@RequestParam String crn, Model model) {
        assessmentService.delete(crn);
        return index(model);
    }
}

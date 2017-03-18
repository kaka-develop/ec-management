package org.group2.webapp.web.mvc.ctrl.admin;

import org.group2.webapp.entity.Assessment;
import org.group2.webapp.service.AssessItemService;
import org.group2.webapp.service.AssessmentService;
import org.group2.webapp.util.ConvertUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/assessment")
public class AssessmentController {

    private final Logger log = LoggerFactory.getLogger(AssessmentController.class);

    public static final String REDIRECT_INDEX = "redirect:/admin/assessment";

    private AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }


    @GetMapping(value = {"/", ""})
    public String index(Model model) {
        model.addAttribute("assessments", assessmentService.findAll());
        return "admin/assessment/assessments";
    }

    @GetMapping("/detail/{crn}")
    public String detail(@PathVariable String crn, Model model) {
        Assessment assessment = assessmentService.findOne(crn);
        if (assessment == null)
            return REDIRECT_INDEX;
        model.addAttribute("assessment", assessment);
        model.addAttribute("items",assessmentService.getNotExistedItems(assessment));
        return "admin/assessment/detail";
    }

    @GetMapping("/items/{crn}")
    public String getItems(@PathVariable String crn, Model model) {
        Assessment assessment = assessmentService.findOne(crn);
        if (assessment == null)
            return REDIRECT_INDEX;
        model.addAttribute("items", assessment.getAssessItems());
        return "admin/assessment/items";
    }

    @PostMapping("/items/new")
    public String addItem(@RequestParam String itemId, @RequestParam(required = false) String crn) {
        if (assessmentService.addItem(ConvertUntil.covertStringToLong(itemId), crn))
            return REDIRECT_INDEX;
        else
            return "admin/assessment/detail";
    }

    @GetMapping("/new")
    public String newAssessment(Model model) {
        model.addAttribute("assessment", new Assessment());
        return "admin/assessment/add";
    }

    @PostMapping("/new")
    public String newAssessment(@Valid @ModelAttribute Assessment assessment, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/assessment/add";
        else
            assessmentService.create(assessment);
        return REDIRECT_INDEX;
    }

    @GetMapping("/edit/{crn}")
    public String editAssessment(@PathVariable String crn, Model model) {
        Assessment assessment = assessmentService.findOne(crn);
        if (assessment == null)
            return REDIRECT_INDEX;

        model.addAttribute("assessment", assessment);
        return "admin/assessment/edit";
    }

    @PostMapping("/edit")
    public String editAssessment(@Valid @ModelAttribute Assessment assessment, BindingResult bindingResult) {
        log.debug("");
        if (bindingResult.hasErrors())
            return "admin/assessment/edit";
        else
            assessmentService.update(assessment);
        return REDIRECT_INDEX;
    }


    @PostMapping("/delete/{crn}")
    public String deleteAssessment(@PathVariable String crn) {
        assessmentService.delete(crn);
        return REDIRECT_INDEX;
    }
}

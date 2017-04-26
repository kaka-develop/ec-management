package org.group2.webapp.web.mvc.ctrl.admin;

import javax.validation.Valid;

import org.group2.webapp.entity.Assessment;
import org.group2.webapp.service.AssessmentService;
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

    @GetMapping("/detail/{code}")
    public String detail(@PathVariable String code, Model model) {
        Assessment assessment = assessmentService.findOne(code);
        if (assessment == null)
            return REDIRECT_INDEX;
        model.addAttribute("assessment", assessment);
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

    @GetMapping("/edit/{code}")
    public String editAssessment(@PathVariable String code, Model model) {
        Assessment assessment = assessmentService.findOne(code);
        if (assessment == null)
            return REDIRECT_INDEX;

        model.addAttribute("assessment", assessment);
        return "admin/assessment/edit";
    }

    @PostMapping("/edit")
    public String editAssessment(@Valid @ModelAttribute Assessment assessment, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/assessment/edit";
        else
            assessmentService.update(assessment);
        return REDIRECT_INDEX;
    }


    @PostMapping("/delete/{code}")
    public String deleteAssessment(Model model,@PathVariable String code) {
        try{
            assessmentService.delete(code);
        }catch (Exception e){
            model.addAttribute("error","Cannot delete this assessment! Had claim");
        }

        model.addAttribute("assessments", assessmentService.findAll());
        return "admin/assessment/assessments";
    }
}

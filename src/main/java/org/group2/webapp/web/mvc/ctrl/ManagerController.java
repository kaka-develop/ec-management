package org.group2.webapp.web.mvc.ctrl;

import org.group2.webapp.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }


    @GetMapping(value = {"/", ""})
    public String index() {
        return "manager/manager";
    }

    @GetMapping("/claims")
    public String findAllClaims(Model model) {
        model.addAttribute("claims", managerService.findAllClaims());
        return "manager/claims";
    }

    @GetMapping("/claims/process")
    public String process(Model model) {
        model.addAttribute("claims", managerService.findAllClaims());
        return "manager/claims-process";
    }

    @GetMapping("/statistics")
    public String getClaimStatistics(Model model) {


        return "manager/statistics";
    }

    @GetMapping("/claims/faculty")
    public String getClaimsByFaculty(Model model) {
        model.addAttribute("claims", managerService.getClaimsPerFaculty());
        return "manager/claims-faculty";
    }

    @GetMapping("/claims/year")
    public String getClaimsByYear(Model model) {
        model.addAttribute("claims", managerService.getClamsPerYear());
        return "manager/claims-year";
    }

    @GetMapping("/claims/faculty/processed")
    public String getProcessedClaimsByFaculty(Model model) {
        model.addAttribute("claims", managerService.getProcessedClaimsPerFaculty());
        return "manager/claims-faculty-processed";
    }

    @GetMapping("/claims/circumstance")
    public String getClaimsByCircumstance(Model model) {
        model.addAttribute("claims", managerService.getClaimsPerCircumstance());
        return "manager/claims-circumstance";
    }

    @GetMapping("/claims/validation")
    public String getClaimsByValidation(Model model) {
        model.addAttribute("claims", managerService.getValidAndInvalidClaims());
        return "manager/claims-validation";
    }

    @GetMapping("/claims/thismonth")
    public String getClaimsInThisMonth(Model model) {
        model.addAttribute("claims", managerService.findAllClaimsInThisMonth());
        return "manager/claims-thismonth";
    }

    @GetMapping("/claims/thisweek")
    public String getClaimsInThisWeek(Model model) {
        model.addAttribute("claims", managerService.findAllClaimsInThisWeek());
        return "manager/claims-thisweek";
    }

    @GetMapping("/claims/custom")
    public String getCustomClaims() {
        return "manager/claims-custom";
    }

    @PostMapping("/claims/custom")
    public String postCustomClaims(Model model, @RequestParam String reportType, @RequestParam(required = false) String month, @RequestParam(required = false) String year) {
        model.addAttribute("claims",managerService.getCustomClaimReport(reportType,month,year));
        return "manager/claims-custom";
    }
}

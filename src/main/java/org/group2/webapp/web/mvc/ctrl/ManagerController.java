package org.group2.webapp.web.mvc.ctrl;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.Faculty;
import org.group2.webapp.service.ClaimService;
import org.group2.webapp.service.FacultyService;
import org.group2.webapp.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
}

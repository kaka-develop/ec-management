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
        model.addAttribute("claimsPerFaculty", managerService.getClaimsPerFaculty());
        model.addAttribute("claimsPerYear", managerService.getClamsPerYear());
        return "manager/statistics";
    }
}

package org.group2.webapp.web.mvc.ctrl.admin;

import org.apache.commons.lang3.StringUtils;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.service.ClaimService;
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
@RequestMapping("/admin/claim")
public class ClaimController {

    private final Logger log = LoggerFactory.getLogger(ClaimController.class);

    private ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        model.addAttribute("claims", claimService.findAll());
        return "admin/claim/claims";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam String id, Model model) {
        Claim claim = claimService.findOne(ConvertUntil.covertStringToLong(id));
        if (claim == null)
            return index(model);
        model.addAttribute("claim", claim);
        return "admin/claim/detail";
    }

    @GetMapping("/year")
    public String allByYear(@RequestParam String year, Model model) {
        if( year == null || !StringUtils.isNumeric(year))
            return index(model);
        List<Claim> claims = claimService.findClaimsByYear(Integer.parseInt(year));
        model.addAttribute("claims", claims);
        return "admin/claim/year";
    }

    @GetMapping("/new")
    public String newClaim(Model model) {
        model.addAttribute("claim", new Claim());
        return "admin/claim/add";
    }

    @PostMapping("/new")
    public String newClaim(@Valid @RequestBody Claim claim, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "admin/claim/add";
        else
            return index(model);
    }

    @GetMapping("/edit")
    public String editClaim(@RequestParam String id, Model model) {
        Claim claim = claimService.findOne(ConvertUntil.covertStringToLong(id));
        if (claim == null)
            return index(model);

        model.addAttribute("claim", claim);
        return "admin/claim/edit";
    }

    @PostMapping("/edit")
    public String editClaim(@Valid @RequestBody Claim claim, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "admin/claim/edit";
        else
            return index(model);
    }


    @PostMapping("/delete")
    public String deleteClaim(@RequestParam String id, Model model) {
        claimService.delete(ConvertUntil.covertStringToLong(id));
        return index(model);
    }
}

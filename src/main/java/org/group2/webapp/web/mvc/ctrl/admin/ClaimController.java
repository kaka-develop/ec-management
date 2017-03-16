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

    public static final String REDIRECT_INDEX = "redirect:/admin/claim";

    private ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        model.addAttribute("claims", claimService.findAll());
        return "admin/claim/claims";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable String id, Model model) {
        Claim claim = claimService.findOne(ConvertUntil.covertStringToLong(id));
        if (claim == null)
            return REDIRECT_INDEX;
        model.addAttribute("claim", claim);
        return "admin/claim/detail";
    }

    @GetMapping("/year")
    public String allByYear(@RequestParam String year, Model model) {
        if( year == null || !StringUtils.isNumeric(year) || year.isEmpty())
            return REDIRECT_INDEX;
        List<Claim> claims = claimService.findClaimsByYear(Integer.parseInt(year));
        model.addAttribute("claims", claims);
        return "admin/claim/claims";
    }

    @GetMapping("/new")
    public String newClaim(Model model) {
        model.addAttribute("claim", new Claim());
        return "admin/claim/add";
    }

    @PostMapping("/new")
    public String newClaim(@Valid @ModelAttribute Claim claim, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/claim/add";
        else
        claimService.create(claim);
            return REDIRECT_INDEX;
    }

    @GetMapping("/edit/{id}")
    public String editClaim(@PathVariable String id, Model model) {
        Claim claim = claimService.findOne(ConvertUntil.covertStringToLong(id));
        if (claim == null)
            return REDIRECT_INDEX;

        model.addAttribute("claim", claim);
        return "admin/claim/edit";
    }

    @PostMapping("/edit")
    public String editClaim(@Valid @ModelAttribute Claim claim, BindingResult bindingResult) {
        log.debug("");
        if (bindingResult.hasErrors())
            return "admin/claim/edit";
        else
            claimService.update(claim);
            return REDIRECT_INDEX;
    }


    @PostMapping("/delete/{id}")
    public String deleteClaim(@PathVariable String id) {
        claimService.delete(ConvertUntil.covertStringToLong(id));
        return REDIRECT_INDEX;
    }
}

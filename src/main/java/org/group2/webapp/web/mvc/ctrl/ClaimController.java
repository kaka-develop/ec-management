package org.group2.webapp.web.mvc.ctrl;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.service.ClaimService;
import org.group2.webapp.web.rest.UserAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/claim")
public class ClaimController {

    private final Logger log = LoggerFactory.getLogger(ClaimController.class);

    private ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        model.addAttribute("claims", claimService.findAll());
        return "claim/claims";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam String id, Model model) {
        Claim claim = claimService.findOne(new Long(id));
        if (claim == null)
            return index(model);
        model.addAttribute("claim", claim);
        return "claim/detail";
    }

    @GetMapping("/new")
    public String newClaim(Model model) {
        model.addAttribute("claim", new Claim());
        return "claim/add";
    }

    @PostMapping("/new")
    public String newClaim(@Valid @RequestBody Claim claim, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "claim/add";
        else
            return index(model);
    }

    @GetMapping("/edit")
    public String editClaim(@RequestParam String id, Model model) {
        Claim claim = claimService.findOne(new Long(id));
        if (claim == null)
            return index(model);

        model.addAttribute("claim", claim);
        return "claim/edit";
    }

    @PostMapping("/edit")
    public String editClaim(@Valid @RequestBody Claim claim, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "claim/edit";
        else
            return index(model);
    }


    @PostMapping("/delete")
    public String deleteClaim(@RequestParam String id, Model model) {
        claimService.delete(new Long(id));
        return index(model);
    }
}

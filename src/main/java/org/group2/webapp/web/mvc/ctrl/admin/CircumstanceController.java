package org.group2.webapp.web.mvc.ctrl.admin;

import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.service.CircumstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/circumstance")
public class CircumstanceController {

    private final Logger log = LoggerFactory.getLogger(CircumstanceController.class);

    private CircumstanceService circumstanceService;

    public CircumstanceController(CircumstanceService circumstanceService) {
        this.circumstanceService = circumstanceService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        model.addAttribute("circumstances", circumstanceService.findAll());
        return "admin/circumstance/circumstances";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam String id, Model model) {
        Circumstance circumstance = circumstanceService.findOne(new Long(id));
        if (circumstance == null)
            return index(model);
        model.addAttribute("circumstance", circumstance);
        return "admin/circumstance/detail";
    }

    @GetMapping("/new")
    public String newCircumstance(Model model) {
        model.addAttribute("circumstance", new Circumstance());
        return "admin/circumstance/add";
    }

    @PostMapping("/new")
    public String newCircumstance(@Valid @RequestBody Circumstance circumstance, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "admin/circumstance/add";
        else
            return index(model);
    }

    @GetMapping("/edit")
    public String editCircumstance(@RequestParam String id, Model model) {
        Circumstance circumstance = circumstanceService.findOne(new Long(id));
        if (circumstance == null)
            return index(model);

        model.addAttribute("circumstance", circumstance);
        return "admin/circumstance/edit";
    }

    @PostMapping("/edit")
    public String editCircumstance(@Valid @RequestBody Circumstance circumstance, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "admin/circumstance/edit";
        else
            return index(model);
    }


    @PostMapping("/delete")
    public String deleteCircumstance(@RequestParam String id, Model model) {
        circumstanceService.delete(new Long(id));
        return index(model);
    }
}

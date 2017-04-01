package org.group2.webapp.web.mvc.ctrl.admin;

import org.apache.commons.lang3.StringUtils;
import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.service.CircumstanceService;
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
@RequestMapping("/admin/circumstance")
public class CircumstanceController {

    private final Logger log = LoggerFactory.getLogger(CircumstanceController.class);

    public static final String REDIRECT_INDEX = "redirect:/admin/circumstance";

    private CircumstanceService circumstanceService;

    public CircumstanceController(CircumstanceService circumstanceService) {
        this.circumstanceService = circumstanceService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        model.addAttribute("circumstances", circumstanceService.findAll());
        return "admin/circumstance/circumstances";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable String id, Model model) {
        Circumstance circumstance = circumstanceService.findOne(ConvertUntil.covertStringToLong(id));
        if (circumstance == null)
            return REDIRECT_INDEX;
        model.addAttribute("circumstance", circumstance);
        return "admin/circumstance/detail";
    }

    @GetMapping("/new")
    public String newCircumstance(Model model) {
        model.addAttribute("circumstance", new Circumstance());
        return "admin/circumstance/add";
    }

    @PostMapping("/new")
    public String newCircumstance(@Valid @ModelAttribute Circumstance circumstance, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/circumstance/add";
        else
            circumstanceService.create(circumstance);
        return REDIRECT_INDEX;
    }

    @GetMapping("/edit/{id}")
    public String editCircumstance(@PathVariable String id, Model model) {
        Circumstance circumstance = circumstanceService.findOne(ConvertUntil.covertStringToLong(id));
        if (circumstance == null)
            return REDIRECT_INDEX;

        model.addAttribute("circumstance", circumstance);
        return "admin/circumstance/edit";
    }

    @PostMapping("/edit")
    public String editCircumstance(@Valid @ModelAttribute Circumstance circumstance, BindingResult bindingResult) {
        log.debug("");
        if (bindingResult.hasErrors())
            return "admin/circumstance/edit";
        else
            circumstanceService.update(circumstance);
        return REDIRECT_INDEX;
    }


    @PostMapping("/delete/{id}")
    public String deleteCircumstance(Model model,@PathVariable String id) {
        try {
            circumstanceService.delete(ConvertUntil.covertStringToLong(id));
        }catch (Exception e){
            model.addAttribute("error","Cannot delete this circumstance! Had claim");
        }
        model.addAttribute("circumstances", circumstanceService.findAll());
        return "admin/circumstance/circumstances";
    }
}

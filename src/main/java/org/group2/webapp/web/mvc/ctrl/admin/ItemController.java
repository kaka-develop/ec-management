package org.group2.webapp.web.mvc.ctrl.admin;


import org.group2.webapp.entity.Item;
import org.group2.webapp.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/item")
public class ItemController {

    private final Logger log = LoggerFactory.getLogger(ItemController.class);

    public static final String REDIRECT_INDEX = "redirect:/admin/item";

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        model.addAttribute("items", itemService.findAll());
        return "admin/item/items";
    }

    @GetMapping("/detail/{code}")
    public String detail(@PathVariable String code, Model model) {
        Item item = itemService.findOne(code);
        if (item == null)
            return REDIRECT_INDEX;
        model.addAttribute("item", item);
        return "admin/item/detail";
    }

    @GetMapping("/new")
    public String newItem(Model model) {
        model.addAttribute("item", new Item());
        return "admin/item/add";
    }

    @PostMapping("/new")
    public String newItem(@Valid @ModelAttribute Item item, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/item/add";
        else
            itemService.create(item);
        return REDIRECT_INDEX;
    }

    @GetMapping("/edit/{code}")
    public String editItem(@PathVariable String code, Model model) {
        Item item = itemService.findOne(code);
        if (item == null)
            return REDIRECT_INDEX;

        model.addAttribute("item", item);
        return "admin/item/edit";
    }

    @PostMapping("/edit")
    public String editItem(@Valid @ModelAttribute Item item, BindingResult bindingResult) {
        log.debug("");
        if (bindingResult.hasErrors())
            return "admin/item/edit";
        else
            itemService.update(item);
        return REDIRECT_INDEX;
    }


    @PostMapping("/delete/{code}")
    public String deleteItem(Model model,@PathVariable String code) {
        try{
            itemService.delete(code);
        }catch (Exception e){
            model.addAttribute("error","Cannot delete this item! Had assessment");
        }
        model.addAttribute("items", itemService.findAll());
        return "admin/item/items";
    }
}

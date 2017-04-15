package org.group2.webapp.web.mvc.ctrl.admin;

import java.util.List;

import javax.validation.Valid;

import org.group2.webapp.service.UserService;
import org.group2.webapp.service.dto.UserDTO;
import org.group2.webapp.web.mvc.vm.UserVM;
import org.group2.webapp.web.rest.admin.UserAPI;
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
@RequestMapping("/admin/user")
public class UserController {

    private final UserService userService;


    private final Logger log = LoggerFactory.getLogger(UserAPI.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public static final String REDIRECT_INDEX = "redirect:/admin/user";


    @GetMapping(value = {"/", ""})
    public String index(Model model) {
        List<UserDTO> users = userService.getAllManagedUsers();
        model.addAttribute("users", users);
        return "admin/user/users";
    }

    @GetMapping("/detail/{username}")
    public String detail(@PathVariable String username, Model model) {
        UserDTO userDTO = userService.findOneByUsername(username);
        if (userDTO == null)
            return REDIRECT_INDEX;
        UserVM userVM = new UserVM(userDTO);
        model.addAttribute("user", userVM);
        return "admin/user/detail";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserVM());
        return "admin/user/add";
    }

    @PostMapping(value = "/new")
    public String newUser(@Valid @ModelAttribute UserVM user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/user/add";
        userService.createUser(user);
        return REDIRECT_INDEX;
    }

    @GetMapping("/edit/{username}")
    public String editUser(@PathVariable String username, Model model) {
        UserDTO userDTO = userService.findOneByUsername(username);
        if (userDTO == null)
            return REDIRECT_INDEX;
        UserVM userVM = new UserVM(userDTO);
        model.addAttribute("user", userVM);
        return "admin/user/edit";
    }

    @PostMapping(value = "/edit")
    public String editUser(@Valid @ModelAttribute UserVM user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/user/edit";
        else
            userService.updateUser(user);
        return REDIRECT_INDEX;
    }


    @PostMapping("/delete/{username}")
    public String deleteUser(Model model,@PathVariable String username) {
        try {
            userService.deleteUser(username);
        } catch (Exception e) {
            model.addAttribute("error","Cannot delete this user! Had claims");
        }
        model.addAttribute("users", userService.getAllManagedUsers());
        return "admin/user/users";
    }

}

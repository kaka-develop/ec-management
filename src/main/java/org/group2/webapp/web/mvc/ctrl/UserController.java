package org.group2.webapp.web.mvc.ctrl;

import org.group2.webapp.service.UserService;
import org.group2.webapp.service.dto.UserDTO;
import org.group2.webapp.web.mvc.vm.UserVM;
import org.group2.webapp.web.rest.UserAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    private final Logger log = LoggerFactory.getLogger(UserAPI.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = {"/",""})
    public String index(Model model) {
        List<UserDTO> users = userService.getAllManagedUsers();
        model.addAttribute("users", users);
        return "user/users";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam String username, Model model){
        UserDTO userDTO = userService.findOneByUsername(username);
        if(userDTO == null)
            return index(model);
        UserVM userVM = new UserVM(userDTO);
        model.addAttribute("user",userVM);
        return "user/detail";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user",new UserVM());
        return "user/add";
    }

    @PostMapping("/new")
    public String newUser(@Valid @RequestBody UserVM userVM, BindingResult bindingResult,Model model) {
        if(bindingResult.hasErrors())
            return "user/add";
        else
            userService.createUser(userVM);
        return index(model);
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam String username, Model model) {
        UserDTO userDTO = userService.findOneByUsername(username);
        if(userDTO == null)
            return index(model);
        UserVM userVM = new UserVM(userDTO);
        model.addAttribute("user",userVM);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String editUser(@Valid @RequestBody UserVM userVM, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors())
            return "user/edit";
        else
            userService.updateUser(userVM);
        return index(model);
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam String username, Model model) {
        userService.deleteUser(username);
        return index(model);
    }

}

package org.group2.webapp.web.mvc.ctrl;

import org.group2.webapp.service.UserService;
import org.group2.webapp.service.dto.UserDTO;
import org.group2.webapp.web.rest.UserAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/new")
    public String newUser() {
        return "user/add";
    }

    @PostMapping("/new")
    public String newUser(@RequestParam String username, @RequestParam String password, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
        if (userService.createUser(username, password, firstName, lastName, email) != null)
            return "user/users";
        else
            return "user/add";
    }

    @GetMapping("/edit")
    public String editUser() {
        return "user/edit";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam String password, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
        userService.updateUser(firstName, lastName, email);
        return "user/users";
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam String username) {
        userService.deleteUser(username);
        return "user/users";
    }

}

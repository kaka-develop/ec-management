package org.group2.webapp.web.rest.admin;

import org.group2.webapp.entity.User;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.service.UserService;
import org.group2.webapp.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class UserAPI {

    private final Logger log = LoggerFactory.getLogger(UserAPI.class);

    private final UserRepository userRepository;


    private final UserService userService;

    public UserAPI(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userRepository.findOneByUsername(userDTO.getUsername().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest().build();
        } else if (userRepository.findOneByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        } else {
            User newUser = userService.createUser(userDTO);
            return ResponseEntity.ok(newUser);
        }
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers()
            throws URISyntaxException {
        final List<UserDTO> list = userService.getAllManagedUsers();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        log.debug("REST request to get User : {}", username);
        if (!userRepository.findOneByUsername(username).isPresent())
            return ResponseEntity.notFound().build();
        UserDTO userDTO = new UserDTO(userRepository.findOneByUsername(username).get());
        return ResponseEntity.ok(userDTO);
    }


    @DeleteMapping("/users/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        log.debug("REST request to delete User: {}", username);
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}

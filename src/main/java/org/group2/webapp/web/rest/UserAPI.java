package org.group2.webapp.web.rest;

import org.group2.webapp.entity.User;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.service.UserService;
import org.group2.webapp.service.dto.UserDTO;
import org.group2.webapp.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserAPI {

    private final Logger log = LoggerFactory.getLogger(UserAPI.class);

    private static final String ENTITY_NAME = "userManagement";

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
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
                    .body(null);
        } else if (userRepository.findOneByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use"))
                    .body(null);
        } else {
            User newUser = userService.createUser(userDTO);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getUsername()))
                    .headers(HeaderUtil.createAlert("A user is created with identifier " + newUser.getUsername(), newUser.getUsername()))
                    .body(newUser);
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
        UserDTO userDTO = new UserDTO(userRepository.findOneByUsername(username).get());
        return ResponseEntity.ok(userDTO);
    }


    @DeleteMapping("/users/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        log.debug("REST request to delete User: {}", username);
        userService.deleteUser(username);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A user is deleted with identifier " + username, username)).build();
    }
}

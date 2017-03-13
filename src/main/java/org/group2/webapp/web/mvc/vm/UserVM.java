package org.group2.webapp.web.mvc.vm;

import org.group2.webapp.entity.Authority;
import org.group2.webapp.entity.User;
import org.group2.webapp.service.dto.UserDTO;

import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.*;

public class UserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public UserVM() {
        // Empty constructor needed for Jackson.
    }

    public UserVM(String username, String password, String firstName, String lastName,
                  String email) {

        super(username, firstName, lastName, email,
                Calendar.getInstance().getTime(), new HashSet<>());

        this.password = password;
    }

    public UserVM(UserDTO userDTO) {
        super(userDTO.getUsername(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                userDTO.getCreatedDate(), userDTO.getAuthorities());
    }


    public String getPassword() {
        return password;
    }
}

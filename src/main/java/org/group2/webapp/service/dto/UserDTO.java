package org.group2.webapp.service.dto;

import org.group2.webapp.entity.Authority;
import org.group2.webapp.entity.User;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO {


    @Size(min = 1, max = 100)
    private String username;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private Date createdDate;

    private Set<String> authorities;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.createdDate = user.getCreatedDate();
        this.authorities = user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet());
    }

    public UserDTO(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO(String username, String firstName, String lastName, String email, Date createdDate, Set<String> authorities) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdDate = createdDate;
        this.authorities = authorities;
    }


    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}

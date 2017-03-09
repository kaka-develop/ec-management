package org.group2.webapp.service;


import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
@Transactional
public class UserServiceTest {

    private final Logger log = LoggerFactory.getLogger(UserServiceTest.class);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private final String USER_NAME = "AAAAA";
    private final String USER_PASSWORD = "AAAAA";
    private final String USER_FIRSTNAME = "AAAAA";
    private final String USER_LASTNAME = "AAAAA";
    private final String USER_EMAIL = "AAAAA@AAAA.com";

    private User user;

    @Before
    public void before() {
        user = userService.createUser(USER_NAME,USER_PASSWORD,USER_FIRSTNAME,USER_LASTNAME,USER_EMAIL);
        log.debug("Done created user");
    }


    @Test
    public void testFindAllUsers() {
        assertThat(!userRepository.findAll().isEmpty());
        assertThat(!userService.getAllManagedUsers().isEmpty());
        assertThat(userService.getAllManagedUsers().size() == 1);
    }

    @Test
    public void testGetUserWithAuthoritiesByUsername() {
        assertThat(userRepository.findOneWithAuthoritiesByUsername(USER_NAME) != null);
        assertThat(userService.getUserWithAuthoritiesByUsername(USER_NAME) != null);
    }

    @Test
    public void testFindOneUser() {
        assertThat(userRepository.findOneByEmail(USER_EMAIL) != null);
        assertThat(userRepository.findOneByUsername(USER_NAME) != null);
    }

    @Test
    public void testUpdateUser() {
        user.setFirstName(USER_FIRSTNAME + USER_FIRSTNAME);
        assertThat(userRepository.save(user) != null);
        user.setLastName(USER_LASTNAME + USER_LASTNAME);
        assertThat(userRepository.save(user) != null);
        user.setUsername(USER_NAME + USER_NAME);
        assertThat(userRepository.save(user) != null);
    }

    @Test
    public void testGetUserWithAuthorities() {
        assertThat(userService.getUserWithAuthorities(user.getId()) != null);
        assertThat(userRepository.findOne(user.getId()) != null);
    }


    @After
    public void after() {
        userService.deleteUser(USER_NAME);
        log.debug("Done delete user");
    }


}
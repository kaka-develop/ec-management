package org.group2.webapp;


import org.group2.webapp.entity.User;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.service.UserService;
import org.group2.webapp.service.dto.UserDTO;
import org.group2.webapp.web.rest.UserAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public class UserServiceIntTest {

    private final Logger log = LoggerFactory.getLogger(UserServiceIntTest.class);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private final String USER_NAME = "AAAAA";
    private final String USER_PASSWORD = "AAAAA";
    private final String USER_FIRSTNAME = "AAAAA";
    private final String USER_LASTNAME = "AAAAA";
    private final String USER_EMAIL = "AAAAA@AAAA.com";

    @Before
    public void before() {
        userService.createUser(USER_NAME,USER_PASSWORD,USER_FIRSTNAME,USER_LASTNAME,USER_EMAIL);
        log.debug("Done created user");
    }


    @Test
    public void testFindAllUsers() {
        assertThat(!userRepository.findAll().isEmpty());
        assertThat(!userService.getAllManagedUsers().isEmpty());
        assertThat(userService.getAllManagedUsers().size() == 1);
    }


    @After
    public void after() {
        userService.deleteUser(USER_NAME);
        log.debug("Done delete user");
    }


}
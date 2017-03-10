package org.group2.webapp.web.rest;

import org.apache.commons.lang3.RandomStringUtils;
import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.service.UserService;
import org.group2.webapp.web.rest.admin.UserAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class UserAPITest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private MockMvc restUserMockMvc;
    
    public static User createEntity(EntityManager em) {
        User user = new User();
        user.setUsername("test");
        user.setPassword(RandomStringUtils.random(60));
        user.setEmail("test@test.com");
        user.setFirstName("test");
        user.setLastName("test");
        em.persist(user);
        em.flush();
        return user;
    }

    @Before
    public void setup() {
        UserAPI userResource = new UserAPI(userRepository, userService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
    }

    @Test
    @Transactional
    public void testGetExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/admin/users/admin")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.lastName").value("ladmin"));
    }

    @Test
    @Transactional
    public void testGetUnknownUser() throws Exception {
        restUserMockMvc.perform(get("/api/admin/users/unknown")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testGetExistingUserWithAnEmailLogin() throws Exception {
        User user = userService.createUser("johnss", "johndoe", "John", "Doe", "john.doe@localhost.com");

        restUserMockMvc.perform(get("/api/admin/users/johnss")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.username").value("johnss"));

        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void testDeleteExistingUserWithAnEmailLogin() throws Exception {
        User user = userService.createUser("johnsa", "johndoe", "John", "Doe", "john.doe@localhost.com");

        restUserMockMvc.perform(delete("/api/admin/users/johnsa")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(userRepository.findOneByUsername("johnsa").isPresent()).isFalse();

        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        User userA = new User();
        userA.setUsername("AAA");
        User userB = new User();
        userB.setUsername("BBB");
        assertThat(userA).isNotEqualTo(userB);
    }
}

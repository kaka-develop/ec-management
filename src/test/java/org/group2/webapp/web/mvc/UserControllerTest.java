package org.group2.webapp.web.mvc;

import org.apache.commons.lang3.RandomStringUtils;
import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.service.UserService;
import org.group2.webapp.service.dto.UserDTO;
import org.group2.webapp.web.mvc.ctrl.admin.UserController;
import org.group2.webapp.web.mvc.vm.UserVM;
import org.group2.webapp.web.util.TestUtil;
import org.junit.After;
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

import javax.persistence.EntityManager;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class UserControllerTest {

    @Autowired
    private UserService userService;

    private MockMvc restUserMockMvc;

    private final String USER_NAME = "AAAAA";
    private final String USER_PASSWORD = "AAAAA";
    private final String USER_FIRSTNAME = "AAAAA";
    private final String USER_LASTNAME = "AAAAA";
    private final String USER_EMAIL = "AAAAA@AAAA.com";

    private UserVM userVM;


    @Before
    public void setup() {
        UserController userResource = new UserController(userService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
    }

    @Before
    public void initTest() {
        userVM = new UserVM(USER_NAME, USER_PASSWORD, USER_FIRSTNAME, USER_LASTNAME, USER_EMAIL);
    }

    public void createUser() {
        userService.createUser(USER_NAME, USER_PASSWORD, USER_FIRSTNAME, USER_LASTNAME, USER_EMAIL);
    }


    @Test
    @Transactional
    public void testIndex() throws Exception {
        restUserMockMvc.perform(get("/admin/user"))
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("admin/user/users"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetNew() throws Exception {
        restUserMockMvc.perform(get("/admin/user/new"))
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("admin/user/add"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostNew() throws Exception {
        restUserMockMvc.perform(post("/admin/user/new")
                .param("username",USER_NAME)
                .param("firstName",USER_FIRSTNAME)
                .param("lastName",USER_LASTNAME)
                .param("email",USER_EMAIL)
                .param("password",USER_PASSWORD))
                .andExpect(view().name(UserController.REDIRECT_INDEX));

        restUserMockMvc.perform(post("/admin/user/new")
                .param("username","")
                .param("firstName",USER_FIRSTNAME)
                .param("lastName",USER_LASTNAME)
                .param("email",USER_EMAIL)
                .param("password",USER_PASSWORD))
                .andExpect(view().name("admin/user/add"));
    }


    @Test
    @Transactional
    public void testGetDetail() throws Exception {
        createUser();

        restUserMockMvc.perform(get("/admin/user/detail/" + USER_NAME))
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("admin/user/detail"));

        restUserMockMvc.perform(get("/admin/user/detail/" + "BBBBBBB"))
                .andExpect(view().name(UserController.REDIRECT_INDEX));
    }


    @Test
    @Transactional
    public void testGetEdit() throws Exception {
        createUser();

        restUserMockMvc.perform(get("/admin/user/edit/" + USER_NAME))
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("admin/user/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostEdit() throws Exception {
        createUser();
        userVM.setFirstName(USER_FIRSTNAME + USER_FIRSTNAME);

        restUserMockMvc.perform(post("/admin/user/edit")
                .param("username",USER_NAME)
                .param("firstName",USER_FIRSTNAME + USER_FIRSTNAME)
                .param("lastName",USER_LASTNAME + USER_LASTNAME))
                .andExpect(view().name(UserController.REDIRECT_INDEX));

        restUserMockMvc.perform(post("/admin/user/edit")
                .param("username","")
                .param("firstName",USER_FIRSTNAME + USER_FIRSTNAME)
                .param("lastName",USER_LASTNAME + USER_LASTNAME))
                .andExpect(view().name("admin/user/edit"));
    }

    @Test
    @Transactional
    public void testPostDelete() throws Exception{
        createUser();

        restUserMockMvc.perform(post("/admin/user/delete/" + USER_NAME))
                .andExpect(view().name(UserController.REDIRECT_INDEX));

        restUserMockMvc.perform(post("/admin/user/delete/" + "BBBBBBBB"))
                .andExpect(view().name(UserController.REDIRECT_INDEX));
    }

    @After
    public void after() {
        userService.deleteUser(USER_NAME);
    }

}
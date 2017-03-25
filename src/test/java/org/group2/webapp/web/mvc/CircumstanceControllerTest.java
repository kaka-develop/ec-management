package org.group2.webapp.web.mvc;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.service.CircumstanceService;
import org.group2.webapp.web.mvc.ctrl.admin.CircumstanceController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class CircumstanceControllerTest {

    @Autowired
    private CircumstanceService circumstanceService;

    private MockMvc restCircumstanceMockMvc;

    private final String CIRCUM_TITLE = "AAAAAAAA";

    private Circumstance circumstance;


    @Before
    public void setup() {
        CircumstanceController circumstanceResource = new CircumstanceController(circumstanceService);
        this.restCircumstanceMockMvc = MockMvcBuilders.standaloneSetup(circumstanceResource).build();
    }

    @Before
    public void initTest() {
        circumstance = new Circumstance();
        circumstance.setTitle(CIRCUM_TITLE);
    }

    public void createCircumstance() {
        circumstanceService.create(circumstance);
    }


    @Test
    @Transactional
    public void testShouldHaveViewForAllCircumstances() throws Exception {
        restCircumstanceMockMvc.perform(get("/admin/circumstance"))
                .andExpect(model().attributeExists("circumstances"))
                .andExpect(view().name("admin/circumstance/circumstances"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldHaveViewForAddingCircumstance() throws Exception {
        restCircumstanceMockMvc.perform(get("/admin/circumstance/new"))
                .andExpect(model().attributeExists("circumstance"))
                .andExpect(view().name("admin/circumstance/add"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldPostAddingOneCircumstance() throws Exception {
        restCircumstanceMockMvc.perform(post("/admin/circumstance/new")
                .param("title",CIRCUM_TITLE))
                .andExpect(view().name(CircumstanceController.REDIRECT_INDEX));

        restCircumstanceMockMvc.perform(post("/admin/circumstance/new")
                .param("title",""))
                .andExpect(view().name("admin/circumstance/add"));
    }


    @Test
    @Transactional
    public void testShouldHaveViewForCircumstanceInfo() throws Exception {
        createCircumstance();

        restCircumstanceMockMvc.perform(get("/admin/circumstance/detail/" + circumstance.getId().toString()))
                .andExpect(model().attributeExists("circumstance"))
                .andExpect(view().name("admin/circumstance/detail"))
                .andExpect(status().isOk());

        restCircumstanceMockMvc.perform(get("/admin/circumstance/detail/" + "BBBBBBBB"))
                .andExpect(view().name(CircumstanceController.REDIRECT_INDEX));
    }


    @Test
    @Transactional
    public void testShouldHaveViewForEditingCircumstance() throws Exception {
        createCircumstance();

        restCircumstanceMockMvc.perform(get("/admin/circumstance/edit/" + circumstance.getId().toString()))
                .andExpect(model().attributeExists("circumstance"))
                .andExpect(view().name("admin/circumstance/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldPostEditingOneCircumstance() throws Exception {
        createCircumstance();

        restCircumstanceMockMvc.perform(post("/admin/circumstance/edit")
                .param("id",circumstance.getId().toString())
                .param("title",CIRCUM_TITLE + CIRCUM_TITLE))
                .andExpect(view().name(CircumstanceController.REDIRECT_INDEX));

        restCircumstanceMockMvc.perform(post("/admin/circumstance/edit")
                .param("title",""))
                .andExpect(view().name("admin/circumstance/edit"));
    }

    @Test
    @Transactional
    public void testShouldPostDeletingOneCircumstance() throws Exception {
        createCircumstance();

        restCircumstanceMockMvc.perform(post("/admin/circumstance/delete/" + circumstance.getId().toString()))
                .andExpect(view().name(CircumstanceController.REDIRECT_INDEX));

        restCircumstanceMockMvc.perform(post("/admin/circumstance/delete/" + "BBBBBBB"))
                .andExpect(view().name(CircumstanceController.REDIRECT_INDEX));
    }

    @After
    public void after() {
        circumstanceService.delete(circumstance.getId());
    }

}

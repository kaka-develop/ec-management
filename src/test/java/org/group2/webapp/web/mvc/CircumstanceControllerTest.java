package org.group2.webapp.web.mvc;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.service.CircumstanceService;
import org.group2.webapp.web.mvc.ctrl.admin.CircumstanceController;
import org.group2.webapp.web.util.TestUtil;
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
        circumstanceService.save(circumstance);
    }


    @Test
    @Transactional
    public void testIndex() throws Exception {
        restCircumstanceMockMvc.perform(get("/admin/circumstance"))
                .andExpect(model().attributeExists("circumstances"))
                .andExpect(view().name("admin/circumstance/circumstances"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetNew() throws Exception {
        restCircumstanceMockMvc.perform(get("/admin/circumstance/new"))
                .andExpect(model().attributeExists("circumstance"))
                .andExpect(view().name("admin/circumstance/add"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostNew() throws Exception {
        restCircumstanceMockMvc.perform(post("/admin/circumstance/new")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(circumstance)))
                .andExpect(model().attributeExists("circumstances"))
                .andExpect(view().name("admin/circumstance/circumstances"))
                .andExpect(status().isOk());

        circumstance.setTitle(null);
        restCircumstanceMockMvc.perform(post("/admin/circumstance/new")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(circumstance)))
                .andExpect(view().name("admin/circumstance/add"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetDetail() throws Exception {
        createCircumstance();

        restCircumstanceMockMvc.perform(get("/admin/circumstance/detail")
                .param("id", circumstance.getId().toString()))
                .andExpect(model().attributeExists("circumstance"))
                .andExpect(view().name("admin/circumstance/detail"))
                .andExpect(status().isOk());

        restCircumstanceMockMvc.perform(get("/admin/circumstance/detail")
                .param("id", "BBBBBBBB"))
                .andExpect(model().attributeExists("circumstances"))
                .andExpect(view().name("admin/circumstance/circumstances"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetEdit() throws Exception {
        createCircumstance();

        restCircumstanceMockMvc.perform(get("/admin/circumstance/edit")
                .param("id", circumstance.getId().toString()))
                .andExpect(model().attributeExists("circumstance"))
                .andExpect(view().name("admin/circumstance/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostEdit() throws Exception {
        createCircumstance();
        circumstance.setTitle(CIRCUM_TITLE + CIRCUM_TITLE);

        restCircumstanceMockMvc.perform(post("/admin/circumstance/edit")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(circumstance)))
                .andExpect(model().attributeExists("circumstances"))
                .andExpect(view().name("admin/circumstance/circumstances"))
                .andExpect(status().isOk());

        circumstance.setTitle(null);
        restCircumstanceMockMvc.perform(post("/admin/circumstance/edit")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(circumstance)))
                .andExpect(view().name("admin/circumstance/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostDelete() throws Exception {
        createCircumstance();

        restCircumstanceMockMvc.perform(post("/admin/circumstance/delete")
                .param("id", circumstance.getId().toString()))
                .andExpect(view().name("admin/circumstance/circumstances"))
                .andExpect(status().isOk());

        restCircumstanceMockMvc.perform(post("/admin/circumstance/delete")
                .param("id", "BBBBBBB"))
                .andExpect(view().name("admin/circumstance/circumstances"))
                .andExpect(status().isOk());
    }

    @After
    public void after() {
            circumstanceService.delete(circumstance.getId());
    }

}

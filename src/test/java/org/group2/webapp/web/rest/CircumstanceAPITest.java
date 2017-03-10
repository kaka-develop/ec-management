package org.group2.webapp.web.rest;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.repository.CircumstanceRepository;
import org.group2.webapp.service.CircumstanceService;
import org.group2.webapp.web.rest.admin.CircumstanceAPI;
import org.group2.webapp.web.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class CircumstanceAPITest {

    private static final String CIRCUM_TITLE = "AAAAAAAA";

    @Autowired
    private CircumstanceRepository circumstanceRepository;

    @Autowired
    private CircumstanceService circumstanceService;


    @Autowired
    private EntityManager em;

    private MockMvc restCircumstanceMockMvc;

    private Circumstance circumstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CircumstanceAPI circumstanceResource = new CircumstanceAPI(circumstanceService);
        this.restCircumstanceMockMvc = MockMvcBuilders.standaloneSetup(circumstanceResource).build();
    }

    public static Circumstance createEntity(EntityManager em) {
        Circumstance circumstance = new Circumstance();
        circumstance.setTitle(CIRCUM_TITLE);
        return circumstance;
    }

    @Before
    public void initTest() {
        circumstance = createEntity(em);
    }

    @Test
    @Transactional
    public void createCircumstance() throws Exception {
        int databaseSizeBeforeCreate = circumstanceRepository.findAll().size();
        

        restCircumstanceMockMvc.perform(post("/api/admin/circumstances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(circumstance)))
                .andExpect(status().isOk());
        
        List<Circumstance> circumstanceList = circumstanceRepository.findAll();
        assertThat(circumstanceList).hasSize(databaseSizeBeforeCreate + 1);
        Circumstance testCircumstance = circumstanceList.get(circumstanceList.size() - 1);
        assertThat(testCircumstance.getTitle()).isEqualTo(CIRCUM_TITLE);
    }

    @Test
    @Transactional
    public void createCircumstanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = circumstanceRepository.findAll().size();
        
        Circumstance existingCircumstance = new Circumstance();
        existingCircumstance.setId(1L);
        
        restCircumstanceMockMvc.perform(post("/api/admin/circumstances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingCircumstance)))
                .andExpect(status().isBadRequest());
        
        List<Circumstance> circumstanceList = circumstanceRepository.findAll();
        assertThat(circumstanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = circumstanceRepository.findAll().size();
        circumstance.setTitle(null);

        restCircumstanceMockMvc.perform(post("/api/admin/circumstances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(circumstance)))
                .andExpect(status().isBadRequest());

        List<Circumstance> circumstanceList = circumstanceRepository.findAll();
        assertThat(circumstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCircumstances() throws Exception {
        // Initialize the database
        circumstanceRepository.saveAndFlush(circumstance);

        // Get all the circumstanceList
        restCircumstanceMockMvc.perform(get("/api/admin/circumstances"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(circumstance.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(CIRCUM_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getCircumstance() throws Exception {
        // Initialize the database
        circumstanceRepository.saveAndFlush(circumstance);

        // Get the circumstance
        restCircumstanceMockMvc.perform(get("/api/admin/circumstances/{id}", circumstance.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(circumstance.getId().intValue()))
                .andExpect(jsonPath("$.title").value(CIRCUM_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCircumstance() throws Exception {
        restCircumstanceMockMvc.perform(get("/api/admin/circumstances/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCircumstance() throws Exception {
        circumstanceService.save(circumstance);

        int databaseSizeBeforeUpdate = circumstanceRepository.findAll().size();

        // Update the circumstance
        Circumstance updatedCircumstance = circumstanceRepository.findOne(circumstance.getId());
        updatedCircumstance
                .setTitle(CIRCUM_TITLE + CIRCUM_TITLE);

        restCircumstanceMockMvc.perform(put("/api/admin/circumstances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCircumstance)))
                .andExpect(status().isOk());

        List<Circumstance> circumstanceList = circumstanceRepository.findAll();
        assertThat(circumstanceList).hasSize(databaseSizeBeforeUpdate);
        Circumstance testCircumstance = circumstanceList.get(circumstanceList.size() - 1);
        assertThat(testCircumstance.getTitle()).isEqualTo(CIRCUM_TITLE + CIRCUM_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCircumstance() throws Exception {
        int databaseSizeBeforeUpdate = circumstanceRepository.findAll().size();

        restCircumstanceMockMvc.perform(put("/api/admin/circumstances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(circumstance)))
                .andExpect(status().isOk());

        List<Circumstance> circumstanceList = circumstanceRepository.findAll();
        assertThat(circumstanceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCircumstance() throws Exception {
        circumstanceService.save(circumstance);

        int databaseSizeBeforeDelete = circumstanceRepository.findAll().size();

        restCircumstanceMockMvc.perform(delete("/api/admin/circumstances/{id}", circumstance.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<Circumstance> circumstanceList = circumstanceRepository.findAll();
        assertThat(circumstanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

}

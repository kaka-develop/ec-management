package org.group2.webapp.web.rest;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Assessment;
import org.group2.webapp.repository.AssessmentRepository;
import org.group2.webapp.service.AssessmentService;
import org.group2.webapp.web.rest.admin.AssessmentAPI;
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
public class AssessmentAPITest {

    private static final String ASSESS_CRN = "AAAAAAAA";
    private static final String ASSESS_TITLE = "AAAAAAAA";

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentService assessmentService;


    @Autowired
    private EntityManager em;

    private MockMvc restAssessmentMockMvc;

    private Assessment assessment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssessmentAPI assessmentAPI = new AssessmentAPI(assessmentService);
        this.restAssessmentMockMvc = MockMvcBuilders.standaloneSetup(assessmentAPI).build();
    }

    public static Assessment createEntity(EntityManager em) {
        Assessment assessment = new Assessment();
        assessment.setCrn(ASSESS_CRN);
        assessment.setTitle(ASSESS_TITLE);
        return assessment;
    }

    @Before
    public void initTest() {
        assessment = createEntity(em);
    }

    @Test
    @Transactional
    public void testShouldResponseAddedAssessment() throws Exception {
        int databaseSizeBeforeCreate = assessmentRepository.findAll().size();


        restAssessmentMockMvc.perform(post("/api/admin/assessments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(status().isOk());

        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeCreate + 1);
        Assessment testAssessment = assessmentRepository.findOne(assessment.getCrn());
        assertThat(testAssessment.getTitle()).isEqualTo(ASSESS_TITLE);
    }

    @Test
    @Transactional
    public void testShouldResponseAssessmentWithExistingCrn() throws Exception {
        int databaseSizeBeforeCreate = assessmentRepository.findAll().size();

        Assessment existingAssessment = new Assessment();
        existingAssessment.setCrn(ASSESS_CRN);

        restAssessmentMockMvc.perform(post("/api/admin/assessments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingAssessment)))
                .andExpect(status().isBadRequest());

        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void testShouldResponseAssessmentIsInvalid() throws Exception {
        int databaseSizeBeforeTest = assessmentRepository.findAll().size();
        assessment.setTitle(null);

        restAssessmentMockMvc.perform(post("/api/admin/assessments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(status().isBadRequest());

        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void testShouldResponseAllAssessments() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        // Get all the assessmentList
        restAssessmentMockMvc.perform(get("/api/admin/assessments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].crn").value(hasItem(assessment.getCrn())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(ASSESS_TITLE.toString())));
    }

    @Test
    @Transactional
    public void testShouldResponseOneAssessmentByCrn() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        // Get the assessment
        restAssessmentMockMvc.perform(get("/api/admin/assessments/{crn}", assessment.getCrn()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.crn").value(assessment.getCrn()))
                .andExpect(jsonPath("$.title").value(ASSESS_TITLE.toString()));
    }

    @Test
    @Transactional
    public void testShouldResponseAssessmentIsNotFound() throws Exception {
        restAssessmentMockMvc.perform(get("/api/admin/assessments/{crn}", "BBBBBBBBB"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testShouldResponseUpdatedAssessment() throws Exception {
        assessmentService.update(assessment);

        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();

        // Update the assessment
        Assessment updatedAssessment = assessmentRepository.findOne(assessment.getCrn());
        updatedAssessment
                .setTitle(ASSESS_TITLE + ASSESS_TITLE);

        restAssessmentMockMvc.perform(put("/api/admin/assessments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAssessment)))
                .andExpect(status().isOk());

        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment =assessmentRepository.findOne(assessment.getCrn());
        assertThat(testAssessment.getTitle()).isEqualTo(ASSESS_TITLE + ASSESS_TITLE);
    }

    @Test
    @Transactional
    public void testShouldResponseOkDeletingAssessmentByCrn() throws Exception {
        assessmentService.create(assessment);

        int databaseSizeBeforeDelete = assessmentRepository.findAll().size();

        restAssessmentMockMvc.perform(delete("/api/admin/assessments/{crn}", assessment.getCrn())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<Assessment> assessmentList = assessmentRepository.findAll();
        assertThat(assessmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

}

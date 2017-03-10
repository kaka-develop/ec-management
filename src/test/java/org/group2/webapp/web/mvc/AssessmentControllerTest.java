package org.group2.webapp.web.mvc;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Assessment;
import org.group2.webapp.service.AssessmentService;
import org.group2.webapp.web.mvc.ctrl.admin.AssessmentController;
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
public class AssessmentControllerTest {

    @Autowired
    private AssessmentService assessmentService;

    private MockMvc restAssessmentMockMvc;

    private final String ASSESS_CRN = "AAAAAAAA";
    private final String ASSESS_TITLE = "AAAAAAAA";

    private Assessment assessment;


    @Before
    public void setup() {
        AssessmentController assessmentResource = new AssessmentController(assessmentService);
        this.restAssessmentMockMvc = MockMvcBuilders.standaloneSetup(assessmentResource).build();
    }

    @Before
    public void initTest() {
        assessment = new Assessment();
        assessment.setCrn(ASSESS_CRN);
        assessment.setTitle(ASSESS_TITLE);
    }

    public void createAssessment() {
        assessmentService.save(assessment);
    }


    @Test
    @Transactional
    public void testIndex() throws Exception {
        restAssessmentMockMvc.perform(get("/admin/assessment"))
                .andExpect(model().attributeExists("assessments"))
                .andExpect(view().name("admin/assessment/assessments"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetNew() throws Exception {
        restAssessmentMockMvc.perform(get("/admin/assessment/new"))
                .andExpect(model().attributeExists("assessment"))
                .andExpect(view().name("admin/assessment/add"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostNew() throws Exception {
        restAssessmentMockMvc.perform(post("/admin/assessment/new")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(model().attributeExists("assessments"))
                .andExpect(view().name("admin/assessment/assessments"))
                .andExpect(status().isOk());

        assessment.setTitle(null);
        restAssessmentMockMvc.perform(post("/admin/assessment/new")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(view().name("admin/assessment/add"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetDetail() throws Exception {
        createAssessment();

        restAssessmentMockMvc.perform(get("/admin/assessment/detail")
                .param("crn", assessment.getCrn()))
                .andExpect(model().attributeExists("assessment"))
                .andExpect(view().name("admin/assessment/detail"))
                .andExpect(status().isOk());

        restAssessmentMockMvc.perform(get("/admin/assessment/detail")
                .param("crn", "BBBBBBBB"))
                .andExpect(model().attributeExists("assessments"))
                .andExpect(view().name("admin/assessment/assessments"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetEdit() throws Exception {
        createAssessment();

        restAssessmentMockMvc.perform(get("/admin/assessment/edit")
                .param("crn", assessment.getCrn()))
                .andExpect(model().attributeExists("assessment"))
                .andExpect(view().name("admin/assessment/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostEdit() throws Exception {
        createAssessment();
        assessment.setTitle(ASSESS_TITLE + ASSESS_TITLE);

        restAssessmentMockMvc.perform(post("/admin/assessment/edit")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(model().attributeExists("assessments"))
                .andExpect(view().name("admin/assessment/assessments"))
                .andExpect(status().isOk());

        assessment.setTitle(null);
        restAssessmentMockMvc.perform(post("/admin/assessment/edit")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(view().name("admin/assessment/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostDelete() throws Exception {
        createAssessment();

        restAssessmentMockMvc.perform(post("/admin/assessment/delete")
                .param("crn", assessment.getCrn()))
                .andExpect(view().name("admin/assessment/assessments"))
                .andExpect(status().isOk());

        restAssessmentMockMvc.perform(post("/admin/assessment/delete")
                .param("crn", "BBBBBBB"))
                .andExpect(view().name("admin/assessment/assessments"))
                .andExpect(status().isOk());
    }

    @After
    public void after() {
            assessmentService.delete(assessment.getCrn());
    }

}

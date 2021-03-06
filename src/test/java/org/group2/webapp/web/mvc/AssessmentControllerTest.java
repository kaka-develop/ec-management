package org.group2.webapp.web.mvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Assessment;
import org.group2.webapp.service.AssessmentService;
import org.group2.webapp.web.mvc.ctrl.admin.AssessmentController;
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
        assessment.setCode(ASSESS_CRN);
        assessment.setTitle(ASSESS_TITLE);
    }

    public void createAssessment() {
        assessmentService.create(assessment);
    }


    @Test
    @Transactional
    public void testShouldHaveViewForAllAssessments() throws Exception {
        restAssessmentMockMvc.perform(get("/admin/assessment"))
                .andExpect(model().attributeExists("assessments"))
                .andExpect(view().name("admin/assessment/assessments"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testShouldHaveViewForAddingOneAssessment() throws Exception {
        restAssessmentMockMvc.perform(get("/admin/assessment/new"))
                .andExpect(model().attributeExists("assessment"))
                .andExpect(view().name("admin/assessment/add"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldPostAddingOneAssessment() throws Exception {
        restAssessmentMockMvc.perform(post("/admin/assessment/new")
                .param("code",ASSESS_CRN)
                .param("title",ASSESS_TITLE))
                .andExpect(view().name(AssessmentController.REDIRECT_INDEX));

        restAssessmentMockMvc.perform(post("/admin/assessment/new")
                .param("title",""))
                .andExpect(view().name("admin/assessment/add"));
    }



    @Test
    @Transactional
    public void testShouldHaveViewForOneAssessment() throws Exception {
        createAssessment();

        restAssessmentMockMvc.perform(get("/admin/assessment/detail/" + assessment.getCode()))
                .andExpect(model().attributeExists("assessment"))
                .andExpect(view().name("admin/assessment/detail"))
                .andExpect(status().isOk());

        restAssessmentMockMvc.perform(get("/admin/assessment/detail/" + "BBBBBBBB"))
                .andExpect(view().name(AssessmentController.REDIRECT_INDEX));
    }


    @Test
    @Transactional
    public void testShouldHaveViewForEditingAssessment() throws Exception {
        createAssessment();

        restAssessmentMockMvc.perform(get("/admin/assessment/edit/" + assessment.getCode().toString()))
                .andExpect(model().attributeExists("assessment"))
                .andExpect(view().name("admin/assessment/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldPostEditingOneAssessment() throws Exception {
        createAssessment();

        restAssessmentMockMvc.perform(post("/admin/assessment/edit")
                .param("code",ASSESS_CRN)
                .param("title",ASSESS_TITLE + ASSESS_TITLE))
                .andExpect(view().name(AssessmentController.REDIRECT_INDEX));

        restAssessmentMockMvc.perform(post("/admin/assessment/edit")
                .param("title",""))
                .andExpect(view().name("admin/assessment/edit"));
    }

    @Test
    @Transactional
    public void testShouldPostDeletingOneAssessment() throws Exception {
        createAssessment();

        restAssessmentMockMvc.perform(post("/admin/assessment/delete/" + assessment.getCode().toString()))
                .andExpect(view().name("admin/assessment/assessments"));

        restAssessmentMockMvc.perform(post("/admin/assessment/delete/" + "BBBBBBB"))
                .andExpect(view().name("admin/assessment/assessments"));
    }

    @After
    public void after() {
        assessmentService.delete(assessment.getCode());
    }

}

package org.group2.webapp.web.mvc;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.service.ClaimService;
import org.group2.webapp.service.ManagerService;
import org.group2.webapp.web.mvc.ctrl.ManagerController;
import org.junit.After;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class ManagerControllerTest {

    @Autowired
    private ManagerService managerService;


    @Autowired
    private ClaimService claimService;


    private MockMvc restManagerMockMvc;

    private final String CLAIM_EVIDENCE = "AAAAAAAA";
    private final String CLAIM_CONTENT = "AAAAAAAA";
    private final Integer CLAIM_STATUS = 1;

    private Claim claim;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ManagerController managerController = new ManagerController(managerService);
        this.restManagerMockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
    }


    @Before
    public void initTest() {
        claim = new Claim();
        claim.setEvidence(CLAIM_EVIDENCE);
        claim.setContent(CLAIM_CONTENT);
        claim.setStatus(CLAIM_STATUS);
        claim = claimService.create(claim);
    }


    @Test
    @Transactional
    public void testShouldHaveViewForAllClaims() throws Exception {
        restManagerMockMvc.perform(get("/manager/claims"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("manager/claims"));
    }


    @Test
    @Transactional
    public void testShaveHaveViewAllClaimsByFaculty() throws Exception{
        restManagerMockMvc.perform(get("/manager/claims/faculty"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("manager/claims-faculty"));
    }

    @Test
    @Transactional
    public void testShaveHaveViewAllClaimsByYear() throws Exception{
        restManagerMockMvc.perform(get("/manager/claims/year"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("manager/claims-year"));
    }

    @Test
    @Transactional
    public void testShaveHaveViewProcessedClaimsByFaculty() throws Exception{
        restManagerMockMvc.perform(get("/manager/claims/faculty/processed"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("manager/claims-faculty-processed"));
    }

    @Test
    @Transactional
    public void testShaveHaveViewClaimsByCircumstance() throws Exception{
        restManagerMockMvc.perform(get("/manager/claims/circumstance"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("manager/claims-circumstance"));
    }

    @Test
    @Transactional
    public void testShaveHaveViewClaimsValidAndInvalid() throws Exception{
        restManagerMockMvc.perform(get("/manager/claims/validation"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("manager/claims-validation"));
    }

    @Test
    @Transactional
    public void testShaveHaveViewAllClaimsThisMonth() throws Exception{
        restManagerMockMvc.perform(get("/manager/claims/thismonth"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("manager/claims-thismonth"));
    }

    @Test
    @Transactional
    public void testShaveHaveViewAllClaimsThisWeek() throws Exception{
        restManagerMockMvc.perform(get("/manager/claims/thisweek"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("manager/claims-thisweek"));
    }


    @Test
    @Transactional
    public void testShaveHaveViewCustomClaimsReport() throws Exception{
        restManagerMockMvc.perform(get("/manager/claims/custom"))
                .andExpect(view().name("manager/claims-custom"));
    }
    @After
    public void after() {
        claimService.delete(claim.getId());
    }


}

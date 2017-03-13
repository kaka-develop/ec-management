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
        claim = claimService.save(claim);
    }


    @Test
    @Transactional
    public void testGetAllClaims() throws Exception {
        restManagerMockMvc.perform(get("/manager/claims"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("manager/claims"));
    }


    @Test
    @Transactional
    public void testGetClaimStatistics() throws Exception{
        restManagerMockMvc.perform(get("/manager/statistics"))
                .andExpect(model().attributeExists("claimsPerFaculty"))
                .andExpect(model().attributeExists("claimsPerYear"))
                .andExpect(view().name("manager/statistics"));
    }

    @After
    public void after() {
        claimService.delete(claim.getId());
    }


}

package org.group2.webapp.web.rest;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.service.ClaimService;
import org.group2.webapp.service.ManagerService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class ManagerAPITest {

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
        ManagerAPI managerAPI = new ManagerAPI(managerService);
        this.restManagerMockMvc = MockMvcBuilders.standaloneSetup(managerAPI).build();
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
    public void testGetAllClaims() throws Exception {
        restManagerMockMvc.perform(get("/api/manager/claims"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(claim.getId().intValue())))
                .andExpect(jsonPath("$.[*].evidence").value(hasItem(CLAIM_EVIDENCE.toString())));
    }


    @Test
    @Transactional
    public void testGetClaimsByFaculty() throws Exception{
        restManagerMockMvc.perform(get("/api/manager/claims/faculty"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    @Transactional
    public void testGetClaimsByYear() throws Exception{
        restManagerMockMvc.perform(get("/api/manager/claims/year"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

}

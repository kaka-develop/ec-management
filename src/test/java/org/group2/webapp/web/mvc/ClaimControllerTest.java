package org.group2.webapp.web.mvc;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.service.ClaimService;
import org.group2.webapp.web.mvc.ctrl.admin.ClaimController;
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

import java.util.Calendar;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class ClaimControllerTest {

    @Autowired
    private ClaimService claimService;

    private MockMvc restClaimMockMvc;

    private final String CLAIM_EVIDENCE = "AAAAAAAA";
    private final String CLAIM_CONTENT = "AAAAAAAA";
    private final Integer CLAIM_STATUS = 1;

    private Claim claim;


    @Before
    public void setup() {
        ClaimController claimResource = new ClaimController(claimService);
        this.restClaimMockMvc = MockMvcBuilders.standaloneSetup(claimResource).build();
    }

    @Before
    public void initTest() {
        claim = new Claim();
        claim.setEvidence(CLAIM_EVIDENCE);
        claim.setContent(CLAIM_CONTENT);
        claim.setStatus(CLAIM_STATUS);
    }

    public void createClaim() {
        claimService.create(claim);
    }


    @Test
    @Transactional
    public void testShouldHaveViewForAllClaims() throws Exception {
        restClaimMockMvc.perform(get("/admin/claim"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("admin/claim/claims"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testShouldHaveViewForClaimInfo() throws Exception {
        createClaim();

        restClaimMockMvc.perform(get("/admin/claim/detail/"+ claim.getId().toString()))
                .andExpect(model().attributeExists("claim"))
                .andExpect(view().name("admin/claim/detail"))
                .andExpect(status().isOk());

        restClaimMockMvc.perform(get("/admin/claim/detail/" + "BBBBBBBB" ))
                .andExpect(view().name(ClaimController.REDIRECT_INDEX));
    }

    @Test
    @Transactional
    public void testShouldHaveViewForFindingClaimsByYear() throws Exception {
        createClaim();

        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);

        restClaimMockMvc.perform(get("/admin/claim/year")
                .param("year",year.toString()))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("admin/claim/claims"))
                .andExpect(status().isOk());

        restClaimMockMvc.perform(get("/admin/claim/year")
                .param("year","BBBBBBB"))
                .andExpect(view().name(ClaimController.REDIRECT_INDEX));
    }


    @Test
    @Transactional
    public void testShouldHaveViewForEditingClaim() throws Exception {
        createClaim();

        restClaimMockMvc.perform(get("/admin/claim/edit/" + claim.getId().toString()))
                .andExpect(model().attributeExists("claim"))
                .andExpect(view().name("admin/claim/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldPostEditingOneClaim() throws Exception {
        createClaim();

//        restClaimMockMvc.perform(post("/admin/claim/edit")
//                .param("id",claim.getId().toString())
//                .param("evidence",CLAIM_EVIDENCE + CLAIM_EVIDENCE)
//                .param("content",CLAIM_CONTENT))
//                .andExpect(view().name(ClaimController.REDIRECT_INDEX));
//
//        restClaimMockMvc.perform(post("/admin/claim/edit")
//                .param("id",claim.getId().toString())
//                .param("evidence","")
//                .param("content",CLAIM_CONTENT))
//                .andExpect(view().name("admin/claim/edit"));
    }

    @Test
    @Transactional
    public void testShouldPostDeletingClaim() throws Exception {
        createClaim();

        restClaimMockMvc.perform(post("/admin/claim/delete/" +  claim.getId().toString()))
                .andExpect(view().name("admin/claim/claims"));

        restClaimMockMvc.perform(post("/admin/claim/delete/" + "BBBBBBB"))
                .andExpect(view().name("admin/claim/claims"));
    }

    @After
    public void after() {
            claimService.delete(claim.getId());
    }

}

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
        claimService.save(claim);
    }


    @Test
    @Transactional
    public void testIndex() throws Exception {
        restClaimMockMvc.perform(get("/admin/claim"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("admin/claim/claims"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetNew() throws Exception {
        restClaimMockMvc.perform(get("/admin/claim/new"))
                .andExpect(model().attributeExists("claim"))
                .andExpect(view().name("admin/claim/add"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostNew() throws Exception {
        restClaimMockMvc.perform(post("/admin/claim/new")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(claim)))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("admin/claim/claims"))
                .andExpect(status().isOk());

        claim.setContent(null);
        restClaimMockMvc.perform(post("/admin/claim/new")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(claim)))
                .andExpect(view().name("admin/claim/add"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetDetail() throws Exception {
        createClaim();

        restClaimMockMvc.perform(get("/admin/claim/detail")
                .param("id", claim.getId().toString()))
                .andExpect(model().attributeExists("claim"))
                .andExpect(view().name("admin/claim/detail"))
                .andExpect(status().isOk());

        restClaimMockMvc.perform(get("/admin/claim/detail")
                .param("id", "BBBBBBBB"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("admin/claim/claims"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetByYear() throws Exception {
        createClaim();

        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);

        restClaimMockMvc.perform(get("/admin/claim/year")
                .param("year", year.toString()))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("admin/claim/year"))
                .andExpect(status().isOk());

        restClaimMockMvc.perform(get("/admin/claim/year")
                .param("year", "BBBBBBBB"))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("admin/claim/claims"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetEdit() throws Exception {
        createClaim();

        restClaimMockMvc.perform(get("/admin/claim/edit")
                .param("id", claim.getId().toString()))
                .andExpect(model().attributeExists("claim"))
                .andExpect(view().name("admin/claim/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostEdit() throws Exception {
        createClaim();
        claim.setEvidence(CLAIM_EVIDENCE + CLAIM_EVIDENCE);

        restClaimMockMvc.perform(post("/admin/claim/edit")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(claim)))
                .andExpect(model().attributeExists("claims"))
                .andExpect(view().name("admin/claim/claims"))
                .andExpect(status().isOk());

        claim.setContent(null);
        restClaimMockMvc.perform(post("/admin/claim/edit")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(claim)))
                .andExpect(view().name("admin/claim/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostDelete() throws Exception {
        createClaim();

        restClaimMockMvc.perform(post("/admin/claim/delete")
                .param("id", claim.getId().toString()))
                .andExpect(view().name("admin/claim/claims"))
                .andExpect(status().isOk());

        restClaimMockMvc.perform(post("/admin/claim/delete")
                .param("id", "BBBBBBB"))
                .andExpect(view().name("admin/claim/claims"))
                .andExpect(status().isOk());
    }

    @After
    public void after() {
            claimService.delete(claim.getId());
    }

}

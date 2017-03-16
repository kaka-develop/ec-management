package org.group2.webapp.web.rest;

import org.group2.webapp.EcManagementApplication;

import org.group2.webapp.entity.Claim;
import org.group2.webapp.repository.ClaimRepository;
import org.group2.webapp.service.ClaimService;

import org.group2.webapp.web.rest.admin.ClaimAPI;
import org.group2.webapp.web.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class ClaimAPITest {

    private static final String CLAIM_EVIDENCE = "AAAAAAAA";
    private static final String CLAIM_CONTENT = "AAAAAAAA";
    private static final Integer CLAIM_STATUS = 1;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private ClaimService claimService;


    @Autowired
    private EntityManager em;

    private MockMvc restClaimMockMvc;

    private Claim claim;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClaimAPI claimResource = new ClaimAPI(claimService);
        this.restClaimMockMvc = MockMvcBuilders.standaloneSetup(claimResource).build();
    }

    public static Claim createEntity(EntityManager em) {
        Claim claim = new Claim();
        claim.setEvidence(CLAIM_EVIDENCE);
        claim.setContent(CLAIM_CONTENT);
        claim.setStatus(CLAIM_STATUS);
        return claim;
    }

    @Before
    public void initTest() {
        claim = createEntity(em);
    }

    @Test
    @Transactional
    public void createClaim() throws Exception {
        int databaseSizeBeforeCreate = claimRepository.findAll().size();
        

        restClaimMockMvc.perform(post("/api/admin/claims")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(claim)))
                .andExpect(status().isOk());
        
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeCreate + 1);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getEvidence()).isEqualTo(CLAIM_EVIDENCE);
    }

    @Test
    @Transactional
    public void createClaimWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = claimRepository.findAll().size();
        
        Claim existingClaim = new Claim();
        existingClaim.setId(1L);
        
        restClaimMockMvc.perform(post("/api/admin/claims")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingClaim)))
                .andExpect(status().isBadRequest());
        
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEvidenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        claim.setEvidence(null);

        restClaimMockMvc.perform(post("/api/admin/claims")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(claim)))
                .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClaims() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        // Get all the claimList
        restClaimMockMvc.perform(get("/api/admin/claims"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(claim.getId().intValue())))
                .andExpect(jsonPath("$.[*].evidence").value(hasItem(CLAIM_EVIDENCE.toString())));
    }

    @Test
    @Transactional
    public void getAllClaimsByYear() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);

        // Get all the claimList
        restClaimMockMvc.perform(get("/api/admin/claims/year")
                .param("year",year.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(claim.getId().intValue())))
                .andExpect(jsonPath("$.[*].evidence").value(hasItem(CLAIM_EVIDENCE.toString())));

        restClaimMockMvc.perform(get("/api/admin/claims/year")
                .param("year","BBBBBBBBBB"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void getClaim() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        // Get the claim
        restClaimMockMvc.perform(get("/api/admin/claims/{id}", claim.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(claim.getId().intValue()))
                .andExpect(jsonPath("$.evidence").value(CLAIM_EVIDENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClaim() throws Exception {
        restClaimMockMvc.perform(get("/api/admin/claims/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClaim() throws Exception {
        claimService.save(claim);

        int databaseSizeBeforeUpdate = claimRepository.findAll().size();

        // Update the claim
        Claim updatedClaim = claimRepository.findOne(claim.getId());
        updatedClaim
                .setEvidence(CLAIM_EVIDENCE + CLAIM_EVIDENCE);

        restClaimMockMvc.perform(put("/api/admin/claims")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClaim)))
                .andExpect(status().isOk());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getEvidence()).isEqualTo(CLAIM_EVIDENCE + CLAIM_EVIDENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingClaim() throws Exception {
        int databaseSizeBeforeUpdate = claimRepository.findAll().size();

        restClaimMockMvc.perform(put("/api/admin/claims")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(claim)))
                .andExpect(status().isOk());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClaim() throws Exception {
        claimService.save(claim);

        int databaseSizeBeforeDelete = claimRepository.findAll().size();

        restClaimMockMvc.perform(delete("/api/admin/claims/{id}", claim.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeDelete - 1);
    }

}

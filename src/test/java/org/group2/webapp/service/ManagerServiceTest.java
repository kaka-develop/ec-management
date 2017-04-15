package org.group2.webapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Claim;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
@Transactional
public class ManagerServiceTest {

    private final Logger log = LoggerFactory.getLogger(ManagerServiceTest.class);


    @Autowired
    private ClaimService claimService;

    @Autowired
    private ManagerService managerService;


    private final String CLAIM_EVIDENCE = "AAAAAAAA";
    private final String CLAIM_CONTENT = "AAAAAAAA";
    private final Integer CLAIM_STATUS = 1;

    private Claim claim;

    @Before
    public void before() {
        claim = new Claim();
        claim.setEvidence(CLAIM_EVIDENCE);
        claim.setContent(CLAIM_CONTENT);
        claim.setStatus(CLAIM_STATUS);
        claim = claimService.create(claim);
        log.debug("done create claim");
    }

    @Test
    public void testShouldHaveClaim() {
        assertThat(!managerService.findAllClaims().isEmpty());
    }

    @Test
    public void testShouldHaveAllClaimsByFaculty() {
        assertThat(!managerService.getClaimsPerFaculty().isEmpty());
    }
    @Test
    public void testShouldHaveAllValidAndInvalidClaims() {
        assertThat(!managerService.getValidAndInvalidClaims().isEmpty());
    }

    @Test
    public void testShouldHaveClaimsForEachYear() {
        assertThat(!managerService.getClamsPerYear().isEmpty());
    }


    @Test
    public void testShouldHaveCustomClaimReport() {
        String type  = "faculty";
        String year = "2017";
        String month = "04";
        assertThat(!managerService.getCustomClaimReport(type,month,year).isEmpty());
    }


    @After
    public void after() {
        claimService.delete(claim.getId());
        log.debug("done delete claim");
    }
}

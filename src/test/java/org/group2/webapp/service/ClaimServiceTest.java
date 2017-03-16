package org.group2.webapp.service;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.repository.ClaimRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
@Transactional
public class ClaimServiceTest {

    private final Logger log = LoggerFactory.getLogger(ClaimServiceTest.class);

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private ClaimService claimService;


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
    public void testFindAll() {
        assertThat(!claimRepository.findAll().isEmpty());
        assertThat(!claimService.findAll().isEmpty());
    }

    @Test
    public void testFindOne() {
        assertThat(claimRepository.findOne(claim.getId())!= null);
        assertThat(claimService.findOne(claim.getId())!= null);

        assertThat(claimRepository.findOne(new Long(11111))== null);
        assertThat(claimService.findOne(new Long(11111))== null);
    }

    @Test
    public void testFindAllByYear() {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);

        assertThat(!claimRepository.findAllByYear(year).isEmpty());
        assertThat(!claimService.findClaimsByYear(year).isEmpty());
    }

    @After
    public void after() {
        claimService.delete(claim.getId());
        log.debug("done delete claim");
    }
}

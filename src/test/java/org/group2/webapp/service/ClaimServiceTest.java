package org.group2.webapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;

import javax.transaction.Transactional;

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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
@Transactional
public class ClaimServiceTest {

    private final Logger log = LoggerFactory.getLogger(ClaimServiceTest.class);

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private ClaimService claimService;

    @Autowired
    private FacultyService facultyService;


    private final String CLAIM_EVIDENCE = "AAAAAAAA";
    private final String CLAIM_CONTENT = "AAAAAAAA";
    private final Integer CLAIM_STATUS = 1;


    private final String FACULTY_TITLE = "AAAAAAAA";

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
        assertThat(!claimRepository.findAll().isEmpty());
        assertThat(!claimService.findAll().isEmpty());
    }

    @Test
    public void testShouldHaveOneClaimByID() {
        assertThat(claimRepository.findOne(claim.getId()) != null);
        assertThat(claimService.findOne(claim.getId()) != null);

        assertThat(claimRepository.findOne(new Long(11111)) == null);
        assertThat(claimService.findOne(new Long(11111)) == null);
    }

    @Test
    public void testShouldHaveAllClaimsByYear() {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);

        assertThat(!claimRepository.findAllByYear(year).isEmpty());
        assertThat(!claimService.findClaimsByYear(year).isEmpty());
    }


    @Test
    public void testShouldHaveAllClaimsByFaculty() {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);

        assertThat(!claimRepository.findAllByYear(year).isEmpty());
        assertThat(!claimService.findClaimsByYear(year).isEmpty());
    }

    @Test
    public void testShouldHaveAllClaimsInThisMonth() {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        assertThat(!claimRepository.findAllByThisMonth(month,year).isEmpty());
        assertThat(!claimService.findClaimsInThisMonth().isEmpty());
    }

    @Test
    public void testShouldHaveAllClaimsInThisWeek() {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        Integer week = calendar.get(Calendar.WEEK_OF_MONTH);
        assertThat(!claimRepository.findAllByThisWeek(week,month,year).isEmpty());
        assertThat(!claimService.findClaimsInThisWeek().isEmpty());
    }



    @After
    public void after() {
        claimService.delete(claim.getId());
        log.debug("done delete claim");
    }
}

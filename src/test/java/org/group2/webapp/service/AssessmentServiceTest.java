package org.group2.webapp.service;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Assessment;
import org.group2.webapp.repository.AssessmentRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
@Transactional
public class AssessmentServiceTest {

    private final Logger log = LoggerFactory.getLogger(AssessmentServiceTest.class);

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentService assessmentService;


    private final String ASSESS_CRN = "AAAAAAAA";
    private final String ASSESS_TITLE = "AAAAAAAA";

    private Assessment assessment;

    @Before
    public void before() {
        assessment = new Assessment();
        assessment.setCrn(ASSESS_CRN);
        assessment.setTitle(ASSESS_TITLE);
        assessment = assessmentService.save(assessment);
        log.debug("done create assessment");
    }

    @Test
    public void testFillAll() {
        assertThat(!assessmentRepository.findAll().isEmpty());
        assertThat(!assessmentService.findAll().isEmpty());
    }


    @Test
    public void testFindOne() {
        assertThat(assessmentRepository.findOne(assessment.getCrn())!= null);
        assertThat(assessmentService.findOne(assessment.getCrn())!= null);

        assertThat(assessmentRepository.findOne("BBBBBBB")== null);
        assertThat(assessmentService.findOne( "BBBBBBB")== null);
    }

    @After
    public void after() {
        assessmentService.delete(assessment.getCrn());
        log.debug("done delete assessment");
    }
}

package org.group2.webapp.service;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Assessment;
import org.group2.webapp.entity.AssessItem;
import org.group2.webapp.repository.AssessItemRepository;
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
public class AssessItemServiceTest {

    private final Logger log = LoggerFactory.getLogger(AssessItemServiceTest.class);

    @Autowired
    private AssessItemRepository assessItemRepository;

    @Autowired
    private AssessItemService assessmentItemService;


    private final String ASSESSITEM_TITLE = "AAAAAAAA";

    @Autowired
    private AssessmentService assessmentService;


    private final String ASSESS_CRN = "AAAAAAAA";
    private final String ASSESS_TITLE = "AAAAAAAA";


    private AssessItem assessItem;
    private Assessment assessment;

    @Before
    public void before() {
         assessment = new Assessment();
        assessment.setCrn(ASSESS_CRN);
        assessment.setTitle(ASSESS_TITLE);
        assessment = assessmentService.create(assessment);

        assessItem = new AssessItem();
        assessItem.setTitle(ASSESSITEM_TITLE);
        assessItem.setAssessment(assessment);
        assessItem = assessmentItemService.save(assessItem);
        log.debug("done create assessItem");
    }

    @Test
    public void testFillAll() {
        assertThat(!assessItemRepository.findAll().isEmpty());
        assertThat(!assessmentItemService.findAll().isEmpty());
    }

    @Test
    public void findAllClaimsByAssessCrn(){
        assertThat(!assessItemRepository.findAllByAssessmentCrn(assessment.getCrn()).isEmpty());
        assertThat(!assessmentItemService.findAllByAssessmentCrn(assessment.getCrn()).isEmpty());

    }



    @Test
    public void testFindOne() {
        assertThat(assessItemRepository.findOne(assessItem.getId())!= null);
        assertThat(assessmentItemService.findOne(assessItem.getId())!= null);

        assertThat(assessItemRepository.findOne(new Long(11111))== null);
        assertThat(assessmentItemService.findOne(new Long(11111))== null);
    }

    @After
    public void after() {
        assessmentItemService.delete(assessItem.getId());
        log.debug("done delete assessItem");
    }
}

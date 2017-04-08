package org.group2.webapp.service;

import org.group2.webapp.entity.Assessment;
import org.group2.webapp.entity.Assessment;
import org.group2.webapp.repository.AssessmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssessmentService {

    private final Logger log = LoggerFactory.getLogger(AssessmentService.class);

    private final AssessmentRepository assessmentRepository;


    public AssessmentService(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }


    @Transactional(readOnly = true)
    public List<Assessment> findAll() {
        log.debug("Request to get all  Assessments");
        List<Assessment> result = assessmentRepository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public Assessment findOne(String crn) {
        log.debug("Request to get  Assessment : {}", crn);
        if (crn == null)
            return null;
        Assessment assessment = assessmentRepository.findOne(crn);
        return assessment;
    }

    public void delete(String crn) {
        log.debug("Request to delete  Assessment : {}", crn);
        if (crn == null || assessmentRepository.findOne(crn) == null)
            return;
        assessmentRepository.delete(crn);
    }

    public Assessment create(Assessment assessment) {
        log.debug("Request to create  assessment: {}", assessment);
        return assessmentRepository.save(assessment);
    }

    public Assessment update(Assessment assessment) {
        log.debug("Request to update  assessment: {}", assessment.getCode());
        if (assessment.getCode() == null)
            return null;
        return assessmentRepository.save(assessment);
    }

}

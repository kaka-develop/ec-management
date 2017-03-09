package org.group2.webapp.service;

import org.group2.webapp.entity.Assessment;
import org.group2.webapp.repository.AssessmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AssessmentService {

    private final Logger log = LoggerFactory.getLogger(AssessmentService.class);

    private final AssessmentRepository assessmentRepository;

    public AssessmentService(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }


    public Assessment save(Assessment assessment) {
        log.debug("Request to save Assessment : {}", assessment);
        Assessment result = assessmentRepository.save(assessment);
        return result;
    }


    @Transactional(readOnly = true)
    public List<Assessment> findAll() {
        log.debug("Request to get all Assessments");
        List<Assessment> result = assessmentRepository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public Assessment findOne(String crn) {
        log.debug("Request to get Assessment : {}", crn);
        Assessment assessment = assessmentRepository.findOne(crn);
        return assessment;
    }

    public void delete(String crn) {
        log.debug("Request to delete Assessment : {}", crn);
        assessmentRepository.delete(crn);
    }
}

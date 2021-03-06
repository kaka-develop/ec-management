package org.group2.webapp.web.rest.admin;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.group2.webapp.entity.Assessment;
import org.group2.webapp.service.AssessmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AssessmentAPI {

    private final Logger log = LoggerFactory.getLogger(AssessmentAPI.class);

    private static final String ENTITY_NAME = "assessment";

    private final AssessmentService assessmentService;

    public AssessmentAPI(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }


    @PostMapping("/assessments")
    public ResponseEntity<Assessment> createAssessment(@Valid @RequestBody Assessment assessment) throws URISyntaxException {
        log.debug("REST request to save Assessment : {}", assessment);
        if (assessment.getCode() == null || assessmentService.findOne(assessment.getCode()) != null) {
            return ResponseEntity.badRequest().build();
        }
        Assessment result = assessmentService.create(assessment);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/assessments")
    public ResponseEntity<Assessment> updateAssessment(@Valid @RequestBody Assessment assessment) throws URISyntaxException {
        log.debug("REST request to update Assessment : {}", assessment);
        if (assessment.getCode() == null) {
            return createAssessment(assessment);
        }
        Assessment result = assessmentService.update(assessment);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/assessments")
    public List<Assessment> getAllAssessments() {
        log.debug("REST request to get all Assessments");
        return assessmentService.findAll();
    }


    @GetMapping("/assessments/{crn}")
    public ResponseEntity<Assessment> getAssessment(@PathVariable String crn) {
        log.debug("REST request to get Assessment : {}", crn);
        Assessment assessment = assessmentService.findOne(crn);
        if(assessment == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(assessment);
    }

    @DeleteMapping("/assessments/{crn}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable String crn) {
        log.debug("REST request to delete Assessment : {}", crn);
        assessmentService.delete(crn);
        return ResponseEntity.ok().build();
    }

}

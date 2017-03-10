package org.group2.webapp.web.rest.admin;

import org.group2.webapp.entity.Assessment;
import org.group2.webapp.service.AssessmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

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
        if (assessment.getCrn() != null) {
            return ResponseEntity.badRequest().build();
        }
        Assessment result = assessmentService.save(assessment);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/assessments")
    public ResponseEntity<Assessment> updateAssessment(@Valid @RequestBody Assessment assessment) throws URISyntaxException {
        log.debug("REST request to update Assessment : {}", assessment);
        if (assessment.getCrn() == null) {
            return createAssessment(assessment);
        }
        Assessment result = assessmentService.save(assessment);
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
        return ResponseEntity.ok(assessment);
    }

    @DeleteMapping("/assessments/{crn}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable String crn) {
        log.debug("REST request to delete Assessment : {}", crn);
        assessmentService.delete(crn);
        return ResponseEntity.ok().build();
    }

}

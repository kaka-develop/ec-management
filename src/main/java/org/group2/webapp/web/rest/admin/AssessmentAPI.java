package org.group2.webapp.web.rest.admin;

import org.group2.webapp.entity.Item;
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
    public ResponseEntity<Item> createAssessment(@Valid @RequestBody Item assessment) throws URISyntaxException {
        log.debug("REST request to save Assessment : {}", assessment);
        if (assessment.getCrn() == null || assessmentService.findOne(assessment.getCrn()) != null) {
            return ResponseEntity.badRequest().build();
        }
        Item result = assessmentService.create(assessment);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/assessments")
    public ResponseEntity<Item> updateAssessment(@Valid @RequestBody Item assessment) throws URISyntaxException {
        log.debug("REST request to update Assessment : {}", assessment);
        if (assessment.getCrn() == null) {
            return createAssessment(assessment);
        }
        Item result = assessmentService.update(assessment);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/assessments")
    public List<Item> getAllAssessments() {
        log.debug("REST request to get all Assessments");
        return assessmentService.findAll();
    }


    @GetMapping("/assessments/{crn}")
    public ResponseEntity<Item> getAssessment(@PathVariable String crn) {
        log.debug("REST request to get Assessment : {}", crn);
        Item assessment = assessmentService.findOne(crn);
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

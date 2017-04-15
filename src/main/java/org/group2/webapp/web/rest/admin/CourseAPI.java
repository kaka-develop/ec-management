package org.group2.webapp.web.rest.admin;

<<<<<<< HEAD
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.group2.webapp.entity.Assessment;
=======
import org.group2.webapp.entity.Item;
>>>>>>> branch 'master' of https://github.com/EnterpriseWebSoftwareDevelopmentSEM7-G2/ec-management
import org.group2.webapp.service.CourseService;
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
public class CourseAPI {

    private final Logger log = LoggerFactory.getLogger(CourseAPI.class);

    private static final String ENTITY_NAME = "course";

    private final CourseService courseService;

    public CourseAPI(CourseService courseService) {
        this.courseService = courseService;
    }


    @PostMapping("/courses")
    public ResponseEntity<Item> createCourse(@Valid @RequestBody Item course) throws URISyntaxException {
        log.debug("REST request to save Course : {}", course);
        if (course.getCrn() == null || courseService.findOne(course.getCrn())!= null) {
            return ResponseEntity.badRequest().build();
        }
        Item result = courseService.create(course);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/courses")
    public ResponseEntity<Item> updateCourse(@Valid @RequestBody Item course) throws URISyntaxException {
        log.debug("REST request to update Course : {}", course);
        if (course.getCrn() == null) {
            return createCourse(course);
        }
        Item result = courseService.update(course);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/courses")
    public List<Item> getAllCourses() {
        log.debug("REST request to get all Courses");
        return courseService.findAll();
    }


    @GetMapping("/courses/{code}")
    public ResponseEntity<Item> getCourse(@PathVariable String code) {
        log.debug("REST request to get Course : {}", code);
        Item course = courseService.findOne(code);
        if(course == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/courses/{code}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String code) {
        log.debug("REST request to delete Course : {}", code);
        courseService.delete(code);
        return ResponseEntity.ok().build();
    }

}

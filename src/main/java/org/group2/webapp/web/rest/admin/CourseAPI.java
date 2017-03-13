package org.group2.webapp.web.rest.admin;

import org.group2.webapp.entity.Course;
import org.group2.webapp.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

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
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to save Course : {}", course);
        if (course.getCode() == null || courseService.findOne(course.getCode())!= null) {
            return ResponseEntity.badRequest().build();
        }
        Course result = courseService.save(course);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/courses")
    public ResponseEntity<Course> updateCourse(@Valid @RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to update Course : {}", course);
        if (course.getCode() == null) {
            return createCourse(course);
        }
        Course result = courseService.save(course);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        log.debug("REST request to get all Courses");
        return courseService.findAll();
    }


    @GetMapping("/courses/{code}")
    public ResponseEntity<Course> getCourse(@PathVariable String code) {
        log.debug("REST request to get Course : {}", code);
        Course course = courseService.findOne(code);
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

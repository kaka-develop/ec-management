package org.group2.webapp.service;

import org.group2.webapp.entity.Course;
import org.group2.webapp.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    public Course save(Course course) {
        log.debug("Request to save Course : {}", course);
        Course result = courseRepository.save(course);
        return result;
    }


    @Transactional(readOnly = true)
    public List<Course> findAll() {
        log.debug("Request to get all Courses");
        List<Course> result = courseRepository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public Course findOne(String code) {
        log.debug("Request to get Course : {}", code);
        Course course = courseRepository.findOne(code);
        return course;
    }

    public void delete(String code) {
        log.debug("Request to delete Course : {}", code);
        courseRepository.delete(code);
    }
}

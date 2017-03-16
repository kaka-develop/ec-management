package org.group2.webapp.service;

import org.group2.webapp.entity. Course;
import org.group2.webapp.repository. CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class  CourseService {

    private final Logger log = LoggerFactory.getLogger( CourseService.class);

    private final  CourseRepository  courseRepository;

    public  CourseService( CourseRepository  courseRepository) {
        this. courseRepository =  courseRepository;
    }


    @Transactional(readOnly = true)
    public List< Course> findAll() {
        log.debug("Request to get all  Courses");
        List< Course> result =  courseRepository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public  Course findOne(String code) {
        log.debug("Request to get  Course : {}", code);
        if(code == null)
            return null;
         Course  course =  courseRepository.findOne(code);
        return  course;
    }

    public void delete(String code) {
        log.debug("Request to delete  Course : {}", code);
        if(code == null ||  courseRepository.findOne(code) == null)
            return;
         courseRepository.delete(code);
    }

    public  Course create( Course  course) {
        log.debug("Request to create  course: {}",  course);
        return  courseRepository.save( course);
    }

    public  Course update( Course  course) {
        log.debug("Request to update  course: {}",  course.getCode());
        if( course.getCode() == null)
            return null;
        return  courseRepository.save( course);
    }
}

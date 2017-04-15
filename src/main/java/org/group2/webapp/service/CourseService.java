package org.group2.webapp.service;

import java.util.List;

import org.group2.webapp.entity. Assessment;
import org.group2.webapp.repository. CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class  CourseService {

    private final Logger log = LoggerFactory.getLogger( CourseService.class);

    private final  CourseRepository  courseRepository;

    public  CourseService( CourseRepository  courseRepository) {
        this. courseRepository =  courseRepository;
    }


    @Transactional(readOnly = true)
    public List< Assessment> findAll() {
        log.debug("Request to get all  Courses");
        List< Assessment> result =  courseRepository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public  Assessment findOne(String code) {
        log.debug("Request to get  Course : {}", code);
        if(code == null)
            return null;
         Assessment  course =  courseRepository.findOne(code);
        return  course;
    }

    public void delete(String code) {
        log.debug("Request to delete  Course : {}", code);
        if(code == null ||  courseRepository.findOne(code) == null)
            return;
         courseRepository.delete(code);
    }

    public  Assessment create( Assessment  course) {
        log.debug("Request to create  course: {}",  course);
        return  courseRepository.save( course);
    }

    public  Assessment update( Assessment  course) {
        log.debug("Request to update  course: {}",  course.getCode());
        if( course.getCode() == null)
            return null;
        return  courseRepository.save( course);
    }
}

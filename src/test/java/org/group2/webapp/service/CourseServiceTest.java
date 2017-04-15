package org.group2.webapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Assessment;
import org.group2.webapp.repository.CourseRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
@Transactional
public class CourseServiceTest {

    private final Logger log = LoggerFactory.getLogger(CourseServiceTest.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;


    private final String COURSE_CODE = "AAAAAAAA";
    private final String COURSE_TITLE = "AAAAAAAA";

    private Assessment course;

    @Before
    public void before() {
        course = new Assessment();
        course.setCode(COURSE_CODE);
        course.setTitle(COURSE_TITLE);
        course = courseService.create(course);
        log.debug("done create course");
    }

    @Test
    public void testShouldHaveCourse() {
        assertThat(!courseRepository.findAll().isEmpty());
        assertThat(!courseService.findAll().isEmpty());
    }

    @Test
    public void testShouldHaveOneCourseByID() {
        assertThat(courseRepository.findOne(course.getCode())!= null);
        assertThat(courseService.findOne(course.getCode())!= null);

        assertThat(courseRepository.findOne("BBBBBBB")== null);
        assertThat(courseService.findOne( "BBBBBBB")== null);
    }

    @After
    public void after() {
        courseService.delete(course.getCode());
        log.debug("done delete course");
    }
}

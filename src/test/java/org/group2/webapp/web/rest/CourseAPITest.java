package org.group2.webapp.web.rest;

import org.group2.webapp.EcManagementApplication;

import org.group2.webapp.entity.Course;
import org.group2.webapp.repository.CourseRepository;
import org.group2.webapp.service.CourseService;

import org.group2.webapp.web.rest.admin.CourseAPI;
import org.group2.webapp.web.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class CourseAPITest {

    private static final String COURSE_CODE = "AAAAAAAAAA";
    private static final String COURSE_TITLE = "AAAAAAAAAA";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;


    @Autowired
    private EntityManager em;

    private MockMvc restCourseMockMvc;

    private Course course;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseAPI courseAPI = new CourseAPI(courseService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseAPI).build();
    }

    public static Course createEntity(EntityManager em) {
        Course course = new Course();
        course.setCode(COURSE_CODE);
        course.setTitle(COURSE_TITLE);
        return course;
    }

    @Before
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();


        restCourseMockMvc.perform(post("/api/admin/courses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(course)))
                .andExpect(status().isOk());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseRepository.findOne(course.getCode());
        assertThat(testCourse.getTitle()).isEqualTo(COURSE_TITLE);
    }

    @Test
    @Transactional
    public void createCourseWithExistingCode() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        Course existingCourse = new Course();
        existingCourse.setCode(COURSE_CODE);

        restCourseMockMvc.perform(post("/api/admin/courses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingCourse)))
                .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        course.setTitle(null);

        restCourseMockMvc.perform(post("/api/admin/courses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(course)))
                .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/admin/courses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].code").value(hasItem(course.getCode())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(COURSE_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/admin/courses/{code}", course.getCode()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.code").value(course.getCode()))
                .andExpect(jsonPath("$.title").value(COURSE_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        restCourseMockMvc.perform(get("/api/admin/courses/{code}", "BBBBBBBBB"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        courseService.save(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findOne(course.getCode());
        updatedCourse
                .setTitle(COURSE_TITLE + COURSE_TITLE);

        restCourseMockMvc.perform(put("/api/admin/courses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCourse)))
                .andExpect(status().isOk());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse =courseRepository.findOne(course.getCode());
        assertThat(testCourse.getTitle()).isEqualTo(COURSE_TITLE + COURSE_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        restCourseMockMvc.perform(put("/api/admin/courses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(course)))
                .andExpect(status().isOk());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        courseService.save(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        restCourseMockMvc.perform(delete("/api/admin/courses/{code}", course.getCode())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }

}

package org.group2.webapp.web.rest;

import org.group2.webapp.EcManagementApplication;

import org.group2.webapp.entity.Item;
import org.group2.webapp.repository.CourseRepository;
import org.group2.webapp.repository.ItemRepository;
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
    private ItemRepository courseRepository;

    @Autowired
    private CourseService courseService;


    @Autowired
    private EntityManager em;

    private MockMvc restCourseMockMvc;

    private Item course;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseAPI courseAPI = new CourseAPI(courseService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseAPI).build();
    }

    public static Item createEntity(EntityManager em) {
        Item course = new Item();
        course.setCrn(COURSE_CODE);
        course.setTitle(COURSE_TITLE);
        return course;
    }

    @Before
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void testShouldResponseAddedCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();


        restCourseMockMvc.perform(post("/api/admin/courses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(course)))
                .andExpect(status().isOk());

        List<Item> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Item testCourse = courseRepository.findOne(course.getCrn());
        assertThat(testCourse.getTitle()).isEqualTo(COURSE_TITLE);
    }

    @Test
    @Transactional
    public void testShouldResponseCourseWithExistingCrn() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        Item existingCourse = new Item();
        existingCourse.setCrn(COURSE_CODE);

        restCourseMockMvc.perform(post("/api/admin/courses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingCourse)))
                .andExpect(status().isBadRequest());

        List<Item> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void testShouldResponseCourseIsInvalid() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        course.setTitle(null);

        restCourseMockMvc.perform(post("/api/admin/courses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(course)))
                .andExpect(status().isBadRequest());

        List<Item> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void testShouldResponseAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/admin/courses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].crn").value(hasItem(course.getCrn())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(COURSE_TITLE.toString())));
    }

    @Test
    @Transactional
    public void testShouldResponseOneCourseByCrn() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/admin/courses/{crn}", course.getCrn()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.crn").value(course.getCrn()))
                .andExpect(jsonPath("$.title").value(COURSE_TITLE.toString()));
    }

    @Test
    @Transactional
    public void testShouldResponseCourseIsNotFound() throws Exception {
        restCourseMockMvc.perform(get("/api/admin/courses/{crn}", "BBBBBBBBB"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testShouldResponseUpdatedCourse() throws Exception {
        courseService.update(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Item updatedCourse = courseRepository.findOne(course.getCrn());
        updatedCourse
                .setTitle(COURSE_TITLE + COURSE_TITLE);

        restCourseMockMvc.perform(put("/api/admin/courses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCourse)))
                .andExpect(status().isOk());

        List<Item> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Item testCourse =courseRepository.findOne(course.getCrn());
        assertThat(testCourse.getTitle()).isEqualTo(COURSE_TITLE + COURSE_TITLE);
    }

    @Test
    @Transactional
    public void testShouldResponseOkDeletingCourseByCrn() throws Exception {
        courseService.create(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        restCourseMockMvc.perform(delete("/api/admin/courses/{crn}", course.getCrn())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<Item> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }

}

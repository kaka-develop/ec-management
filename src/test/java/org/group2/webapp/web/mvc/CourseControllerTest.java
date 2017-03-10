package org.group2.webapp.web.mvc;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Course;
import org.group2.webapp.service.CourseService;
import org.group2.webapp.web.mvc.ctrl.admin.CourseController;
import org.group2.webapp.web.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class CourseControllerTest {

    @Autowired
    private CourseService courseService;

    private MockMvc restCourseMockMvc;

    private final String COURSE_CODE = "AAAAAAAA";
    private final String COURSE_TITLE = "AAAAAAAA";

    private Course course;


    @Before
    public void setup() {
        CourseController courseResource = new CourseController(courseService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource).build();
    }

    @Before
    public void initTest() {
        course = new Course();
        course.setCode(COURSE_CODE);
        course.setTitle(COURSE_TITLE);
    }

    public void createCourse() {
        courseService.save(course);
    }


    @Test
    @Transactional
    public void testIndex() throws Exception {
        restCourseMockMvc.perform(get("/admin/course"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(view().name("admin/course/courses"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetNew() throws Exception {
        restCourseMockMvc.perform(get("/admin/course/new"))
                .andExpect(model().attributeExists("course"))
                .andExpect(view().name("admin/course/add"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostNew() throws Exception {
        restCourseMockMvc.perform(post("/admin/course/new")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(course)))
                .andExpect(model().attributeExists("courses"))
                .andExpect(view().name("admin/course/courses"))
                .andExpect(status().isOk());

        course.setTitle(null);
        restCourseMockMvc.perform(post("/admin/course/new")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(course)))
                .andExpect(view().name("admin/course/add"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetDetail() throws Exception {
        createCourse();

        restCourseMockMvc.perform(get("/admin/course/detail")
                .param("code", course.getCode()))
                .andExpect(model().attributeExists("course"))
                .andExpect(view().name("admin/course/detail"))
                .andExpect(status().isOk());

        restCourseMockMvc.perform(get("/admin/course/detail")
                .param("code", "BBBBBBBB"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(view().name("admin/course/courses"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetEdit() throws Exception {
        createCourse();

        restCourseMockMvc.perform(get("/admin/course/edit")
                .param("code", course.getCode()))
                .andExpect(model().attributeExists("course"))
                .andExpect(view().name("admin/course/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostEdit() throws Exception {
        createCourse();
        course.setTitle(COURSE_TITLE + COURSE_TITLE);

        restCourseMockMvc.perform(post("/admin/course/edit")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(course)))
                .andExpect(model().attributeExists("courses"))
                .andExpect(view().name("admin/course/courses"))
                .andExpect(status().isOk());

        course.setTitle(null);
        restCourseMockMvc.perform(post("/admin/course/edit")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(course)))
                .andExpect(view().name("admin/course/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostDelete() throws Exception {
        createCourse();

        restCourseMockMvc.perform(post("/admin/course/delete")
                .param("code", course.getCode()))
                .andExpect(view().name("admin/course/courses"))
                .andExpect(status().isOk());

        restCourseMockMvc.perform(post("/admin/course/delete")
                .param("code", "BBBBBBB"))
                .andExpect(view().name("admin/course/courses"))
                .andExpect(status().isOk());
    }

    @After
    public void after() {
            courseService.delete(course.getCode());
    }

}

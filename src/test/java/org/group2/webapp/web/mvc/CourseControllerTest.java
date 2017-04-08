package org.group2.webapp.web.mvc;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Assessment;
import org.group2.webapp.service.CourseService;
import org.group2.webapp.web.mvc.ctrl.admin.CourseController;
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

    private Assessment course;


    @Before
    public void setup() {
        CourseController courseResource = new CourseController(courseService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource).build();
    }

    @Before
    public void initTest() {
        course = new Assessment();
        course.setCode(COURSE_CODE);
        course.setTitle(COURSE_TITLE);
    }

    public void createCourse() {
        courseService.create(course);
    }


    @Test
    @Transactional
    public void testShouldHaveViewForAllCourses() throws Exception {
        restCourseMockMvc.perform(get("/admin/course"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(view().name("admin/course/courses"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldHaveViewForAddingCourse() throws Exception {
        restCourseMockMvc.perform(get("/admin/course/new"))
                .andExpect(model().attributeExists("course"))
                .andExpect(view().name("admin/course/add"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldPostAddingOneCourse() throws Exception {
        restCourseMockMvc.perform(post("/admin/course/new")
                .param("code",COURSE_CODE)
                .param("title",COURSE_TITLE))
                .andExpect(view().name(CourseController.REDIRECT_INDEX));

        restCourseMockMvc.perform(post("/admin/course/new")
                .param("title",""))
                .andExpect(view().name("admin/course/add"));
    }


    @Test
    @Transactional
    public void testShouldHaveViewForCourseDetail() throws Exception {
        createCourse();

        restCourseMockMvc.perform(get("/admin/course/detail/" + course.getCode()))
                .andExpect(model().attributeExists("course"))
                .andExpect(view().name("admin/course/detail"))
                .andExpect(status().isOk());

        restCourseMockMvc.perform(get("/admin/course/detail/" + "BBBBBBBB"))
                .andExpect(view().name(CourseController.REDIRECT_INDEX));
    }


    @Test
    @Transactional
    public void testShouldHaveViewForEditingCourse() throws Exception {
        createCourse();

        restCourseMockMvc.perform(get("/admin/course/edit/" + course.getCode()))
                .andExpect(model().attributeExists("course"))
                .andExpect(view().name("admin/course/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldPostEditingOneCourse() throws Exception {
        createCourse();

        restCourseMockMvc.perform(post("/admin/course/edit")
                .param("code",COURSE_CODE)
                .param("title",COURSE_TITLE + COURSE_TITLE))
                .andExpect(view().name(CourseController.REDIRECT_INDEX));

        restCourseMockMvc.perform(post("/admin/course/edit")
                .param("title",""))
                .andExpect(view().name("admin/course/edit"));
    }

    @Test
    @Transactional
    public void testShouldPostDeletingOneCourse() throws Exception {
        createCourse();

        restCourseMockMvc.perform(post("/admin/course/delete/" + course.getCode()))
                .andExpect(view().name("admin/course/courses"));

        restCourseMockMvc.perform(post("/admin/course/delete/" + "BBBBBBB"))
                .andExpect(view().name("admin/course/courses"));
    }

    @After
    public void after() {
        courseService.delete(course.getCode());
    }

}

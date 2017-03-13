package org.group2.webapp.web.mvc;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Faculty;
import org.group2.webapp.service.FacultyService;
import org.group2.webapp.web.mvc.ctrl.admin.FacultyController;
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
public class FacultyControllerTest {

    @Autowired
    private FacultyService facultyService;

    private MockMvc restFacultyMockMvc;

    private final String FACULTY_TITLE = "AAAAAAAA";
    private Faculty faculty;


    @Before
    public void setup() {
        FacultyController facultyResource = new FacultyController(facultyService);
        this.restFacultyMockMvc = MockMvcBuilders.standaloneSetup(facultyResource).build();
    }

    @Before
    public void initTest() {
        faculty = new Faculty();
        faculty.setTitle(FACULTY_TITLE);
    }

    public void createFaculty() {
        facultyService.save(faculty);
    }


    @Test
    @Transactional
    public void testIndex() throws Exception {
        restFacultyMockMvc.perform(get("/admin/faculty"))
                .andExpect(model().attributeExists("faculties"))
                .andExpect(view().name("admin/faculty/faculties"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetNew() throws Exception {
        restFacultyMockMvc.perform(get("/admin/faculty/new"))
                .andExpect(model().attributeExists("faculty"))
                .andExpect(view().name("admin/faculty/add"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostNew() throws Exception {
        restFacultyMockMvc.perform(post("/admin/faculty/new")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(faculty)))
                .andExpect(model().attributeExists("faculties"))
                .andExpect(view().name("admin/faculty/faculties"))
                .andExpect(status().isOk());

        faculty.setTitle(null);
        restFacultyMockMvc.perform(post("/admin/faculty/new")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(faculty)))
                .andExpect(view().name("admin/faculty/add"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetDetail() throws Exception {
        createFaculty();

        restFacultyMockMvc.perform(get("/admin/faculty/detail")
                .param("id", faculty.getId().toString()))
                .andExpect(model().attributeExists("faculty"))
                .andExpect(view().name("admin/faculty/detail"))
                .andExpect(status().isOk());

        restFacultyMockMvc.perform(get("/admin/faculty/detail")
                .param("id", "BBBBBBBB"))
                .andExpect(model().attributeExists("faculties"))
                .andExpect(view().name("admin/faculty/faculties"))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void testGetEdit() throws Exception {
        createFaculty();

        restFacultyMockMvc.perform(get("/admin/faculty/edit")
                .param("id", faculty.getId().toString()))
                .andExpect(model().attributeExists("faculty"))
                .andExpect(view().name("admin/faculty/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostEdit() throws Exception {
        createFaculty();
        faculty.setTitle(FACULTY_TITLE + FACULTY_TITLE);

        restFacultyMockMvc.perform(post("/admin/faculty/edit")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(faculty)))
                .andExpect(model().attributeExists("faculties"))
                .andExpect(view().name("admin/faculty/faculties"))
                .andExpect(status().isOk());

        faculty.setTitle(null);
        restFacultyMockMvc.perform(post("/admin/faculty/edit")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(faculty)))
                .andExpect(view().name("admin/faculty/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testPostDelete() throws Exception {
        createFaculty();

        restFacultyMockMvc.perform(post("/admin/faculty/delete")
                .param("id", faculty.getId().toString()))
                .andExpect(view().name("admin/faculty/faculties"))
                .andExpect(status().isOk());

        restFacultyMockMvc.perform(post("/admin/faculty/delete")
                .param("id", "BBBBBBB"))
                .andExpect(view().name("admin/faculty/faculties"))
                .andExpect(status().isOk());
    }

    @After
    public void after() {
            facultyService.delete(faculty.getId());
    }

}

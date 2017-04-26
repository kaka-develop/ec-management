package org.group2.webapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Faculty;
import org.group2.webapp.repository.FacultyRepository;
import org.group2.webapp.service.FacultyService;
import org.group2.webapp.web.rest.admin.FacultyAPI;
import org.group2.webapp.web.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class FacultyAPITest {

    private static final String FACULTY_TITLE = "AAAAAAAA";

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyService facultyService;


    @Autowired
    private EntityManager em;

    private MockMvc restFacultyMockMvc;

    private Faculty faculty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FacultyAPI facultyResource = new FacultyAPI(facultyService);
        this.restFacultyMockMvc = MockMvcBuilders.standaloneSetup(facultyResource).build();
    }

    public static Faculty createEntity(EntityManager em) {
        Faculty faculty = new Faculty();
        faculty.setTitle(FACULTY_TITLE);
        return faculty;
    }

    @Before
    public void initTest() {
        faculty = createEntity(em);
    }

    @Test
    @Transactional
    public void testShouldResponseAddedFaculty() throws Exception {
        int databaseSizeBeforeCreate = facultyRepository.findAll().size();
        

        restFacultyMockMvc.perform(post("/api/admin/faculties")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(faculty)))
                .andExpect(status().isOk());
        
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeCreate + 1);
        Faculty testFaculty = facultyList.get(facultyList.size() - 1);
        assertThat(testFaculty.getTitle()).isEqualTo(FACULTY_TITLE);
    }

    @Test
    @Transactional
    public void testShouldResponseFacultyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = facultyRepository.findAll().size();
        
        Faculty existingFaculty = new Faculty();
        existingFaculty.setId(1L);
        
        restFacultyMockMvc.perform(post("/api/admin/faculties")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingFaculty)))
                .andExpect(status().isBadRequest());
        
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void testShouldResponseFacultyIsInvalid() throws Exception {
        int databaseSizeBeforeTest = facultyRepository.findAll().size();
        faculty.setTitle(null);

        restFacultyMockMvc.perform(post("/api/admin/faculties")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(faculty)))
                .andExpect(status().isBadRequest());

        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void testShouldResponseAllFaculties() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList
        restFacultyMockMvc.perform(get("/api/admin/faculties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(faculty.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(FACULTY_TITLE.toString())));
    }

    @Test
    @Transactional
    public void testShouldResponseOneFacultyById() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get the faculty
        restFacultyMockMvc.perform(get("/api/admin/faculties/{id}", faculty.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(faculty.getId().intValue()))
                .andExpect(jsonPath("$.title").value(FACULTY_TITLE.toString()));
    }

    @Test
    @Transactional
    public void testShouldResponseFacultyIsNotFound() throws Exception {
        restFacultyMockMvc.perform(get("/api/admin/faculties/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testShouldResponseUpdatedFaculty() throws Exception {
        facultyService.create(faculty);

        int databaseSizeBeforeUpdate = facultyRepository.findAll().size();

        // Update the faculty
        Faculty updatedFaculty = facultyRepository.findOne(faculty.getId());
        updatedFaculty
                .setTitle(FACULTY_TITLE + FACULTY_TITLE);

        restFacultyMockMvc.perform(put("/api/admin/faculties")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFaculty)))
                .andExpect(status().isOk());

        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeUpdate);
        Faculty testFaculty = facultyList.get(facultyList.size() - 1);
        assertThat(testFaculty.getTitle()).isEqualTo(FACULTY_TITLE + FACULTY_TITLE);
    }


    @Test
    @Transactional
    public void testShouldResponseOkDeletingFaculty() throws Exception {
        facultyService.create(faculty);

        int databaseSizeBeforeDelete = facultyRepository.findAll().size();

        restFacultyMockMvc.perform(delete("/api/admin/faculties/{id}", faculty.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeDelete - 1);
    }

}

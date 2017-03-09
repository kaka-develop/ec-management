package org.group2.webapp.service;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Faculty;
import org.group2.webapp.repository.FacultyRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
@Transactional
public class FacultyServiceTest {

    private final Logger log = LoggerFactory.getLogger(FacultyServiceTest.class);

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyService facultyService;


    private final String FACULTY_TITLE = "AAAAAAAA";


    private Faculty faculty;

    @Before
    public void before() {
        faculty = new Faculty();
        faculty.setTitle(FACULTY_TITLE);
        faculty = facultyService.save(faculty);
        log.debug("done create faculty");
    }

    @Test
    public void testFillAll() {
        assertThat(!facultyRepository.findAll().isEmpty());
        assertThat(!facultyService.findAll().isEmpty());
    }

    @Test
    public void testFindOne() {
        assertThat(facultyRepository.findOne(faculty.getId())!= null);
        assertThat(facultyService.findOne(faculty.getId())!= null);

        assertThat(facultyRepository.findOne(new Long(11111))== null);
        assertThat(facultyService.findOne(new Long(11111))== null);
    }

    @After
    public void after() {
        facultyService.delete(faculty.getId());
        log.debug("done delete faculty");
    }
}

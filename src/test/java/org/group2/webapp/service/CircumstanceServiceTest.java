package org.group2.webapp.service;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.repository.CircumstanceRepository;
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
public class CircumstanceServiceTest {

    private final Logger log = LoggerFactory.getLogger(CircumstanceServiceTest.class);

    @Autowired
    private CircumstanceRepository circumstanceRepository;

    @Autowired
    private CircumstanceService circumstanceService;


    private final String CIRCUM_TITLE = "AAAAAAAA";

    private Circumstance circumstance;

    @Before
    public void before() {
        circumstance = new Circumstance();
        circumstance.setTitle(CIRCUM_TITLE);
        circumstance = circumstanceService.create(circumstance);
        log.debug("done create circumstance");
    }

    @Test
    public void testShouldHaveCircumstance() {
        assertThat(!circumstanceRepository.findAll().isEmpty());
        assertThat(!circumstanceService.findAll().isEmpty());
    }

    @Test
    public void testShouldHaveOneCircumstanceByID() {
        assertThat(circumstanceRepository.findOne(circumstance.getId())!= null);
        assertThat(circumstanceService.findOne(circumstance.getId())!= null);

        assertThat(circumstanceRepository.findOne(new Long(11111))== null);
        assertThat(circumstanceService.findOne(new Long(11111))== null);
    }

    @After
    public void after() {
        circumstanceService.delete(circumstance.getId());
        log.debug("done delete circumstance");
    }
}

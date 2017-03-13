package org.group2.webapp.service;

import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.repository.CircumstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CircumstanceService {

    private final Logger log = LoggerFactory.getLogger(CircumstanceService.class);

    private final CircumstanceRepository circumstanceRepository;

    public CircumstanceService(CircumstanceRepository circumstanceRepository) {
        this.circumstanceRepository = circumstanceRepository;
    }


    public Circumstance save(Circumstance circumstance) {
        log.debug("Request to save Circumstance : {}", circumstance);
        Circumstance result = circumstanceRepository.save(circumstance);
        return result;
    }


    @Transactional(readOnly = true)
    public List<Circumstance> findAll() {
        log.debug("Request to get all Circumstances");
        List<Circumstance> result = circumstanceRepository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public Circumstance findOne(Long id) {
        log.debug("Request to get Circumstance : {}", id);
        if(id == null)
            return null;
        Circumstance circumstance = circumstanceRepository.findOne(id);
        return circumstance;
    }

    public void delete(Long id) {
        log.debug("Request to delete Circumstance : {}", id);
        if(id == null || circumstanceRepository.findOne(id) == null)
            return;
        circumstanceRepository.delete(id);
    }

    public List<Circumstance> findCircumstancesPerFaculty(Long id) {
        log.debug("Request to get all Circumstances per Faculty : {}", id);
//        List<Circumstance> result = circumstanceRepository.findAllByFacultyId(id);
        List<Circumstance> result = new ArrayList<>();
        return result;
    }
}

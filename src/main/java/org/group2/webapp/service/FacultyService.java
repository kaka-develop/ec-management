package org.group2.webapp.service;

import org.group2.webapp.entity.Faculty;
import org.group2.webapp.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FacultyService {

    private final Logger log = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    public Faculty save(Faculty faculty) {
        log.debug("Request to save Faculty : {}", faculty);
        Faculty result = facultyRepository.save(faculty);
        return result;
    }


    @Transactional(readOnly = true)
    public List<Faculty> findAll() {
        log.debug("Request to get all Facultys");
        List<Faculty> result = facultyRepository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public Faculty findOne(Long id) {
        log.debug("Request to get Faculty : {}", id);
        Faculty faculty = facultyRepository.findOne(id);
        return faculty;
    }

    public void delete(Long id) {
        log.debug("Request to delete Faculty : {}", id);
        facultyRepository.delete(id);
    }
}

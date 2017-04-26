package org.group2.webapp.service;

import java.util.List;

import org.group2.webapp.entity.Faculty;
import org.group2.webapp.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FacultyService {

    private final Logger log = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
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
        if(id == null)
            return null;
        Faculty faculty = facultyRepository.findOne(id);
        return faculty;
    }

    public void delete(Long id) {
        log.debug("Request to delete Faculty : {}", id);
        if(id == null || facultyRepository.findOne(id) == null)
            return;
        facultyRepository.delete(id);
    }

    public Faculty create(Faculty faculty) {
        log.debug("Request to create faculty: {}", faculty);
        return facultyRepository.save(faculty);
    }

    public Faculty update(Faculty faculty) {
        log.debug("Request to update faculty: {}", faculty.getId());
        if(faculty.getId() == null)
            return null;
        return facultyRepository.save(faculty);
    }
}

package org.group2.webapp.web.rest.admin;

import org.group2.webapp.entity.Faculty;
import org.group2.webapp.service.FacultyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class FacultyAPI {

    private final Logger log = LoggerFactory.getLogger(FacultyAPI.class);

    private static final String ENTITY_NAME = "faculty";

    private final FacultyService facultyService;

    public FacultyAPI(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @PostMapping("/faculties")
    public ResponseEntity<Faculty> createFaculty(@Valid @RequestBody Faculty faculty) throws URISyntaxException {
        log.debug("REST request to save Faculty : {}", faculty);
        if (faculty.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Faculty result = facultyService.save(faculty);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/faculties")
    public ResponseEntity<Faculty> updateFaculty(@Valid @RequestBody Faculty faculty) throws URISyntaxException {
        log.debug("REST request to update Faculty : {}", faculty);
        if (faculty.getId() == null) {
            return createFaculty(faculty);
        }
        Faculty result = facultyService.save(faculty);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/faculties")
    public List<Faculty> getAllFacultys() {
        log.debug("REST request to get all Facultys");
        return facultyService.findAll();
    }


    @GetMapping("/faculties/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        log.debug("REST request to get Faculty : {}", id);
        Faculty faculty = facultyService.findOne(id);
        return ResponseEntity.ok(faculty);
    }

    @DeleteMapping("/faculties/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        log.debug("REST request to delete Faculty : {}", id);
        facultyService.delete(id);
        return ResponseEntity.ok().build();
    }

}

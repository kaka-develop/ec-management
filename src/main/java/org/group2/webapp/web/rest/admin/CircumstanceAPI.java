package org.group2.webapp.web.rest.admin;

import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.service.CircumstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class CircumstanceAPI {

    private final Logger log = LoggerFactory.getLogger(CircumstanceAPI.class);

    private static final String ENTITY_NAME = "circumstance";

    private final CircumstanceService circumstanceService;

    public CircumstanceAPI(CircumstanceService circumstanceService) {
        this.circumstanceService = circumstanceService;
    }


    @PostMapping("/circumstances")
    public ResponseEntity<Circumstance> createCircumstance(@Valid @RequestBody Circumstance circumstance) throws URISyntaxException {
        log.debug("REST request to save Circumstance : {}", circumstance);
        if (circumstance.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Circumstance result = circumstanceService.create(circumstance);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/circumstances")
    public ResponseEntity<Circumstance> updateCircumstance(@Valid @RequestBody Circumstance circumstance) throws URISyntaxException {
        log.debug("REST request to update Circumstance : {}", circumstance);
        if (circumstance.getId() == null) {
            return createCircumstance(circumstance);
        }
        Circumstance result = circumstanceService.update(circumstance);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/circumstances")
    public List<Circumstance> getAllCircumstances() {
        log.debug("REST request to get all Circumstances");
        return circumstanceService.findAll();
    }


    @GetMapping("/circumstances/{id}")
    public ResponseEntity<Circumstance> getCircumstance(@PathVariable Long id) {
        log.debug("REST request to get Circumstance : {}", id);
        Circumstance circumstance = circumstanceService.findOne(id);
        if(circumstance == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(circumstance);
    }

    @DeleteMapping("/circumstances/{id}")
    public ResponseEntity<Void> deleteCircumstance(@PathVariable Long id) {
        log.debug("REST request to delete Circumstance : {}", id);
        circumstanceService.delete(id);
        return ResponseEntity.ok().build();
    }

}

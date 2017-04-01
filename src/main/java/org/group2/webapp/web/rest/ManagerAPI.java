package org.group2.webapp.web.rest;

import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.Faculty;
import org.group2.webapp.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerAPI {
    private final Logger log = LoggerFactory.getLogger(ManagerAPI.class);

    private final ManagerService managerService;

    public ManagerAPI(ManagerService managerService) {
        this.managerService = managerService;
    }


    @GetMapping("/claims")
    public List<Claim> getAllClaims() {
        log.debug("REST request to get all Claims");
        return managerService.findAllClaims();
    }


    @GetMapping("/claims/faculty")
    public HashMap<String,Integer> getAllClaimsPerFaculty() {
        log.debug("REST request to get all Claims per faulty");
        return managerService.getClaimsPerFaculty();
    }

    @GetMapping("/claims/year")
    public HashMap<Integer,Integer> getAllClaimsPerYear() {
        log.debug("REST request to get all Claims per year");
        return managerService.getClamsPerYear();
    }
}

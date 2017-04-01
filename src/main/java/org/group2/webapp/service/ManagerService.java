package org.group2.webapp.service;

import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.Faculty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class ManagerService {
    private final Logger log = LoggerFactory.getLogger(ManagerService.class);

    private ClaimService claimService;

    private FacultyService facultyService;

    private CircumstanceService circumstanceService;

    public ManagerService(ClaimService claimService, FacultyService facultyService, CircumstanceService circumstanceService) {
        this.claimService = claimService;
        this.facultyService = facultyService;
        this.circumstanceService = circumstanceService;
    }

    public List<Claim> findAllClaims() {
        return claimService.findAll();
    }

    public HashMap<String, Integer> getClaimsPerFaculty() {
        HashMap<String, Integer> map = new HashMap<>();
        List<Faculty> faculties = facultyService.findAll();
        for (Faculty item : faculties) {
            List<Claim> claimsFaculty = claimService.findClaimsPerFaculty(item.getId());
            map.put(item.getTitle(), claimsFaculty.size());
        }
        return map;
    }

    public HashMap<Integer, Integer> getClamsPerYear() {
        HashMap<Integer, Integer> map = new HashMap<>();
        List<Claim> claims = findAllClaims();
        for (Claim item : claims) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(item.getCreated_time());
            Integer year = calendar.get(Calendar.YEAR);
            if (!map.containsKey(year))
                map.put(year, 1);
            else
                map.put(year, map.get(year) + 1);

        }
        return map;
    }

    public HashMap<String, Integer> getProcessedClaimsPerFaculty() {
        HashMap<String, Integer> map = new HashMap<>();
        List<Faculty> faculties = facultyService.findAll();
        for (Faculty item : faculties) {
            List<Claim> claimsFaculty = claimService.findProcessedClaimsPerFaculty(item.getId());
            map.put(item.getTitle(), claimsFaculty.size());
        }
        return map;
    }

    public HashMap<String, Integer> getClaimsPerCircumstance() {
        HashMap<String, Integer> map = new HashMap<>();
        List<Circumstance> circumstances = circumstanceService.findAll();
        for (Circumstance item : circumstances) {
            List<Claim> claims = claimService.findClaimsPerCircumstance(item.getId());
            map.put(item.getTitle(), claims.size());
        }
        return map;
    }

    public HashMap<String, Integer> getValidAndInvalidClaims() {
        HashMap<String, Integer> map = new HashMap<>();
        List<Claim> claims = findAllClaims();
        for (Claim item : claims) {
            if (item.isValid()) {
                if (!map.containsKey("Valid"))
                    map.put("Valid", 1);
                else
                    map.put("Valid", map.get("Valid") + 1);
            }
            else {
                if (!map.containsKey("Invalid"))
                    map.put("Invalid", 1);
                else
                    map.put("Invalid", map.get("Invalid") + 1);
            }
        }
        return map;
    }
}

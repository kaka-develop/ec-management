package org.group2.webapp.service;

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

    public ManagerService(ClaimService claimService, FacultyService facultyService) {
        this.claimService = claimService;
        this.facultyService = facultyService;
    }

    public List<Claim> findAllClaims() {
        return claimService.findAll();
    }

    public HashMap<Faculty,List<Claim>> getClaimsPerFaculty() {
        HashMap<Faculty,List<Claim>> map = new HashMap<>();
        List<Faculty> faculties = facultyService.findAll();
        for(Faculty item : faculties){
            List<Claim> claimsFaculty = claimService.findClaimsPerFaculty(item.getId());
            map.put(item,claimsFaculty);
        }
        return map;
    }

    public HashMap<Integer,List<Claim>> getClamsPerYear() {
        HashMap<Integer,List<Claim>> map = new HashMap<>();
        List<Claim> claims = findAllClaims();
        for(Claim item : claims){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(item.getCreated_time());
            Integer year = calendar.get(Calendar.YEAR);

            if(!map.containsKey(year)) {
                List<Claim> list = new ArrayList<>();
                list.add(item);
                map.put(year,list);
            }else {
                map.get(year).add(item);
            }
        }
        return map;
    }

}

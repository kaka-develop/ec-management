package org.group2.webapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.group2.webapp.entity.Claim;
import org.group2.webapp.util.ConvertUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManagerService {
    private final Logger log = LoggerFactory.getLogger(ManagerService.class);

    private ClaimService claimService;

    private FacultyService facultyService;

    private CircumstanceService circumstanceService;

    private final static String QUERY_CLAIMS_PER_FACULTY = "SELECT f.title AS title, COUNT(c.id) AS number FROM claim c, user u,faculty f WHERE c.user_id = u.id and u.faculty_id = f.id GROUP BY f.title";
    private final static String QUERY_CLAIMS_PER_YEAR = "SELECT YEAR(c.created_time) AS title, COUNT(c.id) AS number FROM claim c GROUP BY c.created_time";
    private final static String QUERY_PROCESSED_CLAIMS_PER_FACULTY = "SELECT f.title AS title, COUNT(c.id) AS number FROM claim c, user u,faculty f WHERE c.user_id = u.id and u.faculty_id = f.id and c.processed_time IS NOT NULL GROUP BY f.title";
    private final static String QUERY_CLAIMS_PER_CIRCUMSTANCE = "SELECT cir.title AS title, COUNT(c.id) AS number FROM claim c, claim_circumstance cc,circumstance cir WHERE c.id = cc.claim_id and cc.circumstance_id = cir.id GROUP BY cir.title";

    @Autowired
    private JdbcTemplate jdbcTemplate;


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
        jdbcTemplate.query(QUERY_CLAIMS_PER_FACULTY, new Object[0],
                (rs, rowNum) -> map.put(rs.getString("title"), rs.getInt("number"))
        );
        return map;
    }

    public HashMap<String, Integer> getClamsPerYear() {
        HashMap<String, Integer> map = new HashMap<>();
        jdbcTemplate.query(QUERY_CLAIMS_PER_YEAR, new Object[0],
                (rs, rowNum) -> map.put(rs.getString("title"), rs.getInt("number"))
        );
        return map;
    }

    public HashMap<String, Integer> getProcessedClaimsPerFaculty() {
        HashMap<String, Integer> map = new HashMap<>();
        jdbcTemplate.query(QUERY_PROCESSED_CLAIMS_PER_FACULTY, new Object[0],
                (rs, rowNum) -> map.put(rs.getString("title"), rs.getInt("number"))
        );
        return map;
    }

    public HashMap<String, Integer> getClaimsPerCircumstance() {
        HashMap<String, Integer> map = new HashMap<>();
        jdbcTemplate.query(QUERY_CLAIMS_PER_CIRCUMSTANCE, new Object[0],
                (rs, rowNum) -> map.put(rs.getString("title"), rs.getInt("number"))
        );
        return map;
    }

    public HashMap<String, Integer> getValidAndInvalidClaims() {
        HashMap<String, Integer> map = new HashMap<>();
        String query = "SELECT COUNT(c.id) AS number FROM claim c WHERE c.evidence IS NOT NULL AND c.closed_date >= CURDATE()";
        jdbcTemplate.query(query, new Object[0],
                (rs, rowNum) -> map.put("Valid", rs.getInt("number"))
        );

        query = "SELECT COUNT(c.id) AS number FROM claim c WHERE c.evidence IS NULL OR c.closed_date < CURDATE()";
        jdbcTemplate.query(query, new Object[0],
                (rs, rowNum) -> map.put("Invalid", rs.getInt("number"))
        );
        return map;
    }

    public List<Claim> findAllClaimsInThisMonth() {
        return claimService.findClaimsInThisMonth();
    }

    public List<Claim> findAllClaimsInThisWeek() {
        return claimService.findClaimsInThisWeek();
    }


    public HashMap<String, Integer> getCustomClaimReport(String reportType, String month, String year) {
        HashMap<String, Integer> map = new HashMap<>();
        StringBuilder sb = stringFromReportType(reportType);
        List<Object> data = new ArrayList<>();

        int inMonth = ConvertUntil.convertStringToInteger(month);
        if(inMonth > 0)
            sb.append(" AND MONTH(c.created_time) = " + inMonth);

        int inYear = ConvertUntil.convertStringToInteger(year);
        if(inYear > 1900)
            sb.append(" AND YEAR(c.created_time) = " + inYear);

        sb.append(" GROUP BY title");
        jdbcTemplate.query(sb.toString(), data.toArray(),
                (rs, rowNum) -> map.put(rs.getString("title"), rs.getInt("number"))
        );
        return map;
    }

    private StringBuilder stringFromReportType(String type){
        StringBuilder sb = new StringBuilder("SELECT * FROM claim");
        if (type.equals("faculty"))
            sb = new StringBuilder("SELECT f.title AS title, COUNT(c.id) AS number FROM claim c, user u,faculty f WHERE c.user_id = u.id and u.faculty_id = f.id");

        if(type.equals("circumstance"))
            sb = new StringBuilder("SELECT cir.title AS title, COUNT(c.id) AS number FROM claim c, claim_circumstance cc,circumstance cir WHERE c.id = cc.claim_id and cc.circumstance_id = cir.id");

        if(type.equals("processed"))
            sb = new StringBuilder("SELECT f.title AS title, COUNT(c.id) AS number FROM claim c, user u,faculty f WHERE c.user_id = u.id and u.faculty_id = f.id and c.processed_time IS NOT NULL");

        return sb;
    }

}

package org.group2.webapp.service;

import org.apache.commons.lang3.StringUtils;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.repository.ClaimRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ClaimService {

    private final Logger log = LoggerFactory.getLogger(ClaimService.class);

    private final ClaimRepository claimRepository;

    public ClaimService(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }


    public Claim create(Claim claim) {
        log.debug("Request to create Claim : {}", claim);
        return claimRepository.save(claim);
    }

    public Claim update(Claim claim) {
        log.debug("Request to update Claim : {}", claim.getId());
        if (claim.getId() == null)
            return null;
        return claimRepository.save(claim);
    }


    @Transactional(readOnly = true)
    public List<Claim> findAll() {
        log.debug("Request to get all Claims");
        List<Claim> result = claimRepository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public Claim findOne(Long id) {
        log.debug("Request to get Claim : {}", id);
        if (id == null)
            return null;
        Claim claim = claimRepository.findOne(id);
        return claim;
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Claim : {}", id);
        if (id == null || claimRepository.findOne(id) == null)
            return;
        claimRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Claim> findClaimsPerFaculty(Long facultyId) {
        log.debug("Request to get all Claims per Faculty : {}", facultyId);
        List<Claim> result = claimRepository.findAllByFacultyId(facultyId);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Claim> findClaimsByYear(int year) {
        log.debug("Request to get all Claims by year : {}", year);
        List<Claim> result = claimRepository.findAllByYear(year);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Claim> findProcessedClaimsPerFaculty(Long facultyId) {
        log.debug("Request to get all processed claims per Faculty : {}", facultyId);
        List<Claim> result = claimRepository.findAllProcessedByFacultyId(facultyId);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Claim> findClaimsPerCircumstance(Long circumstanceId) {
        log.debug("Request to get all Claims per Circumstance : {}", circumstanceId);
        List<Claim> result = claimRepository.findAllByCircumstanceId(circumstanceId);
        return result;
    }
}

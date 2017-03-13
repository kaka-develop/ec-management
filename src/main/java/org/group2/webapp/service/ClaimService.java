package org.group2.webapp.service;

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


    public Claim save(Claim claim) {
        log.debug("Request to save Claim : {}", claim);
        Claim result = claimRepository.save(claim);
        return result;
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

    public void delete(Long id) {
        log.debug("Request to delete Claim : {}", id);
        if (id == null || claimRepository.findOne(id) == null)
            return;
        claimRepository.delete(id);
    }

    public List<Claim> findClaimsPerFaculty(Long id) {
        log.debug("Request to get all Claims per Faculty : {}", id);
        List<Claim> result = claimRepository.findAllByFacultyId(id);
        return result;
    }
}

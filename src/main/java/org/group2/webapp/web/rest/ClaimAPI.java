package org.group2.webapp.web.rest;

import org.group2.webapp.entity.Claim;
import org.group2.webapp.service.ClaimService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClaimAPI {

    private final Logger log = LoggerFactory.getLogger(ClaimAPI.class);

    private static final String ENTITY_NAME = "claim";

    private final ClaimService claimService;

    public ClaimAPI(ClaimService claimService) {
        this.claimService = claimService;
    }


    @PostMapping("/claims")
    public ResponseEntity<Claim> createClaim(@Valid @RequestBody Claim claim) throws URISyntaxException {
        log.debug("REST request to save Claim : {}", claim);
        if (claim.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Claim result = claimService.save(claim);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/claims")
    public ResponseEntity<Claim> updateClaim(@Valid @RequestBody Claim claim) throws URISyntaxException {
        log.debug("REST request to update Claim : {}", claim);
        if (claim.getId() == null) {
            return createClaim(claim);
        }
        Claim result = claimService.save(claim);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/claims")
    public List<Claim> getAllClaims() {
        log.debug("REST request to get all Claims");
        return claimService.findAll();
    }


    @GetMapping("/claims/{id}")
    public ResponseEntity<Claim> getClaim(@PathVariable Long id) {
        log.debug("REST request to get Claim : {}", id);
        Claim claim = claimService.findOne(id);
        return ResponseEntity.ok(claim);
    }

    @DeleteMapping("/claims/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        log.debug("REST request to delete Claim : {}", id);
        claimService.delete(id);
        return ResponseEntity.ok().build();
    }

}

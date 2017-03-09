package org.group2.webapp.service;

import org.group2.webapp.entity.Claim;
import org.group2.webapp.repository.ClaimRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClaimService {

    private final Logger log = LoggerFactory.getLogger(ClaimService.class);

    private final ClaimRepository productRepository;

    public ClaimService(ClaimRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Claim save(Claim product) {
        log.debug("Request to save Claim : {}", product);
        Claim result = productRepository.save(product);
        return result;
    }


    @Transactional(readOnly = true)
    public List<Claim> findAll() {
        log.debug("Request to get all Claims");
        List<Claim> result = productRepository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public Claim findOne(Long id) {
        log.debug("Request to get Claim : {}", id);
        Claim product = productRepository.findOne(id);
        return product;
    }

    public void delete(Long id) {
        log.debug("Request to delete Claim : {}", id);
        productRepository.delete(id);
    }
}

package org.group2.webapp.service;

import org.group2.webapp.entity.AssessItem;
import org.group2.webapp.repository.AssessItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssessItemService {

    private final Logger log = LoggerFactory.getLogger(AssessItemService.class);

    private final AssessItemRepository assessItemRepository;

    public AssessItemService(AssessItemRepository assessItemRepository) {
        this.assessItemRepository = assessItemRepository;
    }


    public AssessItem save(AssessItem assessItem) {
        log.debug("Request to save AssessItem : {}", assessItem);
        AssessItem result = assessItemRepository.save(assessItem);
        return result;
    }


    @Transactional(readOnly = true)
    public List<AssessItem> findAll() {
        log.debug("Request to get all AssessmentItems");
        List<AssessItem> result = assessItemRepository.findAll();

        return result;
    }

    @Transactional(readOnly =  true)
    public List<AssessItem> findAllByAssessmentCrn(String crn){
        log.debug("Request to get all AssessmentItems by ass Id");
        List<AssessItem> result = assessItemRepository.findAllByAssessmentCrn(crn);
        return result;
    }

    @Transactional(readOnly = true)
    public AssessItem findOne(Long id) {
        log.debug("Request to get AssessItem : {}", id);
        if(id == null)
            return null;
        AssessItem assessItem = assessItemRepository.findOne(id);
        return assessItem;
    }

    public void delete(Long id) {
        log.debug("Request to delete AssessItem : {}", id);
        if(id == null || assessItemRepository.findOne(id) == null)
            return;
        assessItemRepository.delete(id);
    }

    public AssessItem create(AssessItem item) {
        log.debug("Request to create item: " + item);
        return assessItemRepository.save(item);
    }
}

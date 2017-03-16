package org.group2.webapp.repository;

import org.group2.webapp.entity.AssessItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssessItemRepository extends JpaRepository<AssessItem, Long> {
    List<AssessItem> findAllByAssessmentCrn(String crn);
}

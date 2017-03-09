package org.group2.webapp.repository;

import org.group2.webapp.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, String> {
}

package org.group2.webapp.repository;

import org.group2.webapp.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Assessment, String> {

}

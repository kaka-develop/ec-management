/**
 * 
 */
package org.group2.webapp.repository;

import java.io.Serializable;

import org.group2.webapp.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
public interface AssessmentRepository extends JpaRepository<Assessment, Serializable> {
	
}

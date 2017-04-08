/**
 * 
 */
package org.group2.webapp.repository;

import org.group2.webapp.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */

public interface AssessmentRepository extends JpaRepository<Assessment, String> {

}

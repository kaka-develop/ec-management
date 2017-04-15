/**
 * 
 */
package org.group2.webapp.repository;

import org.group2.webapp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
public interface ItemRepository extends JpaRepository<Item, String> {

}

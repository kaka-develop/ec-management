/**
 * 
 */
package org.group2.webapp.web.util;

import java.util.Optional;

import org.group2.webapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
public class SessionUtils {
	public static Optional<org.group2.webapp.entity.User> getCurrentUserSession(UserRepository userRepo) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepo.findOneByUsername(user.getUsername());
	}
}

/**
 * 
 */
package org.group2.webapp.web.mvc.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
@Controller
@RequestMapping("/son")
public class SStudentController {
	@RequestMapping("/claim")
	public void viewClaim(HttpServletRequest req) {
		User user = (User) req.getSession().getAttribute("user");
		List<Claim> claims = getAllClaimOfStudent(user);
		req.setAttribute("claims", claims);
	}

	public List<Claim> getAllClaimOfStudent(User student) {
		return null;
	}
}

/**
 * 
 */
package org.group2.webapp.web.mvc.ctrl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.ClaimRepository;
import org.group2.webapp.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
@Controller
@RequestMapping("/ecaa")
public class ECManagerController {
	private static final Logger logger = LoggerFactory.getLogger(ECManagerController.class);

	private ClaimRepository claimRepo;

	@GetMapping("")
	@ResponseBody
	public String index(HttpServletRequest req) {
		User currentUser = (User) req.getSession().getAttribute(SecurityUtils.getCurrentUserLogin());
		// List<Claim> claimOfEc=claimRepo.findAll().size()
		logger.debug("ok men: " + claimRepo.findAll().size());
//		return "claims";
		return "jaja";
	}
}

/**
 * 
 */
package org.group2.webapp.web.mvc.ctrl;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.ClaimRepository;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.security.SecurityUtils;
import org.group2.webapp.web.util.SessionUtils;
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
@RequestMapping("/ecmanager")
public class ECManagerController {
	private static final Logger logger = LoggerFactory.getLogger(ECManagerController.class);

	private ClaimRepository claimRepo;
	private UserRepository userRepo;

	public ECManagerController(ClaimRepository claimRepo, UserRepository userRepo) {
		super();
		this.claimRepo = claimRepo;
		this.userRepo = userRepo;
	}

	@GetMapping("")
	public String index(HttpServletRequest req) {
		User currentUser = SessionUtils.getCurrentUserSession(userRepo).get();
		req.setAttribute("claims", findClaimsOfEC(currentUser));
		return "claim/claims";
	}

	private List<Claim> findClaimsOfEC(User ec) {
		List<Claim> claims = claimRepo.findAll();
		return claims.stream().filter(c -> c.getUser().getFaculty().getId() == ec.getFaculty().getId())
				.collect(Collectors.toList());
	}
}

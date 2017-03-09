/**
 * 
 */
package org.group2.webapp.web.mvc.ctrl;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.group2.webapp.entity.Assessment;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.AssessmentRepository;
import org.group2.webapp.repository.ClaimRepository;
import org.group2.webapp.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
@Controller
@RequestMapping("/son")
public class SStudentController {
	private final UserRepository userRepo;
	private final AssessmentRepository assessmentRepo;
	private final ClaimRepository claimRepo;


	public SStudentController(UserRepository userRepo, AssessmentRepository assessmentRepo, ClaimRepository claimRepo) {
		super();
		this.userRepo = userRepo;
		this.assessmentRepo = assessmentRepo;
		this.claimRepo = claimRepo;
	}

	@GetMapping("")
	public String addClaim() {

		return "claim/add";
	}

	@RequestMapping("/claim")
	public String viewClaim(HttpServletRequest req) {
		// User user = (User) req.getSession().getAttribute(SessionKeys.USER);
		User user = userRepo.findOneByUsername("student1").get();
		List<Claim> claims = getAllClaimOfStudent(user);
		req.setAttribute("claims", claims);
		return "claim/claims";
	}

	public List<Assessment> getAllAssetmentOfStudent(User student){
//		return assessmentRepo.findAll().stream().filter(a->student.get)
		return null;
	}

	public List<Claim> getAllClaimOfStudent(User student) {
		List<Claim> claims = claimRepo.findAll();
		return claims.stream().filter(cl -> cl.getUser().getId() == student.getId()).collect(Collectors.toList());
	}

	public static void main(String[] args) {

	}
}

/**
 * 
 */
package org.group2.webapp.web.mvc.ctrl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.group2.webapp.entity.Authority;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.AssessmentRepository;
import org.group2.webapp.repository.CircumstanceRepository;
import org.group2.webapp.repository.ClaimRepository;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.security.AuthoritiesConstants;
import org.group2.webapp.security.SecurityUtils;
import org.group2.webapp.web.util.MailUtils;
import org.group2.webapp.web.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
@Controller
@RequestMapping("/student")
public class StudentController {
	@Autowired
	private final UserRepository userRepo;
	@Autowired
	private final AssessmentRepository assessmentRepo;
	@Autowired
	private final ClaimRepository claimRepo;
	@Autowired
	private final CircumstanceRepository circumRepo;

	public StudentController(UserRepository userRepo, AssessmentRepository assessmentRepo, ClaimRepository claimRepo,
			CircumstanceRepository circumRepo) {
		super();
		this.userRepo = userRepo;
		this.assessmentRepo = assessmentRepo;
		this.claimRepo = claimRepo;
		this.circumRepo = circumRepo;
	}

	@GetMapping("/claim")
	public String viewClaim(HttpServletRequest req) {
		User currentUser = SessionUtils.getCurrentUserSession(userRepo).get();
		List<String> str = Collections.list(req.getSession().getAttributeNames());
		for (String s : str) {
			System.out.println("session name: " + s);
			System.out.println("value of: " + req.getSession().getAttribute(s));
		}
		List<Claim> claims = getAllClaimOfStudent(currentUser);
		req.setAttribute("claims", claims);
		return "claim/claims";
	}

	@GetMapping("/claim/add")
	public String addClaim(HttpServletRequest req) {
		req.setAttribute("allAssessments", assessmentRepo.findAll());
		req.setAttribute("allCircumstances", circumRepo.findAll());
		return "claim/add";
	}

	@PostMapping("/claim/add")
	public String addClaim(String[] assessments, long[] circumstances, @Valid Claim claim, BindingResult result,
			HttpServletRequest req, @RequestParam("file") MultipartFile[] files) {
		if (assessments != null && circumstances != null && !result.hasErrors()) {
			for (String ass : assessments) {
				claim.getAssessment().add(assessmentRepo.getOne(ass));
			}
			for (long cid : circumstances) {
				claim.getCircumstances().add(circumRepo.getOne(cid));
			}
			claim.setStatus(1);
			User currentUser = SessionUtils.getCurrentUserSession(userRepo).get();
			claim.setUser(currentUser);
			claim.setEvidence("file.pdf;anh.img");
			claimRepo.save(claim);

			// getECProcessClaim(currentUser.getFaculty().getId())
			// .ifPresent(ec -> MailUtils.sendInformNewClaimForECCoordinator(ec,
			// claim));
			return "claim/success";
		} else {

			req.setAttribute("errors", result.getFieldErrors());
			addClaim(req);
		}
		return addClaim(req);
	}

	@GetMapping("/claim/detail")
	public String index(long id, HttpServletRequest req) {
		// User currentUser =
		// SessionUtils.getCurrentUserSession(userRepo).get();
		Claim claim = claimRepo.findOne(id);
		System.out.println("claim: " + claim);
		req.setAttribute("claim", claim);
		return "claim/detail";
	}

	public List<Claim> getAllClaimOfStudent(User student) {
		List<Claim> claims = claimRepo.findAll();
		return claims.stream().filter(cl -> cl.getUser().getId() == student.getId()).collect(Collectors.toList());
	}

	public Optional<User> getECProcessClaim(long facultyId) {
		for (User user : userRepo.findAll()) {
			if (user.getFaculty().getId() == facultyId) {
				return Optional.of(user);
			}
		}
		return Optional.empty();
	}
}

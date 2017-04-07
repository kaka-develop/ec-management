/**
 * 
 */
package org.group2.webapp.web.mvc.ctrl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.group2.webapp.constraints.ClaimStatusContraints;
import org.group2.webapp.entity.Assessment;
import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.Item;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.AssessmentRepository2;
import org.group2.webapp.repository.CircumstanceRepository;
import org.group2.webapp.repository.ClaimRepository;
import org.group2.webapp.repository.ItemRepository;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.security.AuthoritiesConstants;
import org.group2.webapp.web.util.MailUtils;
import org.group2.webapp.web.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
@Controller
@RequestMapping("/student/claim")
public class StudentController {
	private static final Logger logger = Logger.getLogger(StudentController.class);
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AssessmentRepository2 assessmentRepo;
	@Autowired
	private ClaimRepository claimRepo;
	@Autowired
	private CircumstanceRepository circumRepo;
	@Autowired
	private ItemRepository itemRepo;

	@GetMapping("/view")
	public String viewClaim(HttpServletRequest req) {
		User currentUser = SessionUtils.getCurrentUserSession(userRepo).get();
		List<String> str = Collections.list(req.getSession().getAttributeNames());
		for (String s : str) {
			System.out.println("session name: " + s);
			System.out.println("value of: " + req.getSession().getAttribute(s));
		}
		List<Claim> claims = claimRepo.findAllByUserId(currentUser.getId());
		Collections.sort(claims, new Comparator<Claim>() {

			@Override
			public int compare(Claim o1, Claim o2) {
				
				return o2.getCreated_time().compareTo(o1.getCreated_time());
			}
		});
		
		
		req.setAttribute("claims", claims);
		return "claim/claims";
	}

	@GetMapping("/add")
	public String addClaim(HttpServletRequest req) {
		List<Assessment> suitableAssessment = assessmentRepo.findAll().stream()
				.filter(ass -> ass.getFaculty().getId() == SessionUtils.getCurrentUserSession(userRepo).get()
						.getFaculty().getId())
				.collect(Collectors.toList());

		req.setAttribute("allAssessments", suitableAssessment);
		req.setAttribute("allCircumstances", circumRepo.findAll());
		logger.debug("suitableAssessment: " + suitableAssessment.size());
		return "claim/add";
	}

	@PostMapping("/add")
	public String addClaim(String[] itemCrns, Long[] circumstances, String content,
			HttpServletRequest req) {
		if (itemCrns != null && circumstances != null && content != null) {
			List<Circumstance> myCirumstance = circumRepo.findAll(Arrays.asList(circumstances));
			User currentUser = SessionUtils.getCurrentUserSession(userRepo).get();
			for (String ass : itemCrns) {
				Item item = itemRepo.findOne(ass);
				Claim myClaim = new Claim();
				myClaim.setStatus(ClaimStatusContraints.PENDING);
				myClaim.setUser(currentUser);
				myClaim.setEvidence("file.pdf;anh.img");
				myClaim.setContent(content);
				myClaim.setItem(item);
				myClaim.getCircumstances().addAll(myCirumstance);
				claimRepo.save(myClaim);
				System.out.println("Claim chuan bi save: " + myClaim);
				MailUtils.sendClaimNewsForCoordinators(myClaim,
						userRepo.findAllUserByAuthority(AuthoritiesConstants.COORDINATOR));
			}
			req.setAttribute("claimAdded", true);
			return viewClaim(req);
		} else {
			return addClaim(req);
		}

	}

	@GetMapping("/detail")
	public String index(Long id, HttpServletRequest req) {
		Claim claim = claimRepo.findOne(id);
		System.out.println("claim: " + claim);
		req.setAttribute("claim", claim);
		return "claim/detail";
	}
}

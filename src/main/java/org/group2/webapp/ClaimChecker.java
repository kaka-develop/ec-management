/**
 * 
 */
package org.group2.webapp;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.AssessmentRepository;
import org.group2.webapp.repository.AuthorityRepository;
import org.group2.webapp.repository.CircumstanceRepository;
import org.group2.webapp.repository.ClaimRepository;
import org.group2.webapp.repository.FacultyRepository;
import org.group2.webapp.repository.ItemRepository;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.security.AuthoritiesConstants;
import org.group2.webapp.web.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
@Component
public class ClaimChecker extends Thread {
	private static final Logger logger = Logger.getLogger(ClaimChecker.class);
	private static final int CHECK_FREQUENCY = 1000 * 3;
	private static final int SO_NGAY_PHAI_XU_LY = 14;
	private static final int SO_NGAY_NHAC_NHO = 10;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ClaimRepository claimRepository;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				checkForAllClaim();
				Thread.sleep(CHECK_FREQUENCY);
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.warn("Claim checker thread have been disabled!");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start() {
		super.start();
		logger.info("ClaimChecker started");
	}

	/**
	 * ${tags}
	 */
	private void checkForAllClaim() {
		List<Claim> claimsNotBeProcessed = claimRepository.findAllNotBeProcessed();
		LocalDateTime today = LocalDateTime.now();
		for (Claim claim : claimsNotBeProcessed) {
			LocalDateTime submitDate = LocalDateTime.ofInstant(claim.getCreated_time().toInstant(),
					ZoneId.systemDefault());
			int code = 0;

			if (today.isAfter(submitDate.plusDays(SO_NGAY_PHAI_XU_LY))) {
				code = 1;
			} else if (today.isAfter(submitDate.plusDays(SO_NGAY_NHAC_NHO))) {
				code = 2;
			} else {
				continue;
			}

			Date lastTime = claim.getLastTimeRemind();
			if (lastTime == null || !today.toLocalDate()
					.equals(LocalDateTime.ofInstant(lastTime.toInstant(),
							ZoneId.systemDefault()).toLocalDate())) {
				claim.setLastTimeRemind(java.sql.Date.valueOf(today.toLocalDate()));
				claimRepository.save(claim);

				List<User> coordinators = userRepository.findAllUserByAuthorityAndFacultyId(
						AuthoritiesConstants.COORDINATOR, claim.getUser().getFaculty().getId());

				if (code == 1) {
					MailSender.sendMailForClaimNotProcessedOverDeadline(claim, coordinators);
					logger.debug("Update claim qua han: " + claim.hashCode());
				} else if (code == 2) {
					MailSender.sendMailForClaimNotProcessedNearDeadline(claim, coordinators);
					logger.debug("Nhac nho mail sap het han: " + claim.hashCode());
				}
			}
		}
		logger.debug("claimsNotBeProcessed:" + claimsNotBeProcessed.size());
	}
}

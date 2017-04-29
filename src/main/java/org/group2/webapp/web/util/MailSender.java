/**
 * 
 */
package org.group2.webapp.web.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.group2.webapp.ClaimChecker;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.security.AuthoritiesConstants;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ForbiddenContextVariableRestriction;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
@Component
public class MailSender {
	private static final Logger logger = Logger.getLogger(MailSender.class);

	private static EmailPattern informStudentClaimProcessed = new EmailPattern();
	private static EmailPattern informStudentNearEvidence = new EmailPattern();
	private static EmailPattern informStudentOverEvidence = new EmailPattern();
	
	private static EmailPattern informCoordinatorNewClaim = new EmailPattern();
	private static EmailPattern informCoordinatorNearDeadline = new EmailPattern();
	private static EmailPattern informCoordinatorOverDeadline = new EmailPattern();
	

	private static Boolean ready = false;
	static {
		if (!ready) {
			informStudentClaimProcessed.loadFromProperties("data/informStudentClaimProcessed.properties");
			informStudentNearEvidence.loadFromProperties("data/informStudentNearEvidence.properties");
			informStudentOverEvidence.loadFromProperties("data/informStudentOverEvidence.properties");
			
			informCoordinatorNewClaim.loadFromProperties("data/informCoordinatorNewClaim.properties");
			informCoordinatorNearDeadline.loadFromProperties("data/informCoordinatorNearDeadline.properties");
			informCoordinatorOverDeadline.loadFromProperties("data/informCoordinatorOverDeadline.properties");
			ready = true;
		}
	}
	
	public static void a(){}
	public static void main(String[] args) {
		a();
		System.out.println(informStudentNearEvidence);
	}

	private static String replaceVal(String str, Claim claim) {
		LocalDateTime submitTime = LocalDateTime.ofInstant(claim.getCreated_time().toInstant(), ZoneId.systemDefault());
		LocalDateTime now = LocalDateTime.now();
//		long soNgayQuaHan = submitTime.plusDays(ClaimChecker).until(now, ChronoUnit.DAYS);
//		str = str.replaceAll("_NUMBER_DAY_FROM_SUBMIT", String.valueOf(soNgayQuaHan));
		
		
		str = str.replaceAll("_ITEM_TITLE", claim.getItem().getTitle());
		str = str.replaceAll("_ASSESSMENT_TITLE", claim.getItem().getAssessment().getTitle());
		
		str = str.replaceAll("_CLAIM_COORDINATOR_LINK",
				"http://localhost:8080/eccoordinator/claim/process?id=" + claim.getId());
		LocalDateTime processDeadline=submitTime.plusDays(ClaimChecker.SO_NGAY_HET_HAN_XU_LY);
		long processDateRemain=now.until(processDeadline, ChronoUnit.DAYS);
		long daysOverDeadline=processDeadline.until(now, ChronoUnit.DAYS);
		str = str.replaceAll("_PROCESS_DEADLINE", processDeadline.toString());
		str = str.replaceAll("_PROCESS_DATE_REMAIN", String.valueOf(processDateRemain));
		str = str.replaceAll("_DAYS_OVER_DEADLINE", String.valueOf(daysOverDeadline));
		
		str = str.replaceAll("_CLAIM_STUDENT_LINK",
				"http://localhost:8080/student/claim/detail?id=" + claim.getId());
		LocalDateTime evidenceDeadline=submitTime.plusDays(ClaimChecker.SO_NGAY_HET_HAN_UPLOAD_EVIDENCE);
		long uploadEvidenceRemain=now.until(evidenceDeadline, ChronoUnit.DAYS);
		str = str.replaceAll("_UPLOAD_EVIDENCE_DEADLINE", evidenceDeadline.toString());
		str = str.replaceAll("_UPLOAD_EVIDENCE_REMAIN", String.valueOf(uploadEvidenceRemain));
		str = str.replaceAll("_STUDENT_NAME", claim.getUser().getFirstName());
		return str;
	}

	private static EmailPattern passParamSubject(EmailPattern emailPattern, Claim claim) {
		String subject = replaceVal(emailPattern.getSubject(), claim);
		String content = replaceVal(emailPattern.getContent(), claim);
		return new EmailPattern(subject, content);
	}

	public static void informCoordinatorNewClaim(Claim claim, List<User> users) {
		logger.info("So luong coordinator: " + users.size());
		EmailPattern email = passParamSubject(informCoordinatorNewClaim, claim);
		for (User user : users) {
			if (user.getFaculty().getId() == claim.getUser().getFaculty().getId()) {
				MailUtils.mail(user.getEmail(), email.getSubject(), email.getContent());
				logger.debug("[Email] send email for coordinator name=" + user.getFirstName() + ", email="
						+ user.getEmail());
			}
		}
	}

	public static void informCoordinatorClaimDeadline(Claim claim, List<User> coordinators) {
//		LocalDateTime submitTime = LocalDateTime.ofInstant(claim.getCreated_time().toInstant(), ZoneId.systemDefault());
//		LocalDateTime now = LocalDateTime.now();
//		long soNgayQuaHan = submitTime.until(now, ChronoUnit.DAYS);

//		String subject = "Claim da bi qua han";
//		StringBuilder content = new StringBuilder();
		EmailPattern email = passParamSubject(informCoordinatorOverDeadline, claim);
		for (User coordinator : coordinators) {
			MailUtils.mail(coordinator.getEmail(), email.getSubject(), email.getContent());
			logger.debug("Claim da bi qua han. Send mail cho: " + coordinator.getEmail());
		}
	}

	public static void informCoordinatorClaimNearDeadline(Claim claim, List<User> coordinators) {
//		LocalDateTime submitTime = LocalDateTime.ofInstant(claim.getCreated_time().toInstant(), ZoneId.systemDefault());
//		LocalDateTime now = LocalDateTime.now();
//		long soNgayQuaHan = submitTime.until(now, ChronoUnit.DAYS);
//
//		String subject = "Claim chuan bi het han";
//		StringBuilder content = new StringBuilder();
		EmailPattern email = passParamSubject(informCoordinatorNearDeadline, claim);
		for (User coordinator : coordinators) {
			MailUtils.mail(coordinator.getEmail(), email.getSubject(), email.getContent());
			logger.debug("Claim chuan bi het han. Send mail cho: " + coordinator.getEmail());
		}
	}
	
	public static void informStudentNearDeadlineEvidence(Claim claim) {
		EmailPattern email = passParamSubject(informStudentNearEvidence, claim);
		System.out.println(email);
		MailUtils.mail(claim.getUser().getEmail(), email.getSubject(), email.getContent());
	}
	
	public static void informStudentOverDeadlineEvidence(Claim claim) {
		EmailPattern email = passParamSubject(informStudentOverEvidence, claim);
		System.out.println(email);
		MailUtils.mail(claim.getUser().getEmail(), email.getSubject(), email.getContent());
	}

	public static void informStudentThatTheClaimProcessed(Claim claim) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("<p>Your claim has final decision!</p>");
//		sb.append("<p>Claim for: ").append(claim.getItem().getTitle()).append("</p>");
//		sb.append("<a href='http://localhost:8080/student/claim/detail?id=").append(claim.getId())
//				.append("'>Click here to see</a>");

		User user = claim.getUser();
		EmailPattern email = passParamSubject(informStudentClaimProcessed, claim);
		MailUtils.mail(user.getEmail(), email.getSubject(), email.getContent());
//		logger.debug("[Email] send email for student name=" + user.getFirstName() + ", email=" + user.getEmail());
	}
}

class MailUtils {
	private static Properties config = null;
	private static Session mailSs;

	static {
		if (config == null) {
			config = new Properties();
			config.put("mail.smtp.auth", "true");
			config.put("mail.smtp.starttls.enable", "true");
			config.put("mail.smtp.host", "smtp.gmail.com");
			config.put("mail.smtp.port", "587");

			mailSs = Session.getInstance(config, new OurAuthentication());
		}
	}

	public static void mail(String to, String subject, String content) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Message msg = new MimeMessage(mailSs);
					msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
					msg.setSubject(subject);
					msg.setContent(content, "text/html");
					Transport.send(msg);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}

class OurAuthentication extends Authenticator {
	private static final String USERNAME = "systemec2017@gmail.com";
	private static final String PASSWORD = "-ec12356789";

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(USERNAME, PASSWORD);
	}
}

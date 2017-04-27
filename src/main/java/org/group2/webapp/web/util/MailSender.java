/**
 * 
 */
package org.group2.webapp.web.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.security.AuthoritiesConstants;
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
	@Autowired
	private UserRepository userRepo;

	public static final MailSender INSTANCE = new MailSender();

	public static void sendClaimNewsForCoordinators(Claim claim, List<User> users) {
		logger.info("So luong coordinator: " + users.size());

		String subject = "New EC Claim";
		StringBuilder contents = new StringBuilder();

		contents.append("You have new EC claim from student!\n");
		contents.append("Claim for: ").append(claim.getItem().getTitle()).append("\n");
		contents.append("<a href='http://localhost:8080/eccoordinator/claim/process?id=").append(claim.getId())
				.append("'>Click here to see</a>");
		for (User user : users) {
			if (user.getFaculty().getId() == claim.getUser().getFaculty().getId()) {
				MailUtils.mail(user.getEmail(), subject, contents.toString());
				logger.debug("[Email] send email for coordinator name=" + user.getFirstName() + ", email="
						+ user.getEmail());
			}
		}
	}

	public static void sendMailForClaimNotProcessedOverDeadline(Claim claim, List<User> coordinators) {
		LocalDateTime submitTime = LocalDateTime.ofInstant(claim.getCreated_time().toInstant(), ZoneId.systemDefault());
		LocalDateTime now = LocalDateTime.now();
		long soNgayQuaHan = submitTime.until(now, ChronoUnit.DAYS);

		String subject = "Claim da bi qua han";
		StringBuilder content = new StringBuilder();
		for (User coordinator : coordinators) {
			MailUtils.mail(coordinator.getEmail(), subject, content.toString());
			logger.debug("Claim da bi qua han. Send mail cho: " + coordinator.getEmail());
		}
	}

	public static void sendMailForClaimNotProcessedNearDeadline(Claim claim, List<User> coordinators) {
		LocalDateTime submitTime = LocalDateTime.ofInstant(claim.getCreated_time().toInstant(), ZoneId.systemDefault());
		LocalDateTime now = LocalDateTime.now();
		long soNgayQuaHan = submitTime.until(now, ChronoUnit.DAYS);

		String subject = "Claim chuan bi het han";
		StringBuilder content = new StringBuilder();
		for (User coordinator : coordinators) {
			MailUtils.mail(coordinator.getEmail(), subject, content.toString());
			logger.debug("Claim chuan bi het han. Send mail cho: " + coordinator.getEmail());
		}
	}

	public static void sendClaimNewsForStudent(Claim claim) {
		StringBuilder sb = new StringBuilder();
		sb.append("<p>Your claim has final decision!</p>");
		sb.append("<p>Claim for: ").append(claim.getItem().getTitle()).append("</p>");
		sb.append("<a href='http://localhost:8080/student/claim/detail?id=").append(claim.getId())
				.append("'>Click here to see</a>");

		User user = claim.getUser();
		MailUtils.mail(user.getEmail(), "EC Claim", sb.toString());
		logger.debug("[Email] send email for student name=" + user.getFirstName() + ", email=" + user.getEmail());
	}

	public static void main(String[] args) {
		MailUtils.mail("sondcgc00681@fpt.edu.vn", "Title",
				"<a href='http://localhost:8080/student/claim/detail?id=1'>Click vao day</a>");
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

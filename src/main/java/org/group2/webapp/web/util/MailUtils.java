/**
 * 
 */
package org.group2.webapp.web.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.User;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
public class MailUtils {

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

	public static void sendInformNewClaim(User user, Claim claim) {
		String subject = "New claim";
		String content = "You have a new claim, click here to see";
		sendMail(user.getEmail(), user.getEmail(), subject, content);
	}

	public static void sendMail(String from, String to, String subject, String content) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Message msg = new MimeMessage(mailSs);
					msg.setFrom(new InternetAddress(from));
					msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
					msg.setSubject(subject);
					msg.setText(content);
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
	private static final String PASSWORD = "ec123456789";

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(USERNAME, PASSWORD);
	}
}

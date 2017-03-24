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

	public static void sendInformNewClaimForECCoordinator(User user, Claim claim) {
		StringBuilder sb=new StringBuilder();
		sb.append("You have new EC claim from student!\n");
		sb.append("<a href='localhost:8080'>Click here to see</a>");
		sendMail(user.getEmail(), user.getEmail(), "New EC Claim", sb.toString());
	}
	

	public static void sendInformNewClaimProcessForStudent(User user, Claim claim) {
		StringBuilder sb=new StringBuilder();
		sb.append("Your EC claim has final decision!\n");
		sb.append("<a href='localhost:8080'>Click here to see</a>");
		
		sendMail(user.getEmail(), user.getEmail(), "EC Claim", sb.toString());
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
	
	public static void main(String[] args) {
		sendMail("kunedo1104@gmail.com", "sondcgc00681@fpt.edu.vn", "Moi", "Noi dung");
		System.out.println("Done");
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

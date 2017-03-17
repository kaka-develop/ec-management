/**
 * 
 */
package org.group2.webapp.service;

import javax.mail.MessagingException;

import org.group2.webapp.entity.User;
import org.group2.webapp.web.util.MailUtils;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
public class MailTest extends TestCase {
	@Test
	public void testSendingMail() throws MessagingException {
		User user=new User();
		user.setEmail("kunedo1104@gmail.com");
		MailUtils.sendInformNewClaim(user, null);
	}
}

/**
 * 
 */
package org.group2.webapp.web.mvc.ctrl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
@Controller
@RequestMapping("/son")
public class SonTestController {

	private UserRepository userRepo;

	public SonTestController(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@GetMapping("")
	@ResponseBody
	public String test(HttpServletRequest req) {

		Enumeration<String> e = req.getSession().getAttributeNames();
		List<String> lst = Collections.list(e);

		System.out.println("lenght of: " + lst.size());
		for (String str : lst) {
			System.out.println("session name: " + str);
			System.out.println("Valuee: " + req.getSession().getAttribute(str));
			System.out.println("class: " + req.getSession().getAttribute(str).getClass().getName());
		}
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		org.group2.webapp.entity.User currentUser = userRepo.findOneByUsername(user.getUsername()).get();
		System.out.println("dan choi: " + user);
		System.out.println("ok men: " + user.getUsername());
		System.out.println("userRepo: " + userRepo.findOneByUsername(user.getUsername()));
		System.out.println("user session: " + currentUser);
		return "hihi";
	}
}

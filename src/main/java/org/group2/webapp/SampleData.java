/**
 * 
 */
package org.group2.webapp;

import org.apache.log4j.Logger;
import org.group2.webapp.entity.Authority;
import org.group2.webapp.entity.Faculty;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.AuthorityRepository;
import org.group2.webapp.repository.FacultyRepository;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.service.FacultyService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import javassist.tools.reflect.Sample;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
 *
 */
@Component
public class SampleData {
	private static final Logger logger = Logger.getLogger(SampleData.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private FacultyRepository facultyRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public SampleData() {
		super();
	}

	public void run() {
		if (userRepository.findAll().size() > 0) {
			return;
		} else {
			userRepository.deleteAll();
			authorityRepository.deleteAll();
			facultyRepository.deleteAll();
		}

		logger.info("Initializing sample data...");

		Authority roleAdmin = new Authority("ROLE_ADMIN");
		Authority roleManagere = new Authority("ROLE_MANAGER");
		Authority roleCoordinator = new Authority("ROLE_COORDINATOR");
		Authority roleStudent = new Authority("ROLE_STUDENT");

		authorityRepository.save(roleAdmin);
		authorityRepository.save(roleManagere);
		authorityRepository.save(roleCoordinator);
		authorityRepository.save(roleStudent);

		Faculty f1 = new Faculty("Faculty 1");
		Faculty f2 = new Faculty("Faculty 2");
		Faculty f3 = new Faculty("Faculty 3");
		Faculty f4 = new Faculty("Faculty 4");
		Faculty f5 = new Faculty("Faculty 5");

		facultyRepository.save(f1);
		facultyRepository.save(f2);
		facultyRepository.save(f3);
		facultyRepository.save(f4);
		facultyRepository.save(f5);

		User admin = new User("admin", passwordEncoder.encode("1234"), "adminFirstName",
				"adminLastName", "email1@fpt.edu.vn");
		admin.getAuthorities().add(roleAdmin);
		User manager = new User("manager", passwordEncoder.encode("1234"),
				"managerFirstName",
				"managerLastName", "email2@fpt.edu.vn");
		manager.getAuthorities().add(roleManagere);
		User coordinator1 = new User("coordinator1", passwordEncoder.encode("1234"),
				"coordinator1FirstName", "coordinator1LastName", "email3@fpt.edu.vn",
				f1);
		coordinator1.getAuthorities().add(roleCoordinator);
		User coordinator2 = new User("coordinator2", passwordEncoder.encode("1234"),
				"coordinator2FirstName", "coordinator2LastName", "email4@fpt.edu.vn",
				f2);
		coordinator2.getAuthorities().add(roleCoordinator);
		User s1 = new User("s1", passwordEncoder.encode("1234"), "s1firstname",
				"s1LastName",
				"email5@fpt.edu.vn", f1);
		s1.getAuthorities().add(roleStudent);
		User s2 = new User("s2", passwordEncoder.encode("1234"), "s2firstname",
				"s2LastName",
				"email6@fpt.edu.vn", f1);
		s2.getAuthorities().add(roleStudent);
		User s3 = new User("s3", passwordEncoder.encode("1234"), "s3firstname",
				"s3LastName",
				"email7@fpt.edu.vn", f2);
		s3.getAuthorities().add(roleStudent);
		User s4 = new User("s4", passwordEncoder.encode("1234"), "s4firstname",
				"s4LastName",
				"email8@fpt.edu.vn", f2);
		s4.getAuthorities().add(roleStudent);

		userRepository.save(admin);
		userRepository.save(manager);
		userRepository.save(coordinator1);
		userRepository.save(coordinator2);
		userRepository.save(s1);
		userRepository.save(s2);
		userRepository.save(s3);
		userRepository.save(s4);

		logger.info("Sample data ready!");
	}

}

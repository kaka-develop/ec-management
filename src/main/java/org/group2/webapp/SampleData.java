/**
 * 
 */
package org.group2.webapp;

import java.util.Date;

import org.apache.log4j.Logger;
import org.group2.webapp.entity.Assessment;
import org.group2.webapp.entity.Authority;
import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.Course;
import org.group2.webapp.entity.Faculty;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.AssessmentRepository;
import org.group2.webapp.repository.AuthorityRepository;
import org.group2.webapp.repository.CircumstanceRepository;
import org.group2.webapp.repository.ClaimRepository;
import org.group2.webapp.repository.CourseRepository;
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
	private AssessmentRepository assessmentRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private CircumstanceRepository circumstanceRepository;
	@Autowired
	private ClaimRepository claimRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired

	public SampleData() {
		super();
	}

	public void run() {
		if (claimRepository.findAll().size() > 0) {
			return;
		} else {
			claimRepository.deleteAll();
			assessmentRepository.deleteAll();
			circumstanceRepository.deleteAll();
			userRepository.deleteAll();
			courseRepository.deleteAll();
			facultyRepository.deleteAll();
			authorityRepository.deleteAll();
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

		Course c1 = new Course("COMP-1108", "Project", f1);
		Course c2 = new Course("COMP-1639", "Database Engineering", f2);
		Course c3 = new Course("COMP-1640", "Enterprise Web Software Dev", f2);
		Course c4 = new Course("COMP-1661", "Application Dev for Mobile Dev", f1);
		Course c5 = new Course("COMP-1649", "Interaction Design", f3);
		Course c6 = new Course("COMP-1714", "Software Engineeing Mgmnt", f3);
		Course c7 = new Course("COMP-1648", "Dev Framework & Methods", f1);

		courseRepository.save(c1);
		courseRepository.save(c2);
		courseRepository.save(c3);
		courseRepository.save(c4);
		courseRepository.save(c5);
		courseRepository.save(c6);
		courseRepository.save(c7);

		Assessment ass1 = new Assessment("23718", c1, "COMP 1108 Demonstration");
		Assessment ass2 = new Assessment("23717", c1, "COMP 1108 Final Report");
		Assessment ass3 = new Assessment("24761", c2, "COMP 1639 Exam");
		Assessment ass4 = new Assessment("24760", c2, "COMP 1639 Practical Coursework");
		Assessment ass5 = new Assessment("24767", c3, "COMP 1640 Coureswork");
		Assessment ass6 = new Assessment("25042", c7, "COMP 1648 Coursework");
		Assessment ass7 = new Assessment("25045", c5, "COMP 1649 Coursework");
		Assessment ass8 = new Assessment("25066", c4, "COMP 1661 Coursework");
		Assessment ass9 = new Assessment("25067", c4, "COMP 1661 Logbook");
		Assessment ass10 = new Assessment("25391", c6, "COMP 1714 Coursework");
		Assessment ass11 = new Assessment("25392", c6, "COMP 1714 Exam");

		assessmentRepository.save(ass1);
		assessmentRepository.save(ass2);
		assessmentRepository.save(ass3);
		assessmentRepository.save(ass4);
		assessmentRepository.save(ass5);
		assessmentRepository.save(ass6);
		assessmentRepository.save(ass7);
		assessmentRepository.save(ass8);
		assessmentRepository.save(ass9);
		assessmentRepository.save(ass10);
		assessmentRepository.save(ass11);

		Circumstance cir1 = new Circumstance("Accident");
		Circumstance cir2 = new Circumstance("Bereavement");
		Circumstance cir3 = new Circumstance("Harassment or Assault");
		Circumstance cir4 = new Circumstance("Jury Service");
		Circumstance cir5 = new Circumstance("Medical");
		Circumstance cir6 = new Circumstance("Organisational maladministration");
		Circumstance cir7 = new Circumstance("Unexpected personal or family difficulties");
		Circumstance cir8 = new Circumstance("Work (part-time and placement studends only)");
		Circumstance cir9 = new Circumstance("Other");

		circumstanceRepository.save(cir1);
		circumstanceRepository.save(cir2);
		circumstanceRepository.save(cir3);
		circumstanceRepository.save(cir4);
		circumstanceRepository.save(cir5);
		circumstanceRepository.save(cir6);
		circumstanceRepository.save(cir7);
		circumstanceRepository.save(cir8);
		circumstanceRepository.save(cir9);

		Claim cl1 = new Claim();
		cl1.getAssessment().add(ass1);
		cl1.getCircumstances().add(cir1);
		cl1.setUser(s1);
		cl1.setProcessed_time(new Date());
		cl1.setContent("hello world");

		claimRepository.save(cl1);

		logger.info("Sample data ready!");
	}

}

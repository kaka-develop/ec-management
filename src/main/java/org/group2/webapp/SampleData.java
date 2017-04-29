
package org.group2.webapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.group2.webapp.entity.Assessment;
import org.group2.webapp.entity.Authority;
import org.group2.webapp.entity.Circumstance;
import org.group2.webapp.entity.Claim;
import org.group2.webapp.entity.Faculty;
import org.group2.webapp.entity.Item;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.AssessmentRepository;
import org.group2.webapp.repository.AuthorityRepository;
import org.group2.webapp.repository.CircumstanceRepository;
import org.group2.webapp.repository.ClaimRepository;
import org.group2.webapp.repository.FacultyRepository;
import org.group2.webapp.repository.ItemRepository;
import org.group2.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Dam Cao Son
 * @Contact kunedo1104@gmail.com
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
    private ItemRepository itemRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private CircumstanceRepository circumstanceRepository;
    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private PasswordEncoder passmwordEncoder;

	@Autowired

	public SampleData() {
		super();
	}

	public void run() {
		if (claimRepository.findAll().size() > 0) {
			return;
		} else {
			claimRepository.deleteAll();
			itemRepository.deleteAll();
			circumstanceRepository.deleteAll();
			userRepository.deleteAll();
			assessmentRepository.deleteAll();
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

		User admin = new User("admin", passmwordEncoder.encode("1234"), "adminFirstName",
				"adminLastName", "email1@fpt.edu.vn");
		admin.getAuthorities().add(roleAdmin);
		admin.getAuthorities().add(roleManagere);
		User manager = new User("manager", passmwordEncoder.encode("1234"),
				"managerFirstName",
				"managerLastName", "email2@fpt.edu.vn");
		manager.getAuthorities().add(roleManagere);
		User coordinator1 = new User("coordinator1", passmwordEncoder.encode("1234"),
				"coordinator1FirstName", "coordinator1LastName", "kunedo1104@gmail.com",
				f1);
		coordinator1.getAuthorities().add(roleCoordinator);
		User coordinator2 = new User("coordinator2", passmwordEncoder.encode("1234"),
				"coordinator2FirstName", "coordinator2LastName", "email4@fpt.edu.vn",
				f2);
		coordinator2.getAuthorities().add(roleCoordinator);
		User s1 = new User("student", passmwordEncoder.encode("1234"), "s1firstname",
				"s1LastName",
				"sondcgc00681@fpt.edu.vn", f1);
		s1.getAuthorities().add(roleStudent);
		User s2 = new User("s2", passmwordEncoder.encode("1234"), "s2firstname",
				"s2LastName",
				"email6@fpt.edu.vn", f1);
		s2.getAuthorities().add(roleStudent);
		User s3 = new User("s3", passmwordEncoder.encode("1234"), "s3firstname",
				"s3LastName",
				"email7@fpt.edu.vn", f4);
		s3.getAuthorities().add(roleStudent);
		User s4 = new User("s4", passmwordEncoder.encode("1234"), "s4firstname",
				"s4LastName",
				"email8@fpt.edu.vn", f2);
		s4.getAuthorities().add(roleStudent);

		User s5 = new User("s5", passmwordEncoder.encode("1234"), "s5firstname",
				"s5LastName",
				"email9@fpt.edu.vn", f5);
		s5.getAuthorities().add(roleStudent);

		userRepository.save(admin);
		userRepository.save(manager);
		userRepository.save(coordinator1);
		userRepository.save(coordinator2);
		userRepository.save(s1);
		userRepository.save(s2);
		userRepository.save(s3);
		userRepository.save(s4);
		userRepository.save(s5);

		Assessment ass1 = new Assessment("COMP-1108", "Project", f1);
		Assessment ass2 = new Assessment("COMP-1639", "Database Engineering", f2);
		Assessment ass3 = new Assessment("COMP-1640", "Enterprise Web Software Dev", f2);
		Assessment ass4 = new Assessment("COMP-1661", "Application Dev for Mobile Dev", f1);
		Assessment ass5 = new Assessment("COMP-1649", "Interaction Design", f3);
		Assessment ass6 = new Assessment("COMP-1714", "Software Engineeing Mgmnt", f3);
		Assessment ass7 = new Assessment("COMP-1648", "Dev Framework & Methods", f1);

		assessmentRepository.save(ass1);
		assessmentRepository.save(ass2);
		assessmentRepository.save(ass3);
		assessmentRepository.save(ass4);
		assessmentRepository.save(ass5);
		assessmentRepository.save(ass6);
		assessmentRepository.save(ass7);

		Item item1 = new Item("23718", ass1, "COMP 1108 Demonstration");
		Item item2 = new Item("23717", ass1, "COMP 1108 Final Report");
		Item item3 = new Item("24761", ass2, "COMP 1639 Exam");
		Item item4 = new Item("24760", ass2, "COMP 1639 Practical Coursework");
		Item item5 = new Item("24767", ass3, "COMP 1640 Coureswork");
		Item item6 = new Item("25042", ass7, "COMP 1648 Coursework");
		Item item7 = new Item("25045", ass5, "COMP 1649 Coursework");
		Item item8 = new Item("25066", ass4, "COMP 1661 Coursework");
		Item item9 = new Item("25067", ass4, "COMP 1661 Logbook");
		Item item10 = new Item("25391", ass6, "COMP 1714 Coursework");
		Item item11 = new Item("25392", ass6, "COMP 1714 Exam");

		itemRepository.save(item1);
		itemRepository.save(item2);
		itemRepository.save(item3);
		itemRepository.save(item4);
		itemRepository.save(item5);
		itemRepository.save(item6);
		itemRepository.save(item7);
		itemRepository.save(item8);
		itemRepository.save(item9);
		itemRepository.save(item10);
		itemRepository.save(item11);

		Circumstance cir1 = new Circumstance("Accident");
		Circumstance cir2 = new Circumstance("Bereavement");
		Circumstance cir3 = new Circumstance("Haritemment or Assault");
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
		cl1.setItem(item1);
		cl1.getCircumstances().add(cir1);
		cl1.setUser(s1);
		cl1.setProcessed_time(new Date());
		cl1.setContent("hello world");

		claimRepository.save(cl1);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		try {
			cl1 = new Claim("Evidence1", "Content1", new Date(), new Date(), 1, s2, new Date());
			cl1.clearCircumstances();
			cl1.addCircumstance(cir1);
			cl1.addCircumstance(cir2);
			cl1.addCircumstance(cir3);
			cl1.setItem(item1);
			claimRepository.save(cl1);

			cl1 = new Claim("Evidence2", "Content2", new Date(), null, 1, s2, formatter.parse("2017-05-05"));
			cl1.clearCircumstances();
			cl1.addCircumstance(cir2);
			cl1.addCircumstance(cir3);
			cl1.addCircumstance(cir4);
			cl1.setItem(item3);
			claimRepository.save(cl1);

			cl1 = new Claim("Evidence3", "Content3", formatter.parse("2016-05-05"), null, 1, s2, new Date());
			cl1.clearCircumstances();
			cl1.addCircumstance(cir4);
			cl1.addCircumstance(cir5);
			cl1.setItem(item4);
			claimRepository.save(cl1);

			cl1 = new Claim("Evidence4", "Content4", new Date(), new Date(), 1, s4, formatter.parse("2017-04-28"));
			cl1.clearCircumstances();
			cl1.addCircumstance(cir5);
			cl1.addCircumstance(cir6);
			cl1.setItem(item5);
			claimRepository.save(cl1);

			cl1 = new Claim("Evidence5", "Content5", new Date(), null, 1, s3, formatter.parse("2017-04-30"));
			cl1.clearCircumstances();
			cl1.addCircumstance(cir7);
			cl1.addCircumstance(cir6);
			cl1.setItem(item6);
			claimRepository.save(cl1);

			cl1 = new Claim("Evidence6", "Content6", formatter.parse("2015-05-05"), new Date(), 1, s5,
					formatter.parse("2017-04-06"));
			cl1.clearCircumstances();
			cl1.addCircumstance(cir6);
			cl1.addCircumstance(cir8);
			cl1.setItem(item7);
			claimRepository.save(cl1);

			cl1 = new Claim("Evidence7", "Content7", formatter.parse("2015-05-05"), new Date(), 1, s5,
					formatter.parse("2017-06-20"));
			cl1.clearCircumstances();
			cl1.addCircumstance(cir1);
			cl1.addCircumstance(cir2);
			cl1.setItem(item8);
			claimRepository.save(cl1);

			cl1 = new Claim("Evidence8", "Content8", new Date(), null, 1, s4, formatter.parse("2017-04-01"));
			claimRepository.save(cl1);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		logger.info("Sample data ready!");
	}

	/**
	 * ${tags}
	 */
	public void test() {
		// List<Assessment> assessments = assessmentRepository.findAll();
		// System.out.println("assessments: " + assessments.size());
		// for (Assessment ass : assessments) {
		// System.out.println("Item of assessment(" + ass.getTitle() + "): " +
		// ass.getItems().size());
		// }
	}

	public static void main(String[] args) {

	}
}

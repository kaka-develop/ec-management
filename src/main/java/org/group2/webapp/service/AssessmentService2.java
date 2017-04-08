package org.group2.webapp.service;

import org.apache.log4j.Logger;
import org.group2.webapp.repository.AssessmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AssessmentService2 {

	private final Logger logger = Logger.getLogger(AssessmentService2.class);

	private final AssessmentRepository assessmentRepository;

	public AssessmentService2(AssessmentRepository assessmentRepository) {
		super();
		this.assessmentRepository = assessmentRepository;
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.group2.webapp.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author dfChicken
 */
@Entity
@Table(name = "faculty_course")
public class FacultyCourse implements Serializable {
    @EmbeddedId
    private FacultyCoursePK id;
}

class FacultyCoursePK implements Serializable {

    @OneToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty_id;

    @OneToOne
    @JoinColumn(name = "course_code")
    private Course course_code;
}

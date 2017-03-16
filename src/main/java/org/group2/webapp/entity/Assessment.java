/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.group2.webapp.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dfChicken
 */
@Entity
@Table(name = "assessment")
public class Assessment implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 0, max = 50)
    @Id
    @Column(length = 50, unique = true, nullable = false)
    private String crn;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, unique = true, nullable = false)
    private String title;

    @ManyToOne
    private Course course;

    @OneToMany(mappedBy = "assessment")
    private Set<AssessItem> assessItems;

    @ManyToMany
    @JoinTable(
            name = "assessment_claim",
            joinColumns = {@JoinColumn(name = "assessment_crn", referencedColumnName = "crn")},
            inverseJoinColumns = {@JoinColumn(name = "claim_id", referencedColumnName = "id")})
    private Set<Claim> claim = new HashSet<>();

    public Assessment() {
    }

    public Set<AssessItem> getAssessItems() {
        return assessItems;
    }

    public void setAssessItems(Set<AssessItem> assessItems) {
        this.assessItems = assessItems;
    }

    public Set<Claim> getClaim() {
        return claim;
    }

    public void setClaim(Set<Claim> claim) {
        this.claim = claim;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}

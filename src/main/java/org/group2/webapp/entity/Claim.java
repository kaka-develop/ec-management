/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.group2.webapp.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Entity
@Table(name = "claim")
public class Claim implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(columnDefinition = "TEXT", unique = true, nullable = true)
    private String evidence;

    @NotNull
    @Column(columnDefinition = "TEXT", unique = true, nullable = true)
    private String content;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String decision;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false)
    private Date created_time = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "processed_time")
    private Date processed_time;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "closed_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date closedDate;

    @Column(name = "status")
    private int status;


    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "assessment_claim",
            joinColumns = {@JoinColumn(name = "claim_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "assessment_crn", referencedColumnName = "crn")})
    private Set<Assessment> assessment = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "claim_circumstance",
            joinColumns = {@JoinColumn(name = "claim_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "circumstance_id", referencedColumnName = "id")})
    private Set<Circumstance> circumstances = new HashSet<>();

    public Claim() {
    }

    public Set<Circumstance> getCircumstances() {
        return circumstances;
    }

    public void setCircumstances(Set<Circumstance> circumstances) {
        this.circumstances = circumstances;
    }

    public Set<Assessment> getAssessment() {
        return assessment;
    }

    public void setAssessment(Set<Assessment> assessment) {
        this.assessment = assessment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public Date getProcessed_time() {
        return processed_time;
    }

    public void setProcessed_time(Date processed_time) {
        this.processed_time = processed_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public boolean isMissClosedDate() {
        if (this.closedDate == null)
            return false;
        if (this.closedDate.getTime() >= Calendar.getInstance().getTime().getTime())
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "id=" + id +
                ", evidence='" + evidence + '\'' +
                ", content='" + content + '\'' +
                ", created_time=" + created_time +
                ", processed_time=" + processed_time +
                ", status=" + status +
                '}';
    }

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.group2.webapp.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "claim")
public class Claim implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	@Column(name="seen")
	private Boolean seen;

	@ManyToOne
	private User user;

	@ManyToOne
	private Item item;

	@JsonIgnore
	@OneToMany(mappedBy = "claim")
	private Set<Evidence> evidences = new HashSet<>();

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "claim_circumstance", joinColumns = {
			@JoinColumn(name = "claim_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "circumstance_id", referencedColumnName = "id") })
	private Set<Circumstance> circumstances = new HashSet<>();

	public Claim() {
	}

	public Claim(Long id,String evidence, String content, String decision, Date created_time, Date processed_time, Date closedDate, int status) {
		this.id = id;
		this.evidence = evidence;
		this.content = content;
		this.decision = decision;
		this.created_time = created_time;
		this.processed_time = processed_time;
		this.closedDate = closedDate;
		this.status = status;
	}


	public Set<Circumstance> getCircumstances() {
		return circumstances;
	}

	public void setCircumstances(Set<Circumstance> circumstances) {
		this.circumstances = circumstances;
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
		if (this.closedDate.getTime() > Calendar.getInstance().getTime().getTime())
			return false;
		return true;
	}

	public boolean isLackOfEvidence() {
		if (evidence == null)
			return true;
		return false;
	}

	public boolean isValid() {
		if (isMissClosedDate())
			return false;
		if (evidence == null)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Claim [id=" + id + ", evidence=" + evidence + ", content=" + content + ", decision=" + decision
				+ ", created_time=" + created_time + ", processed_time=" + processed_time + ", closedDate=" + closedDate
				+ ", status=" + status + ", user=" + user + ", item=" + item + ", evidences=" + evidences
				+ ", circumstances=" + circumstances + "]";
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Boolean getSeen() {
		return seen;
	}

	public void setSeen(Boolean seen) {
		this.seen = seen;
	}
}

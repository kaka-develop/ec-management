/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.group2.webapp.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private String code;

	@NotNull
	@Size(min = 1, max = 100)
	@Column(length = 100, unique = true, nullable = false)
	private String title;

	@ManyToOne
	private Faculty faculty;

	@OneToMany(mappedBy = "assessment", fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Item> items = new HashSet<>();

	public Assessment(String code, String title, Faculty faculty) {
		super();
		this.code = code;
		this.title = title;
		this.faculty = faculty;
	}

	public Assessment() {
	}

	public void setAssessments(Set<Item> assessments) {
		this.items = assessments;
	}

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Course{" +
				"code='" + code + '\'' +
				", title='" + title + '\'' +
				'}';
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.group2.webapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author dfChicken
 */
@Entity
@Table(name = "circumstance")
public class Circumstance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 1, max = 100)
	@Column(columnDefinition = "TEXT", unique = true, nullable = true)
	private String title;

	@ManyToMany(mappedBy = "circumstances")
	private Set<Claim> claims = new HashSet<>();

	public Circumstance() {
	}

	public Long getId() {
		return id;
	}

	public Circumstance(String title) {
		super();
		this.title = title;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Claim> getClaims() {
		return claims;
	}

	public void setClaims(Set<Claim> claims) {
		this.claims = claims;
	}

	@Override
	public String toString() {
		return "Circumstance{" +
				"id=" + id +
				", title='" + title + '\'' +
				'}';
	}
}

package com.mavenbro.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * HOD class to represent HOD objects. Connected to database table "HOD"
 * 
 * @author brona
 *
 */
@Entity
@Table(name = "HOD")
public class HOD {
	// HOD Fields
	@Id
	@Column(name = "ID")
	private int id;
	// this id is the user.id
	@OneToOne
	@MapsId
	@JoinColumn(name = "ID")
	private User user;

// hod constructors
	public HOD() {
	}

	public HOD(User user) {
		this.user = user;
	}

//getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

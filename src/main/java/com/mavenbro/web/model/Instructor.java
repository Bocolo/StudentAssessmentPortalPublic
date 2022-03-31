package com.mavenbro.web.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "instructor")
public class Instructor {
	@Id
	@Column(name = "ID")
	private int id;
	@OneToMany(mappedBy = "instructor", fetch = FetchType.EAGER)
	private Set<Course> courses = new HashSet<Course>();
	@OneToOne
	@MapsId
	@JoinColumn(name = "ID")
	private User user;

	public Instructor() {
		super();
	}

	public Instructor(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void addCourse(Course course) {
		this.courses.add(course);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

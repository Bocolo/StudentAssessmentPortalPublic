package com.mavenbro.web.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Student class to represent Student objects. Connected to database table
 * "Student"
 * 
 * @author brona
 *
 */
@Entity
@Table(name = "student")
public class Student {
	@Id
	@Column(name = "ID")
	private int id;
	/*
	 * In the Database table, any StudentCourse in this set will have a column with
	 * this.id referencing this Student. if (this) is deleted, every StudentCourse
	 * in this set will also be deleted
	 */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "student")
	private Set<StudentCourse> studentCourses;
	// this id is thr user.id
	@OneToOne
	@MapsId
	@JoinColumn(name = "ID")
	private User user;

	/**
	 * Student Constructors
	 */
	public Student() {
		super();
	}

	public Student(User user) {
		this.user = user;
	}

//Getters and setters
	public int getId() {
		return this.id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<StudentCourse> getStudentCourses() {
		return this.studentCourses;
	}

	public void setStudentCourses(Set<StudentCourse> sc) {
		this.studentCourses = sc;
	}

	/**
	 * method to add a student course to studentCourses set
	 * 
	 * @param sc student course to add to set
	 */
	public void addStudentCourse(StudentCourse sc) {
		this.studentCourses.add(sc);
	}
}

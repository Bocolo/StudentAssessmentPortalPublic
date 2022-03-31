package com.mavenbro.web.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * User class to represent User objects. Connected to database table "user"
 * 
 * @author brona
 *
 */
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(name = "username")
	private String username;
	@Column(name = "email")
	private String email;
	@Column(name = "name")
	private String name;
	@Column(name = "password")
	private String password;
	@Column(name = "permissionlevel")
	private int permissionLevel;
	/*
	 * Each user will relate to a student, instrcutor or HOD
	 */
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Instructor instructor;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Student student;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private HOD hod;

	/*
	 * User Constructors
	 */
	public User() {
	}

	public User(String name, String email, String username, String password, int permissionLevel) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.permissionLevel = permissionLevel;
		this.name = name;
	}

	public User(String name, String email, int id, String username, String password, int permissionLevel) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.permissionLevel = permissionLevel;
		this.id = id;
		this.name = name;
	}

	/*
	 * User Getters and Setters
	 */
	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public HOD getHod() {
		return hod;
	}

	public void setHod(HOD hod) {
		this.hod = hod;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPermissionLevel() {
		return permissionLevel;
	}

	public void setPermissionLevel(int permissionLevel) {
		this.permissionLevel = permissionLevel;
	}

	/**
	 * Overriddin to String method
	 */
	@Override
	public String toString() {
		return this.name + " --- " + this.id + " --- " + this.email + "<br>";
	}
}

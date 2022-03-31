package com.mavenbro.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Course class to represent Course objects. Connected to database table
 * "course"
 * 
 * @author brona
 *
 */
@Entity
@Table(name = "course")
public class Course {
	// course fields
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "quizPercent")
	private int quizPercent;
	@Column(name = "assignmentPercent")
	private int assignmentPercent;
	/*
	 * In the Database table, this Course will have column with a related instructor
	 * id.
	 */
	@ManyToOne(optional = true, cascade = CascadeType.MERGE)
	@JoinColumn(name = "INSTRUCTORID", referencedColumnName = "ID")
	private Instructor instructor;
	/*
	 * In the Database table, any Quizzes in this set will have a column with
	 * this.id referencing this Course. if (this) is deleted, every quiz in this set
	 * will also be deleted
	 */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "course")
	private List<Quiz> quizzes = new ArrayList<Quiz>();
	/*
	 * In the Database table, any Assignment in this set will have a column with
	 * this.id referencing this Course. if (this) is deleted, every Assignment in
	 * this set will also be deleted
	 */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "course")
	private List<Assignment> assignments = new ArrayList<Assignment>();
	/*
	 * In the Database table, any StudentCourse in this set will have a column with
	 * this.id referencing this Course. if (this) is deleted, every StudentCourse in
	 * this set will also be deleted
	 */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "course")
	private Set<StudentCourse> studentCourses;

	/**
	 * Course constructors
	 */
	public Course() {
	}

	public Course(String name) {
		this.name = name;
	}

	public Course(String name, int id) {
		this.name = name;
		this.id = id;
	}

	/**
	 * method to add a student course to studentCourses set
	 * 
	 * @param sc student course to add to set
	 */
	public void addStudentCourse(StudentCourse sc) {
		this.studentCourses.add(sc);
	}

	// course getters and setters
	public Set<StudentCourse> getStudentCourses() {
		return this.studentCourses;
	}

	public void setStudentCourses(Set<StudentCourse> sc) {
		this.studentCourses = sc;
	}

	public List<Quiz> getQuizzes() {
		return quizzes;
	}

	public List<Assignment> getAssignments() {
		return assignments;
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

	public int getQuizPercent() {
		return quizPercent;
	}

	public void setQuizPercent(int quizPercent) {
		this.quizPercent = quizPercent;
	}

	public int getAssignmentPercent() {
		return assignmentPercent;
	}

	public void setAssignmentPercent(int assignmentPercent) {
		this.assignmentPercent = assignmentPercent;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
}

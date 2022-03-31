package com.mavenbro.web.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Assignment class to represent assignment objects. Connected to database table
 * "assignment"
 * 
 * @author brona
 *
 */
@Entity
@Table(name = "assignment")
public class Assignment {
	// Assignment Fields
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "assignmentPercent")
	private int assignmentPercent;
	/*
	 * In the Database table, this Assignment will have column with a related
	 * student course id. If the course is deleted, the assignment will also be
	 * deleted
	 */
	@ManyToOne(optional = true, cascade = CascadeType.MERGE)
	@JoinColumn(name = "COURSE_ID", referencedColumnName = "ID")
	private Course course;
	/*
	 * In the Database table, any studentAssignments in this set will have a column
	 * with this.id referencing this Assignment. if (this) is deleted, every
	 * studentAssignment in this set will also be deleted
	 */
	@OneToMany(mappedBy = "assignment", fetch = FetchType.EAGER)
	private Set<StudentAssignment> studentAssignments = new HashSet<StudentAssignment>();

	/**
	 * Assignment constructors
	 */
	public Assignment() {
	}

	public Assignment(int assignmentPercent, String name) {
		this.name = name;
		this.assignmentPercent = assignmentPercent;
	}

	public Assignment(String name, int id, int quizPercent, Course course) {
		this.name = name;
		this.id = id;
		this.assignmentPercent = quizPercent;
		this.course = course;
	}

	/**
	 * Overriding to String method
	 */
	@Override
	public String toString() {
		return this.name + " --- " + this.id + " --- " + "<br>   " + this.assignmentPercent + "   ";
	}

	// Getters and Setters of private assignment fields
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Set<StudentAssignment> getStudentAssignment() {
		return studentAssignments;
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

	public int getAssignmentPercent() {
		return assignmentPercent;
	}

	public void setAssignmentPercent(int assignmentPercent) {
		this.assignmentPercent = assignmentPercent;
	}
}

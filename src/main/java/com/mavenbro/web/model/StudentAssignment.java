package com.mavenbro.web.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * StudentAssignment class to represent StudentAssignment objects. Connected to
 * database table "student_assignment"
 * 
 * @author brona
 *
 */
@Entity
@Table(name = "student_assignment")
public class StudentAssignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(name = "result")
	private int result;
	// Each student assignment is related to a StudentCourse
	@ManyToOne(optional = true, cascade = CascadeType.MERGE)
	@JoinColumn(name = "STUDENTCOURSE_ID", referencedColumnName = "ID")
	private StudentCourse studentCourse;
	// Each student assignment is related to an Assignment
	@ManyToOne(optional = true, cascade = CascadeType.MERGE)
	@JoinColumn(name = "ASSIGNMENT_ID", referencedColumnName = "ID")
	private Assignment assignment;

	/*
	 * Student Assignment Constructors
	 */
	public StudentAssignment(StudentCourse studentCourse, Assignment assignment) {
		this.assignment = assignment;
		this.studentCourse = studentCourse;
	}

	public StudentAssignment() {
	}

	/*
	 * Getters and Setters
	 * 
	 */
	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int quizPercent) {
		this.result = quizPercent;
	}

	public StudentCourse getStudentCourse() {
		return studentCourse;
	}

	public void setStudentCourse(StudentCourse studentCourse) {
		this.studentCourse = studentCourse;
	}

	/**
	 * Overridden toString method
	 */
	@Override
	public String toString() {
		return "Student Quiz: \nid- " + this.getId() + "\nSC id: " + this.getStudentCourse().getId() + "\nResult: "
				+ this.getResult();
	}
}

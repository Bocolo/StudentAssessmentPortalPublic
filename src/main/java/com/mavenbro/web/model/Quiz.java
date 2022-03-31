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
 * Quiz class to represent Quiz objects. Connected to database table
 * "Quiz"
 * 
 * @author brona
 *
 */
@Entity
@Table(name = "quiz")
public class Quiz {
	//Quiz Fields
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "quizPercent")
	private int quizPercent;
	/*
	 * In the Database table, this Quiz will have column with a related
	 * student course id. If the course is deleted, the Quiz will also be deleted
	 */
	@ManyToOne(optional = true, cascade = CascadeType.MERGE)
	@JoinColumn(name = "COURSE_ID", referencedColumnName = "ID")
	private Course course;
	/*
	 * In the Database table, any studentQuizzes in this set will have a column
	 * with this.id referencing this Quiz. if (this) is deleted, every
	 * studentQuiz in this set will also be deleted
	 */
	@OneToMany(mappedBy = "quiz", fetch = FetchType.EAGER)
	private Set<StudentQuiz> studentQuizzes = new HashSet<StudentQuiz>();



	/**
	 * Quiz constructors
	 */
	public Quiz() {
	}


	public Quiz(int quizPercent, String name) {
		this.name = name;
		this.quizPercent = quizPercent;
	}


	public Quiz(String name, int id, int quizPercent, Course course) {
		this.name = name;
		this.id = id;
		this.quizPercent = quizPercent;
		this.course = course;
	}

	// Getters and Setters of private Quiz fields
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}


	public Set<StudentQuiz> getStudentQuizzes() {
		return studentQuizzes;
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
	//Overrding to string method
	@Override
	public String toString() {
		return this.name + " --- " + this.id + " --- " + "<br>   " + this.quizPercent + "   ";
	}
}
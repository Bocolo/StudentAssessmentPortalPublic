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
 * StudentQuiz class to represent StudentQuiz objects. Connected to
 * database table "student_quiz"
 * 
 * @author brona
 *
 */
@Entity
@Table(name="student_quiz")
public class StudentQuiz {
	 @Id
	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	 @Column(name="id")
	private int id;
	 @Column(name="result")
	private int result;
		// Each student Quiz is related to a StudentCourse
	@ManyToOne(optional = true, cascade = CascadeType.MERGE)
    @JoinColumn(name = "STUDENTCOURSE_ID", referencedColumnName = "ID")
	private StudentCourse studentCourse;
	// Each student Quiz is related to a Quiz
	@ManyToOne(optional = true, cascade = CascadeType.MERGE)
    @JoinColumn(name = "QUIZ_ID", referencedColumnName = "ID")
	private Quiz quiz;

	/*
	 * Student Quiz Constructors
	 */
	public StudentQuiz(StudentCourse studentCourse, Quiz quiz) {
		this.quiz=quiz;
		this.studentCourse = studentCourse;
	}
	public StudentQuiz() {
	}

	/*
	 * Getters and Setters
	 * 
	 */
	public Quiz getQuiz() {
		return quiz;
	}
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
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
	public void setResult(int result) {
		this.result = result;
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
		return "Student Quiz: \nid- "+this.getId() 
		+"\nSC id: "+ this.getStudentCourse().getId() 
		+"\nResult: "+ this.getResult();
	}
}
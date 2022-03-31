package com.mavenbro.web.model;

import java.util.ArrayList;
import java.util.List;

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
 * StudentCourse class to represent StudentCourse objects. Connected to database
 * table "student_course"
 * 
 * @author brona
 *
 */
@Entity
@Table(name = "student_course")
public class StudentCourse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	// Each student Course is related to a Student
	@ManyToOne
	@JoinColumn(name = "STUDENT_ID")
	private Student student;
	// Each student Course is related to a Course
	@ManyToOne
	@JoinColumn(name = "COURSE_ID")
	private Course course;
	@Column(name = "MID_RESULT")
	private int midResult;
	@Column(name = "FINAL_RESULT")
	private int finalResult;
	@Column(name = "COURSE_TOTAL")
	private int courseTotalResult;
	/*
	 * In the Database table, any StudentQuiz in this set will have a column with
	 * this.id referencing this StudentCourse. if (this) is deleted, every
	 * StudentQuiz in this set will also be deleted
	 */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "studentCourse")
	private List<StudentQuiz> studentQuizzes = new ArrayList<StudentQuiz>();
	/*
	 * In the Database table, any StudentAssignment in this set will have a column
	 * with this.id referencing this StudentCourse. if (this) is deleted, every
	 * StudentAssignment in this set will also be deleted
	 */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "studentCourse")
	private List<StudentAssignment> studentAssignments = new ArrayList<StudentAssignment>();

	public StudentCourse() {
	}

	public StudentCourse(Course course, Student student) {
		this.student = student;
		this.course = course;
	}

	/**
	 * Adds a studentQuiz to the studentQuizzes set
	 * 
	 * @param studentQuiz the student quiz to be added to the set
	 */
	public void addStudentQuiz(StudentQuiz studentQuiz) {
		studentQuizzes.add(studentQuiz);
	}

	/**
	 * Adds a StudentAssignment to the StudentAssignments set
	 * 
	 * @param studentAssignment the student assignment to be added to the set
	 */
	public void addStudentAssignment(StudentAssignment studentAssignment) {
		studentAssignments.add(studentAssignment);
	}

	/**
	 * method to calculate and set the total student course score from the sum of
	 * all assignment and quiz results summed with the mid and final result
	 */
	public void calculateAndSetCourseTotal() {
		int total = 0;
		total += midResult;
		total += finalResult;
		for (StudentAssignment studentAssignment : studentAssignments) {
			total += (studentAssignment.getResult());
		}
		for (StudentQuiz studentQuiz : studentQuizzes) {
			total += (studentQuiz.getResult());
		}
		this.courseTotalResult = total;
	}

	/*
	 * Student Course Getters and Setters
	 */
	public List<StudentQuiz> getStudentQuizzes() {
		return studentQuizzes;
	}

	public List<StudentAssignment> getStudentAssignments() {
		return studentAssignments;
	}

	public int getCourseTotalResult() {
		return courseTotalResult;
	}

	public void setCourseTotalResult(int courseTotalResult) {
		this.courseTotalResult = courseTotalResult;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Course getCourses() {
		return course;
	}

	public void setCourses(Course course) {
		this.course = course;
	}

	public int getMidResult() {
		return midResult;
	}

	public void setMidResult(int midResult) {
		this.midResult = midResult;
	}

	public int getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(int finalResult) {
		this.finalResult = finalResult;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	/**
	 * Overridding to String method
	 */
	@Override
	public String toString() {
		return id + "  " + midResult + "  " + finalResult;
	}
}
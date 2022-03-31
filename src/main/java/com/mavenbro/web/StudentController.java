package com.mavenbro.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.mavenbro.web.dao.CourseDao;
import com.mavenbro.web.dao.StudentAssignmentDao;
import com.mavenbro.web.dao.StudentCourseDao;
import com.mavenbro.web.dao.StudentDao;
import com.mavenbro.web.dao.StudentQuizDao;
import com.mavenbro.web.model.Assignment;
import com.mavenbro.web.model.Course;
import com.mavenbro.web.model.Quiz;
import com.mavenbro.web.model.Student;
import com.mavenbro.web.model.StudentAssignment;
import com.mavenbro.web.model.StudentCourse;
import com.mavenbro.web.model.StudentQuiz;
import com.mavenbro.web.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * This servlet manages all behaviours related to students
 * 
 * @author brona
 *
 */
@WebServlet("/studentController")
public class StudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentDao studentDao;
	private CourseDao courseDao;
	private StudentCourseDao studentCourseDao;

	/**
	 * method that sets the student, studentcourse and course dao's
	 */
	public void init() {
		studentDao = new StudentDao();
		courseDao = new CourseDao();
		studentCourseDao = new StudentCourseDao();
	}

	/**
	 * the servlet post method, calls the get method
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * the get method uses a switch-case to call the correct method based on the
	 * student action that is passed as a parameter
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String studentAction = request.getParameter("studentAction");
		try {
			switch (studentAction) {
			case "registerForCourse":
				registerForCourse(request, response);
				break;
			case "unregisterFromCourse":
				unregisterFromCourse(request, response);
				break;
			case "getStudentCourses":
				getStudentCourses(request, response);
				break;
			case "seeCourseResult":
				getCourseResult(request, response);
				break;
			case "seeAllCourseResults":
				getAllCourseResults(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	/**
	 * gets all student courses for the signed in user and redirects to the
	 * resultBreakdown page
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getAllCourseResults(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		HttpSession session = request.getSession();
		int studentId = ((User) session.getAttribute("signedInUser")).getId();
		Set<StudentCourse> studentCourses = studentCourseDao.getAllStudentCoursesByStudent(studentId);
		RequestDispatcher dispatcher = request.getRequestDispatcher("resultBreakdown.jsp");
		request.setAttribute("studentCourses", studentCourses);
		dispatcher.forward(request, response);
	}

	/**
	 * gets a specific student course using course and student id and redirects to
	 * the result breakdown page
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getCourseResult(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int studentId = Integer.parseInt(request.getParameter("studentId"));
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		StudentCourse studentCourse = studentCourseDao.getStudentCourseByCourseStudent(courseId, studentId);
		RequestDispatcher dispatcher = request.getRequestDispatcher("resultBreakdown.jsp");
		request.setAttribute("studentCourse", studentCourse);
		dispatcher.forward(request, response);
	}

	/**
	 * gets all the students registered and unregistered courses redirects to
	 * showCourses page
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getStudentCourses(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		HttpSession session = request.getSession();
		User user = ((User) session.getAttribute("signedInUser"));
		Set<StudentCourse> studentCourses = studentCourseDao.getAllStudentCoursesByStudent(user.getId());
		Set<Course> registeredCourses = new HashSet<Course>();
		for (StudentCourse studentCourse : studentCourses) {
			registeredCourses.add(studentCourse.getCourse());
		}
		Set<Course> unregisteredCourseList = courseDao.getAllCoursesExcluding(studentCourses);
		request.setAttribute("unregisteredCourses", unregisteredCourseList);
		request.setAttribute("registeredCourses", registeredCourses);
		RequestDispatcher dispatcher = request.getRequestDispatcher("showCourses.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * registers a student for a course. creates a student course and the relevant
	 * student quizzes and assignments. assigns a course result and calls the
	 * getStudentCourses method
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void registerForCourse(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		HttpSession session = request.getSession();
		StudentQuizDao studentQuizDao = new StudentQuizDao();
		StudentAssignmentDao studentAssignmentDao = new StudentAssignmentDao();
		Course course = courseDao.getCourse(courseId);
		User user = ((User) session.getAttribute("signedInUser"));
		Student student = studentDao.getStudent(user.getId());
		StudentCourse studentCourse = new StudentCourse(course, student);
		student.addStudentCourse(studentCourse);
		course.addStudentCourse(studentCourse);
		studentDao.updateStudent(student);
		courseDao.updateCourse(course);
		studentCourseDao.saveStudentCourse(studentCourse);
		session.setAttribute("signedInUser", user);
		for (Quiz quiz : course.getQuizzes()) {
			StudentQuiz studentQuiza = new StudentQuiz(studentCourse, quiz);
			studentCourse.addStudentQuiz(studentQuiza);
			studentQuizDao.saveStudentQuiz(studentQuiza);
		}
		for (Assignment assignment : course.getAssignments()) {
			StudentAssignment studentAssignmenta = new StudentAssignment(studentCourse, assignment);
			studentCourse.addStudentAssignment(studentAssignmenta);
			studentAssignmentDao.saveStudentAssignment(studentAssignmenta);
		}
		studentCourse.calculateAndSetCourseTotal();
		getStudentCourses(request, response);
	}

	/**
	 * unregisters a student from a course. deletes the related student course from
	 * the db by course and user id. calls the getStudentCourses method
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void unregisterFromCourse(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		HttpSession session = request.getSession();
		User user = ((User) session.getAttribute("signedInUser"));
		studentCourseDao.deleteStudentCourse(courseId, user.getId());
		session.setAttribute("signedInUser", user);
		getStudentCourses(request, response);
	}
}

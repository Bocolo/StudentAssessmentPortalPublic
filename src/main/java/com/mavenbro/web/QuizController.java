package com.mavenbro.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import com.mavenbro.web.dao.CourseDao;
import com.mavenbro.web.dao.QuizDao;
import com.mavenbro.web.dao.StudentCourseDao;
import com.mavenbro.web.dao.StudentQuizDao;
import com.mavenbro.web.model.Course;
import com.mavenbro.web.model.Quiz;
import com.mavenbro.web.model.StudentCourse;
import com.mavenbro.web.model.StudentQuiz;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * This servlet manages all behaviours related to quizzes
 * 
 * @author brona
 *
 */
@WebServlet("/quizController")
public class QuizController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private QuizDao quizDao;
	private CourseDao courseDao;
	/**
	 * method that sets the quiz and course dao's
	 */
	public void init() {
		quizDao = new QuizDao();
		courseDao = new CourseDao();
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
	 * quiz action that is passed as a parameter
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String quizAction = request.getParameter("quizAction");
		try {
			switch (quizAction) {
			case "deleteQuiz":
				deleteQuiz(request, response);
				break;
			case "updateQuiz":
				updateQuiz(request, response);
				break;
			case "createQuiz":
				addQuiz(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}
	/**
	 * This method creates an quizzes and assigns it to a course, to a student
	 * course and saves to the database if the following conditions are met 1. if
	 * the % set for it, plus the accumulate % of all other quizzes in that
	 * course do not exceed the courses total quiz % 2. if % set for it is not
	 * zero or lower if not met, flags are activated that display error msgs to the
	 * user
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void addQuiz(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		String name = request.getParameter("name");
		int quizPercent = Integer.parseInt(request.getParameter("quizPercent"));
		Quiz newQuiz = new Quiz(quizPercent, name);
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		Course course = courseDao.getCourse(courseId);
		boolean isOver = false;
		int accPercent = quizPercent;
		for (Quiz quiz : course.getQuizzes()) {
			accPercent += quiz.getQuizPercent();
		}
		if (accPercent > course.getQuizPercent()) {
			isOver = true;
		} else {
			isOver = false;
		}
		if (isOver) {
			request.setAttribute("quizzesOverMaxPC", true);
			request.setAttribute("quizLessThanZero", false);
		} else if ((!isOver) && (quizPercent > 0)) {
			request.setAttribute("quizzesOverMaxPC", false);
			request.setAttribute("quizLessThanZero", false);
			newQuiz.setCourse(course);
			quizDao.saveQuiz(newQuiz);
			StudentCourseDao studentCourseDao = new StudentCourseDao();
			Set<StudentCourse> studentCourses = studentCourseDao.getAllStudentCoursesByCourse(courseId);
			StudentQuizDao studentQuizDao = new StudentQuizDao();
			for (StudentCourse studentCourse : studentCourses) {
				StudentQuiz studentQuiz = new StudentQuiz(studentCourse, newQuiz);
				studentCourse.addStudentQuiz(studentQuiz);
				studentQuizDao.saveStudentQuiz(studentQuiz);
			}
			course = courseDao.getCourse(courseId);
		} else {
			request.setAttribute("quizLessThanZero", true);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("addQuiz.jsp");
		request.setAttribute("course", course);
		dispatcher.forward(request, response);
	}
	/**
	 * This method updates an quiz and saves it to the database if the
	 * following conditions are met 
	 * 1. if the % set for it, plus the accumulate % of
	 * all other quizzes in that course do not exceed the courses total
	 * quiz % 
	 * 2. if % set for it is not zero or lower if not met, bools/flags are
	 * activated that display error msgs to the user
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void updateQuiz(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Quiz originalQuiz = quizDao.getQuiz(id);
		int originalPercent = originalQuiz.getQuizPercent();
		int quizPercent = Integer.parseInt(request.getParameter("quizPercent"));
		String name = request.getParameter("name");
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		Course course = courseDao.getCourse(courseId);
		boolean isOver = false;
		if (quizPercent == 0) {
			request.setAttribute("quizLessThanZero", true);
		}
		int accPercent = 0;
		accPercent = (accPercent + quizPercent);
		accPercent = (accPercent - originalPercent);
		for (Quiz quiz : course.getQuizzes()) {
			accPercent = (accPercent + quiz.getQuizPercent());
		}
		if (accPercent > course.getQuizPercent()) {
			isOver = true;
		} else {
			isOver = false;
		}
		if (isOver) {
			request.setAttribute("quizzesOverMaxPC", true);
		} else {
			Quiz quiz = new Quiz(name, id, quizPercent, course);
			quizDao.updateQuiz(quiz);
			course = courseDao.getCourse(courseId);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("addQuiz.jsp");
		request.setAttribute("course", course);
		dispatcher.forward(request, response);
	}
	/**
	 * This method deletes the quiz and resets the "course" attribute
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteQuiz(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		int quizId = Integer.parseInt(request.getParameter("quizId"));
		Course course = courseDao.getCourse(courseId);
		quizDao.deleteQuiz(quizId);
		course = courseDao.getCourse(courseId);
		RequestDispatcher dispatcher = request.getRequestDispatcher("addQuiz.jsp");
		request.setAttribute("course", course);
		dispatcher.forward(request, response);
	}
}
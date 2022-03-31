package com.mavenbro.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.mavenbro.web.dao.CourseDao;
import com.mavenbro.web.dao.InstructorDao;
import com.mavenbro.web.dao.StudentAssignmentDao;
import com.mavenbro.web.dao.StudentCourseDao;
import com.mavenbro.web.dao.StudentQuizDao;
import com.mavenbro.web.model.Assignment;
import com.mavenbro.web.model.Course;
import com.mavenbro.web.model.Instructor;
import com.mavenbro.web.model.Quiz;
import com.mavenbro.web.model.StudentAssignment;
import com.mavenbro.web.model.StudentCourse;
import com.mavenbro.web.model.StudentQuiz;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * This servlet manages all behaviours related to instructors
 * 
 * @author brona
 *
 */
@WebServlet("/instructorController")
public class InstructorController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InstructorDao instructorDao = null;
	private CourseDao courseDao = null;

	/**
	 * method that sets the course and instructor daos
	 */
	public void init() {
		instructorDao = new InstructorDao();
		courseDao = new CourseDao();
	}

	/**
	 * the servlets post method, calls the get method
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * the get method uses a switch-case to call the correct method based on the
	 * instructor action that is passed as a request parameter
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String instructorAction = request.getParameter("instructorAction");
		try {
			switch (instructorAction) {
			case "listCourses":
				listCourses(request, response);
				break;
			case "setAssessmentPercent":
				setAssessmentPercent(request, response);
				break;
			case "addQuiz":
				addQuiz(request, response);
				break;
			case "addAssignment":
				addAssignment(request, response);
				break;
			case "courseBreakdown":
				courseBreakdown(request, response);
				break;
			case "showStudentTable":
				showStudentResultPage(request, response);
				break;
			case "gradeStudent":
				setStudentGrades(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	/**
	 * Sets the quiz and assignment total of a course and redirects to the page
	 * assigned from the "page" request param. will not set the quiz /assignment
	 * total %s if 1. the sum of the quiz and assignment total exceeds 20%. 2. the
	 * quiz or assignment total % is less than 5 or greater than 15%. 3. the quiz or
	 * assignment total % is lower than the accumulated quizzes %s or assignments
	 * %s, respectively. under condition 3 & 2 a warning statement is populated and
	 * set as a attribute under condition 1 a boolean is set true and set as an
	 * attribute
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void setAssessmentPercent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int quizPercent = Integer.parseInt(request.getParameter("quizPercent"));
		int assignmentPercent = Integer.parseInt(request.getParameter("assignmentPercent"));
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		Course course = courseDao.getCourse(courseId);
		List<Quiz> quizzes = course.getQuizzes();
		int currentQuizTotal = 0;
		for (Quiz quiz : quizzes) {
			currentQuizTotal += quiz.getQuizPercent();
		}
		List<Assignment> assignments = course.getAssignments();
		int currentAssignmentTotal = 0;
		for (Assignment assignment : assignments) {
			currentAssignmentTotal += assignment.getAssignmentPercent();
		}
		String page = request.getParameter("page");
		if ((quizPercent + assignmentPercent) > 20) {
			request.setAttribute("isOverPercentage", true);
		} else if (((quizPercent < 5) || (assignmentPercent < 5)) || ((quizPercent > 15) || (assignmentPercent > 15))) {
			request.setAttribute("isOverPercentage", false);
			String warningStatement = "WARNING:  ";
			if (assignmentPercent < 5) {
				warningStatement += ("The minimum assignment total is 5%. ");
			}
			if (quizPercent < 5) {
				warningStatement += ("The minimum quiz total is 5%. ");
			}
			if (assignmentPercent > 15) {
				warningStatement += ("The maximum assignment total is 15%. ");
			}
			if (quizPercent > 15) {
				warningStatement += ("The maximum quiz total is 15%. ");
			}
			request.setAttribute("canSetTotal", warningStatement);
		} else if ((currentQuizTotal > quizPercent) || (currentAssignmentTotal > assignmentPercent)) {
			request.setAttribute("isOverPercentage", false);
			String warningStatement = "WARNING: ";
			if ((currentAssignmentTotal > assignmentPercent)) {
				warningStatement += ("You cannot set the assignment total lower than the sum of assignments" + ":  "
						+ currentAssignmentTotal + "% is greater than " + assignmentPercent + "%. ");
			}
			if ((currentQuizTotal > quizPercent)) {
				warningStatement += ("You cannot set the Quiz total lower than the sum of the quizzes" + ":  "
						+ currentQuizTotal + "% is greater than " + quizPercent + "%. ");
			}
			request.setAttribute("canSetTotal", warningStatement);
		} else {
			request.setAttribute("isOverPercentage", false);
			course.setQuizPercent(quizPercent);
			course.setAssignmentPercent(assignmentPercent);
			courseDao.updateCourse(course);// check all these necessary?
			int instructorId = Integer.parseInt(request.getParameter("instructor"));
			Instructor instructor = instructorDao.getInstructor(instructorId);
			instructorDao.updateInstructor(instructor);
			HttpSession session = request.getSession();
			session.setAttribute("instructor", instructor);
		}
		course = courseDao.getCourse(courseId);
		RequestDispatcher dispatcher = request.getRequestDispatcher(page);
		request.setAttribute("course", course);
		dispatcher.forward(request, response);
	}

	/**
	 * sets the student grades so long as the input grades are not greater than the
	 * relative quiz/assignment /mid /final total sets error booleans true for each
	 * unacceptable grade input sets error booleans as attributes ends by calling
	 * the showStudentResultPage method
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void setStudentGrades(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		int studentCourseId = Integer.parseInt(request.getParameter("studentCourseId"));
		int midResult = Integer.parseInt(request.getParameter("midResult"));
		int finalResult = Integer.parseInt(request.getParameter("finalResult"));
		boolean midError = false;
		boolean finalError = false;
		boolean assignmentError = false;
		boolean quizError = false;
		StudentCourseDao studentCourseDao = new StudentCourseDao();
		StudentQuizDao studentQuizDao = new StudentQuizDao();
		StudentAssignmentDao studentAssignmentDao = new StudentAssignmentDao();
		StudentCourse studentCourse = studentCourseDao.getStudentCourse(studentCourseId);
		List<StudentAssignment> studentAssignments = studentCourse.getStudentAssignments();
		List<StudentQuiz> studentQuizzes = studentCourse.getStudentQuizzes();
		if (midResult > 30) {
			midError = true;
		}
		if (finalResult > 50) {
			finalError = true;
		}
		for (StudentAssignment studentAssignment : studentAssignments) {
			int assignmentResult = Integer.parseInt(
					request.getParameter("assignmentResult" + studentAssignment.getId() + "" + studentCourse.getId()));
			if (assignmentResult > (studentAssignment.getAssignment().getAssignmentPercent())) {
				assignmentError = true;
			}
		}
		for (StudentQuiz studentQuiz : studentQuizzes) {
			int quizResult = Integer
					.parseInt(request.getParameter("quizResult" + studentQuiz.getId() + "" + studentCourse.getId()));
			if (quizResult > (studentQuiz.getQuiz().getQuizPercent())) {
				quizError = true;
			}
		}
		if ((midError == true) || (finalError == true) || (assignmentError == true) || (quizError)) {
		} else {
			studentCourse.setFinalResult(finalResult);
			studentCourse.setMidResult(midResult);
			for (StudentAssignment studentAssignment : studentAssignments) {
				int assignmentResult = Integer.parseInt(request
						.getParameter("assignmentResult" + studentAssignment.getId() + "" + studentCourse.getId()));
				studentAssignment.setResult(assignmentResult);
				studentAssignmentDao.updateStudentAssignment(studentAssignment);
			}
			for (StudentQuiz studentQuiz : studentQuizzes) {
				int quizResult = Integer.parseInt(
						request.getParameter("quizResult" + studentQuiz.getId() + "" + studentCourse.getId()));
				studentQuiz.setResult(quizResult);
				studentQuizDao.updateStudentQuiz(studentQuiz);
			}
			studentCourse.calculateAndSetCourseTotal();
			studentCourseDao.updateStudentCourse(studentCourse);
		}
		request.setAttribute("midError", midError);
		request.setAttribute("finalError", finalError);
		request.setAttribute("assignmentError", assignmentError);
		request.setAttribute("quizError", quizError);
		request.setAttribute("courseId", courseId);
		showStudentResultPage(request, response);
	}

	/**
	 * gets a course by id, sets as an attribute. sets its no of quizzes and
	 * asignment as attributes. sets a Set of student courses for the relevant
	 * course as an attribute and redirects to studentResultPage.jsp
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showStudentResultPage(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("courseId"));
		Course course = courseDao.getCourse(id);
		int noOfQuizzes = course.getQuizzes().size();
		int noOfAssignment = course.getAssignments().size();
		StudentCourseDao studentCourseDao = new StudentCourseDao();
		Set<StudentCourse> studentCourses = studentCourseDao.getAllStudentCoursesByCourse(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("studentResultPage.jsp");
		request.setAttribute("studentCourses", studentCourses);
		request.setAttribute("course", course);
		request.setAttribute("noQuizzes", noOfQuizzes);
		request.setAttribute("noAssignments", noOfAssignment);
		dispatcher.forward(request, response);
	}

	/**
	 * sets the course attribute using the id param, redirects to
	 * courseBreakdown.jsp
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void courseBreakdown(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("courseId"));
		Course course = courseDao.getCourse(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("courseBreakdown.jsp");
		request.setAttribute("course", course);
		dispatcher.forward(request, response);
	}

	/**
	 * sets the course attribute using the id param, redirects to addAssignment.jsp
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void addAssignment(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("courseId"));
		Course course = courseDao.getCourse(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("addAssignment.jsp");
		request.setAttribute("course", course);
		dispatcher.forward(request, response);
	}

	/**
	 * sets the course attribute using the id param, redirects to addQuiz.jsp
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void addQuiz(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("courseId"));
		Course course = courseDao.getCourse(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("addQuiz.jsp");
		request.setAttribute("course", course);
		dispatcher.forward(request, response);
	}

	/**
	 * redirects to showCourses.jsp
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void listCourses(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("showCourses.jsp");
		dispatcher.forward(request, response);
	}
}

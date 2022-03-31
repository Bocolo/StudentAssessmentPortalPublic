package com.mavenbro.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import com.mavenbro.web.dao.AssignmentDao;
import com.mavenbro.web.dao.CourseDao;
import com.mavenbro.web.dao.StudentAssignmentDao;
import com.mavenbro.web.dao.StudentCourseDao;
import com.mavenbro.web.model.Assignment;
import com.mavenbro.web.model.Course;
import com.mavenbro.web.model.StudentAssignment;
import com.mavenbro.web.model.StudentCourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This servlet manages all behaviours related to assignments
 * 
 * @author brona
 *
 */
@WebServlet("/assignmentController")
public class AssignmentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AssignmentDao assignmentDao;
	private CourseDao courseDao;

	/**
	 * method that sets the assignment and course dao's
	 */
	public void init() {
		assignmentDao = new AssignmentDao();
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
	 * assignment action that is passed as a parameter
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String assignmentAction = request.getParameter("assignmentAction");
		try {
			switch (assignmentAction) {
			case "deleteAssignment":
				deleteAssignment(request, response);
				break;
			case "updateAssignment":
				updateAssignment(request, response);
				break;
			case "createAssignment":
				addAssignment(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	/**
	 * This method creates an assignments and assigns it to a course, to a student
	 * course and saves to the database if the following conditions are met 1. if
	 * the % set for it, plus the accumulate % of all other assignments in that
	 * course do not exceed the courses total assignment % 2. if % set for it is not
	 * zero or lower if not met, flags are activated that display error msgs to the
	 * user
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void addAssignment(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		String name = request.getParameter("name");
		int assignmentPercent = Integer.parseInt(request.getParameter("assignmentPercent"));
		Assignment newAssignment = new Assignment(assignmentPercent, name);
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		Course course = courseDao.getCourse(courseId);
		boolean isOver = false;
		int accPercent = assignmentPercent;
		for (Assignment assignment : course.getAssignments()) {
			accPercent += assignment.getAssignmentPercent();
		}
		if (accPercent > course.getAssignmentPercent()) {
			isOver = true;
		} else {
			isOver = false;
		}
		if (isOver) {
			request.setAttribute("assignmentsOverMaxPC", true);
			request.setAttribute("assignmentLessThanZero", false);
		} else if ((!isOver) && (assignmentPercent > 0)) {
			request.setAttribute("assignmentsOverMaxPC", false);
			request.setAttribute("assignmentLessThanZero", false);
			newAssignment.setCourse(course);
			assignmentDao.saveAssignment(newAssignment);
			StudentCourseDao studentCourseDao = new StudentCourseDao();
			StudentAssignmentDao studentAssignmentDao = new StudentAssignmentDao();
			Set<StudentCourse> studentCourses = studentCourseDao.getAllStudentCoursesByCourse(courseId);
			for (StudentCourse studentCourse : studentCourses) {
				StudentAssignment studentAssignment = new StudentAssignment(studentCourse, newAssignment);
				studentCourse.addStudentAssignment(studentAssignment);
				studentAssignmentDao.saveStudentAssignment(studentAssignment);
			}
			course = courseDao.getCourse(courseId);
		} else {
			request.setAttribute("assignmentLessThanZero", true);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("addAssignment.jsp");
		request.setAttribute("course", course);
		dispatcher.forward(request, response);
	}

	/**
	 * This method updates an assignment and saves it to the database if the
	 * following conditions are met 
	 * 1. if the % set for it, plus the accumulate % of
	 * all other assignments in that course do not exceed the courses total
	 * assignment % 
	 * 2. if % set for it is not zero or lower if not met, bools/flags are
	 * activated that display error msgs to the user
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void updateAssignment(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Assignment originalAssignment = assignmentDao.getAssignment(id);
		int originalPercent = originalAssignment.getAssignmentPercent();
		int assignmentPercent = Integer.parseInt(request.getParameter("assignmentPercent"));
		String name = request.getParameter("name");
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		Course course = courseDao.getCourse(courseId);
		if (assignmentPercent == 0) {
			request.setAttribute("assignmentLessThanZero", true);
		} else {
			boolean isOver = false;
			int accPercent = 0;
			accPercent = (accPercent + assignmentPercent);
			accPercent = (accPercent - originalPercent);
			for (Assignment assignment : course.getAssignments()) {
				accPercent = (accPercent + assignment.getAssignmentPercent());
			}
			if (accPercent > course.getAssignmentPercent()) {
				isOver = true;
			} else {
				isOver = false;
			}
			if (isOver) {
				request.setAttribute("assignmentsOverMaxPC", true);
			} else {
				Assignment assignment = new Assignment(name, id, assignmentPercent, course);
				assignmentDao.updateAssignment(assignment);
				course = courseDao.getCourse(courseId);
			}
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("addAssignment.jsp");
		request.setAttribute("course", course);
		dispatcher.forward(request, response);
	}
	/**
	 * This method deletes the assignment and resets the "course" attribute
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteAssignment(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("assignmentId"));
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		assignmentDao.deleteAssignment(id);
		Course course = courseDao.getCourse(courseId);
		RequestDispatcher dispatcher = request.getRequestDispatcher("addAssignment.jsp");
		request.setAttribute("course", course);
		dispatcher.forward(request, response);
	}
}
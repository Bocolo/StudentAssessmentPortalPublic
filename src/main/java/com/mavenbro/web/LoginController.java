package com.mavenbro.web;

import java.io.IOException;

import com.mavenbro.web.dao.HODDao;
import com.mavenbro.web.dao.InstructorDao;
import com.mavenbro.web.dao.StudentDao;
import com.mavenbro.web.dao.UserDao;
import com.mavenbro.web.model.HOD;
import com.mavenbro.web.model.Instructor;
import com.mavenbro.web.model.Student;
import com.mavenbro.web.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * This servlet manages all behaviours related to login
 * 
 * @author brona
 *
 */
//@WebServlet("/loginController")
public class LoginController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the servlets post method. retrieves the user from the database from the
	 * passed parameters. if the password param matches the user param checks the
	 * users permission and retrieve the relevant student/hod or instructor account
	 * using the use id. redirects to the showCourses page. if the passwords dont
	 * match redirects to the login page
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDao userDao = new UserDao();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = userDao.getUser(username);
		int permission = user.getPermissionLevel();
		if (password.equals(user.getPassword())) {
			HttpSession session = request.getSession();
			if (permission == 1) {
				StudentDao studentDao = new StudentDao();
				Student student = studentDao.getStudent(user.getId());
				session.setAttribute("student", student);
			} else if (user.getPermissionLevel() == 2) {
				HODDao HODDao = new HODDao();
				HOD hod = HODDao.getHOD(user.getId());
				session.setAttribute("hod", hod);
			} else if (user.getPermissionLevel() == 3) {
				InstructorDao instructorDao = new InstructorDao();
				Instructor instructor = instructorDao.getInstructor(user.getId());
				session.setAttribute("instructor", instructor);
			}
			session.setAttribute("signedInUser", user);
			session.setAttribute("username", username);
			session.setAttribute("permission", user.getPermissionLevel());
			request.setAttribute("courseAction", "listCourses");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/courseController");
			dispatcher.forward(request, response);
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
		}
	}
}

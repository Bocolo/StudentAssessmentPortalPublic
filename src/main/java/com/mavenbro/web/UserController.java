package com.mavenbro.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.mavenbro.web.dao.CourseDao;
import com.mavenbro.web.dao.HODDao;
import com.mavenbro.web.dao.InstructorDao;
import com.mavenbro.web.dao.StudentDao;
import com.mavenbro.web.dao.UserDao;
import com.mavenbro.web.model.Course;
import com.mavenbro.web.model.HOD;
import com.mavenbro.web.model.Instructor;
import com.mavenbro.web.model.Student;
import com.mavenbro.web.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * This servlet manages all behaviours related to users
 * 
 * @author brona
 *
 */
@WebServlet("/userController")
public class UserController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserDao userDao;

	/**
	 * method that sets the user dao
	 */
	public void init() {
		userDao = new UserDao();
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
	 * user action that is passed as a parameter
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userAction = request.getParameter("userType");
		try {
			switch (userAction) {
			case "insert":
				insertUser(request, response);
				break;
			case "update":
				updateUser(request, response);
				break;
			case "userForm":
				showUserForm(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	/**
	 * redirects to the userForm.jsp
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showUserForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("userForm.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * creates a new user from the request parameters. creates
	 * student/instructor/hod based on the permission level param. redirects to
	 * "showCourses.jsp"
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void insertUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		HttpSession session = request.getSession();
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		int permissionLevel = Integer.parseInt(request.getParameter("permissionlevel"));
		User newUser = new User(name, email, username, password, permissionLevel);
		userDao.saveUser(newUser);
		CourseDao courseDao = new CourseDao();
		List<Course> listCourse = courseDao.getAllCourses();
		InstructorDao instructorDao = new InstructorDao();
		request.setAttribute("coursesList", listCourse);
		if (permissionLevel == 1) {
			StudentDao studentDao = new StudentDao();
			Student newStudent = new Student(newUser);
			studentDao.saveStudent(newStudent);
			request.setAttribute("unregisteredCourses", listCourse);
		} else if (permissionLevel == 2) {
			HODDao hodDao = new HODDao();
			HOD newHOD = new HOD(newUser);
			List<Instructor> listOfInstructors = instructorDao.getAllInstructors();
			hodDao.saveHod(newHOD);
			request.setAttribute("instructorList", listOfInstructors);
		} else if (permissionLevel == 3) {
			Instructor newInstructor = new Instructor(newUser);
			instructorDao.saveInstructor(newInstructor);
		}
		session.setAttribute("signedInUser", newUser);
		session.setAttribute("name", name);
		session.setAttribute("username", username);
		session.setAttribute("permission", permissionLevel);
		RequestDispatcher dispatcher = request.getRequestDispatcher("showCourses.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * updates a signed in user with the request parameters. redirects to course
	 * controller page, with course actions = list courses
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void updateUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		HttpSession session = request.getSession();
		int id = Integer.parseInt(request.getParameter("id"));
		User user = userDao.getUser(id);
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		user.setEmail(email);
		user.setUsername(username);
		user.setName(name);
		user.setPassword(password);
		userDao.updateUser(user);
		session.setAttribute("signedInUser", user);
		request.setAttribute("courseAction", "listCourses");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/courseController");
		dispatcher.forward(request, response);
	}
}
package com.mavenbro.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mavenbro.web.dao.CourseDao;
import com.mavenbro.web.dao.InstructorDao;
import com.mavenbro.web.dao.StudentCourseDao;
import com.mavenbro.web.model.Course;
import com.mavenbro.web.model.Instructor;
import com.mavenbro.web.model.Student;
import com.mavenbro.web.model.StudentCourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * This servlet manages all behaviours related to courses
 * 
 * @author brona
 *
 */
@WebServlet("/courseController")
public class CourseController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CourseDao courseDao;
	private InstructorDao instructorDao;

	/**
	 * method that sets the course and instructor daos
	 */
	public void init() {
		courseDao = new CourseDao();
		instructorDao = new InstructorDao();
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
	 * course action that is passed as a parameter
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String courseAction;
		if (request.getAttribute("courseAction") == null) {
			courseAction = request.getParameter("courseAction");
		} else {
			courseAction = (String) request.getAttribute("courseAction");
		}
		try {
			switch (courseAction) {
			case "addCourse":
				showNewForm(request, response);
				break;
			case "insertCourse":
				insertCourse(request, response);
				break;
			case "deleteCourse":
				deleteCourse(request, response);
				break;
			case "editCourse":
				showCourseEditForm(request, response);
				break;
			case "updateCourse":
				submitCourseUpdate(request, response);
				break;
			case "listCourses":
				listCourses(request, response);
				break;
			case "addInstructor":
				addInstructorToCourse(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	/**
	 * this method lists all courses on the showCourse page if there is a signed in
	 * user else redirects to the Login page. some attributes are set based on the
	 * user type /permissions. student - set registered and unregistered courses.
	 * HOD - sets the instructor list
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void listCourses(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		HttpSession session = request.getSession();
		List<Course> listCourse = courseDao.getAllCourses();
		if (session.getAttribute("signedInUser") != null) {
			int permission = (int) session.getAttribute("permission");
			if (permission == 1) {
				Student student = (Student) session.getAttribute("student");
				StudentCourseDao studentCourseDao = new StudentCourseDao();
				Set<StudentCourse> studentCourses = studentCourseDao.getAllStudentCoursesByStudent(student.getId());
				Set<Course> registeredCourses = new HashSet<Course>();
				Set<Course> unregisteredCourseList = new HashSet<Course>();
				if (studentCourses.size() != 0) {
					for (StudentCourse studentCourse : studentCourses) {
						registeredCourses.add(studentCourse.getCourse());
					}
					unregisteredCourseList = courseDao.getAllCoursesExcluding(studentCourses);
				} else {
					unregisteredCourseList = courseDao.getAllCoursesSet();
				}
				request.setAttribute("unregisteredCourses", unregisteredCourseList);
				request.setAttribute("registeredCourses", registeredCourses);
			} else if (permission == 2) {
				List<Instructor> listOfInstructors = instructorDao.getAllInstructors();
				request.setAttribute("instructorList", listOfInstructors);
			}
			request.setAttribute("coursesList", listCourse);
			RequestDispatcher dispatcher = request.getRequestDispatcher("showCourses.jsp");
			dispatcher.forward(request, response);
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
		}
	}

	/**
	 * Redirects to the course form jsp and sets the list of instructors
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Instructor> listOfInstructors = instructorDao.getAllInstructors();
		request.setAttribute("instructorList", listOfInstructors);
		RequestDispatcher dispatcher = request.getRequestDispatcher("courseForm.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirects to the course form jsp and sets the list of instructors and courses
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showCourseEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Course selectedCourse = courseDao.getCourse(id);
		List<Instructor> listOfInstructors = instructorDao.getAllInstructors();
		RequestDispatcher dispatcher = request.getRequestDispatcher("courseForm.jsp");
		request.setAttribute("course", selectedCourse);
		request.setAttribute("instructorList", listOfInstructors);
		dispatcher.forward(request, response);
	}

	/**
	 * assigns an instructor to a course from the request parameters Redirects to
	 * the show courses jsp and sets the list of instructors and courses
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addInstructorToCourse(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int courseId = Integer.parseInt(request.getParameter("id"));
		int instructorId = Integer.parseInt(request.getParameter("instructor"));
		Course existingCourse = courseDao.getCourse(courseId);
		Instructor instructor = instructorDao.getInstructor(instructorId);
		existingCourse.setInstructor(instructor);
		instructor.addCourse(existingCourse);
		courseDao.updateCourse(existingCourse);
		instructorDao.updateInstructor(instructor);
		List<Course> listCourse = courseDao.getAllCourses();
		List<Instructor> listOfInstructors = instructorDao.getAllInstructors();
		request.setAttribute("coursesList", listCourse);
		request.setAttribute("instructorList", listOfInstructors);
		RequestDispatcher dispatcher = request.getRequestDispatcher("showCourses.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * creates a new course if the name param is not null. will assign an instructor
	 * to the course if the instructor id string is not empty. ends by calling the
	 * list course method.
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void insertCourse(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		String name = request.getParameter("name");
		if (name != "") {
			Course newCourse = new Course(name);
			String instructorIdString = request.getParameter("instructor");
			if (!instructorIdString.equals("")) {
				int instructorId;
				instructorId = Integer.parseInt(instructorIdString);
				Instructor instructor = instructorDao.getInstructor(instructorId);
				newCourse.setInstructor(instructor);
				instructor.addCourse(newCourse);// is all this necessary
				instructorDao.updateInstructor(instructor);
			}
			courseDao.saveCourse(newCourse);
		}
		listCourses(request, response);
	}

	/**
	 * updates a course if the name param is not null. will assign an instructor to
	 * the course if the instructor id string is not empty. ends by calling the list
	 * course method.
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void submitCourseUpdate(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String instructorIdString = request.getParameter("instructor");
		String name = request.getParameter("name");
		if (name != "") {
			Course course = new Course(name, id);
			if (!instructorIdString.equals("")) {
				int instructorId;
				instructorId = Integer.parseInt(instructorIdString);
				Instructor instructor = instructorDao.getInstructor(instructorId);
				course.setInstructor(instructor);
				instructor.addCourse(course);
				instructorDao.updateInstructor(instructor);
			}
			courseDao.updateCourse(course);
		}
		listCourses(request, response);
	}

	/**
	 * deletes a course from the databases using its id, recieved from the request
	 * param ends by calling the list course method.
	 * 
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteCourse(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		courseDao.deleteCourse(id);
		listCourses(request, response);
	}
}
package com.mavenbro.web;
import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
public class SystemController extends HttpServlet {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected void doPost(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
       doGet(request, response);
   }
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
       String action = request.getServletPath();
       try {
           switch (action) {
               case "/login":
                   login(request, response);
                   break;
               case "/logout":
                   logout(request, response); 
                   break;
               case "/registerUser":
                   registerUser(request, response);
                   break;
               case "/showUsers":
                   showUsers(request, response);
                   break;
               case "/registerCourse":
                   registerCourse(request, response);
                   break;
               case "/showCourses":
                   showCourses(request, response);
                   break;
               case "/HOD":
                   showHODPage(request, response);
                   break;
               case "/instructor":
            	   showInstructorPage(request, response);
                   break;
               case "/student":
                   showStudentPage(request, response);
                   break;
               case "/testtoo":
                   test(request, response);
                   break;
               default:
                   homepage(request, response);
                   break;
           }
       } catch (SQLException ex) {
           throw new ServletException(ex);
       }
   }
   private void test(HttpServletRequest request, HttpServletResponse response) 
		   throws SQLException, IOException, ServletException {
	   RequestDispatcher dispatcher = request.getRequestDispatcher("pool");
       dispatcher.forward(request, response);
}
private void showCourses(HttpServletRequest request, HttpServletResponse response) 	
		   throws SQLException, IOException, ServletException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("showCourses.jsp");
        dispatcher.forward(request, response);
	}
	private void registerCourse(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("courseForm.jsp");
        dispatcher.forward(request, response);
	}
	private void showUsers(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		// TODO Auto-generated method stub
		RequestDispatcher dispatcher = request.getRequestDispatcher("showUser.jsp");
        dispatcher.forward(request, response);
	}
	private void homepage(HttpServletRequest request, HttpServletResponse response) 
			   throws SQLException, IOException, ServletException {
		   RequestDispatcher dispatcher = request.getRequestDispatcher("");
	       dispatcher.forward(request, response);
	}
	private void registerUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("userForm.jsp");
        dispatcher.forward(request, response);
	}
	private void logout(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("logoutController");
        dispatcher.forward(request, response);
	}
	private void login(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
        dispatcher.forward(request, response);
	}
	private void showStudentPage(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("StudentLoggedIn.jsp");
        dispatcher.forward(request, response);
	}
	private void showInstructorPage(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("InstructorLoggedIn.jsp");
        dispatcher.forward(request, response);
	}
	private void showHODPage(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("HODLoggedIn.jsp");
        dispatcher.forward(request, response);
	}
}

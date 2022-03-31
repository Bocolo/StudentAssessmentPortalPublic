package com.mavenbro.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * This servlet manages all behaviours related to logging out
 * 
 * @author brona
 *
 */
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * the servlets get method, removes all session attributes, invalidates the
	 * session and redirects to the login page
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("username");
		session.removeAttribute("permission");
		session.removeAttribute("signedInUser");
		session.removeAttribute("student");
		session.removeAttribute("hod");
		session.removeAttribute("instructor");
		session.invalidate();
		response.sendRedirect("login.jsp");
	}
}

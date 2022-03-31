<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<!--  This page is for adding or updating courses on  the database, can only be done by HODS 
if no user is signed  a login button is display,
if a signed user without the correct permission accesses the page an access denied notification 
and a "back to courses" button is displayed 
otherwise, a HOD can add and edit course-->
<html>
<head>
<meta charset="ISO-8859-1">
<title>Course Form</title>
</head>
<body>
	<h1>Course Management</h1>
	<hr>
	<c:if test="${signedInUser ==null}">
		<form action="logoutController" method="get">
			<input type="submit" value="Login">
		</form>
	</c:if>
	<c:if test="${signedInUser !=null}">
		<h2>Welcome ${signedInUser.getName()}</h2>
		<form action="logoutController" method="get">
			<input type="submit" value="Logout">
		</form>
		<br>
		<form action="courseController" method="get">
			<input type="hidden" name="courseAction" value="listCourses" /> <input
				type="submit" value="Go Back To Courses">
		</form>
		<c:if test="${permission!=2 }">
			<br>
		You do not have permission to access this page
		</c:if>
		<c:if test="${permission==2 }">
			<div align="center">
				<c:if test="${course != null}">
					<form action="courseController" method="post">
						<input type="hidden" name="courseAction" value="updateCourse" />
						<h1>Update A Course</h1>
				</c:if>
				<c:if test="${course eq null}">
					<form action="courseController" method="post">
						<input type="hidden" name="courseAction" value="insertCourse" />
						<h1>Add a New Course</h1>
				</c:if>
				<table border="1">
					<c:if test="${course != null}">
						<input type="hidden" name="id"
							value="<c:out value='${course.id}' />" />
					</c:if>
					<tr>
						<th>Course Name:</th>
						<td><input type="text" name="name" size="45"
							value="<c:out value='${course.name}' />" /></td>
					</tr>
					<tr>
						<th>Course Instructor:</th>
						<td><c:if test="${instructorList != null}">
								<select id="instructor" name="instructor">
									<c:if test="${course.instructor != null}">
										<option value='${course.instructor.getId()}'>
											<c:out value="${course.instructor.getUser().getName()}" />
										</option>
									</c:if>
									<c:if test="${course.instructor == null}">
										<option value=""></option>
									</c:if>
									<c:forEach items="${instructorList}" var="instructor">
										<option value="${instructor.getId()}">
											<c:out value="${instructor.getUser().getName()}" />
										</option>
									</c:forEach>
								</select>
							</c:if></td>
					</tr>
					<tr>
						<td colspan="2" align="center"><input type="submit"
							value="Save" /></td>
					</tr>
				</table>
				</form>

			</div>
		</c:if>
	</c:if>
</body>
</html>

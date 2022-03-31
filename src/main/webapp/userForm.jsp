<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<!--  This page is for registering for non-users or for
updating user details for signed in users -->
<html>
<head>
<meta charset="ISO-8859-1">
<title>User Form</title>
</head>
<body>
	<h1 align="center">User Management</h1>
	<hr>
	<form action="logoutController" method="get">
		<input type="submit" value="Back To Login">
	</form>
	<div align="center">
		<c:if test="${signedInUser != null}">
			<form action="courseController" method="get">
				<input type="hidden" name="courseAction" value="listCourses" /> <input
					type="submit" value="Go Back To Courses"> <input
					type="hidden" name="id"
					value="<c:out value='${signedInUser.id}' />" />
			</form>
			<form action="userController" method="post">
				<input type="hidden" name="userType" value="update" />
				<h1>Update Profile</h1>
		</c:if>
		<c:if test="${signedInUser eq null}">
			<form action="userController" method="post">
				<input type="hidden" name="userType" value="insert" />
				<h1>Sign Up</h1>
		</c:if>
		<table border="1">

			<tr>
				<th>Name:</th>
				<td><input type="text" name="name" size="45"
					value="<c:out value='${signedInUser.name}' />" /></td>
			</tr>
			<tr>
				<th>User Email:</th>
				<td><input type="email" name="email" size="45"
					value="<c:out value='${signedInUser.email}' />" /></td>
			</tr>
			<tr>
				<th>Username:</th>
				<td><input type="text" name="username" size="15"
					value="<c:out value='${signedInUser.username}' />" /></td>
			</tr>
			<tr>
				<th>Password:</th>
				<td><input type="password" name="password" size="15"
					value="<c:out value='${signedInUser.password}' />" /></td>
			</tr>
			<c:if test="${signedInUser eq null}">
				<tr>
					<th>Position:</th>
					<td><select id="permissionlevel" name="permissionlevel">
							<option value=1>Student</option>
							<option value=2>HOD</option>
							<option value=3 selected>Instructor</option>
					</select></td>
				</tr>
			</c:if>
			<tr>
				<td colspan="2" align="center"><input type="hidden" name="id"
					type="text" value="${signedInUser.getId() }" /> <input
					type="submit" value="Save" /></td>
			</tr>
		</table>
		</form>
	</div>
</body>
</html>
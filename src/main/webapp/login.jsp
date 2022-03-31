<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<!--  This page is for Logging in or registering for an account. 
Once logged in you will be redirected to the showCourse s page
 -->
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login Page</title>
</head>
<body>
	<form action="loginController" method="post">
		<h1 align="center">Welcome to the Student Management System</h1>
		<h3>Please Log In or Register</h3>
		<hr>
		<h3>Sign In Here</h3>
		<table>
			<tr>
				<td>Username:</td>
				<td><input type="text" name="username"></td>
			</tr>
			<tr>
				<td>Enter Password:</td>
				<td><input type="password" name="password"></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Login"></td>
			</tr>
		</table>
	</form>
	<hr>
	<h3>Not a member yet? Register Here</h3>
	<form action="userController" method="post">
		<input type="hidden" name="userType" value="userForm" /> <input
			type="submit" value="Register">
	</form>
	<hr>
	<div>
	<h2>Try one of these logins to see features</h2>
		<div>
			<h3>HOD Login</h3>
			<div>Username: Lisa Simpson</div>
			<div>Password: Lisa</div>
			<hr>
		</div>
		<div>
			<h3>Instructor Login</h3>
			<div>Username: L.J. Gibbs</div>
			<div>Password: Gibbs</div>
			<hr>
		</div>
		<div>
			<h3>Student Login</h3>
			<div>Username: Ron Weasley</div>
			<div>Password: Ron</div>
			<hr>
		</div>
	</div>
</body>
</html>

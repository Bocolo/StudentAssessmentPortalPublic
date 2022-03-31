<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.mavenbro.web.model.Course"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.List"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<!--  This page is for displaying course details to users
if the user is a hod, courses will be displayed with the option to edit,delete or assign 
instructors to courses.  Hods also have the option to create courses
if the user is a instuctors, courses they are assigned will be displayed with options 
to grade, manage quizzes and assignments, and view course breakdownd
if the user is a student, they can register or unregister from courses and click to 
view their results
if not signed in  a login button is displayed-->
<html>
<head>
<meta charset="ISO-8859-1">
<title>Course Manager</title>
</head>
<body>
	<c:if test="${signedInUser ==null}">
		<h2>Please Sign In</h2>
		<form action="logoutController" method="get">
			<input type="submit" value="Login">
		</form>
		<br>
	</c:if>
	<c:if test="${signedInUser !=null}">
		<h1>Course Manager</h1>
		<hr>
		<h2>Welcome ${signedInUser.getName()}</h2>
		<form action="logoutController" method="get">
			<input type="submit" value="Logout">
		</form>
		<br>
		<form action="userController" method="post">
			<input type="hidden" name="id" value="${signedInUser.getId()}" /> <input
				type="hidden" name="userType" value="userForm" /> <input type="submit"
				value="Update Account">
		</form>
		<br>
	</c:if>
	<c:if test="${permission ==1}">
		<form action="studentController" method="post">
			<input type="hidden" name="studentAction" value="seeAllCourseResults" />
			<input type="hidden" name="courseId" value='${course.id}' /> <input
				type="hidden" name="studentId" value='${signedInUser.getId() }' />
			<input type="submit" value="See Your Results">
		</form>
		<h3>Unregistered Courses</h3>
		<div align="center">
			<table border="1">
				<tr>
					<th>ID</th>
					<th>Course Name</th>
					<th>Instructor Name</th>
					<th>Quiz %</th>
					<th>Assignment %</th>
					<th>Register</th>
				</tr>
				<c:forEach items="${unregisteredCourses}" var="course">
					<tr>
						<td><c:out value="${course.getId()}" /></td>
						<td><c:out value="${course.getName()}" /></td>
						<td><c:out value="${course.getInstructor().getUser().getName()}" /></td>
						<td><c:out value="${course.getQuizPercent()}" /></td>
						<td><c:out value="${course.getAssignmentPercent()}" /></td>
						<td>
							<form action="studentController" method="post">
								<input type="hidden" name="studentAction"
									value=registerForCourse /> <input type="hidden"
									name="courseId" value='${course.id}' />
								<input type="hidden" name="studentId"
									value='${signedInUser.getId() }' /> <input type="submit"
									value="Register">
							</form>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<h3>Registered Courses</h3>
		<div align="center">
			<table border="1">
				<tr>
					<th>ID</th>
					<th>Course Name</th>
					<th>Instructor Name</th>
					<th>Quiz %</th>
					<th>Assignment %</th>
					<th>UnRegister</th>
					<th>Results</th>
				</tr>
				<c:forEach items="${registeredCourses}" var="course">
					<tr>
						<td><c:out value="${course.getId()}" /></td>
						<td><c:out value="${course.getName()}" /></td>
						<td><c:out value="${course.getInstructor().getUser().getName()}" /></td>
						<td><c:out value="${course.getQuizPercent()}" /></td>
						<td><c:out value="${course.getAssignmentPercent()}" /></td>
						<td>
							<form action="studentController" method="post">
								<input type="hidden" name="studentAction"
									value=unregisterFromCourse /> <input type="hidden"
									name="courseId" value='${course.id}' /> <input type="hidden"
									name="studentId" value='${signedInUser.getId() }' /> <input
									type="submit" value="Unregister">
							</form>
						</td>
						<td>
							<form action="studentController" method="post">
								<input type="hidden" name="studentAction" value=seeCourseResult />
								<input type="hidden" name="courseId" value='${course.id}' /> <input
									type="hidden" name="studentId" value='${signedInUser.getId() }' />
								<input type="submit" value="See Results">
							</form>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:if>
	<c:if test="${permission ==2}">
		<form action="courseController" method="post">
			<input type="hidden" name="courseAction" value="addCourse" /> <input
				type="submit" value="Add New Course">
		</form>
		<div align="center">
			<table border="1">
				<caption>
					<h2>Available Courses</h2>
				</caption>
				<tr>
					<th>ID</th>
					<th>Course Name</th>
					<th>Instructor Name</th>
					<th>Quiz %</th>
					<th>Assignment %</th>
					<th>Edit</th>
					<th>Delete</th>
					<th>Add Instructor</th>
				</tr>
				<c:forEach items="${coursesList}" var="course">
					<tr>
						<td><c:out value="${course.getId()}" /></td>
						<td><c:out value="${course.getName()}" /></td>
						<td><c:out value="${course.getInstructor().getUser().getName()}" /></td>
						<td><c:out value="${course.getQuizPercent()}" /></td>
						<td><c:out value="${course.getAssignmentPercent()}" /></td>
						<td>
							<form action="courseController" method="post">
								<input type="hidden" name="courseAction" value=editCourse /> <input
									type="hidden" name="id" value='${course.id}' /> <input
									type="submit" value="Edit">
							</form>
						</td>
						<td>
							<form action="courseController" method="post">
								<input type="hidden" name="courseAction" value="deleteCourse" />
								<input type="hidden" name="id" value='${course.id}' /> <input
									type="submit" value="Delete">
							</form>
						</td>
						<td>
							<form action="courseController" method="post">
								<c:if test="${instructorList != null}">
									<select id="instructor" name="instructor">
										<c:forEach items="${instructorList}" var="instructor">
											<option value="${instructor.getId()}">
												<c:out value="${instructor.getUser().getName()}" />
											</option>
										</c:forEach>
									</select>
									<input type="hidden" name="id" value='${course.id}' />
									<input type="hidden" name="courseAction" value="addInstructor" />
									<input type="submit" value="Add Instructor">
								</c:if>
								<c:if test="${instructorList eq null}">
									<h2>no instuctorList</h2>
								</c:if>
							</form>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:if>
	<c:if test="${permission ==3}">
		<br>
		<h2>Welcome To Your Dashboard ${signedInUser.getName()}</h2>
		<c:if test="${isOverPercentage != null}">
			<c:if test="${isOverPercentage eq true}">
				<h2>WARNING: Assignment Total + Quiz Total Can not be greater
					than 20%</h2>
				<h3>Please try again</h3>
			</c:if>
		</c:if>
		<div align="center">
			<table border="1">
				<caption>
					<h2>These are your Courses</h2>
				</caption>
				<tr>
					<th>ID</th>
					<th>Course Name</th>
					<th>Quiz Total</th>
					<th>Assignment Total</th>
					<th>Sum Total</th>
					<th>Add Quiz</th>
					<th>Add Assignment</th>
					<th>See Breakdown</th>
					<th>Grade Manager</th>
				</tr>
				<c:forEach items="${instructor.getCourses()}" var="course">
					<tr>
						<td><c:out value="${course.getId()}" /></td>
						<td><c:out value="${course.getName()}" /></td>
						<td><c:out value="${course.getQuizPercent()}%" /></td>
						<td><c:out value="${course.getAssignmentPercent()}%" /></td>
						<td><c:out
								value="${course.getAssignmentPercent()+course.getQuizPercent()}%" /></td>
						<td>
							<form action="instructorController" method="get">
								<input type="hidden" name="instructorAction" value="addQuiz" />
								<input type="hidden" name="courseId" value='${course.id}' />
								<input type="submit" value="Manage Quizzes">
							</form>
						</td>
						<td>
							<form action="instructorController" method="get">
								<input type="hidden" name="instructorAction"
									value="addAssignment" /> <input type="hidden" name="courseId"
									value='${course.id}' />
								<input type="submit" value="Manage Assignments">
							</form>
						</td>
						<td>
							<form action="instructorController" method="get">
								<input type="hidden" name="instructorAction"
									value="courseBreakdown" /> <input type="hidden"
									name="courseId" value='${course.id}' />
								<input type="submit" value="Course Breakdown">
							</form>
						</td>
						<td>
							<form action="instructorController" method="get">
								<input type="hidden" name="instructorAction"
									value="showStudentTable" /> <input type="hidden"
									name="courseId" value='${course.id}' /> <input type="submit"
									value="Grade Manager">
							</form>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:if>
</body>
</html>

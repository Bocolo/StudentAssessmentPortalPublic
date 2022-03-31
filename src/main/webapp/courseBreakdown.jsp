<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.mavenbro.web.model.Course"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.List"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<!--  This page is for a simple view of course breakdown for an instructor 
its show every assignment and quiz in a course with their assigned percentages-->
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<c:if test="${signedInUser != null}"><br>
	<form action="courseController" method="get">
				<input type="hidden" name="courseAction" value="listCourses" /> <input
					type="submit" value="Go Back To Courses">
			</form>
			<br><h2>Hello ${signedInUser.getName()}</h2>
		<c:if test="${permission eq 3}">
			<div align="center">
				<table border="1">
					<caption>Breakdown of ${course.getName()}</caption>
					<tr>
						<th>Assessment</th>
						<th>Name</th>
						<th>Percent</th>
					</tr>
					<c:forEach items="${course.getQuizzes()}" var="quiz">
						<tr>
							<td>Quiz</td>
							<td><c:out value="${quiz.getName()}" /></td>
							<td><c:out value="${quiz.getQuizPercent()}%" /></td>
						</tr>
					</c:forEach>
					<c:forEach items="${course.getAssignments()}" var="assignment">
						<tr>
							<td>Assignment</td>
							<td><c:out value="${assignment.getName()}" /></td>
							<td><c:out value="${assignment.getAssignmentPercent()}%" /></td>
						</tr>
					</c:forEach>
					<tr>
						<td>Mid</td>
						<td>Mid Term</td>
						<td>30%</td>
					</tr>
					<tr>
						<td>Final</td>
						<td>Final Exam</td>
						<td>50%</td>
					</tr>
				</table>
		</c:if>
	</c:if>
</body>
</html>
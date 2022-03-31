<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.mavenbro.web.model.Course"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.List"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<!--This page is for students to view a breakdown of their courses and results
if not logged in a logout button is displayed, if not a student a show courses button
is displayed -->
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<c:if test="${signedInUser != null}">
		<form action="courseController" method="get">
			<input type="hidden" name="courseAction" value="listCourses" /> <input
				type="submit" value="Go Back To Courses">
		</form>
		<c:if test="${permission != 1}">You do not have permission to access this page</c:if>
		<c:if test="${permission eq 1}">
			<h2>Hello ${signedInUser.getName()}</h2>
			<div align="left">
				<c:if test="${studentCourse==null }">
					<c:forEach items="${studentCourses}" var="studentCourse">
						<br>
						<br>
						<table border="1">
							<c:set var="course" value='${studentCourse.getCourse() }' />
							<c:set var="noStudentQuizzes"
								value="${fn:length(studentCourse.getStudentQuizzes())}" />
							<c:set var="noStudentAssignments"
								value="${fn:length(studentCourse.getStudentAssignments())}" />
							<caption>
								<h3 align="left">Breakdown of Your Results for
									${course.getName()}</h3>
							</caption>
							<tr>
								<th rowspan="2"></th>
								<th colspan='${noStudentQuizzes}'>Quizzes
									(${studentCourse.getCourse().getQuizPercent()}%)</th>
								<th colspan=' ${noStudentAssignments}'>Assignments
									(${studentCourse.getCourse().getAssignmentPercent()}%)</th>
								<th rowspan="2">Mid (30%)</th>
								<th rowspan="2">Final (50%)</th>
								<th rowspan="2">Total (100%)</th>
							</tr>
							<tr>
								<c:if test="${noStudentQuizzes == 0}">
									<th>-</th>
								</c:if>
								<c:if test="${noStudentQuizzes != 0}">
									<c:forEach items="${course.getQuizzes()}" var="quiz">
										<th><c:out
												value="${quiz.getName()}(${quiz.getQuizPercent()}%)" /></th>
									</c:forEach>
								</c:if>
								<c:if test="${noStudentAssignments == 0}">
									<th>-</th>
								</c:if>
								<c:if test="${noStudentAssignments != 0}">
									<c:forEach items="${course.getAssignments()}" var="assignment">
										<th><c:out
												value="${assignment.getName()}(${assignment.getAssignmentPercent()}%)" />
										</th>
									</c:forEach>
								</c:if>
							</tr>
							<tr>
								<th scope="row">Results</th>
								<c:if test="${noStudentQuizzes == 0}">
									<td align="center">0%</td>
								</c:if>
								<c:if test="${noStudentQuizzes != 0}">
									<c:forEach items="${studentCourse.getStudentQuizzes()}"
										var="studentQuiz">
										<td align="center"><c:out
												value="${studentQuiz.getResult()}%" /></td>
									</c:forEach>
								</c:if>
								<c:if test="${noStudentAssignments == 0}">
									<td align="center">0%</td>
								</c:if>
								<c:if test="${noStudentAssignments != 0}">
									<c:forEach items="${studentCourse.getStudentAssignments()}"
										var="studentAssignment">
										<td align="center"><c:out
												value="${studentAssignment.getResult()}%" /></td>
									</c:forEach>
								</c:if>
								<td align="center"><c:out
										value="${studentCourse.getMidResult()}%" /></td>
								<td align="center"><c:out
										value="${studentCourse.getFinalResult()}%" /></td>
								<td align="center"><c:out
										value="${studentCourse.getCourseTotalResult()}%" /></td>
							</tr>
						</table>
					</c:forEach>
				</c:if>
				<c:if test="${studentCourse!=null }">
					<table border="1">
						<c:set var="course" value='${studentCourse.getCourse() }' />
						<c:set var="noStudentQuizzes"
							value="${fn:length(studentCourse.getStudentQuizzes())}" />
						<c:set var="noStudentAssignments"
							value="${fn:length(studentCourse.getStudentAssignments())}" />
						<caption>
							<h3 align="left">Breakdown of Your Results for
								${course.getName()}</h3>
						</caption>
						<tr>
							<th rowspan="2"></th>
							<th colspan='${noStudentQuizzes}'>Quizzes
								(${studentCourse.getCourse().getQuizPercent()}%)</th>
							<th colspan=' ${noStudentAssignments}'>Assignments
								(${studentCourse.getCourse().getAssignmentPercent()}%)</th>
							<th rowspan="2">Mid (30%)</th>
							<th rowspan="2">Final (50%)</th>
							<th rowspan="2">Total (100%)</th>
						</tr>
						<tr>
							<c:forEach items="${course.getQuizzes()}" var="quiz">
								<th><c:out
										value="${quiz.getName()}(${quiz.getQuizPercent()}%)" /></th>
							</c:forEach>
							<c:forEach items="${course.getAssignments()}" var="assignment">
								<th><c:out
										value="${assignment.getName()}(${assignment.getAssignmentPercent()}%)" />
								</th>
							</c:forEach>
						</tr>
						<tr>
							<th scope="row">Results</th>
							<c:if test="${noStudentQuizzes == 0}">
								<td align="center">0%</td>
							</c:if>
							<c:if test="${noStudentQuizzes != 0}">
								<c:forEach items="${studentCourse.getStudentQuizzes()}"
									var="studentQuiz">
									<td align="center"><c:out
											value="${studentQuiz.getResult()}%" /></td>
								</c:forEach>
							</c:if>
							<c:if test="${noStudentAssignments == 0}">
								<td align="center">0%</td>
							</c:if>
							<c:if test="${noStudentAssignments != 0}">
								<c:forEach items="${studentCourse.getStudentAssignments()}"
									var="studentAssignment">
									<td align="center"><c:out
											value="${studentAssignment.getResult()}%" /></td>
								</c:forEach>
							</c:if>
							<td align="center"><c:out
									value="${studentCourse.getMidResult()}%" /></td>
							<td align="center"><c:out
									value="${studentCourse.getFinalResult()}%" /></td>
							<td align="center"><c:out
									value="${studentCourse.getCourseTotalResult()}%" /></td>
						</tr>
					</table>
				</c:if>
				<br> <br>
			</div>
		</c:if>
	</c:if>
</body>
</html>
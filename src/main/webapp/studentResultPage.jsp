<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.mavenbro.web.model.Course"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.List"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<!--  This page is for adding viewing and inputting student results for a course
if no user is signed  a login button is display,
if a signed user without the correct permission accesses the page an access denied notification 
and a "back to courses" button is displayed 
 -->
<html>
<head>
<meta charset="ISO-8859-1">
<title>Student Grade Page</title>
</head>
<body>
	<c:set var="quizLen" value="${fn:length(course.getQuizzes())}" />
	<c:if test="${signedInUser ==null}">
		<form action="logoutController" method="get">
			<input type="submit" value="Login">
		</form>
	</c:if>

	<c:if test="${signedInUser != null}">
		<form action="courseController" method="get">
			<input type="hidden" name="courseAction" value="listCourses" /> <input
				type="submit" value="Go Back To Courses">
		</form>
		<h1>${course.getName()}</h1>
		<c:if test="${permission != 3}">

			<h1>You do not have access to this page</h1>
		</c:if>
		<c:if test="${permission eq 3}">
			<c:if
				test="${midError ==true || finalError ==true || assignmentError ==true||quizError ==true}">
				<h2>ERROR</h2>
				<c:if test="${midError ==true }">
					<h3>The Mid Term result cannot be higher than 30%</h3>
				</c:if>
				<c:if test="${finalError ==true }">
					<h3>The Final Term result cannot be higher than 50%</h3>
				</c:if>
				<c:if test="${assignmentError ==true }">
					<h3>Assignment results cannot be higher than the assigned %</h3>
				</c:if>
				<c:if test="${quizError ==true }">
					<h3>Quiz results cannot be higher than the assigned %</h3>
				</c:if>
				<h3>Please input results again</h3>
			</c:if>
			<div align="left">
				<table border="1">
					<caption>
						<h2 align="left">Grade Students Here</h2>
					</caption>
					<tr>
						<th rowspan="2">Student No.</th>
						<th colspan='${noQuizzes}'>Quizzes
							(${course.getQuizPercent()}%)</th>
						<th colspan=' ${noAssignments}'>Assignments
							(${course.getAssignmentPercent()}%)</th>
						<th rowspan="2">Mid (30%)</th>
						<th rowspan="2">Final (50%)</th>
						<th rowspan="2">Total (100%)</th>
						<th rowspan="2">Set Grade</th>
					</tr>
					<tr>
						<c:if test="${noQuizzes == 0}">
							<th>-</th>
						</c:if>
						<c:if test="${noQuizzes != 0}">
							<c:forEach items="${course.getQuizzes()}" var="quiz"
								varStatus="loop">
								<th><c:out
										value="${quiz.getName()}(${quiz.getQuizPercent()}%)" /></th>
							</c:forEach>
						</c:if>
						<c:if test="${noAssignments == 0}">
							<th>-</th>
						</c:if>
						<c:if test="${noAssignments != 0}">
							<c:forEach items="${course.getAssignments()}" var="assignment">
								<th><c:out
										value="${assignment.getName()}(${assignment.getAssignmentPercent()}%)" />
								</th>
							</c:forEach>
						</c:if>
					</tr>
					<c:forEach items="${studentCourses}" var="sc">
						<form>
							<tr>
								<th><c:out value="${sc.getStudent().getId()}" /></th>
								<c:if test="${noQuizzes == 0}">
									<td align="center">-</td>
								</c:if>
								<c:if test="${noQuizzes != 0}">
									<c:forEach items="${sc.getStudentQuizzes()}" var="studentQuiz">
										<th><input type="text"
											name="quizResult${studentQuiz.getId()}${sc.getId()}"
											value="<c:out value="${studentQuiz.getResult()}"  />" /></th>
									</c:forEach>
								</c:if>
								<c:if test="${noAssignments == 0}">
									<td align="center">-</td>
								</c:if>
								<c:if test="${noAssignments != 0}">
									<c:forEach items="${sc.getStudentAssignments()}"
										var="studentAssignment">
										<th><input type="text"
											name="assignmentResult${studentAssignment.getId()}${sc.getId()}"
											value="<c:out value="${studentAssignment.getResult()}"/>" />
										</th>
									</c:forEach>
								</c:if>
								<td><input type="text" name="midResult"
									value="<c:out value="${sc.getMidResult()}"  />" /></td>
								<td><input type="text" name="finalResult"
									value="<c:out value="${sc.getFinalResult()}"  />" /></td>
								<td><c:out value="${sc.getCourseTotalResult()}" /></td>
								<td><input type="hidden" name="instructorAction"
									value="gradeStudent" /> <input type="hidden"
									name="studentId${sc.getStudent().getId()}"
									value="${sc.getStudent().getId()}" /> <input type="hidden"
									name="courseId" value='${course.id}' /> <input type="hidden"
									name="studentCourseId" value='${sc.getId()}' /> <input
									type="hidden" name="instructorId"
									value='${signedInUser.getId()}' /> <input type="hidden"
									name="page" value="studentResultPage.jsp" /> <input
									type="submit" value="Set Grades"></td>
							</tr>
						</form>
					</c:forEach>
				</table>
				<br>
				<table border="1">
					<caption>
						<h2 align="left">Breakdown of Student Results</h2>
					</caption>
					<tr>
						<th rowspan="2">Student No.</th>
						<th colspan='${noQuizzes}'>Quizzes
							(${course.getQuizPercent()}%)</th>
						<th colspan=' ${noAssignments}'>Assignments
							(${course.getAssignmentPercent()}%)</th>
						<th rowspan="2">Mid (30%)</th>
						<th rowspan="2">Final (50%)</th>
						<th rowspan="2">Total (100%)</th>
					</tr>
					<tr>
						<c:if test="${noQuizzes == 0}">
							<th>-</th>
						</c:if>
						<c:if test="${noQuizzes != 0}">
							<c:forEach items="${course.getQuizzes()}" var="quiz">
								<th><c:out
										value="${quiz.getName()}(${quiz.getQuizPercent()}%)" /></th>
							</c:forEach>
						</c:if>
						<c:if test="${noAssignments == 0}">
							<th>-</th>
						</c:if>
						<c:if test="${noAssignments != 0}">
							<c:forEach items="${course.getAssignments()}" var="assignment">
								<th><c:out
										value="${assignment.getName()}(${assignment.getAssignmentPercent()}%)" />
								</th>
							</c:forEach>
						</c:if>
					</tr>
					<c:forEach items="${studentCourses}" var="sc">
						<tr>
							<th><c:out value="${sc.getStudent().getId()}" /></th>
							<c:if test="${noQuizzes == 0}">
								<td align="center">-</td>
							</c:if>
							<c:if test="${noQuizzes != 0}">
								<c:forEach items="${sc.getStudentQuizzes()}" var="studentQuiz">
									<td><c:out value="${studentQuiz.getResult()}" /></td>
								</c:forEach>
							</c:if>
							<c:if test="${noAssignments == 0}">
								<td align="center">-</td>
							</c:if>
							<c:if test="${noAssignments != 0}">
								<c:forEach items="${sc.getStudentAssignments()}"
									var="studentAssignment">
									<td><c:out value="${studentAssignment.getResult()}" /></td>
								</c:forEach>
							</c:if>
							<td><c:out value="${sc.getMidResult()}" /></td>
							<td><c:out value="${sc.getFinalResult()}" /></td>
							<td><c:out value="${sc.getCourseTotalResult()}" /></td>
					</c:forEach>
				</table>
			</div>
		</c:if>
	</c:if>
</body>
</html>

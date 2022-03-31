<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<!--  This page is for adding quizzes to a course 
if no user is signed  a login button is display,
if a signed user without the correct permission accesses the page an access denied notification 
and a "back to courses" button is displayed 
otherwise, an instructor can add and edit quizzes related to a course -->
<html>
<head>
<meta charset="ISO-8859-1">
<title>Course Form</title>
</head>
<body>
	<c:if test="${signedInUser ==null}">
		<form action="logoutController" method="get">
			<input type="submit" value="Login">
		</form>
	</c:if>
	<c:if test="${signedInUser !=null}">

		<c:if test="${permission!=3 }">You do not have permission to access this page</c:if>
			<form action="courseController" method="get">
				<input type="hidden" name="courseAction" value="listCourses" /> <input
					type="submit" value="Go Back To Courses">
			</form>
		<c:if test="${permission==3 }">
		
			<br>
			<form action="logoutController" method="get">
				<input type="submit" value="Logout">
			</form>
			<c:if test="${isOverPercentage != null}">
				<c:if test="${isOverPercentage eq true}">
					<h2>WARNING: Assignment Total + Quiz Total Can not be greater
						than 20%</h2>
					<h3>Please try again</h3>
				</c:if>
			</c:if>
			<c:if test="${canSetTotal != null}">
				<h2>
					<c:out value="${canSetTotal}" />
				</h2>
				<h3>Please try again</h3>
			</c:if>
			<h2 align="center">Assign Percentages</h2>
			<div align="center">
				<form action="instructorController" method="post">
					<table border="1">
						<tr>
							<th colspan="2">Quiz Total %</th>
							<th colspan="2">Assignment Total %</th>
						</tr>
						<tr>
							<td><input type="text" name="quizPercent"
								value="${course.getQuizPercent()}" /></td>
							<td><c:out value="${course.getQuizPercent()}%" /></td>
							<td><input type="text" name="assignmentPercent"
								value="${course.getAssignmentPercent()}" /></td>
							<td><c:out value="${course.getAssignmentPercent()}%" /></td>
						</tr>
						<tr>
							<td align="center" colspan="4"><input type="hidden"
								name="courseId" value='${course.id}' /> <input type="hidden"
								name="instructor" value='${signedInUser.getId()}' /> <input
								type="hidden" name="page" value="addQuiz.jsp" /> <input
								type="hidden" name="instructorAction"
								value="setAssessmentPercent" /> <input type="submit"
								value="Set Assessment Percents"></td>
						</tr>
					</table>
				</form>
			</div>
			<div align="center">
				<c:if test="${course != null}">
					<h1>Quiz Manager for ${course.getName()}</h1>
					<div>The total Quiz % is ${course.getQuizPercent() }%</div>
					<c:if test="${quizzesOverMaxPC == true}">
						<h2>WARNING: The Accumulation of All Quizzes must not exceed
							${course.getQuizPercent() }%</h2>
						<h3>Please try again</h3>
					</c:if>
					<c:if test="${quizLessThanZero == true}">
						<h2>WARNING: Cannot save a quiz with 0%</h2>
						<h3>Please try again</h3>
					</c:if>
					<c:set var="len" value="${fn:length(course.getQuizzes())}" />
					<c:if test="${len lt 5}">
						<form action="quizController" method="post">
							<input type="hidden" name="quizAction" value="createQuiz" />
							<h3>Add a New Quiz</h3>
							<table border="1">
								<c:if test="${course != null}">
									<input type="hidden" name="courseId"
										value="<c:out value='${course.id}' />" />
								</c:if>
								<tr>
									<th>Course Name:</th>
									<td><c:out value='${course.getName()}' /></td>
								</tr>
								<tr>
									<th>Quiz Name:</th>
									<td><input type="text" name="name" size="45" value="" /></td>
								</tr>
								<tr>
									<th>Quiz % out of ${course.getQuizPercent() }%:</th>
									<td><input type="text" name="quizPercent" size="45"
										value="0" /></td>
								</tr>
								<tr>
									<td colspan="2" align="center"><input type="submit"
										value="Save Quiz" /></td>
								</tr>
							</table>
						</form>
					</c:if>
				</c:if>

				<c:if test="${fn:length(course.getQuizzes()) gt 4}">
					<h3>----- You already have 5 quizzes assigned, you cannot
						assign any more -----</h3>
				</c:if>
				<br>
				<hr>
				<c:if test="${course != null}">
					<c:forEach items="${course.getQuizzes()}" var="quiz">
						<h3>Update ${quiz.getName()}</h3>
						<table border="1">
							<form action="quizController" method="post">
								<input type="hidden" name="id" value='${quiz.getId() }' /> <input
									type="hidden" name="quizAction" value="updateQuiz" />
								<c:if test="${course != null}">
									<input type="hidden" name="courseId"
										value="<c:out value='${course.id}' />" />
								</c:if>
								<tr>
									<th>Course Name:</th>
									<td><c:out value='${course.getName()}' /></td>
								</tr>
								<tr>
									<th>Quiz Name:</th>
									<td><input type="text" name="name" size="45"
										value="<c:out value='${quiz.getName()}' />" /></td>
								</tr>
								<tr>
									<th>Quiz % out of ${course.getQuizPercent() }%:</th>
									<td><input type="text" name="quizPercent" size="45"
										value="<c:out value='${quiz.getQuizPercent()}' />" /></td>
								</tr>
								<tr>
									<td colspan="2" align="center"><input type="submit"
										value="Update Quiz" /> <!--    </td>
		             <td> -->
							</form>

							<form action="quizController" method="post">
								<input type="hidden" name="quizAction" value="deleteQuiz" /> <input
									type="hidden" name="quizId" value='${quiz.getId()}' /> <input
									type="hidden" name="courseId" value='${course.id}' /> <input
									type="submit" value="Delete">
							</form>
							</td>
							</tr>
						</table>
					</c:forEach>
				</c:if>
			</div>
		</c:if>
	</c:if>
</body>
</html>

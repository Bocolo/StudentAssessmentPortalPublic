<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<!--  This page is for adding assignments to a course 
if no user is signed  a login button is display,
if a signed user without the correct permission accesses the page an access denied notification 
and a "back to courses" button is displayed 
otherwise, an instructor can add and edit assignments related to a course -->
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
					type="submit" value="Go Back To Courses" />
			</form>
		<c:if test="${permission==3 }">
	
			<form action="logoutController" method="get">
				<input type="submit" value="Logout" />
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
								type="hidden" name="page" value="addAssignment.jsp" /> <input
								type="hidden" name="instructorAction"
								value="setAssessmentPercent" /> <input type="submit"
								value="Set Assessment Percents"></td>
						</tr>
					</table>
				</form>
			</div>
			<div align="center">
				<c:if test="${course != null}">
					<h1>Assignment Manager for ${course.getName()}</h1>
					<div>The total Assignment % is
						${course.getAssignmentPercent() }</div>
					<c:if test="${assignmentsOverMaxPC == true}">
						<h2>WARNING: The Accumulation of All Assignments must not
							exceed ${course.getAssignmentPercent() }%</h2>
						<h3>Please try again</h3>
					</c:if>
					<c:if test="${assignmentLessThanZero == true}">
						<h2>WARNING: Cannot save an assignment with 0%</h2>
						<h3>Please try again</h3>
					</c:if>
					<c:set var="len" value="${fn:length(course.getAssignments())}" />
					<c:if test="${len lt 3}">
						<form action="assignmentController" method="post">
							<input type="hidden" name="assignmentAction"
								value="createAssignment" />
							<h3>Add a New Assignment</h3>
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
									<th>Assignment Name:</th>
									<td><input type="text" name="name" size="45" value="" /></td>
								</tr>
								<tr>
									<th>Assignment % out of ${course.getAssignmentPercent()
										}%:</th>
									<td><input type="text" name="assignmentPercent" size="45"
										value="0" /></td>
								</tr>
								<tr>
									<td colspan="2" align="center"><input type="submit"
										value="Save Assignment" /></td>
								</tr>
							</table>
						</form>
					</c:if>
				</c:if>
				<c:if test="${fn:length(course.getAssignments()) gt 2}">
					<h3>----- You already have 3 assignments assigned, you cannot
						assign any more -----</h3>
				</c:if>
				<c:if test="${course != null}">
					<c:forEach items="${course.getAssignments()}" var="assignment">
						<h3>Edit ${assignment.getName()}</h3>
						<h3>Update Assignment</h3>
						<table border="1">
							<form action="assignmentController" method="post">
								<input type="hidden" name="id" value='${assignment.getId() }' />
								<input type="hidden" name="assignmentAction"
									value="updateAssignment" />
								<caption>Edit ${assignment.getName()}</caption>
								<c:if test="${course != null}">
									<input type="hidden" name="courseId"
										value="<c:out value='${course.id}' />" />
								</c:if>
								<tr>
									<th>Course Name:</th>
									<td><c:out value='${course.getName()}' /></td>
								</tr>
								<tr>
									<th>Assignment Name:</th>
									<td><input type="text" name="name" size="45"
										value="<c:out value='${assignment.getName()}' />" /></td>
								</tr>
								<tr>
									<th>Assignment % out of ${course.getAssignmentPercent()
										}%:</th>
									<td><input type="text" name="assignmentPercent" size="45"
										value="<c:out value='${assignment.getAssignmentPercent()}' />" />
									</td>
								</tr>
								<tr>
									<td colspan="2" align="center"><input type="submit"
										value="Update Assignment" />
							</form>
							<form action="assignmentController" method="post">
								<input type="hidden" name="assignmentAction"
									value="deleteAssignment" /> <input type="hidden"
									name="assignmentId" value='${assignment.getId()}' /> <input
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

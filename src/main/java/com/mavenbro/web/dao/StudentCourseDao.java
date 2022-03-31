package com.mavenbro.web.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mavenbro.web.model.StudentCourse;
import com.mavenbro.web.util.HibernateUtil;

/**
 * handles database access responsibilities for student courses
 * 
 * @author brona
 *
 */
public class StudentCourseDao {
	/**
	 * queries the database(db) saves a StudentCourse to the db
	 * 
	 * @param StudentCourse to be saved
	 */
	public void saveStudentCourse(StudentCourse sc) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(sc);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
	/**
	 * queries the database(db). deletes a StudentCourse on the db. course is
	 * identified by the combined student id and course id
	 * 
	 * @param courseId  course id used to find the correct student course
	 * @param studentId student id used to find the correct student course
	 */
	public void deleteStudentCourse(int courseId, int studentId) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			StudentCourse studentCourse = (StudentCourse) session
					.createQuery("from StudentCourse where Course_Id= :courseID AND Student_ID= :studentId")
					.setParameter("courseID", courseId).setParameter("studentId", studentId).uniqueResult();
			if (studentCourse != null) {
				session.delete(studentCourse);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	/**
	 * queries the database(db) updates a StudentCourse on the db
	 * 
	 * @param StudentCourse to be updated
	 */
	public void updateStudentCourse(StudentCourse course) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.update(course);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	/**
	 * queries the database(db) returns a StudentCourse
	 * 
	 * @param id of StudentCourse to be returned
	 * @return a StudentCourse object
	 */
	public StudentCourse getStudentCourse(int id) {
		Transaction transaction = null;
		StudentCourse course = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			course = session.get(StudentCourse.class, id);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return course;
	}

	/**
	 * queries the database(db) returns a list of all StudentCourses
	 * 
	 * @return a list of StudentCourses
	 */
	@SuppressWarnings("unchecked")
	public List<StudentCourse> getAllStudentCourses() {
		Transaction transaction = null;
		List<StudentCourse> listOfStudentCourse = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			listOfStudentCourse = (List<StudentCourse>) session.createQuery("from StudentCourse").getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfStudentCourse;
	}

	/**
	 * queries the database(db) returns a set of StudentCourses related to the
	 * course that has courseId param as an id
	 * 
	 * @param courseId of all student courses to be returned
	 * @return a set of StudentCourses
	 */
	@SuppressWarnings("unchecked")
	public Set<StudentCourse> getAllStudentCoursesByCourse(int courseId) {
		Transaction transaction = null;
		Set<StudentCourse> listOfStudentCourse = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			String sql = "from StudentCourse where Course_Id=" + courseId;
			listOfStudentCourse = new HashSet<StudentCourse>(
					(ArrayList<StudentCourse>) session.createQuery(sql).getResultList());
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfStudentCourse;
	}

	/**
	 * queries the database(db) returns a set of StudentCourses related to the
	 * student that has studentId param as an id
	 * 
	 * @param studentId of all student courses to be returned
	 * @return a set of StudentCourses
	 */
	@SuppressWarnings("unchecked")
	public Set<StudentCourse> getAllStudentCoursesByStudent(int studentId) {
		Transaction transaction = null;
		Set<StudentCourse> listOfStudentCourse = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			String sql = "from StudentCourse where Student_Id=" + studentId;
			listOfStudentCourse = new HashSet<StudentCourse>(
					(ArrayList<StudentCourse>) session.createQuery(sql).getResultList());
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfStudentCourse;
	}

	/**
	 * queries the database(db) returns the student course that related to the
	 * course id and student id that are passed as params
	 * 
	 * @param courseId  of the studentCourse to be returned
	 * @param studentId of the studentCourse to be returned
	 * @return a studentCourse object
	 */
	@SuppressWarnings("unchecked")
	public StudentCourse getStudentCourseByCourseStudent(int courseId, int studentId) {
		Transaction transaction = null;
		StudentCourse studentCourse = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			String sql = "from StudentCourse where Course_Id=" + courseId + " AND Student_ID=" + studentId;
			List<StudentCourse> studentCourses = (List<StudentCourse>) session.createQuery(sql).getResultList();
			studentCourse = studentCourses.get(0);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return studentCourse;
	}
}

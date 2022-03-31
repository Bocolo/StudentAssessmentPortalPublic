package com.mavenbro.web.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mavenbro.web.model.Course;
import com.mavenbro.web.model.StudentCourse;
import com.mavenbro.web.util.HibernateUtil;

/**
 * handle CRUD responsibilities for course objects
 * 
 * @author brona
 *
 */
public class CourseDao {
	/**
	 * queries the database(db) saves a course ot the db
	 * 
	 * @param course to be saved
	 */
	public void saveCourse(Course course) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(course);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	/**
	 * queries the database(db) deletes an course on the db
	 * 
	 * @param id of course to be deleted
	 */
	public void deleteCourse(int id) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Course course = session.get(Course.class, id);
			if (course != null) {
				session.delete(course);
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
	 * queries the database(db) updates a course on the db
	 * 
	 * @param course to be updated
	 */
	public void updateCourse(Course course) {
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
	 * queries the database(db) returns an course
	 * 
	 * @param id of course to be returned
	 * @return a course object
	 */
	public Course getCourse(int id) {
		Transaction transaction = null;
		Course course = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			course = session.get(Course.class, id);
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
	 * queries the database(db) returns a list of all courses
	 * 
	 * @return a list of courses
	 */
	@SuppressWarnings("unchecked")
	public List<Course> getAllCourses() {
		Transaction transaction = null;
		List<Course> listOfCourse = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			listOfCourse = (List<Course>) session.createQuery("from Course").getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfCourse;
	}

	/**
	 * queries the database(db) returns a set of all courses
	 * 
	 * @return a set of courses
	 */
	@SuppressWarnings("unchecked")
	public Set<Course> getAllCoursesSet() {
		Transaction transaction = null;
		Set<Course> listOfCourse = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			listOfCourse = new HashSet<Course>((ArrayList<Course>) session.createQuery("from Course").getResultList());
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfCourse;
	}

	/**
	 * queries the database(db) returns a set of courses excluded studentcourses in
	 * the passed param set
	 * 
	 * @param studentCourses not to be included in the returned set
	 * @return a set of courses
	 */
	@SuppressWarnings("unchecked")
	public Set<Course> getAllCoursesExcluding(Set<StudentCourse> studentCourses) {
		Transaction transaction = null;
		Set<Course> listOfCourses = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			String courseIds = "";
			List<String> ids = new ArrayList<String>();
			String sql = "";
			if (studentCourses.size() != 0) {
				for (StudentCourse studentCourse : studentCourses) {
					courseIds += studentCourse.getCourse().getId() + ", ";
					ids.add(studentCourse.getCourse().getId() + "");
				}
				courseIds = courseIds.substring(0, courseIds.length() - 2);
				sql = "from Course where ID NOT IN (" + courseIds + ")";
			} else {
				sql = "from Course";
			}
			listOfCourses = new HashSet<Course>((ArrayList<Course>) session.createQuery(sql).getResultList());
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfCourses;
	}
}

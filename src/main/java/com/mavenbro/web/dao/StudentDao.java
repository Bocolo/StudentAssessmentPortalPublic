package com.mavenbro.web.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mavenbro.web.model.Student;
import com.mavenbro.web.util.HibernateUtil;
/**
 * handle database access responsibilities for Student objects
 * 
 * @author brona
 *
 */
public class StudentDao {
	/**
	 * queries the database(db) saves a Student to the db
	 * 
	 * @param Student to be saved
	 */
	public void saveStudent(Student student) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(student);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
	/**
	 * queries the database(db) updated a Student on the db
	 * 
	 * @param Student to be updated
	 */
	public void updateStudent(Student student) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.update(student);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	/**
	 * queries the database(db) returns an Student
	 * 
	 * @param id of Student to be returned
	 * @return a Student object
	 */
	public Student getStudent(int id) {
		Transaction transaction = null;
		Student student = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			student = session.get(Student.class, id);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return student;
	}
	/**
	 * queries the database(db) returns a list of all Students
	 * 
	 * @return a list of Students
	 */

	@SuppressWarnings("unchecked")
	public List<Student> getAllStudents() {
		Transaction transaction = null;
		List<Student> listOfStudent = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			listOfStudent = (List<Student>) session.createQuery("from Student").getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfStudent;
	}
}
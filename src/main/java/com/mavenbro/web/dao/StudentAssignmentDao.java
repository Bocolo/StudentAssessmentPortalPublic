package com.mavenbro.web.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mavenbro.web.model.StudentAssignment;
import com.mavenbro.web.util.HibernateUtil;
/**
 * handles database access responsibilities for student assignments
 * 
 * @author brona
 *
 */
public class StudentAssignmentDao {
	/**
	 * queries the database(db) saves a StudentAssignment to the db
	 * 
	 * @param StudentAssignment to be saved
	 */
	public void saveStudentAssignment(StudentAssignment studentAssignment) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(studentAssignment);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
	/**
	 * queries the database(db) updates a StudentAssignment on the db
	 * 
	 * @param StudentAssignment to be updated
	 */
	public void updateStudentAssignment(StudentAssignment studentAssignment) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.update(studentAssignment);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}


	/**
	 * queries the database(db) returns a list of all StudentAssignments
	 * 
	 * @return a list of StudentAssignments
	 */
	@SuppressWarnings("unchecked")
	public List<StudentAssignment> getAllStudentAssignments() {
		Transaction transaction = null;
		List<StudentAssignment> listOfStudentAssignments = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			listOfStudentAssignments = (List<StudentAssignment>) session.createQuery("from StudentAssignment")
					.getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfStudentAssignments;
	}

}
package com.mavenbro.web.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mavenbro.web.model.Instructor;
import com.mavenbro.web.util.HibernateUtil;
/**
 * handle database access responsibilities for Instructor objects
 * 
 * @author brona
 *
 */
public class InstructorDao {
	/**
	 * queries the database(db) saves a Instructor to the db
	 * 
	 * @param Instructor to be saved
	 */
	public void saveInstructor(Instructor instructor) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(instructor);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
	/**
	 * queries the database(db) updated a Instructor on the db
	 * 
	 * @param Instructor to be updated
	 */
	public void updateInstructor(Instructor instructor) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.update(instructor);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}


	/**
	 * queries the database(db) returns a list of all Instructors
	 * 
	 * @return a list of Instructors
	 */
	@SuppressWarnings("unchecked")
	public List<Instructor> getAllInstructors() {
		Transaction transaction = null;
		List<Instructor> listOfInstructors = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			listOfInstructors = (List<Instructor>) session.createQuery("from Instructor").getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfInstructors;
	}
	/**
	 * queries the database(db) returns an Instructor
	 * 
	 * @param id of Instructor to be returned
	 * @return a Instructor object
	 */
	public Instructor getInstructor(int id) {
		Transaction transaction = null;
		Instructor instructor = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			instructor = session.get(Instructor.class, id);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return instructor;
	}
}

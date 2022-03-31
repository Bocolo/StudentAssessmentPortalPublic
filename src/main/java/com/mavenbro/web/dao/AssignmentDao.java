package com.mavenbro.web.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mavenbro.web.model.Assignment;
import com.mavenbro.web.util.HibernateUtil;

/**
 * handle CRUD responsibilities for assignment objects
 * 
 * @author brona
 *
 */
public class AssignmentDao {
	/**
	 * queries the database(db) saves an assignment ot the db
	 * 
	 * @param assignment to be saved
	 */
	public void saveAssignment(Assignment assignment) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(assignment);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	/**
	 * queries the database(db) updates an assignment on the db
	 * 
	 * @param assignment to be updated
	 */
	public void updateAssignment(Assignment assignment) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.update(assignment);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	/**
	 * queries the database(db) deletes an assignment on the db
	 * 
	 * @param id of assignment to be deleted
	 */
	public void deleteAssignment(int id) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Assignment assignment = session.get(Assignment.class, id);
			if (assignment != null) {
				session.delete(assignment);
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
	 * queries the database(db) returns an assignment
	 * 
	 * @param id of assignment to be returned
	 * @return an assignment object
	 */
	public Assignment getAssignment(int id) {
		Transaction transaction = null;
		Assignment assignment = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			assignment = session.get(Assignment.class, id);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return assignment;
	}


	/**
	 * queries the database(db) returns a list of all assignments
	 * 
	 * @return a list of assignments
	 */
	@SuppressWarnings("unchecked")
	public List<Assignment> getAllAssignments() {
		Transaction transaction = null;
		List<Assignment> listOfAssignment = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			listOfAssignment = (List<Assignment>) session.createQuery("from Assignment").getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfAssignment;
	}
}

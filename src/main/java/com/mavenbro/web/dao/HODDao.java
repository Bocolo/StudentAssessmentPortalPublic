package com.mavenbro.web.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mavenbro.web.model.HOD;
import com.mavenbro.web.util.HibernateUtil;
/**
 * handles database access responsibilities for Hod objects
 * 
 * @author brona
 *
 */
public class HODDao {
	/**
	 * queries the database(db) saves a Hod to the db
	 * 
	 * @param Hod to be saved
	 */
	public void saveHod(HOD hodUser) {
	
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(hodUser);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
	/**
	 * queries the database(db) returns an Hod
	 * 
	 * @param id of Hod to be returned
	 * @return a Hod object
	 */
	public HOD getHOD(int id) {
		Transaction transaction = null;
		HOD hod = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			hod = session.get(HOD.class, id);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return hod;
	}


	/**
	 * queries the database(db) returns a list of all Hods
	 * 
	 * @return a list of Hods
	 */
	@SuppressWarnings("unchecked")
	public List<HOD> getAllHODs() {
		Transaction transaction = null;
		List<HOD> listOfHOD = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			listOfHOD = (List<HOD>) session.createQuery("from HOD").getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfHOD;
	}
}

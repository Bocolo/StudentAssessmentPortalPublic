package com.mavenbro.web.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mavenbro.web.model.StudentQuiz;
import com.mavenbro.web.util.HibernateUtil;
/**
 * handle database access responsibilities for StudentQuiz objects
 * 
 * @author brona
 *
 */
public class StudentQuizDao {
	/**
	 * queries the database(db) saves a StudentQuiz to the db
	 * 
	 * @param StudentQuiz to be saved
	 */
	public void saveStudentQuiz(StudentQuiz studentQuiz) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(studentQuiz);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
	/**
	 * queries the database(db) updated a StudentQuiz on the db
	 * 
	 * @param StudentQuiz to be updated
	 */
	public void updateStudentQuiz(StudentQuiz studentQuiz) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.update(studentQuiz);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	/**
	 * queries the database(db) returns a list of all StudentQuizs
	 * 
	 * @return a list of StudentQuizs
	 */
	@SuppressWarnings("unchecked")
	public List<StudentQuiz> getAllStudentQuizzes() {
		Transaction transaction = null;
		List<StudentQuiz> listOfStudentQuizzes = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			listOfStudentQuizzes = (List<StudentQuiz>) session.createQuery("from StudentQuiz").getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfStudentQuizzes;
	}
}
package com.mavenbro.web.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mavenbro.web.model.Quiz;
import com.mavenbro.web.util.HibernateUtil;

/**
 * handle CRUD responsibilities for quiz objects
 * 
 * @author brona
 *
 */
public class QuizDao {
	/**
	 * queries the database(db) saves a quiz ot the db
	 * 
	 * @param quiz to be saved
	 */
	public void saveQuiz(Quiz quiz) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(quiz);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	/**
	 * queries the database(db) updates a quiz on the db
	 * 
	 * @param quiz to be updated
	 */
	public void updateQuiz(Quiz quiz) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.update(quiz);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	/**
	 * queries the database(db) deletes an quiz on the db
	 * 
	 * @param id of quiz to be deleted
	 */
	public void deleteQuiz(int id) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Quiz quiz = session.get(Quiz.class, id);
			if (quiz != null) {
				session.delete(quiz);
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
	 * queries the database(db) returns an quiz
	 * 
	 * @param id of quiz to be returned
	 * @return a quiz object
	 */
	public Quiz getQuiz(int id) {
		Transaction transaction = null;
		Quiz quiz = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			quiz = session.get(Quiz.class, id);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return quiz;
	}


	/**
	 * queries the database(db) returns a list of all quizzes
	 * 
	 * @return a list of quizzes
	 */
	@SuppressWarnings("unchecked")
	public List<Quiz> getAllQuizzes() {
		Transaction transaction = null;
		List<Quiz> listOfQuiz = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			listOfQuiz = (List<Quiz>) session.createQuery("from Quiz").getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return listOfQuiz;
	}
}

package com.mavenbro.web.util;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import com.mavenbro.web.model.Assignment;
import com.mavenbro.web.model.Course;
import com.mavenbro.web.model.HOD;
import com.mavenbro.web.model.Instructor;
import com.mavenbro.web.model.Quiz;
import com.mavenbro.web.model.Student;
import com.mavenbro.web.model.StudentAssignment;
import com.mavenbro.web.model.StudentCourse;
import com.mavenbro.web.model.StudentQuiz;
import com.mavenbro.web.model.User;

/**
 * This class handles all setting for connected the Sql database using hibernate
 * 
 * @author brona
 *
 */
public class HibernateUtil {
	private static SessionFactory sessionFactory;

	/**
	 * this method sets the session factory setting. connects with the database and
	 * adds the relevant database classes
	 * 
	 * @return
	 */
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();
				Properties settings = new Properties();
				settings.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
				settings.put(Environment.URL, "jdbc:mysql://localhost:3306/maven_db4?useSSL=false");
				settings.put(Environment.USER, "root");
				//Password is here
				settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect");
				settings.put(Environment.SHOW_SQL, "true");
				settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
				settings.put(Environment.HBM2DDL_AUTO, "none");
				configuration.setProperties(settings);
				configuration.addAnnotatedClass(User.class);
				configuration.addAnnotatedClass(Course.class);
				configuration.addAnnotatedClass(Student.class);
				configuration.addAnnotatedClass(Instructor.class);
				configuration.addAnnotatedClass(HOD.class);
				configuration.addAnnotatedClass(Assignment.class);
				configuration.addAnnotatedClass(Quiz.class);
				configuration.addAnnotatedClass(StudentCourse.class);
				configuration.addAnnotatedClass(StudentQuiz.class);
				configuration.addAnnotatedClass(StudentAssignment.class);
				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
				return sessionFactory;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}
}

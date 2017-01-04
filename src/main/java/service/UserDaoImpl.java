package service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.User;
import persistance.GenericDAO;

public class UserDaoImpl extends GenericDAO{
	
	private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

	public void createUser(String name, String forename, Date added) {
		log.info("Create a User");
		EntityManager em = createEntityManager();
		EntityTransaction tx = null;
		try{
			tx = em.getTransaction();
			tx.begin();
			User user=new User(name,forename,added);
			em.persist(user);
			tx.commit();
		}catch(Exception re){
			if(tx!=null)
				log.error("Something went wrong; Discard all partial changes");
			tx.rollback();
		}finally{
			closeEntityManager();
		}
	}

	public void createUser(List<User> users){
		log.info("Create list User");
		EntityManager em = createEntityManager();
		EntityTransaction tx = null;
		try{
			tx = em.getTransaction();
			tx.begin();
			em.persist(users);
			tx.commit();
		}catch(Exception re){
			if(tx!=null)
				log.error("Something went wrong; Discard all partial changes");
			tx.rollback();
		}finally{
			closeEntityManager();
		}
	}

	@SuppressWarnings("unchecked")
	public List<User> findAll(){
		log.info("Find all Users");
		EntityManager em = createEntityManager();
		EntityTransaction tx = null;
		List<User> res = null;
		try{
			tx = em.getTransaction();
			tx.begin();
			Query query = em.createQuery("select * from USER");
			res = query.getResultList();
			tx.commit();
		}catch(Exception re){
			if(tx!=null)
				log.error("Something went wrong; Discard all partial changes");
			tx.rollback();
		}finally{
			closeEntityManager();
		}
		return res;
	}

	public User findById(int id){
		log.info("Find user by Id");
		EntityManager em = createEntityManager();
		EntityTransaction tx = null;
		User us = null;
		try{
			tx = em.getTransaction();
			tx.begin();
			us = em.find(User.class, id);
			tx.commit();
		}catch(Exception re){
			if(tx!=null)
				log.error("Something went wrong; Discard all partial changes");
			tx.rollback();
		}finally{
			closeEntityManager();
		}
		return us;
	}

	public void deleteUser(User persistentInstance){
		
	}
}
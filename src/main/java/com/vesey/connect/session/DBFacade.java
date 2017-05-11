package com.vesey.connect.session;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

import com.vesey.connect.entity.User;
import com.vesey.connect.utils.HibernateUtils;
import com.vesey.connect.utils.PasswordUtils;

@Named
@Stateful
@RequestScoped
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBFacade {

	@Inject
	private Logger log;

	@PersistenceContext(unitName = "connectPU")
	EntityManager em;

	public enum Role {
		ROLE_1, ROLE_2, ROLE_3
	}

	public <T extends Object> T getEntity(Class<T> type, Integer id) {
		try {
			return em.find(type, id);
		} catch (Exception e) {
			log.warn("getEntity: Exception: no Entity found for ID : " + id);
			return null;
		}
	}
	
	public <T extends Object> T getInitializedEntity(Class<T> entityClass, Integer id) {
		T u = getEntity(entityClass, id);
		switch (u.getClass().getName())
		{
		case "com.vesey.connect.entity.User":
			HibernateUtils.initialize((User)u);
		}
		return u;
	}


	public void remove(Class entityClass, Object primaryKey) {
		em.remove(em.getReference(entityClass, primaryKey));	
	}

	public User findUserByUsername(String username) {
		TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_USERNAME, User.class);
		query.setParameter("username", username);
		try {
			User user = query.getSingleResult();
			return user;
		} catch (NoResultException e) {
			// OK to get exception here.
			return null;
		}

	}

	public User getUserByUsernameAndPassword(String username, String password) {
		TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_LOGIN_PASSWORD, User.class);
		query.setParameter("username", username);
		query.setParameter("password", PasswordUtils.digestPassword(password));
		try {
			User user = query.getSingleResult();
			return user;
		} catch (NoResultException e) {
			// OK to get exception here.
			return null;
		}
	}
	
	public boolean persist(Object c) {
		// log.info("persist: Start");
		try {
			em.persist(c);
			return true;
		} catch (Exception e) {
			log.error("persist: Error Persisting : " + e);
			return false;
		}
		// log.info("persist: End");
	}

	public List<User> findAllUsers() {
		TypedQuery<User> query = em.createNamedQuery(User.FIND_ALL, User.class);
		List<User> allUsers = query.getResultList();
		return allUsers;
	}

	
	
	
}

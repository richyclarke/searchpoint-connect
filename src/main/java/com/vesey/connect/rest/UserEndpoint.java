package com.vesey.connect.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.joda.time.LocalDateTime;

import com.vesey.connect.auth.JWTTokenNeeded;
import com.vesey.connect.entity.User;
import com.vesey.connect.utils.KeyGenerator;
import com.vesey.connect.utils.PasswordUtils;

import java.security.Key;

import java.util.Date;
import java.util.List;
import org.jboss.logging.Logger;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */
@Path("/users")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
public class UserEndpoint {

	// ======================================
	// = Injection Points =
	// ======================================

	@Context
	private UriInfo uriInfo;

	@Inject
	private Logger log;

	@Inject
	private KeyGenerator keyGenerator;

	@PersistenceContext
	private EntityManager em;

	// ======================================
	// = Business methods =
	// ======================================

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response registerUser(@FormParam("username") String username, @FormParam("password") String password) {

		

			log.info("#### username/password : " + username + "/" + password);

			
			TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_USERNAME, User.class);
			query.setParameter("username", username);
			User user = null;
			try
			{
				user = query.getSingleResult();
			} catch(NoResultException e)
			{
				// OK to get exception here.
			}

				if (user == null)
				{
					// Username is available - OK to create
					
					User newUser = new User();
					newUser.setUsername(username);  
					String hashedPassword = PasswordUtils.digestPassword(password);
					newUser.setPassword(hashedPassword);  
					
					// Issue a token for the user
					String token = issueToken(username);

					// Return the token on the response
					return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();					
				}
				else
				{
					//user already exists
					return Response.status(UNAUTHORIZED).build();
				}


		
	}
	
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authenticateUser(@FormParam("username") String username, @FormParam("password") String password) {

		try {

			log.info("#### username/password : " + username + "/" + password);

			//String hashedPassword = PasswordUtils.digestPassword(password);
			
			
			// Authenticate the user using the credentials provided
			authenticate(username, password);

			// Issue a token for the user
			String token = issueToken(username);

			// Return the token on the response
			return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

		} catch (Exception e) {
			return Response.status(UNAUTHORIZED).build();
		}
	}

	private void authenticate(String username, String password) throws Exception {
		TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_LOGIN_PASSWORD, User.class);
		query.setParameter("username", username);
		query.setParameter("password", PasswordUtils.digestPassword(password));
		User user = query.getSingleResult();

		if (user == null)
			throw new SecurityException("Invalid user/password");
	}

	private String issueToken(String login) {
		Key key = keyGenerator.generateKey();
		String jwtToken = Jwts.builder().setSubject(login).setIssuer(uriInfo.getAbsolutePath().toString()).setIssuedAt(new Date()).setExpiration(LocalDateTime.now().plusMinutes(15).toDate()).signWith(SignatureAlgorithm.HS512, key).compact();
		log.info("#### generating token for a key : " + jwtToken + " - " + key);
		return jwtToken;

	}

	@POST
	public Response create(User user) {
		em.persist(user);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getUserid().toString()).build()).build();
	}

	@GET
	@Path("/{id}")
	@JWTTokenNeeded
	public Response findById(@PathParam("id") String id) {
		Integer userId = null;
		try {
			userId = Integer.parseInt(id);
		} catch (Exception e) {
			return Response.status(NOT_FOUND).build();
		}
			User user = em.find(User.class, userId);

			if (user == null)
			{
				return Response.status(NOT_FOUND).build();
			}
			return Response.ok(user).build();
	}

	@GET
	public Response findAllUsers() {
		TypedQuery<User> query = em.createNamedQuery(User.FIND_ALL, User.class);
		List<User> allUsers = query.getResultList();

		if (allUsers == null)
			return Response.status(NOT_FOUND).build();

		return Response.ok(allUsers).build();
	}

	@DELETE
	@Path("/{id}")
	public Response remove(@PathParam("id") String id) {
		em.remove(em.getReference(User.class, id));
		return Response.noContent().build();
	}

}
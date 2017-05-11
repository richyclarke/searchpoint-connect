package com.vesey.connect.rest;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.security.Key;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.joda.time.LocalDateTime;

import com.vesey.connect.auth.Secured;
import com.vesey.connect.entity.User;
import com.vesey.connect.session.CurrentUserManager;
import com.vesey.connect.session.DBFacade;
import com.vesey.connect.utils.KeyGenerator;
import com.vesey.connect.utils.PasswordUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */
@Path("/users")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class UserEndpoint {

	@Context
	SecurityContext securityContext;
	
	@Inject
	CurrentUserManager currentUserManager;
	
	@Context
	private UriInfo uriInfo;

	@Inject
	private Logger log;

	@Inject
	private KeyGenerator keyGenerator;

	@Inject
	private DBFacade dbFacade;

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response registerUser(@FormParam("username") String username, @FormParam("password") String password) {
		log.info("registerUser: Start username/password : " + username + "/" + password);
		
		Principal principal = securityContext.getUserPrincipal();
		String username1 = principal.getName();
		
		User user = dbFacade.findUserByUsername(username);

		if (user == null) {
			// Username is available - OK to create

			User newUser = new User();
			newUser.setUsername(username);
			String hashedPassword = PasswordUtils.digestPassword(password);
			newUser.setPassword(hashedPassword);

			// Issue a token for the user
			String token = issueToken(username);

			// Return the token on the response
			return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();
		} else {
			// user already exists
			return Response.status(UNAUTHORIZED).build();
		}

	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authenticateUser(@FormParam("username") String username, @FormParam("password") String password) {
			log.info("#### username/password : " + username + "/" + password);

			// Authenticate the user using the credentials provided
			try
			{
				authenticate(username, password);
			}
			catch(SecurityException se)
			{
				return Response.status(UNAUTHORIZED).build();
			}
			// Issue a token for the user
			String token = issueToken(username);
			
			// Return the token on the response
			return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

		
			
		
	}

	private void authenticate(String username, String password) throws SecurityException {
		User user = dbFacade.getUserByUsernameAndPassword(username, password);
		currentUserManager.init(user);
		if (user == null)
		{
			throw new SecurityException("Invalid user/password");
		}
		
	}

	private String issueToken(String login) {
		Key key = keyGenerator.generateKey();
		String jwtToken = Jwts.builder()
				.setSubject(login)
				.claim("roles", currentUserManager.getRoles())
				.setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(new Date())
				.setExpiration(LocalDateTime.now().plusMinutes(15).toDate())
				.signWith(SignatureAlgorithm.HS512, key).compact();
		log.info("#### generating token for a key : " + jwtToken + " - " + key);
		return jwtToken;

	}

	@POST
	public Response create(User user) {
		dbFacade.persist(user);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getUserid().toString()).build()).build();
	}

	@GET
	@Path("/{id}")
	@Secured({DBFacade.Role.ROLE_1})
	public Response findById(@PathParam("id") String id) {
		Integer userId = null;
		try {
			userId = Integer.parseInt(id);
		} catch (Exception e) {
			return Response.status(NOT_FOUND).build();
		}
		User user = dbFacade.getInitializedEntity(User.class, userId);

		if (user == null) {
			return Response.status(NOT_FOUND).build();
		}
		return Response.ok(user).build();
	}

	@GET
	public Response findAllUsers() {
		List<User> allUsers = dbFacade.findAllUsers();

		if (allUsers == null)
			return Response.status(NOT_FOUND).build();

		return Response.ok(allUsers).build();
	}

	@DELETE
	@Path("/{id}")
	public Response remove(@PathParam("id") String id) {
		dbFacade.remove(User.class, id);
		return Response.noContent().build();
	}

}
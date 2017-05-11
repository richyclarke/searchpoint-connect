package com.vesey.connect.session;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.jboss.logging.Logger;


import com.vesey.connect.entity.Role;
import com.vesey.connect.entity.User;
import com.vesey.connect.session.DBFacade;
import com.vesey.connect.utils.HibernateUtils;
import com.vesey.connect.utils.Utils;

@Named
@Stateful
@RequestScoped
public class CurrentUserManager implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	DBFacade dbFacade;

	@Inject
	Logger log;

	
	private User currentUser;
	List<Role> roleList;

	public void init(User user) {
		log.info("init: Start.");
		initializeCurrentUser(user);
		log.info("init: End.");
	}

	public String getSafeUserName() {
		return Utils.getFormattedUserName(currentUser);
	}

	public boolean getIsSuperuser() {
		if (hasRole("superuser")) {
			return true;
		} else {
			return false;
		}
	}



	public User getCurrentUser() {
		return currentUser;
	}

	public boolean isLoggedIn() {
		return currentUser != null;
	}

	public boolean getIsSupplier() {
		if (hasRole("supplier")) {
			return true;
		} else {
			return false;
		}
	}

	public void initializeCurrentUser(User newUser) {
		roleList = new ArrayList<>();
		if ((newUser != null) && !newUser.equals(currentUser)) {
			currentUser = dbFacade.getEntity(User.class, newUser.getUserid());
			HibernateUtils.initialize(currentUser);
			if (!Utils.isEmpty(currentUser.getRoleCollection())) {
				for (Role thisRole : currentUser.getRoleCollection()) {
					roleList.add(thisRole);
				}
			}
		} else {
			currentUser = null;
		}
	}

	public boolean hasRole(String role) {
		return getHasRole(role);
	}

	public boolean getHasRole(String role) {
		if (role == null) {
			return false;
		}

		if (currentUser != null) {
			if (!Utils.isEmpty(roleList)) {
				for (Role thisRole : roleList) {
					if (role.equalsIgnoreCase(thisRole.getName())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();
		if (currentUser != null) {
			if (!Utils.isEmpty(roleList)) {
				for (Role thisRole : roleList) {
					roles.add(thisRole.getName());
				}
			}
		}
		return roles;
	}
}

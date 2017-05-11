package com.vesey.connect.utils;

import org.hibernate.Hibernate;

import org.jboss.logging.Logger;

import com.vesey.connect.entity.Role;
import com.vesey.connect.entity.User;


public class HibernateUtils {
	private transient static final Logger log = Logger.getLogger(HibernateUtils.class);

	public static void initialize(User proxy) {
		// log.info("initialize(User): Start");

		if (proxy == null) {
			return;
		}

		if (!Hibernate.isInitialized(proxy)) {
			Hibernate.initialize(proxy);
		}

		if (!Utils.isEmpty(proxy.getRoleCollection())) {
			for (Role thisRole : proxy.getRoleCollection()) {
				Hibernate.initialize(thisRole); // iterative
			}
		}
	}
}
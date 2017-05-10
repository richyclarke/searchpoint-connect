package com.vesey.connect.rest;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jboss.logging.Logger;


@RequestScoped
@Path("app")
public class TestService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	Logger log;

    @GET
    @Path("test")
    @Produces({ "application/json" })
    public Response getCounts(@Context SecurityContext sc) {

    	if (sc.isUserInRole("PreferredCustomer")) {
    		return Response.status(Response.Status.FORBIDDEN).entity("Forbidden").build();
        } 
    	
    	String retVal = "Hello World";
        Response x = Response.ok(retVal).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
        return x;
        
        
    }
}

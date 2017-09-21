package com.jspeedbox.services.locator.context;

import com.jspeedbox.web.servlet.controller.rest.facade.UserServiceFacade;

public class InitialContext {
	
	public static final String CONTEXT_USER_SERVICE = "USER_SERVICE";
	
	public Object lookup(String jndiName) {

		if(jndiName.equalsIgnoreCase(CONTEXT_USER_SERVICE)){
			UserServiceFacade facade = new UserServiceFacade();
			return facade;
		}
		return null;
	}
	
}

package com.jspeedbox.web.servlet.controller.rest.facade;

import java.util.Properties;

import com.jspeedbox.rest.user.UserProfile;
import com.jspeedbox.security.domain.user.UserForAuthentication;
import com.jspeedbox.services.locator.ServiceLocator;
import com.jspeedbox.services.locator.context.InitialContext;

public class UserServiceFacade {
	
	public UserServiceFacade(){
		
	}
	
	public UserProfile validateUser(UserForAuthentication securedUser){
		UserProfile userProfile = new UserProfile();
		userProfile.setAuthenticated(true);
		
		Properties props = new Properties();
		props.setProperty("delete", "true");
		props.setProperty("update", "true");
				
		userProfile.setCredentials(props);
		
		return userProfile;
	}
	
	public static UserServiceFacade getInstance(){
		Object instance = ServiceLocator.getService(InitialContext.CONTEXT_USER_SERVICE);
		if(instance instanceof UserServiceFacade){
			return (UserServiceFacade)instance;
		}
		return null;
	}

}

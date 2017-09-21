package com.jspeedbox.rest.user;

import java.util.Properties;

public class UserProfile {
	
	private boolean isAuthenticated = false;
	
	private String username = null;
	
	private Properties credentials = null;
	
	public UserProfile(){
		
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
	
	public Properties getCredentials() {
		return credentials;
	}

	public void setCredentials(Properties credentials) {
		this.credentials = credentials;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

}

package com.jspeedbox.security.domain.user;

import java.util.Date;

import com.jspeedbox.security.jass.utils.credential.UserCredential;


public class AuthenticatedUser {
	
	private String username = null;
	private String role	= null;
	
	private Date lastlogonTime = null;
	
	private UserCredential userCredentials = null;
	
	public AuthenticatedUser(String username, Date lastlogonTime, UserCredential userCredentials){
		this.username = username;
		this.lastlogonTime = lastlogonTime;
		this.userCredentials = userCredentials;
	}

	public String getUsername() {
		return username;
	}

	public Date getLastlogonTime() {
		return lastlogonTime;
	}

	public void setLastlogonTime(Date lastlogonTime) {
		this.lastlogonTime = lastlogonTime;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	

}

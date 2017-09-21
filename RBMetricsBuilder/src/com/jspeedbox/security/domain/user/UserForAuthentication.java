package com.jspeedbox.security.domain.user;

public class UserForAuthentication {
	
	private final String userName;
	private final String password;
	
	public UserForAuthentication(final String userName, final String password){
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

}

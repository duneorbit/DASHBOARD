package com.jspeedbox.security.jass.utils.principal;

import java.security.Principal;
import java.util.Date;

import com.jspeedbox.security.domain.user.AuthenticatedUser;

public class UserPrincipal implements Principal, java.io.Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6783625715549585851L;
	/**
	 * 
	 */
	private AuthenticatedUser user = null;
	
	public UserPrincipal(AuthenticatedUser user) {
		this.user = user;
	}

	@Override
	public String getName() {
		return this.user.getUsername();
	}

	public Date getLoginTime() {
		return this.user.getLastlogonTime();
	}

	public String getRole() {
		return this.user.getRole();
	}

	public void setRole(String role) {
		this.user.setRole(role);
	}
	
}

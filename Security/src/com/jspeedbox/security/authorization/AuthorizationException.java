package com.jspeedbox.security.authorization;

public class AuthorizationException extends Exception{
	
	public static final String NO_MAPPING_FOUND = "No Request Mapping found for specified resource";
	public static final String USER_PRINCIPAL_EMPTY = "User Principle is Empty";
	
	private static final long serialVersionUID = -5139410224868736602L;

	public AuthorizationException(String msg){
		super(msg);
	}

}

package com.jspeedbox.web.servlet.controller.validator;

public class ValidationException extends RuntimeException{
	
	public static final String MSG_NO_DASHBOARD_ROOT = "Dashboard %s has not been built";
	
	private static final long serialVersionUID = -7511534858130927139L;

	public ValidationException(String message){
		super(message);
	}

}

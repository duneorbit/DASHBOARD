package com.jspeedbox.utils.logging;

public class LoggingUtils {
	
	public static final String STATIC_INITIALIZER = "STATIC INITIALIZER";
	public static final String CONSTRUCTOR = "CONSTRUCTOR";
	
	public static String buildParamsPlaceHolders(String ... params){
		StringBuffer paramsPlaceHolder = new StringBuffer();
		for(String param : params){
			paramsPlaceHolder.append(param).append("[{}]").append(" ");
		}
		return paramsPlaceHolder.toString();
	}

}

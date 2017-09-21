package com.jspeedbox.utils;

public class StringUtils {
	
	public static boolean notNull(String in){
		return in!=null;
	}
	public static boolean notEmpty(String in){
		if(notNull(in)){
			return !in.trim().equals("");
		}
		return false;
	}

}

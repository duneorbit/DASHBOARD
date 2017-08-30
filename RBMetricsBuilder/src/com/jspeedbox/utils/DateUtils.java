package com.jspeedbox.utils;

import java.util.Date;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
	
	private static DateTimeFormatter forwardSlashDateformatter = DateTimeFormat.forPattern("dd/MM/yy");
	private static DateTimeFormatter hiphenDateformatter = DateTimeFormat.forPattern("dd-MM-yy");
	private static DateTimeFormatter timestampDateformatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
	
	public static Date formatForwardSlashDate(String date){
		return forwardSlashDateformatter.parseDateTime(date).toDate();
	}
	
	public static Date formatHiphenDate(String date){
		return hiphenDateformatter.parseDateTime(date).toDate();
	}
	
	public static Date formatTimestampSlashDate(String date){
		return timestampDateformatter.parseDateTime(date).toDate();
	}

}

package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import java.util.Date;

import com.jspeedbox.utils.DateUtils;

public class DateBase {
	
	private Date parsedStartDate = null;
	
	protected void formatStartDate(String date){
		parsedStartDate = DateUtils.formatHiphenDate(date);
	}
	
	public Date getParsedStartDate(){
		return parsedStartDate;
	}
}

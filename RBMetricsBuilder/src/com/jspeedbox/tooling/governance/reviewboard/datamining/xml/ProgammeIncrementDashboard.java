package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jspeedbox.tooling.governance.dashboard.xml.ReviewTimeLine;
import com.jspeedbox.utils.IOUtils;

@XmlRootElement
public class ProgammeIncrementDashboard {
	
	@JsonIgnoreProperties
	private String pistartStr = null;
	@JsonIgnoreProperties
	private String piendStr = null;
	@JsonIgnoreProperties
	private String piName = null;
	
	private List<ProgrammeIncrement> programmeIncrements = new ArrayList<ProgrammeIncrement>();
	
	public ProgammeIncrementDashboard(){
		
	}

	@XmlElement
	public List<ProgrammeIncrement> getProgrammeIncrements() {
		return programmeIncrements;
	}

	public void setProgrammeIncrements(List<ProgrammeIncrement> programmeIncrements) {
		this.programmeIncrements = programmeIncrements;
	}
	
	@JsonIgnoreProperties
	public String getPINameFromTimestamp(String timestamp) throws ParseException{
		Date reviewDate = IOUtils.getPI_SOURCE_FORMAT().parseDateTime(timestamp).toDate();
		String t = IOUtils.getDATE_TO_STRING_FORMATTER().format(reviewDate);
		
		for(ProgrammeIncrement programmeIncrement : programmeIncrements){
			Date piStart = IOUtils.getDAY_MONTH_YEAR_FORMATTER().parse(programmeIncrement.getStartDate());
			Date piEnd = IOUtils.getDAY_MONTH_YEAR_FORMATTER().parse(programmeIncrement.getEndDate());
			pistartStr = IOUtils.getDATE_TO_STRING_FORMATTER().format(piStart);
			piendStr = IOUtils.getDATE_TO_STRING_FORMATTER().format(piEnd);
			if((reviewDate.compareTo(piStart)> 0) && (reviewDate.compareTo(piEnd) < 0)){
				piName = programmeIncrement.getName();
				break;
			}else if(pistartStr.equals(t)){
				piName = programmeIncrement.getName();
				break;
			}else if(piendStr.equals(t)){
				piName = programmeIncrement.getName();
				break;
			}
		}
		return piName;
	}

	@JsonIgnoreProperties
	public String getPistartStr() {
		return pistartStr;
	}

	@JsonIgnoreProperties
	public String getPiendStr() {
		return piendStr;
	}

	@JsonIgnoreProperties
	public String getPiName() {
		return piName;
	}
	
	private void initStartDates(){
		for(ProgrammeIncrement pi : getProgrammeIncrements()){
			pi.formatStartDate(pi.getStartDate());
			for(Sprint sprint : pi.getSprints()){
				sprint.formatStartDate(sprint.getStartDate());
			}
		}
	}
	
	public void sort(){
		initStartDates();
		Collections.sort(programmeIncrements, new Comparator<ProgrammeIncrement>(){
			public int compare(ProgrammeIncrement first, ProgrammeIncrement second){
				return first.getParsedStartDate().compareTo(second.getParsedStartDate());
			}
		});
		for(ProgrammeIncrement pi : programmeIncrements){
			pi.sort();
		}
	}

}

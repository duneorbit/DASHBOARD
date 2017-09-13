package com.jspeedbox.tooling.governance.dashboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.tooling.governance.reviewboard.User;
import com.jspeedbox.utils.logging.LoggingUtils;

public class SprintSummaryDashBoard {
	
	private String spintName = null;
	
	private Map<String, Totals> developers = new HashMap<String, Totals>();
	
	private Map<String, DeveloperHistory> developersHistory = new HashMap<String, DeveloperHistory>();
	
	DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private Date start = null;
	private Date end = null;
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(SprintSummaryDashBoard.class);
	
	public SprintSummaryDashBoard(String spirntName, String instart, String inend) throws Exception{
		this.spintName = spirntName;
		for(User user : ReviewBoardHelper.getInstance().getTeamDashboard().getUsers()){
			developers.put(user.getUsername(), new Totals(user.getUsername()));
		}
		
		try{
			start = sourceFormat.parse(instart);
			end = sourceFormat.parse(inend);
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", LoggingUtils.CONSTRUCTOR, e);
			throw e;
		}
	}
	
	public boolean isSprint(Date current){
		String s = null;
		String e = null;
		String c = null;
		if((current.compareTo(start)> 0) && (current.compareTo(end) < 0)){
			return true;
		}else{
			s = sourceFormat.format(start);
			e = sourceFormat.format(end);
			c = sourceFormat.format(current);
			if(s.equals(c)){
				return true;
			}else if(e.equals(c)){
				return true;
			}
		}
		return false;
	}
	
	public void updateCommentCount(String developer){
		if(developers.get(developer)==null){
			developers.put(developer, new Totals());
		}
		developers.get(developer).incTotal();
	}
	
	public Map<String, Totals> getDevelopers(){
		return developers;
	}
	
	public String getSpirntName(){
		return spintName;
	}
	
	public DeveloperHistory getDevelopersHistory(String key){
		if(developersHistory.get(key)==null){
			developersHistory.put(key, new DeveloperHistory());
		}
		return developersHistory.get(key);
	}
	
	public Map<String, DeveloperHistory> getDevelopersHistory(){
		return developersHistory;
	}

}

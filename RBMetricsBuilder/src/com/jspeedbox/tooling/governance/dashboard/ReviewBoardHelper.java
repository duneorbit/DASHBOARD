package com.jspeedbox.tooling.governance.dashboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ProgammeIncrementDashboard;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ProgrammeIncrement;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.Sprint;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.TeamDashboard;

public class ReviewBoardHelper {
	
	private static List<String> cosmeticCommentsLookup = new ArrayList<String>();
	private static List<String> cosmeticCommentsLookupRaw = new ArrayList<String>();
	
	private static List<String> performanceCommentsLookup = new ArrayList<String>();
	private static List<String> performanceCommentsLookupRaw = new ArrayList<String>();
	
	private static TeamDashboard teamDashboard = null;
	private static ProgammeIncrementDashboard piDashboard = null;
	
	private static ReviewBoardHelper helper = null;
	
	private ReviewBoardHelper(){
		BufferedReader perfReader = null;
		BufferedReader cosmeticReader = null;
		try {
			perfReader = new BufferedReader(new FileReader(new File("C:\\Users\\v_mccoskerj\\workspace\\RBMetricsBuilder\\resources\\performance-comments.txt")));
			cosmeticReader = new BufferedReader(new FileReader(new File("C:\\Users\\v_mccoskerj\\workspace\\RBMetricsBuilder\\resources\\cosmetic-metrics.txt")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			String line = perfReader.readLine();
			while(line!=null){
				performanceCommentsLookup.add(line.replaceAll("~", ""));
				performanceCommentsLookupRaw.add(line);
				line = perfReader.readLine();
			}
			String line2 = cosmeticReader.readLine();
			while(line2!=null){
				cosmeticCommentsLookup.add(line2.replaceAll("~", ""));
				cosmeticCommentsLookupRaw.add(line2);
				line2 = cosmeticReader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				perfReader.close();
				cosmeticReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static synchronized ReviewBoardHelper getInstance(){
		if(helper==null){
			helper = new ReviewBoardHelper();
		}
		return helper;
	}
	
	public List<String> getCosmeticCommentsLookup(){
		return cosmeticCommentsLookup;
	}
	
	public List<String> getPerformanceCommentsLookup(){
		return performanceCommentsLookup;
	}
	
	public List<String> getCosmeticCommentsLookupRaw(){
		return cosmeticCommentsLookupRaw;
	}
	
	public List<String> getPerformanceCommentsLookupRaw(){
		return performanceCommentsLookupRaw;
	}
	
	public Map<String,Map<String,SprintSummaryDashBoard>> buildAllSprintDashboards() throws Exception{
		Map<String,Map<String,SprintSummaryDashBoard>> sprintDashBoards = new HashMap<String,Map<String,SprintSummaryDashBoard>>();
		
		for(ProgrammeIncrement pi : getPiDashboard().getProgrammeIncrements()){
			Map<String,SprintSummaryDashBoard> spDashboard = new HashMap<String,SprintSummaryDashBoard>();
			int index = 1;
			for(Sprint sprint : pi.getSprints()){
				String key = "sp"+index;
				spDashboard.put(key, new SprintSummaryDashBoard(sprint.getName(), sprint.getStartDate(), sprint.getEndDate()));
				index = index + 1;
			}
			sprintDashBoards.put(pi.getName(), spDashboard);
		}
		return sprintDashBoards;
	}
	
	public void setPiDashboard(ProgammeIncrementDashboard piDashboard){
		this.piDashboard = piDashboard;
	}
	
	public ProgammeIncrementDashboard getPiDashboard(){
		return piDashboard;
	}
	
	public void setTeamDashboard(TeamDashboard teamDashboard){
		this.teamDashboard = teamDashboard;
	}
	
	public TeamDashboard getTeamDashboard(){
		return teamDashboard;
	}
}

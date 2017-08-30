package com.jspeedbox.tooling.governance.dashboard;

import java.util.HashMap;
import java.util.Map;

public abstract class PIDashBoardBase {
	
	public static Map<String,Map<String,SprintSummaryDashBoard>> allSprintDashboards = new HashMap<String,Map<String,SprintSummaryDashBoard>>();
	
	public Map<String,SprintSummaryDashBoard> piSprintDashboard = null;
	
	static{
		try {
			allSprintDashboards = ReviewBoardHelper.getInstance().buildAllSprintDashboards();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void initSprintSummaryDashBoards(String piName){
		piSprintDashboard = allSprintDashboards.get(piName);
	}
	
	public Map<String,SprintSummaryDashBoard> getPiSprintDashboard(){
		return piSprintDashboard;
	}

}

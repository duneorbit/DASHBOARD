package com.jspeedbox.tooling.governance.dashboard;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.utils.logging.LoggingUtils;

public abstract class PIDashBoardBase {
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(PIDashBoardBase.class);
	
	public static Map<String,Map<String,SprintSummaryDashBoard>> allSprintDashboards = new HashMap<String,Map<String,SprintSummaryDashBoard>>();
	
	public Map<String,SprintSummaryDashBoard> piSprintDashboard = null;
	
	static{
		try {
			allSprintDashboards = ReviewBoardHelper.getInstance().buildAllSprintDashboards();
		} catch (Exception e) {
			LOGGER_.error("Method[{}] Exception[{}] ", LoggingUtils.STATIC_INITIALIZER, e);
		}
	}
	
	protected void initSprintSummaryDashBoards(String piName){
		piSprintDashboard = allSprintDashboards.get(piName);
	}
	
	public Map<String,SprintSummaryDashBoard> getPiSprintDashboard(){
		return piSprintDashboard;
	}

}

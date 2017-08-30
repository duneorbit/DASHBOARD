package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TeamDashboards {
	
	private List<TeamDashboard> dashboards = new ArrayList<TeamDashboard>();
	
	public TeamDashboards(){
		
	}

	@XmlElement
	public List<TeamDashboard> getDashboards() {
		return dashboards;
	}

	public void setDashboards(List<TeamDashboard> dashboards) {
		this.dashboards = dashboards;
	}

}

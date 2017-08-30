package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.jspeedbox.tooling.governance.reviewboard.User;

@XmlRootElement
public class TeamDashboard {
	
	private String msg = "";
	
	private boolean success = false;
	
	private String dashboardName = null;
	
	private List<User> users = null;
	
	public TeamDashboard(){
		
	}

	@XmlElement
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@XmlAttribute
	public String getDashboardName() {
		return dashboardName;
	}

	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}

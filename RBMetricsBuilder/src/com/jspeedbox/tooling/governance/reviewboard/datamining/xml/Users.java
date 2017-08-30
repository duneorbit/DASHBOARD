package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.jspeedbox.tooling.governance.reviewboard.User;

@XmlRootElement
public class Users {
	
	private List<User> users = new ArrayList<User>();
	
	public Users(){
		
	}

	@XmlElement
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}

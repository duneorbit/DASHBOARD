package com.jspeedbox.web.servlet;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;

import com.jspeedbox.tooling.governance.reviewboard.User;

public class Config {
	
	private static boolean configRead = false;
	
	private static Map<String, String> initParams = new HashMap<String, String>();
	private static List<User> userList = new ArrayList<User>();
	
	private static Config INSTANCE = null;
	
	private Config(){
		
	}
	
	public static synchronized Config getInstance(){
		if(INSTANCE==null){
			INSTANCE = new Config();
		}
		return INSTANCE;
	}
	
	public boolean isConfigRead(){
		return configRead;
	}
	
	public void readConfig(ServletConfig config){
		Enumeration<String> enumerations = config.getInitParameterNames();
		while(enumerations.hasMoreElements()){
			String key = enumerations.nextElement();
			initParams.put(key, config.getInitParameter(key));
		}
		System.out.println(">>>>>>>>>>> " + initParams);
	}
	
	public Map<String, String> getInitParams(){
		return initParams;
	}
	
	public String getParam(String key){
		return initParams.get(key);
	}
	
	public void addAllUsers(List<User> userList){
		this.userList.addAll(userList);
	}
	
	public List<User> getUserList(){
		return userList;
	}

}

package com.jspeedbox.web.servlet;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.tooling.governance.reviewboard.User;
import com.jspeedbox.utils.logging.LoggingUtils;

public class Config {
	
	private static String contextPath = null;
	private static String serverName = null;
	private static String port = null;
	private static String scheme = null;

	private static boolean configRead = false;
	
	private static Map<String, String> initParams = new HashMap<String, String>();
	private static List<User> userList = new ArrayList<User>();
	
	private static Config INSTANCE = null;
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(Config.class);
	
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
		LOGGER_.debug(LoggingUtils.buildParamsPlaceHolders("method", "initParams"), "readConfig", initParams);
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
	
	private static String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		if(this.contextPath==null){
			this.contextPath = contextPath;
		}
	}

	private static String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		if(this.serverName==null){
			this.serverName = serverName;
		}
	}
	
	private static String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		if(this.scheme ==null){
			this.scheme = scheme;
		}
	}

	public static String getPort() {
		return port;
	}

	public void setPort(String port) {
		if(this.port==null){
			this.port = port;
		}
	}
	
	public static String getRestUrlPrefix(){
		StringBuffer url = new StringBuffer();
		url.append(getScheme()).append("://").append(getServerName()).append(":").append(getPort()).append(getContextPath());
		return url.toString();
	}

}

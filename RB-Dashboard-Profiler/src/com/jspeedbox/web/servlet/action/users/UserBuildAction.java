package com.jspeedbox.web.servlet.action.users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspeedbox.tooling.governance.reviewboard.datamining.ReviewBoardDataMineService;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.Users;
import com.jspeedbox.utils.IOUtils;
import com.jspeedbox.utils.XMLUtils;
import com.jspeedbox.web.servlet.Config;
import com.jspeedbox.web.servlet.action.IAction;
import com.jspeedbox.web.servlet.controller.validator.ConfigValidator;

public class UserBuildAction implements IAction{
	
	public UserBuildAction(){
		
	}

	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		ReviewBoardDataMineService rbService = new ReviewBoardDataMineService();
		Users users = rbService.mineUsers();
		if(!IOUtils.rbUsersExists()){
			if(!IOUtils.makeRbUsers()){
				throw IOUtils.ioException("Cannot create directory["+IOUtils.getRbUsersPath()+"]");
			}
		}
		
		if(!IOUtils.getRbUsersXML().exists()){
			if(!IOUtils.makeRbUsersXML()){
				throw IOUtils.ioException("Could not create file["+IOUtils.getRbUsersXML().getAbsolutePath()+"]");
			}
		}
		
		try{
			XMLUtils.saveXMLDocument(users, Users.class, IOUtils.getRbUsersXML());
		}catch(Exception e){
			throw new IOException(e.getMessage());
		}
		
		Config.getInstance().addAllUsers(users.getUsers());
		
		request.getRequestDispatcher("./profiler.jsp").forward(request, response);
	}

}

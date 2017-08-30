package com.jspeedbox.web.servlet.controller.validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspeedbox.utils.IOUtils;
import com.jspeedbox.utils.XMLUtils;
import com.jspeedbox.web.servlet.Config;
import com.jspeedbox.web.servlet.action.IAction;

public class ConfigValidator {
	
	public static void validateConfigs(HttpServletRequest request, HttpServletResponse response) throws ValidationException{
		if(!IOUtils.getRbUsersXML().exists()){
			request.setAttribute(IAction.REQUEST_ATTRIBUTE_USERS_XML, new Boolean(false));
		}else{
			if(Config.getInstance().getUserList().size()==0){
				Config.getInstance().addAllUsers(XMLUtils.unmarshallUsers().getUsers());
				if(Config.getInstance().getUserList().size()==0){
					throw new ValidationException("There are no users available in config file["+IOUtils.getRbUsersXML().getAbsolutePath()+"]");
				}
			}
		}

		if(!IOUtils.getDashBoardsSummaryXml().exists()){
			request.setAttribute(IAction.REQUEST_ATTRIBUTE_TEAMS_DASHBOARD_XML, new Boolean(false));
		}
		
		if(!IOUtils.getPISprintDatesConigXML().exists()){
			request.setAttribute(IAction.REQUEST_ATTRIBUTE_PI_SPRINT_DATES, new Boolean(false));
		}
		
		if(request.getParameter(IAction.PARAM_ACTION)!=null){
			if(request.getParameter(IAction.PARAM_ACTION).indexOf(IAction.JSP_NAME_VIEW_DAHSBOARD)!=0){
				String dashboard = request.getParameter(IAction.PARAM_DASHBOARD);
				try{
					IOUtils.getDashBoardRoot(dashboard);
					request.setAttribute(IAction.REQUEST_ATTRIBUTE_NO_DASHBOARD_ROOT, new Boolean(true));
				}catch(Exception e){	
					request.setAttribute(IAction.REQUEST_ATTRIBUTE_NO_DASHBOARD_ROOT, new Boolean(false));
				}
			}
		}
	}
	
}

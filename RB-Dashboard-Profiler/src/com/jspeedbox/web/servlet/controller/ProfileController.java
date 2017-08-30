package com.jspeedbox.web.servlet.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.jspeedbox.utils.schedule.ScheduleManager;
import com.jspeedbox.utils.schedule.ScheduledJob;
import com.jspeedbox.web.servlet.Config;
import com.jspeedbox.web.servlet.action.ActionClassLoader;
import com.jspeedbox.web.servlet.action.IAction;
import com.jspeedbox.web.servlet.controller.validator.ConfigValidator;

public class ProfileController extends HttpServlet{

	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			Config.getInstance().readConfig(this.getServletConfig());
			ScheduleManager.getInstance();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		try {
			ScheduleManager.shutdown();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 141746434506591440L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!Config.getInstance().isConfigRead()){
			Config.getInstance().readConfig(this.getServletConfig());
		}
		
		if(request.getParameter(IAction.PARAM_ACTION)!=null){
			if(request.getParameter(IAction.PARAM_ACTION).indexOf(IAction.JSP)!=0){
				ConfigValidator.validateConfigs(request, response);
				request.getRequestDispatcher(request.getParameter(IAction.PARAM_ACTION)).forward(request, response);
			}else{
				StringBuffer className = new StringBuffer();
				className.append(IAction.ACTION_BASE).append(request.getParameter(IAction.PARAM_ACTION));
				System.out.println("calling action["+className.toString()+"]");
				ActionClassLoader classLoader = new ActionClassLoader();
				classLoader.invoke(className.toString(), request, response);
			}
		}else{
			request.getRequestDispatcher(IAction.JSP_PROFILER).forward(request, response);
		}
	}

}

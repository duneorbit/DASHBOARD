package com.jspeedbox.web.servlet.action;

import java.lang.reflect.Constructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionClassLoader {
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(ActionClassLoader.class);
	
	public ActionClassLoader(){
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void invoke(String className, HttpServletRequest request, HttpServletResponse response){
		ClassLoader loader = this.getClass().getClassLoader();
		try{
			Class clazz = loader.loadClass(className);
			Constructor constructor = clazz.getConstructor();
			IAction action = (IAction)constructor.newInstance();
			action.process(request, response);
		}catch(Exception e){
			//LOGGER_.error(arg0, arg1);
			e.printStackTrace();
		}
	}

}

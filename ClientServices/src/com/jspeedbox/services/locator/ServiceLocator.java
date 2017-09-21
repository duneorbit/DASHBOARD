package com.jspeedbox.services.locator;


import com.jspeedbox.cache.Cache;
import com.jspeedbox.services.locator.context.InitialContext;

public class ServiceLocator {
	
	private static Cache cache;
	
	static {
		cache = new Cache();
	}
	
	public static Object getService(String jndiName) {

		Object service = cache.getService(jndiName);

		if (service != null) {
			return service;
		}

		InitialContext context = new InitialContext();
		service = context.lookup(jndiName);
		
		if(service!=null){
			synchronized(cache){
				cache.addService(jndiName, service);
			}
			return service;
		}
		
		return null;
	}


}

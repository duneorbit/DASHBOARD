package com.jspeedbox.cache;

import java.util.Hashtable;
import java.util.Map;

public class Cache {
	
	private Map<String, Object> services;

	   public Cache(){
	      services = new Hashtable<String, Object>();
	   }
	   
	   public Object getService(String serviceName){
		   if(services.containsKey(serviceName)){
			   return services.get(serviceName);
		   }
		   return null;
	   }

	   public void addService(String key, Object newService){
		  synchronized(services){
		      if(!services.containsKey(key)){
		    	  services.put(key, newService);
		      }
		  }
	   }
}

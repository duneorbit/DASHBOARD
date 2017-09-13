package com.jspeedbox.transaction;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.utils.logging.LoggingUtils;

public class TransactionState {
	
	private static TransactionState TRANSACTION_STATE = null;
	
	private ThreadLocal<Map<String, Map<String, Object>>> threadHolder = new ThreadLocal<Map<String, Map<String, Object>>>();
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(TransactionState.class);
	
	private TransactionState(){
		
	}
	
	protected static synchronized TransactionState getInstance(){
		if(TRANSACTION_STATE==null){
			TRANSACTION_STATE = new TransactionState();
		}
		return TRANSACTION_STATE;
	}
	
	protected void applyThreadLocal(String threadKey, String objectKey, Object type, Object clonedType){
		synchronized(threadHolder){
			if(threadHolder.get()==null){
				threadHolder.set(new HashMap<String, Map<String, Object>>());
			}
			if(threadHolder.get().get(threadKey)==null){
				LOGGER_.debug(LoggingUtils.buildParamsPlaceHolders("method", "threadkey", "not found creating new one"), threadKey);
				
				Map<String, Object> objectMap = new Hashtable<String, Object>();
				threadHolder.get().put(threadKey, objectMap);
			}
			threadHolder.get().get(threadKey).put(objectKey, type);
			if(clonedType!=null){
				StringBuffer cloneKey = new StringBuffer();
				cloneKey.append(Transaction.KEY_CLONED_PREFIX).append(objectKey);
				threadHolder.get().get(threadKey).put(cloneKey.toString(), clonedType);
			}
		}
	}
	
	protected Object get(String threadKey, String objectKey) throws TransactionStateException{
		try{
			synchronized(threadHolder){
				return threadHolder.get().get(threadKey).get(objectKey);
			}
		}catch(Exception e){
			throw new TransactionStateException(e);
		}
	}
	
	public void removeThreadKey(String threadKey){
		synchronized(threadHolder){
			threadHolder.get().remove(threadKey);
		}
	}
	
	public void destroy(){
		synchronized(threadHolder){
			threadHolder.remove();
		}
	}

}

package com.jspeedbox.transaction.management;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.transaction.Transaction;
import com.jspeedbox.transaction.TransactionException;
import com.jspeedbox.transaction.TransactionParticipant;
import com.jspeedbox.transaction.TransactionState;
import com.jspeedbox.transaction.TransactionStateException;
import com.jspeedbox.utils.logging.LoggingUtils;

public class TransactionManager {
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(TransactionManager.class);
	
	private static LinkedList<TransactionParticipant> transactions = new LinkedList<TransactionParticipant>();
	
	private static Long transactionID = 0L;
	
	public static void addTransactionParticipant(TransactionParticipant participant){
		transactions.add(participant);
	}
	
	public static boolean doTransaction(){
		
		generateTransactionID();
		
		for(TransactionParticipant transaction : transactions){
			if(!transaction.join(transactionID)){
				return false;
			}
			transaction.getTransaction().setTransactionID(transactionID);
		}
		
		try{
			commitAll();
			LOGGER_.debug("Successfully completed all transactions["+transactions.size()+"]");
			return true;
		}catch(TransactionException e){
		}
		
		try{
			rollbackAll();
		}catch(TransactionStateException e){
			
		}
		
		return false;
		
	}
	
	private static void commitAll() throws TransactionException{
		for(TransactionParticipant transaction : transactions){
			transaction.commit(transactionID);
		}
	}
	
	private static void rollbackAll() throws TransactionStateException{
		LOGGER_.error("Transaction Failed Rolling back");
		for(TransactionParticipant transaction : transactions){
			transaction.rollback(transactionID);
		}
	}
	
	private static synchronized void generateTransactionID(){
		transactionID = 20000L;
	}
	
	public static void clear(){
		transactions.clear();
		transactions = new LinkedList<TransactionParticipant>();
		Transaction.Factory.destroySessionState();
	}
	
	public static void applySessionState(String key, Object objectState, Object clonedObjectState){
		Transaction.Factory.applySessionState(Transaction.Factory.getThreadKey(), key, objectState, clonedObjectState);
	}
	
	public static Object getSessionState(){
		return null;
	}
}

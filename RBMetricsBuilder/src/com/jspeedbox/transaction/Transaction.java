package com.jspeedbox.transaction;

public interface Transaction {
	
	public final long TRANSACTION_COMPLETE_IDENTIFIER = -1;
	
	public static String KEY_ADDITIONAL_SCHEDULE = "ADIITONAL_SCHEDULE_";
	public static String KEY_CLONED_PREFIX = "CLONED_";
	
	public void commit(long transactionID) throws TransactionException;
	public void rollback(long transactionID) throws TransactionStateException;
	public void setTransactionID(long transactionID);
	
	public static final class Factory{
		
		@SuppressWarnings("unchecked")
		public static void applySessionState(String threadKey, String objectKey, Object objectState, Object clonedObjectState){
			TransactionState.getInstance().applyThreadLocal(threadKey, objectKey, objectState, clonedObjectState);
		}
		
		public static void destroySessionState(){
			TransactionState.getInstance().removeThreadKey(Thread.currentThread().getName());
		}
		
		public static String getThreadKey(){
			return Thread.currentThread().getName();
		}
		
		public static Object getObjectForCommit(String threadKey, String objectKey) throws TransactionStateException{
			return TransactionState.getInstance().get(threadKey, objectKey);
		}
		
		public static Object getRollbackFromCloned(String threadKey, String objectKey)throws TransactionStateException{
			String clonedKey = Transaction.KEY_CLONED_PREFIX+objectKey;
			return TransactionState.getInstance().get(threadKey, clonedKey);
		}
		
		private Factory(){}
	}
	
}

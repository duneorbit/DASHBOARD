package com.jspeedbox.transaction;

public interface TransactionParticipant extends Transaction{
	
	public boolean join(long transactionID);
	public Transaction getTransaction();

}

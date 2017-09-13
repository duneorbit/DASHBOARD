package com.jspeedbox.transaction;

public class TransactionParticipantImpl implements TransactionParticipant{
	
	private long currentTransaction = 0L;
	
	private Transaction transaction = null;
	
	public TransactionParticipantImpl(Transaction transaction){
		this.transaction = transaction;
	}

	@Override
	public boolean join(long transactionID) {
		if (currentTransaction != 0) {
		      return false;
		    } else {
		      currentTransaction = transactionID;
		      return true;
		    }
	}

	@Override
	public void commit(long transactionID) throws TransactionException {
		transaction.commit(transactionID);
	}

	@Override
	public void rollback(long transactionID) throws TransactionStateException {
		transaction.rollback(transactionID);
	}

	@Override
	public void setTransactionID(long transactionID) {
		currentTransaction = transactionID;
		
	}

	@Override
	public Transaction getTransaction() {
		return transaction;
	}


}

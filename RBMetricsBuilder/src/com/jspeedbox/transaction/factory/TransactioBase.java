package com.jspeedbox.transaction.factory;

import com.jspeedbox.transaction.TransactionStateException;

public abstract class TransactioBase {
	
	protected abstract Object getCommitTransactionObject(String objectKey) throws TransactionStateException;
	protected abstract Object getRollbackTransactionObject(String objectKey) throws TransactionStateException;

}

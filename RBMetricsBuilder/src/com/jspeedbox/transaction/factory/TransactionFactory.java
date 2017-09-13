package com.jspeedbox.transaction.factory;

import com.jspeedbox.transaction.Transaction;
import com.jspeedbox.transaction.TransactionStateException;

public class TransactionFactory extends TransactioBase{

	@Override
	protected Object getCommitTransactionObject(String objectKey) throws TransactionStateException {
		return Transaction.Factory.getObjectForCommit(Transaction.Factory.getThreadKey(), objectKey);
	}

	@Override
	protected Object getRollbackTransactionObject(String objectKey) throws TransactionStateException {
		return Transaction.Factory.getRollbackFromCloned(Transaction.Factory.getThreadKey(), objectKey);
	}

}

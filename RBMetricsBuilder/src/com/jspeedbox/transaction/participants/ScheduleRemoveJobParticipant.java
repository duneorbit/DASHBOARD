package com.jspeedbox.transaction.participants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.AdditionalSchedule;
import com.jspeedbox.transaction.Transaction;
import com.jspeedbox.transaction.TransactionException;
import com.jspeedbox.utils.schedule.ScheduleManager;

public class ScheduleRemoveJobParticipant implements Transaction{
	
	private long transactionID = 0L;
	
	private AdditionalSchedule additionalSchedule = null;
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(ScheduleRemoveJobParticipant.class);
	
	public ScheduleRemoveJobParticipant(AdditionalSchedule additionalSchedule){
		this.additionalSchedule = additionalSchedule;
	}

	@Override
	public void commit(long transactionID) throws TransactionException {
		if(this.transactionID != transactionID){
			throw new TransactionException("Invalid Transaction");
		}
		try{
			ScheduleManager.removeJob(additionalSchedule);
			transactionID = Transaction.TRANSACTION_COMPLETE_IDENTIFIER;
			LOGGER_.debug("Completed Transaction["+this.getClass().getName()+"]");
		}catch(Exception e){
			throw new TransactionException(e);
		}
	}

	@Override
	public void rollback(long transactionID) {
		if(this.transactionID == Transaction.TRANSACTION_COMPLETE_IDENTIFIER) {
			this.transactionID = 0;
			
			try{
				ScheduleManager.addJob(additionalSchedule);
				LOGGER_.error("Rolledback Transaction["+this.getClass().getName()+"]");
			}catch(Exception e){
				LOGGER_.error("Failed to rollback Transaction["+this.getClass().getName()+"]");
			}
		}
	}

	@Override
	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}

}

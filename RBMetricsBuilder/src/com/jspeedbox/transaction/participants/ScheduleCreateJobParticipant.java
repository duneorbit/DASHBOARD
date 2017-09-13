package com.jspeedbox.transaction.participants;

import org.springframework.web.client.RestTemplate;

import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.AdditionalSchedule;
import com.jspeedbox.transaction.Transaction;
import com.jspeedbox.transaction.TransactionException;
import com.jspeedbox.web.servlet.Config;
import com.jspeedbox.web.servlet.controller.rest.utils.RestServiceConstants;

public class ScheduleCreateJobParticipant implements Transaction{
	
	private long transactionID = 0L;
	
	private AdditionalSchedule additionalSchedule = null;
	
	public ScheduleCreateJobParticipant(AdditionalSchedule additionalSchedule){
		this.additionalSchedule = additionalSchedule;
	}

	@Override
	public void commit(long transactionID) throws TransactionException {
		if(this.transactionID != transactionID){
			throw new TransactionException("Invalid Transaction");
		}
		RestTemplate restTemplate = new RestTemplate();
		StringBuffer url = new StringBuffer();
		url.append(Config.getInstance().getRestUrlPrefix()).append("/rest/").append(RestServiceConstants.CREATE_ADDITIONAL_SCHEDULE);
		System.out.println(Config.getInstance().getRestUrlPrefix());
		additionalSchedule = restTemplate.postForObject(
				url.toString(), 
				additionalSchedule, AdditionalSchedule.class);
		transactionID = Transaction.TRANSACTION_COMPLETE_IDENTIFIER;
	}

	@Override
	public void rollback(long transactionID) {
		if(this.transactionID == Transaction.TRANSACTION_COMPLETE_IDENTIFIER) {
			this.transactionID = 0;
			//via rest template delete schedule
		}
	}

	@Override
	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}

	

}

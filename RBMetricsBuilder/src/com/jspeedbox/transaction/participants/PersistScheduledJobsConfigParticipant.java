package com.jspeedbox.transaction.participants;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.AdditionalSchedule;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ScheduledJobConfig;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ScheduledJobsConfig;
import com.jspeedbox.transaction.Transaction;
import com.jspeedbox.transaction.TransactionException;
import com.jspeedbox.transaction.TransactionStateException;
import com.jspeedbox.transaction.factory.TransactionFactory;
import com.jspeedbox.utils.IOUtils;
import com.jspeedbox.utils.XMLUtils;

public class PersistScheduledJobsConfigParticipant extends TransactionFactory implements Transaction{
	
	private long transactionID = 0L;
	
	private Object xmlObject = null;
	
	private Class clazz = null;
	
	private File file = null;
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(PersistScheduledJobsConfigParticipant.class);
	
	public PersistScheduledJobsConfigParticipant(Object xmlObject, Class clazz, File file){
		this.xmlObject = xmlObject;
		this.clazz = clazz;
		this.file = file;
	}

	@Override
	public void commit(long transactionID) throws TransactionException {
		if(this.transactionID != transactionID){
			throw new TransactionException("Invalid Transaction");
		}
		try{
			AdditionalSchedule additionalSchedule = (AdditionalSchedule)getCommitTransactionObject(Transaction.KEY_ADDITIONAL_SCHEDULE);
			
			ScheduledJobsConfig jobsConfig = XMLUtils.getScheduledJobs();
			List<ScheduledJobConfig> jobList = jobsConfig.getJobs();
			Iterator<ScheduledJobConfig> safeItr = jobList.iterator();
			while(safeItr.hasNext()){
				ScheduledJobConfig currJob = safeItr.next();
				if(currJob.getAdditionalSchedule()!=null){
					if(currJob.getAdditionalSchedule().getScheduleName().equals(additionalSchedule.getScheduleName())){
						currJob.setAdditionalSchedule(null);
					}
				}
			}
		
			XMLUtils.saveXMLDocument(jobsConfig, ScheduledJobsConfig.class, IOUtils.getScheduledJobsConfigXML(false));
			transactionID = Transaction.TRANSACTION_COMPLETE_IDENTIFIER;
			LOGGER_.debug("Completed Transaction["+this.getClass().getName()+"]");
		}catch(Exception e){
			throw new TransactionException(e);
		}
	}

	@Override
	public void rollback(long transactionID) throws TransactionStateException {
		if(this.transactionID == Transaction.TRANSACTION_COMPLETE_IDENTIFIER) {
			this.transactionID = 0;
			
			AdditionalSchedule additionalSchedule = (AdditionalSchedule)getRollbackTransactionObject(Transaction.KEY_ADDITIONAL_SCHEDULE);
			
			ScheduledJobsConfig jobsConfig = XMLUtils.getScheduledJobs();
			List<ScheduledJobConfig> jobList = jobsConfig.getJobs();
			Iterator<ScheduledJobConfig> safeItr = jobList.iterator();
			while(safeItr.hasNext()){
				ScheduledJobConfig currJob = safeItr.next();
				if(currJob.getAdditionalSchedule().getScheduleName().equals(additionalSchedule.getScheduleName())){
					try{
						currJob.setAdditionalSchedule(
								(AdditionalSchedule)Transaction.Factory.getRollbackFromCloned(
										Transaction.Factory.getThreadKey(), Transaction.KEY_ADDITIONAL_SCHEDULE));
					}catch(Exception e){
						
					}
				}
			}
			try{
				XMLUtils.saveXMLDocument(jobsConfig, ScheduledJobsConfig.class, IOUtils.getScheduledJobsConfigXML(false));
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

package com.me.java.transaction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

interface AppointmentTransactionParticipant extends Remote {
	  public boolean join(long transactionID) throws RemoteException;

	  public void commit(long transactionID) throws TransactionException,
	      RemoteException;

	  public void cancel(long transactionID) throws RemoteException;

	  public boolean changeDate(long transactionID, Appointment appointment,
	      Date newStartDate) throws TransactionException, RemoteException;
	}

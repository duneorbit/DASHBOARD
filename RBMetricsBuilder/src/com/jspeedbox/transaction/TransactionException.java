package com.jspeedbox.transaction;

public class TransactionException extends Exception{

	
	private static final long serialVersionUID = -8198030015572298140L;
	
	public TransactionException(){
		super();
	}
	
	public TransactionException(String message){
		super(message);
	}
	
	public TransactionException(Throwable e){
		super(e);
	}

}

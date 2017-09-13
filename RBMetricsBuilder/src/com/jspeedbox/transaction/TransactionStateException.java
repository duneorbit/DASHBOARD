package com.jspeedbox.transaction;

public class TransactionStateException extends Exception {
	
	
	private static final long serialVersionUID = -2132087142314024228L;

	public TransactionStateException(Throwable e){
		super(e);
	}

}

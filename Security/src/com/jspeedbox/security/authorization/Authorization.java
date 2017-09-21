package com.jspeedbox.security.authorization;

import java.io.Serializable;

public abstract class Authorization<T, Id extends Serializable> {
	
	protected abstract String getMethodRequestMapping(Object object);
	protected abstract T getUserPrincipal();
	protected abstract boolean checkCredentials(String methodRequestMapping, Object principle);

}

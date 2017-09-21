package com.jspeedbox.security.jass;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.rest.user.UserProfile;
import com.jspeedbox.security.domain.user.AuthenticatedUser;
import com.jspeedbox.security.domain.user.UserForAuthentication;
import com.jspeedbox.security.jass.utils.credential.UserCredential;
import com.jspeedbox.security.jass.utils.principal.UserPrincipal;
import com.jspeedbox.security.utils.EncryptionUtils;
import com.jspeedbox.utils.logging.LoggingUtils;
import com.jspeedbox.web.servlet.controller.rest.facade.UserServiceFacade;

public class Login implements LoginModule {

	private String password = null;
	private String username = null;
	
	private Map<String, ?> state = null;
	private Map<String, ?> options = null;
	
	Callback[] callbacks = null;
	private Subject subject = null;
	
	@SuppressWarnings("rawtypes")
	private Vector credentialsMapper = new Vector();
	
	private CallbackHandler callbackHandler = null;
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(Login.class);
	
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> state, Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.state = state;
		
		Callback[] callbacks = new Callback[] {
            new NameCallback("Username: "),
            new PasswordCallback("Password: ", false)
        };
		
		this.callbacks = callbacks;
		
		try{
			this.callbackHandler.handle(callbacks);
		}catch(Exception e){
			LOGGER_.error(LoggingUtils.buildParamsPlaceHolders(
					"method", "failed to construct callbackHandler"), "initialize", e.getMessage());
			throw new RuntimeException(e);
		}

	}

	public boolean login() throws LoginException {
		
		this.username = ((NameCallback)this.callbacks[0]).getName();
		
		PasswordCallback pcallback = (PasswordCallback)this.callbacks[1]; 
		this.password = EncryptionUtils.sha1(new String(pcallback.getPassword()));
		
		((PasswordCallback)this.callbacks[1]).clearPassword();
		
		boolean success = validate(new UserForAuthentication(this.username, this.password));
		
		this.callbacks[0]=null;
		this.callbacks[1]=null;
		
		if(!success){
			throw new LoginException("Authentication Failed");
		}
		
		return true;
	}

	@Override
	public boolean abort() throws LoginException {
		logout();
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean commit() throws LoginException {
		this.subject.getPrivateCredentials().add(credentialsMapper);
		this.subject.getPublicCredentials().addAll(credentialsMapper);
		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		credentialsMapper.clear();
		((PasswordCallback)this.callbacks[1]).clearPassword();
		Iterator<UserPrincipal> pricipleItr = this.subject.getPrincipals(UserPrincipal.class).iterator();
		while(pricipleItr.hasNext()){
			UserPrincipal userPrinciple = pricipleItr.next();
			this.subject.getPrincipals().remove(userPrinciple);
		}
		Iterator<UserCredential> credentialItr = this.subject.getPrivateCredentials(UserCredential.class).iterator();
		while(credentialItr.hasNext()){
			UserCredential userCredential = credentialItr.next();
			this.subject.getPublicCredentials().remove(userCredential);
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private boolean validate(UserForAuthentication securedUser){
		UserServiceFacade userServiceFacade = UserServiceFacade.getInstance();
		if(userServiceFacade==null){
			LOGGER_.error(LoggingUtils.buildParamsPlaceHolders("method", "failed to create serviceFacade"), 
					"validate", UserServiceFacade.class.getName());
			
			return false;
		}
		
		//talk to ESB (Mongo DB and LADP orechestration building profile with roles and credentials)
		UserProfile userProfile = userServiceFacade.validateUser(securedUser);
		
		if(userProfile.isAuthenticated()){
			UserCredential userCredentials = new UserCredential();
			userCredentials.setProperty("delete", userProfile.getCredentials().getProperty("delete"));
			userCredentials.setProperty("update", userProfile.getCredentials().getProperty("update"));
			credentialsMapper.add(userCredentials);
			//TODO - switch out this.username to retrieving from profile
			this.subject.getPrivateCredentials().add(credentialsMapper);
			this.subject.getPublicCredentials().addAll(credentialsMapper);
			this.subject.getPrincipals().add(new UserPrincipal(new AuthenticatedUser(this.username, new Date(), userCredentials)));
		}
		return true;
	}

}

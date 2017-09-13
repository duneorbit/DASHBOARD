package it.springwebjaas;

import it.springwebjaas.principal.UserPrincipal;
import it.springwebjaas.security.EncodeDecode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class Login implements LoginModule {

	private String password;
	private String username;
	private Subject subject;

	public boolean login() throws LoginException {

		// Check the password against the username "admin" or "customer"
		if (username == null
				|| (!username.equals("admin") && !username.equals("customer"))) {
			throw new LoginException("User not valid");
		}

		// Check password valid
		String passwordDecrypted = EncodeDecode.Decrypt(password);
		String[] passwordSplitted = null;

		// Split at "_"
		if (passwordDecrypted != null)
			passwordSplitted = passwordDecrypted.split("_");
		else
			throw new LoginException("Password not valid");

		if (passwordSplitted.length == 2) {

			// Check the password and validity of that.
			if (passwordSplitted[0].equals(EncodeDecode.ADMIN_PASSWORD) && (isDateRangeValid(passwordSplitted[1]))) {			
				subject.getPrincipals().add(new UserPrincipal(username));
				return true;
			} else
				throw new LoginException("Password not valid");
		} else
			throw new LoginException("Password not valid");
	}

	private boolean isDateRangeValid(String passwordDate) throws LoginException {
		// Check the time validity
//		SimpleDateFormat dateFormat = new SimpleDateFormat(
//				"yyyy-MM-dd HH-mm-ss");
//
//		try {
//			Date convertedDate = dateFormat.parse(passwordDate);
//
//			long timePasswordDifference = (new java.util.Date().getTime() - convertedDate
//					.getTime()) / (60 * 1000) % 60;
//
//			if (timePasswordDifference < EncodeDecode.MINUTE_PASSWORD_EXPIRES)
//				return true;
//			else
//				return false;
		
		return true;

//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			throw new LoginException("User not valid");
//		}
	}

	@Override
	public boolean abort() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean commit() throws LoginException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> state, Map<String, ?> options) {
		this.subject = subject;

		try {
			NameCallback nameCallback = new NameCallback("prompt");
			PasswordCallback passwordCallback = new PasswordCallback("prompt", false);

			callbackHandler.handle(new Callback[] { nameCallback,
					passwordCallback });

			password = new String(passwordCallback.getPassword());
			username = nameCallback.getName();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

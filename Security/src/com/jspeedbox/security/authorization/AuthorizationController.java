package com.jspeedbox.security.authorization;

import org.springframework.security.authentication.jaas.JaasGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspeedbox.security.jass.utils.principal.UserPrincipal;

public class AuthorizationController extends Authorization{
	
	private AuthorizationController(){
		
	}
	
	public static boolean doPrivileged(Object object) throws AuthorizationException{
		Authorization authorization = new AuthorizationController();
		String methodRequestMapping = authorization.getMethodRequestMapping(object);
		if(methodRequestMapping==null){
			throw new AuthorizationException(AuthorizationException.NO_MAPPING_FOUND);
		}
		UserPrincipal userPrincipal = (UserPrincipal)authorization.getUserPrincipal();
		if(userPrincipal==null){
			throw new AuthorizationException(AuthorizationException.USER_PRINCIPAL_EMPTY);
		}
		return authorization.checkCredentials(methodRequestMapping, userPrincipal);
	}

	@Override
	protected String getMethodRequestMapping(Object object) {
		if(object!=null){
			RequestMapping mapping = object.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class);
			if(mapping!=null){
				return mapping.value()[0];
			}
		}
		return null;
	}

	@Override
	protected UserPrincipal getUserPrincipal() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JaasGrantedAuthority jaasGrantedAuthority = (JaasGrantedAuthority)(auth.getAuthorities().toArray()[0]);
		
		UserPrincipal userPrincipal = (UserPrincipal)jaasGrantedAuthority.getPrincipal();
		userPrincipal.setRole(jaasGrantedAuthority.getAuthority());
		return userPrincipal;
	}

	@Override
	protected boolean checkCredentials(String methodRequestMapping, Object principle) {
		// TODO Auto-generated method stub
		return true;
	}
}

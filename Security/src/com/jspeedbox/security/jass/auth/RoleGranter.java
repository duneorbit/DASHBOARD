package com.jspeedbox.security.jass;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.authentication.jaas.AuthorityGranter;

public class RoleGranter implements AuthorityGranter{

	@Override
	public Set<String> grant(Principal principal) {
		if (principal.getName().equals("admin")){
    		return Collections.singleton("ADMIN");
		}else{
    		return Collections.singleton("CUSTOMER");
		}
	}

}

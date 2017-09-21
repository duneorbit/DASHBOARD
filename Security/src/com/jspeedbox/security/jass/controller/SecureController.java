package com.jspeedbox.security.jass.controller;

import org.springframework.security.authentication.jaas.JaasGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspeedbox.security.jass.utils.principal.UserPrincipal;

@Controller
public class SecureController {

	@RequestMapping(value="/dashboards/team/update")
	//public String getOrder(ModelMap model, java.security.Principal principal) {
	public String getAdmin(ModelMap model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JaasGrantedAuthority jaasGrantedAuthority = (JaasGrantedAuthority)(auth.getAuthorities().toArray()[0]);
		
		UserPrincipal userPrincipal = (UserPrincipal)jaasGrantedAuthority.getPrincipal();
		userPrincipal.setRole(jaasGrantedAuthority.getAuthority());
		
		model.addAttribute("userPrincipal", userPrincipal);
		return "/dashboards/team/update";
	}
	
//	@RequestMapping(value="/customer/index")
//	//public String getOrder(ModelMap model, java.security.Principal principal) {
//	public String getCustomer(ModelMap model) {
//
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		JaasGrantedAuthority jaasGrantedAuthority = (JaasGrantedAuthority)(auth.getAuthorities().toArray()[0]);
//		
//		UserPrincipal userPrincipal = (UserPrincipal)jaasGrantedAuthority.getPrincipal();
//		userPrincipal.setRole(jaasGrantedAuthority.getAuthority());
//		
//		model.addAttribute("userPrincipal", userPrincipal);
//		return "customer/index";
//	}

}

package com.jspeedbox.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspeedbox.web.servlet.controller.validator.ConfigValidator;

public class ConfigState implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("FILTER ----------------------------");
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		
		ConfigValidator.validateConfigs(httpServletRequest, httpServletResponse);
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}

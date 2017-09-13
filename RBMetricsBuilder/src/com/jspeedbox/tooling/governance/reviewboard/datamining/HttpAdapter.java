package com.jspeedbox.tooling.governance.reviewboard.datamining;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.utils.logging.LoggingUtils;

public class HttpAdapter {
	
	private final static String USER_AGENT = "Mozilla/5.0";
	
	private final static String KEY_USER_AGENT = "User-Agent";
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(HttpAdapter.class);
	
	public HttpAdapter(){
		
	}
	
	public ByteArrayOutputStream call(String requestURL, String requestMethod) throws Exception{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		URL url = new URL(requestURL);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod(requestMethod);
		connection.setRequestProperty(KEY_USER_AGENT, USER_AGENT);
		
		int rCode = connection.getResponseCode();
		
		//LOGGER_.debug(LoggingUtils.buildParamsPlaceHolders("method", "http response code", "thread"), 
				//"call", rCode, Thread.currentThread().getName());
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = null;
		StringBuffer response = new StringBuffer();
		while((line=reader.readLine())!=null){
			response.append(line);
		}
		reader.close();
		stream.write(response.toString().getBytes());
		return stream;
	}

}

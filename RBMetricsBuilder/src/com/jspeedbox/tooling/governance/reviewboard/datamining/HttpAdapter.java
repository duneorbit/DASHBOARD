package com.jspeedbox.tooling.governance.reviewboard.datamining;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpAdapter {
	
	private final static String USER_AGENT = "Mozilla/5.0";
	
	private final static String KEY_USER_AGENT = "User-Agent";
	
	public HttpAdapter(){
		
	}
	
	public ByteArrayOutputStream call(String requestURL, String requestMethod) throws Exception{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		URL url = new URL(requestURL);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod(requestMethod);
		connection.setRequestProperty(KEY_USER_AGENT, USER_AGENT);
		
		int rCode = connection.getResponseCode();
		
		//System.out.println("http response code["+rCode+"] thread["+Thread.currentThread().getName()+"]");
		
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

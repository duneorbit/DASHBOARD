package com.jspeedbox.utils.json;

import com.jspeedbox.utils.IOUtils;

public class JSONSyndicate{
	
	private boolean success = true;
	
	private String msg = null;
	private String encodedContent = null;
	
	public JSONSyndicate(){
		
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getEncodedContent() {
		return encodedContent;
	}

	public void setEncodedContent(String encodedContent) {
		this.encodedContent = IOUtils.encode(encodedContent);
	}

}

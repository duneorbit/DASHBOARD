package com.jspeedbox.security.utils;

import org.apache.commons.codec.digest.DigestUtils;

import com.jspeedbox.utils.StringUtils;

public class EncryptionUtils {
	
	public static String sha1(final String in){
		if(StringUtils.notEmpty(in)){
			final String out;
			out = DigestUtils.sha1Hex(in).toLowerCase();
			return out;
		}
		return null;
	}

}

package com.jspeedbox.security.jass.utils;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncodeDecode {

	// password for encryption
	final static String keyPassword = "password12345678";
	// put this as key in AES
	static SecretKeySpec key = new SecretKeySpec(keyPassword.getBytes(), "AES");
	
	public final static String ADMIN_PASSWORD = "admin";
	public final static int MINUTE_PASSWORD_EXPIRES = 2;

	public static String Encrypt(String input) {

//		try {
//			// Parameter specific algorithm
//			AlgorithmParameterSpec paramSpec = new IvParameterSpec(
//					keyPassword.getBytes());
//			// Whatever you want to encrypt/decrypt
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//			// You can use ENCRYPT_MODE (ENCRYPTunderscoreMODE) or DECRYPT_MODE
//			// (DECRYPT underscore MODE)
//			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
//			// encrypt data
//			byte[] ecrypted = cipher.doFinal(input.getBytes());
//			// encode data using standard encoder
//			@SuppressWarnings("restriction")
//			String output = Base64.getEncoder().encodeToString(ecrypted);
//			return output;
//		} catch (Exception e) {
//			return null;
//		}
		return input;
	}

	public static String Decrypt(String input) {
//		try {
//			AlgorithmParameterSpec paramSpec = new IvParameterSpec(
//					keyPassword.getBytes());
//			// Whatever you want to encrypt/decrypt using AES /CBC padding
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//			// You can use ENCRYPT_MODE or DECRYPT_MODE
//			cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
//			// decode data using standard decoder
//			@SuppressWarnings("restriction")
//			byte[] output = Base64.getDecoder().decode(input);
//			// Decrypt the data
//			byte[] decrypted = cipher.doFinal(output);
//			
//			return new String(decrypted);
//			
//		} catch (Exception e) {
//			return null;
//		}
		return input;
	}
}

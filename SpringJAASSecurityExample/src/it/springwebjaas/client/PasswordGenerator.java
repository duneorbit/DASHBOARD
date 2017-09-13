package it.springwebjaas.client;

import it.springwebjaas.security.EncodeDecode;

import java.text.SimpleDateFormat;

public class PasswordGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		
		String encrypt = EncodeDecode.Encrypt(EncodeDecode.ADMIN_PASSWORD + "_" + formatter.format(new java.util.Date()));
		System.out.println(encrypt);
		
		String decrypt = EncodeDecode.Decrypt(encrypt);
		System.out.println(decrypt);
	}
}

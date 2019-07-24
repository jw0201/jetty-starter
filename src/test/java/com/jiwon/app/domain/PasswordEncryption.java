package com.jiwon.app.domain;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class PasswordEncryption {

	public static void main(String[] args) {

		StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
		standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
		standardPBEStringEncryptor.setPassword("BRACE_PASS");
		String encodedPass = standardPBEStringEncryptor.encrypt(args[0]);
		System.out.println(args[0]);
		System.out.println("Encrypted Password is : " + encodedPass);
	}
}

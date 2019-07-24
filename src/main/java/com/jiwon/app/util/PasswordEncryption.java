package com.jiwon.app.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PasswordEncryption {
	
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		StandardPBEStringEncryptor spe = (StandardPBEStringEncryptor) context.getBean(StandardPBEStringEncryptor.class);
		System.out.println(args[0]);
		System.out.println("Encrypted Password is : " + spe.encrypt(args[0]));
		System.exit(0);
	}
}
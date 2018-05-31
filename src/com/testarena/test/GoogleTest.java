package com.testarena.test;
import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.IOException;

import com.generic.InitiateScript;
import com.generic.ObjectRespositories;


public class GoogleTest implements ObjectRespositories {

	public static void main(String[] args) throws HeadlessException, InterruptedException, IOException, AWTException {
		InitiateScript.setUp();
		InitiateScript.openBrowser("http://www.gmail.com");
		System.out.println("assas");
		InitiateScript.inputText(EmailInput, "dasdadasdasd");
		InitiateScript.clickObject(NextButton);
		InitiateScript.inputText(PasswordInput, "passwordtype");
		System.out.println("adaswqwe");
	}
}

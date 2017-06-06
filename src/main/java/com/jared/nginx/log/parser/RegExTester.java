package com.jared.nginx.log.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExTester {

	
	// Actual Entry : 10.10.100.151 - 72.37.100.86, 192.36.20.508 - - - [04/Jul/2016:12:50:06 +0000]  https https https "GET / HTTP/1.1" 200 20027 "-" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36"
	public static String logEntry = "10.10.100.151 - 72.37.100.86, 192.36.20.508 - - - [04/Jul/2016:12:50:06 +0000]  https https https \"GET / HTTP/1.1\" 200 20027 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36\"\r\n";

	//public static String regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
	//public static String regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
	public static void main (String [] args){
		
		String regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})[-\\s]+(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).+?\\[(.+?)\\].*?\\\"(.+?)\\\"\\s(\\d{3}).*$ ";
		regexChecker(regex, logEntry);
		//regex = "\\[*\\]\\s.";
		//regexChecker(regex, logEntry);
	}
	
	public static void regexChecker(String regex, String str){
		
		Pattern pattern = Pattern.compile(regex);
		
		Matcher matcher = pattern.matcher(logEntry);
		//String firstIP = matcher.group(0);
		//String secondIP = matcher.group();
		//String timestamp = 
		while(matcher.find()){
			System.out.println( matcher.group(0));
		}
	}
}

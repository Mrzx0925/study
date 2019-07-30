package com.zx.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
	public static boolean isPhoneNumber(String account) {
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";	 
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(account);
	        boolean isMatch = m.matches();	        
	        return isMatch;
	    
	}
	
	public static boolean isEmail(String account) {
		String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p;
		Matcher m;
		p = Pattern.compile(regEx1);
		m = p.matcher(account);
		if (m.matches())
			return true;
		else
			return false;
	    
	}
}

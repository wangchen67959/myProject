package com.test.util;

public class StringUtil {

	/**
	 * 将字符首个字母大写
	 * @param fieldName
	 * @return
	 */
	public static String firstCharUpper(String fieldName){
		String v = "";
		if(null != fieldName) {
			v = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		}
		return v;
		
	}
}

package com.test.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	/**
	 * 字符串返回日期类型
	 * @param str
	 * @return
	 * @throws Exception 
	 */
	public static Date str2Date(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
		Date date = null;
		if(null == str || "".equals(str)){
			return date;
		}
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 日期类型转为字符串时分秒
	 * @param date
	 * @return
	 */
	public static String Date2Str(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
		String str = "";
		if(null != date){
			str = sdf.format(date);
		}
		return str;
	}
	
}

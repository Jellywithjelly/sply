package com.ly.vrps.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadLocalDateUtil {

	private String date_format = null;
	private ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

	public ThreadLocalDateUtil(final String format_str) {
		date_format = format_str;
	}

	private SimpleDateFormat getDateFormat() {
		SimpleDateFormat df = threadLocal.get();
		if (df == null) {
			df = new SimpleDateFormat(date_format);
			threadLocal.set(df);
		}
		return df;
	}

	public String format(Object date) {
		return getDateFormat().format(date);
	}

	public Date parse(String strDate) throws ParseException {
		return getDateFormat().parse(strDate);
	}
}
package com.ehyf.ewashing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DateUtil.class);

	public static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat(
			"yyyyMMdd");
	
	public static final SimpleDateFormat formatDate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");


	
	public static void main(String args[]){
		//System.out.println(strToStrDate("2017-05-25 14:45:31","yyyyMMddHHmmss"));
//		Date d1 = parseData("2017-05-25 14:00:33");
//		Date d2 = parseData("2017-05-25 14:00:30");
//		System.out.println(getDiffM(d1, d2));
		
		System.out.println(new Date().getTime());
	}
	
	public static Date parseData(String str){
		return str2Date(str, formatDate);
	}
	
	/**
	 * 将长时间格式字符串转换为时间 yyyy年MM月dd日 HH时mm分ss秒
	 * 
	 * @param strDate
	 * @return
	 */
	public static String strToStrDate(String strDate) {

		return strToStrDate(strDate, "yyyy年MM月dd日 HH时mm分ss秒");
	}

	/**
	 * 将默认数字类型的时间,按照自定义输出格式
	 * 
	 * @author: pfliu@iflytek.com
	 * @createTime: 2016-12-7 上午10:56:23
	 * @history:
	 * @param strDate
	 * @param format
	 * @return String
	 */
	public static String strToStrDate(String strDate, String format) {

		// 将当前字符串转成时间
		Date parse = str2Date(strDate, formatDate);

		return getSDFormat(format).format(parse);
	}

	/**
	 * 当前日期
	 * 
	 * @return 系统当前时间
	 */
	public static Date getDate() {
		return new Date();
	}

	/**
	 * 获得默认字符串格式时间, 格式:yyyyMMddHHmmss
	 * 
	 * @author: pfliu@iflytek.com
	 * @createTime: 2016-12-7 下午4:43:23
	 * @history:
	 * @return String
	 */
	public static String getDefaultStrDate() {
		return yyyyMMddHHmmss.format(getDate());
	}

	/**
	 * 指定日期按指定格式显示
	 * 
	 * @param date
	 *            指定的日期
	 * @param pattern
	 *            指定的格式
	 * @return 指定日期按指定格式显示
	 */
	public static String formatDate(Date date, String pattern) {
		return getSDFormat(pattern).format(date);
	}
	
	public static String defaultFormatDate(Date date){
		return  yyyyMMddHHmmss.format(date);
	}

	/**
	 * 指定模式的时间格式
	 * 
	 * @author: pfliu@iflytek.com
	 * @createTime: 2016-12-7 上午10:54:03
	 * @history:
	 * @param pattern
	 * @return SimpleDateFormat
	 */
	private static SimpleDateFormat getSDFormat(String pattern) {
		return new SimpleDateFormat(pattern);
	}

	/**
	 * 格式化时间
	 * 
	 * @param data
	 * @param format
	 * @return
	 */
	public static String dataformat(String data, String format) {
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sformat.parse(data);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
		}
		return sformat.format(date);
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @param sdf
	 * @return
	 */
	public synchronized static Date str2Date(String str, SimpleDateFormat sdf) {
		if (null == str || "".equals(str)) {
			return null;
		}
		Date date = null;
		try {
			date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: getLastWeek
	 * @Description: 最近一周
	 * @return String
	 * @date 2016-12-7 上午9:01:56
	 * @throws
	 */
	public static String getLastWeek() {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, -1);
		date = calendar.getTime();
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}

	/**
	 * 
	 * @Title: getRecentMonth
	 * @Description: 获取最近几个月日期
	 * @param month
	 * @return String
	 * @date 2016-12-7 上午9:48:11
	 * @throws
	 */
	public static String getRecentMonth(int month) {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 0 - month);
		date = calendar.getTime();
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}
	
	public static int getMonth(String dateStr){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseData(dateStr));
		int month = calendar.get(Calendar.MONTH)+1;  
		return month;
	}
	/**
	 * 是否为闰年
	 * @param year
	 * @return
	 */
	public static boolean checkLeapYear(int year){ 
		boolean flag=false; 
		if((year%4 == 0) && ((year%100 != 0) || (year%400 == 0))){ 
			flag=true; 
		} 
		return flag;
	}
	
	/**
	 * 根据d1\d2求两个日期的秒数差
	 */
	public static long getDiffSecond(Date d1,Date d2){
		long diff = d2.getTime()-d1.getTime();
		if(diff>0){
			return diff/1000;
		}
		return -1;
	}
	
}

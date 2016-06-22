package com.sinotrans.transport.common.util;


import java.text.ParseException;
import java.util.*;

public class CommonUtils {
//	static final String a = "AA";

	/**
	 *
	 * @param date  当前时间点
	 * @param degree  1:分钟    2:小时    3:天   为单位
	 * @param outDegree  超时时间
	 * @param ifAfter  true:生成之后的时间   false:生成之前的时间
	 * @return
	 * @throws ParseException
	 * 	 * 获取outDay天后的时间
	 */
	public static Date generateOrdrTimeOut(Date date,int degree,int outDegree,boolean ifAfter) throws ParseException {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		if(ifAfter) {
			if(1 == degree) {
				int minute =  cld.get(Calendar.MINUTE) + outDegree;
				cld.set(Calendar.MINUTE, minute);
			} else if(2 == degree) {
				int hour = cld.get(Calendar.HOUR) + outDegree;
				cld.set(Calendar.HOUR, hour);
			} else if(3 == degree) {
				int day = cld.get(Calendar.DATE) + outDegree;
				cld.set(Calendar.DATE, day);
			}
		} else {
			if(1 == degree) {
				int minute =  cld.get(Calendar.MINUTE) - outDegree;
				cld.set(Calendar.MINUTE, minute);
			} else if(2 == degree) {
				int hour = cld.get(Calendar.HOUR) - outDegree;
				cld.set(Calendar.HOUR, hour);
			} else if(3 == degree) {
				int day = cld.get(Calendar.DATE) - outDegree;
				cld.set(Calendar.DATE, day);
			}
		}
		return cld.getTime();
	}

	/**
	 * 生成随机字符串
	 * @param length  随机字符串长度
	 * @param type	  1:数字    2:字母   3及其他:数字和字符串类型
	 * @return
	 */
	public static String generateRandom(int length,int type) {
		if(1 == type) {
			Random r = new Random();
			int randNum = 0;
			while(randNum < 100000) {
				randNum = (int) r.nextInt(999999);
			}
			return randNum + "";
		}
		String base = null;
		if (2 == type) {
			base = "abcdefghijklmnopqrstuvwxyz";
		} else {
			base = "abcdefghijklmnopqrstuvwxyz0123456789";
		}
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}


	/**
	 * 计算两个时间点的分钟数
	 * @param dayBegin
	 * @param dayEnd
	 * @return
	 */
	public static int calculateDayMinutes(String dayBegin,String dayEnd) {
		int hour = Integer.parseInt(dayEnd.substring(0,2)) - Integer.parseInt(dayBegin.substring(0,2));
		int minute = Integer.parseInt(dayEnd.substring(3)) - Integer.parseInt(dayBegin.substring(3));
		return hour*60 + minute;
	}


	public static int fetchTogetherSize(Set inputSet, Set searchedSet) {
		inputSet.retainAll(searchedSet);
		return inputSet.size();
	}


	public static List<String> generateListByString(String arrayStr, String splits) {
		if (null == arrayStr) {
			return new ArrayList<String>(0);
		}
		List<String> sl = new ArrayList<String>();
		for (String s : arrayStr.split(splits)) {
			if (!"".equals(s.trim())) {
				sl.add(s.trim());
			}
		}
		return sl;
	}

}

package com.sm.fix;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class MsgUtils {

	public static String getOrderID(String type) {
		String orderId = null;
		if(type.equals("OrderId"))
			orderId="SM/";
		if(type.equals("ExecId"))
			orderId = "NF-EXCH/";
		
		int max = 99999999;
		int min = 10000000;
		int id = (int) ((Math.random() * max) + min);
		orderId = orderId + String.valueOf(id);
		return orderId;
	}

	
	public static BigDecimal getPrice() {
		int max = 99;
		int min = 100;
		double id = (long) ((Math.random() * max) + min)/Math.random();
		return new BigDecimal(id).setScale(5, RoundingMode.UP);
	}
	
	public static long getQty() {
		int max = 99;
		int min = 100;
		long id = (long) ((Math.random() * max) + min);
		return id;
	}

	public static void main(String[] args) {
		while (true) {
			System.out.println(getQty());
		}
	}

	public static long getTime() {
		return new Date().getTime();
	}
	
	public static boolean isNull(Object obj){
		return obj==null;
	}

}

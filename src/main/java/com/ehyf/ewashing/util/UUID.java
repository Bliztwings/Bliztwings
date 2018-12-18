package com.ehyf.ewashing.util;

/**
 * UUID 实用工具类
 * 
 * @author jelly
 *
 */
public class UUID {
	public static String getUUID32() {
        String uuid = java.util.UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
	
	public static void main(String args[]){
		System.out.println(getUUID32());
	}
}

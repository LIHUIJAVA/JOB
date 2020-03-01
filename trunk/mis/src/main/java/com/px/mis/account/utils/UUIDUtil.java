package com.px.mis.account.utils;

import java.util.UUID;

public class UUIDUtil {
	
	public static String getUUID32(String start,String middle) {
		String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		return start+middle+uuid;
	}
}

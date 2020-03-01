package com.px.mis.account.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.px.mis.util.GetOrderNo;

public class Test {
	public static void main(String[] args) {
//		try {
//			String seqNo = new GetOrderNo().getSeqNo("xs", "zs");
//			System.out.println(seqNo);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
//		 String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
//		 System.out.println("!!!!!!!!!"+UUID.randomUUID().toString());
//		    System.out.println(uuid);
		
		    
	    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		String[] split = "".split(",");
//		System.out.println(split.length);
//		for(String a:split) {
//			System.out.println(a+"=====");
//		}
	}
}

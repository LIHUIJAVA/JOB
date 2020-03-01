package com.px.mis.purc.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalcAmt {
	
	//计算未税金额  金额=未税数量*未税单价
	public static  BigDecimal noTaxAmt(BigDecimal noTaxUprc,BigDecimal qty){	
		return noTaxUprc.multiply(qty);
	}
	
	//计算税额  税额=未税金额*税率
	public static  BigDecimal taxAmt(BigDecimal noTaxUprc,BigDecimal qty,BigDecimal taxRate){
		return noTaxAmt(noTaxUprc, qty).multiply(taxRate);
    }
	
	//计算含税单价  含税单价=无税单价*税率+无税单价
	public static  BigDecimal cntnTaxUprc(BigDecimal noTaxUprc,BigDecimal qty,BigDecimal taxRate){
		return noTaxUprc.multiply(taxRate).add(noTaxUprc);
    }
	
	//计算价税合计  价税合计=无税金额*税率+无税金额=税额+无税金额
	public static  BigDecimal prcTaxSum(BigDecimal noTaxUprc,BigDecimal qty,BigDecimal taxRate){
		return taxAmt(noTaxUprc, qty,taxRate).add(noTaxAmt(noTaxUprc, qty));
    }
	
	/* 计算失效日期       releaseDate  生产日期
	                      day  保质期     */
	public static String getDate(String releaseDate,int day){
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();//获取当前时间
		 
		Date d = new Date () ;			
		try{
		     calendar.setTime(df.parse(releaseDate));//字符串转日期,再设置calendar的时间
		}catch(ParseException e){
		    e.printStackTrace();
		}
      //calendar.add(Calendar.DATE, day);//这句话可以替代下面的两行
		int day1 = calendar.get(Calendar.DAY_OF_YEAR);//返回给定日历字段的值
		calendar.set(Calendar.DAY_OF_YEAR, day1 + day );//将给定的日历字段设置为给定值
		d= calendar.getTime();//返回表示calendar的时间值的 Date 对象			  
		String  date = df.format(d);//日期转字符串
        	
		return date;//失效日期
     }
	

}

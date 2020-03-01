package com.px.mis.purc.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalcAmt {
	
	//����δ˰���  ���=δ˰����*δ˰����
	public static  BigDecimal noTaxAmt(BigDecimal noTaxUprc,BigDecimal qty){	
		return noTaxUprc.multiply(qty);
	}
	
	//����˰��  ˰��=δ˰���*˰��
	public static  BigDecimal taxAmt(BigDecimal noTaxUprc,BigDecimal qty,BigDecimal taxRate){
		return noTaxAmt(noTaxUprc, qty).multiply(taxRate);
    }
	
	//���㺬˰����  ��˰����=��˰����*˰��+��˰����
	public static  BigDecimal cntnTaxUprc(BigDecimal noTaxUprc,BigDecimal qty,BigDecimal taxRate){
		return noTaxUprc.multiply(taxRate).add(noTaxUprc);
    }
	
	//�����˰�ϼ�  ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
	public static  BigDecimal prcTaxSum(BigDecimal noTaxUprc,BigDecimal qty,BigDecimal taxRate){
		return taxAmt(noTaxUprc, qty,taxRate).add(noTaxAmt(noTaxUprc, qty));
    }
	
	/* ����ʧЧ����       releaseDate  ��������
	                      day  ������     */
	public static String getDate(String releaseDate,int day){
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();//��ȡ��ǰʱ��
		 
		Date d = new Date () ;			
		try{
		     calendar.setTime(df.parse(releaseDate));//�ַ���ת����,������calendar��ʱ��
		}catch(ParseException e){
		    e.printStackTrace();
		}
      //calendar.add(Calendar.DATE, day);//��仰����������������
		int day1 = calendar.get(Calendar.DAY_OF_YEAR);//���ظ��������ֶε�ֵ
		calendar.set(Calendar.DAY_OF_YEAR, day1 + day );//�������������ֶ�����Ϊ����ֵ
		d= calendar.getTime();//���ر�ʾcalendar��ʱ��ֵ�� Date ����			  
		String  date = df.format(d);//����ת�ַ���
        	
		return date;//ʧЧ����
     }
	

}

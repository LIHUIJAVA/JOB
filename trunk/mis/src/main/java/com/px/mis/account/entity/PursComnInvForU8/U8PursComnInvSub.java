package com.px.mis.account.entity.PursComnInvForU8;

import java.math.BigDecimal;

public class U8PursComnInvSub {
	//�����ӱ����
	/**
	 ÿһ��DataRow �����ж��DataDetails
	 �ṹΪ:
		 DataTable
		 
					DataDetails
		 DataRow    DataDetails
		 			DataDetails
		 
					DataDetails
		 DataRow    DataDetails
		 			DataDetails
		 
		 			DataDetails
		 DataRow    DataDetails
		 			DataDetails
		 			
		 DataTable
	**/	 	
	
	private String cinvcode;//�������
	private BigDecimal quantity;//����
	private BigDecimal iprice;//����
	private BigDecimal itax;//˰��
	private String citemcode;//��Ŀ����
	private BigDecimal itaxrate;//˰��
	
	
	public BigDecimal getItax() {
		return itax;
	}
	public void setItax(BigDecimal itax) {
		this.itax = itax;
	}
	
	public String getCinvcode() {
		return cinvcode;
	}
	public void setCinvcode(String cinvcode) {
		this.cinvcode = cinvcode;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getIprice() {
		return iprice;
	}
	public void setIprice(BigDecimal iprice) {
		this.iprice = iprice;
	}
	public String getCitemcode() {
		return citemcode;
	}
	public void setCitemcode(String citemcode) {
		this.citemcode = citemcode;
	}
	public BigDecimal getItaxrate() {
		return itaxrate;
	}
	public void setItaxrate(BigDecimal itaxrate) {
		this.itaxrate = itaxrate;
	}

	


}

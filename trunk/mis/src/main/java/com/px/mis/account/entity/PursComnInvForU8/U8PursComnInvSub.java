package com.px.mis.account.entity.PursComnInvForU8;

import java.math.BigDecimal;

public class U8PursComnInvSub {
	//这是子表对象
	/**
	 每一个DataRow 可以有多个DataDetails
	 结构为:
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
	
	private String cinvcode;//存货编码
	private BigDecimal quantity;//数量
	private BigDecimal iprice;//单价
	private BigDecimal itax;//税额
	private String citemcode;//项目编码
	private BigDecimal itaxrate;//税率
	
	
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

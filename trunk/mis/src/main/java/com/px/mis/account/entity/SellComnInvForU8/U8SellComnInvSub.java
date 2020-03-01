package com.px.mis.account.entity.SellComnInvForU8;

import java.math.BigDecimal;


public class U8SellComnInvSub {
	private String cinvcode;// 存货编码 必填
	private BigDecimal quantity;// 数量 必填
	private BigDecimal iprice;// 单价 必填
	private String citemcode;// 项目编码 必填
	private BigDecimal itaxrate;// 税率 必填
//	private BigDecimal itax;//税额
	private String cbatch;// 批号
	private String cwhcode;// 仓库
	
//	public BigDecimal getItax() {
//		return itax;
//	}
//	public void setItax(BigDecimal itax) {
//		this.itax = itax;
//	}
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
	public String getCbatch() {
		return cbatch;
	}
	public void setCbatch(String cbatch) {
		this.cbatch = cbatch;
	}
	public String getCwhcode() {
		return cwhcode;
	}
	public void setCwhcode(String cwhcode) {
		this.cwhcode = cwhcode;
	}


	

}

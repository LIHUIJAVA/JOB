package com.px.mis.ec.entity;

import java.math.BigDecimal;

/**
 * 订单付款方式关系映射
 * @author lxya0
 */
public class PlatOrderPaymethod {
	
	private Integer payId;
	private String payStatus; //支付状态 默认成功
	private String orderId; //订单编号
	private String platId; //平台编号
	private String storeId; //店铺编号
	
	private String paymentNumber; //交易单号
	private String paymoneyTime; //支付时间
	private String payWay; //支付方式	
	private BigDecimal payMoney; //支付金额
	
	private String banktypecode; //支付方式编码
	private String offLinePayFlag; //线下支付    
	private String merchantPercent; //商家的承担比例
	private String platformPercent; //平台的承担比例
	
	
	public Integer getPayId() {
		return payId;
	}
	public void setPayId(Integer payId) {
		this.payId = payId;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPlatId() {
		return platId;
	}
	public void setPlatId(String platId) {
		this.platId = platId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getPaymentNumber() {
		return paymentNumber;
	}
	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}
	public String getPaymoneyTime() {
		return paymoneyTime;
	}
	public void setPaymoneyTime(String paymoneyTime) {
		this.paymoneyTime = paymoneyTime;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public String getBanktypecode() {
		return banktypecode;
	}
	public void setBanktypecode(String banktypecode) {
		this.banktypecode = banktypecode;
	}
	public String getOffLinePayFlag() {
		return offLinePayFlag;
	}
	public void setOffLinePayFlag(String offLinePayFlag) {
		this.offLinePayFlag = offLinePayFlag;
	}
	public String getMerchantPercent() {
		return merchantPercent;
	}
	public void setMerchantPercent(String merchantPercent) {
		this.merchantPercent = merchantPercent;
	}
	public String getPlatformPercent() {
		return platformPercent;
	}
	public void setPlatformPercent(String platformPercent) {
		this.platformPercent = platformPercent;
	}
	
	
	
	
}

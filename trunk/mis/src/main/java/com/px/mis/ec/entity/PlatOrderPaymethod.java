package com.px.mis.ec.entity;

import java.math.BigDecimal;

/**
 * �������ʽ��ϵӳ��
 * @author lxya0
 */
public class PlatOrderPaymethod {
	
	private Integer payId;
	private String payStatus; //֧��״̬ Ĭ�ϳɹ�
	private String orderId; //�������
	private String platId; //ƽ̨���
	private String storeId; //���̱��
	
	private String paymentNumber; //���׵���
	private String paymoneyTime; //֧��ʱ��
	private String payWay; //֧����ʽ	
	private BigDecimal payMoney; //֧�����
	
	private String banktypecode; //֧����ʽ����
	private String offLinePayFlag; //����֧��    
	private String merchantPercent; //�̼ҵĳе�����
	private String platformPercent; //ƽ̨�ĳе�����
	
	
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

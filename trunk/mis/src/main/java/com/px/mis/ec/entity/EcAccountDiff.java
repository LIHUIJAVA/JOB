package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class EcAccountDiff {
	private String storeId;//����id
	private String storeName;//��������
	private String ecOrderId;//ƽ̨������
	private BigDecimal ordrMoneySum;//�������
	private BigDecimal refundMoneySum;//�˿���
	private BigDecimal accountMoneySum;//���˵����
	private BigDecimal difference;//������������+�˻����-���˵���
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getEcOrderId() {
		return ecOrderId;
	}
	public void setEcOrderId(String ecOrderId) {
		this.ecOrderId = ecOrderId;
	}
	public BigDecimal getOrdrMoneySum() {
		return ordrMoneySum;
	}
	public void setOrdrMoneySum(BigDecimal ordrMoneySum) {
		this.ordrMoneySum = ordrMoneySum;
	}
	public BigDecimal getRefundMoneySum() {
		return refundMoneySum;
	}
	public void setRefundMoneySum(BigDecimal refundMoneySum) {
		this.refundMoneySum = refundMoneySum;
	}
	public BigDecimal getAccountMoneySum() {
		return accountMoneySum;
	}
	public void setAccountMoneySum(BigDecimal accountMoneySum) {
		this.accountMoneySum = accountMoneySum;
	}
	public BigDecimal getDifference() {
		return difference;
	}
	public void setDifference(BigDecimal difference) {
		this.difference = difference;
	}
	
}

package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class EcAccountDiff {
	private String storeId;//店铺id
	private String storeName;//店铺名称
	private String ecOrderId;//平台订单号
	private BigDecimal ordrMoneySum;//订单金额
	private BigDecimal refundMoneySum;//退款金额
	private BigDecimal accountMoneySum;//对账单金额
	private BigDecimal difference;//差异金额（订单金额+退货金额-对账单金额）
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

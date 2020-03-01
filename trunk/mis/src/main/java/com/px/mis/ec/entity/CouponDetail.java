package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class CouponDetail {
	private int id;
	private String platId;//平台id，京东JD，天猫JD
	private String storeId;//店铺id
	private String orderId;//订单号
	private String skuId;
	private String couponCode;//优惠类型编码
	private String couponType;//优惠名称
	private BigDecimal couponPrice;//优惠金额
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public BigDecimal getCouponPrice() {
		return couponPrice;
	}
	public void setCouponPrice(BigDecimal couponPrice) {
		this.couponPrice = couponPrice;
	}
	

}

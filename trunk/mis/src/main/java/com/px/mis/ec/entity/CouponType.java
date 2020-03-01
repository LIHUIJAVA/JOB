package com.px.mis.ec.entity;

public class CouponType {
	private int id;
	private String platId;//平台id，京东JD，天猫TM
	private String couponName;//优惠名称
	private String code;//优惠编码
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPlatId() {
		return platId;
	}
	public void setPlatId(String platId) {
		this.platId = platId;
	}
	
	

}

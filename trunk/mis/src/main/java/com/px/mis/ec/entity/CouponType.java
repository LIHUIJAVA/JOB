package com.px.mis.ec.entity;

public class CouponType {
	private int id;
	private String platId;//ƽ̨id������JD����èTM
	private String couponName;//�Ż�����
	private String code;//�Żݱ���
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

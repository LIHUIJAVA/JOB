package com.px.mis.ec.entity;
//电子面单平台开通店铺档案
public class EcExpressSettings {
	private int id;
	private String platId;//平台ID
	private String storeId;//店铺ID
	private String venderId;//商家ID
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getVenderId() {
		return venderId;
	}
	public void setVenderId(String venderId) {
		this.venderId = venderId;
	}
	

}

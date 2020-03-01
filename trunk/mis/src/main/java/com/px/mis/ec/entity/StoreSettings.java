package com.px.mis.ec.entity;

import java.util.Date;

public class StoreSettings {
	
	private String storeId;//店铺编号
	private String storeName;//店铺名称
	private String appKey;
	private String appSecret;
	private String accessToken;
	private String tokenDate;//token过期时间
	private String venderId;//商家id
	private String shopId;//店铺id
	
	
	public String getVenderId() {
		return venderId;
	}
	public void setVenderId(String venderId) {
		this.venderId = venderId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getTokenDate() {
		return tokenDate;
	}
	public void setTokenDate(String tokenDate) {
		this.tokenDate = tokenDate;
	}
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
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	

}

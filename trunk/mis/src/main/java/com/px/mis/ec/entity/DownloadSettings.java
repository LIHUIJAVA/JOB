package com.px.mis.ec.entity;

import java.util.Date;

public class DownloadSettings {

	private Integer id;
	private String shopId;
	private String shopName;
	private String intervalTime;
	private String recentHours;
	private Date nextTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getIntervalTime() {
		return intervalTime;
	}
	public void setIntervalTime(String intervalTime) {
		this.intervalTime = intervalTime;
	}
	public String getRecentHours() {
		return recentHours;
	}
	public void setRecentHours(String recentHours) {
		this.recentHours = recentHours;
	}
	public Date getNextTime() {
		return nextTime;
	}
	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}
	
	
}

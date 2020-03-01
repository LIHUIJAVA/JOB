package com.px.mis.ec.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

//商品活动中间表
public class GoodsActivityMiddle {

	private Integer no;//序号
	private String storeId;//店铺编码
	private String invtyEncd;//存货编码
	private Integer sublistNo;//促销活动子表序号
	private Integer priority;//优先级
	private String startTime;//起始时间
	private String endTime;//结束时间
	private Integer allGoods;//是否全部商品；
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public Integer getSublistNo() {
		return sublistNo;
	}
	public void setSublistNo(Integer sublistNo) {
		this.sublistNo = sublistNo;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public Integer getAllGoods() {
		return allGoods;
	}
	public void setAllGoods(Integer allGoods) {
		this.allGoods = allGoods;
	}
	@Override
	public String toString() {
		return "GoodsActivityMiddle [no=" + no + ", storeId=" + storeId + ", invtyEncd=" + invtyEncd + ", sublistNo="
				+ sublistNo + ", priority=" + priority + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", allGoods=" + allGoods + "]";
	}
	
	
}

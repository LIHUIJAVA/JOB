package com.px.mis.ec.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

//��Ʒ��м��
public class GoodsActivityMiddle {

	private Integer no;//���
	private String storeId;//���̱���
	private String invtyEncd;//�������
	private Integer sublistNo;//������ӱ����
	private Integer priority;//���ȼ�
	private String startTime;//��ʼʱ��
	private String endTime;//����ʱ��
	private Integer allGoods;//�Ƿ�ȫ����Ʒ��
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

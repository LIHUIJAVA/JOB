package com.px.mis.ec.entity;

public class UnionQuery {
	private int type;//��������
	private String orderId;//����id
	private String audit;//��˽��
	private String orderName;//��������
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAudit() {
		return audit;
	}
	public void setAudit(String audit) {
		this.audit = audit;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	
}

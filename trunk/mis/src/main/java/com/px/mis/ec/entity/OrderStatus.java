package com.px.mis.ec.entity;

public class OrderStatus {
	
    private String orderStatusId;//×´Ì¬±àºÅ
    private String orderStatusName;//×´Ì¬Ãû³Æ
    private String memo;//±¸×¢

    public String getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(String orderStatusId) {
        this.orderStatusId = orderStatusId == null ? null : orderStatusId.trim();
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName == null ? null : orderStatusName.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}
package com.px.mis.ec.entity;

public class SpecialSettings {
	
    private String settingId;//设置编号
    private Integer downTime;//订单下载默认间隔时间
    private Integer receipt;//已对账已收款
    private Integer noReceipt;//已对账未收款
    private Integer noReceiptDays;//已对账未收款天数
    private Integer noCheck;//已发货未对账
    private Integer noCheckDays;//已发货未对账天数
    private Integer orderDate;//订单日期来源

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId == null ? null : settingId.trim();
    }

    public Integer getDownTime() {
        return downTime;
    }

    public void setDownTime(Integer downTime) {
        this.downTime = downTime;
    }

    public Integer getReceipt() {
        return receipt;
    }

    public void setReceipt(Integer receipt) {
        this.receipt = receipt;
    }

    public Integer getNoReceipt() {
        return noReceipt;
    }

    public void setNoReceipt(Integer noReceipt) {
        this.noReceipt = noReceipt;
    }

    public Integer getNoReceiptDays() {
        return noReceiptDays;
    }

    public void setNoReceiptDays(Integer noReceiptDays) {
        this.noReceiptDays = noReceiptDays;
    }

    public Integer getNoCheck() {
        return noCheck;
    }

    public void setNoCheck(Integer noCheck) {
        this.noCheck = noCheck;
    }

    public Integer getNoCheckDays() {
        return noCheckDays;
    }

    public void setNoCheckDays(Integer noCheckDays) {
        this.noCheckDays = noCheckDays;
    }

    public Integer getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Integer orderDate) {
        this.orderDate = orderDate;
    }
}
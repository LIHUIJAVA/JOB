package com.px.mis.ec.entity;

public class SpecialSettings {
	
    private String settingId;//���ñ��
    private Integer downTime;//��������Ĭ�ϼ��ʱ��
    private Integer receipt;//�Ѷ������տ�
    private Integer noReceipt;//�Ѷ���δ�տ�
    private Integer noReceiptDays;//�Ѷ���δ�տ�����
    private Integer noCheck;//�ѷ���δ����
    private Integer noCheckDays;//�ѷ���δ��������
    private Integer orderDate;//����������Դ

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
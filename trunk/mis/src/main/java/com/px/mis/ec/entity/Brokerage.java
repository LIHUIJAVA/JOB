package com.px.mis.ec.entity;

public class Brokerage {
	
    private String brokId;//Ӷ��۵���
    private String brokName;//Ӷ��۵�����
    private String memo;//��ע

    public String getBrokId() {
        return brokId;
    }

    public void setBrokId(String brokId) {
        this.brokId = brokId == null ? null : brokId.trim();
    }

    public String getBrokName() {
        return brokName;
    }

    public void setBrokName(String brokName) {
        this.brokName = brokName == null ? null : brokName.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}
package com.px.mis.ec.entity;

public class ECPlatform {
	
    private String ecId;//����ƽ̨���
    private String ecName;//����ƽ̨����
    private String memo;//��ע

    public String getEcId() {
        return ecId;
    }

    public void setEcId(String ecId) {
        this.ecId = ecId == null ? null : ecId.trim();
    }

    public String getEcName() {
        return ecName;
    }

    public void setEcName(String ecName) {
        this.ecName = ecName == null ? null : ecName.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}
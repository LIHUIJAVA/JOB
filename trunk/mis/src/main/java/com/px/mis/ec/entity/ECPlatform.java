package com.px.mis.ec.entity;

public class ECPlatform {
	
    private String ecId;//电商平台编号
    private String ecName;//电商平台名称
    private String memo;//备注

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
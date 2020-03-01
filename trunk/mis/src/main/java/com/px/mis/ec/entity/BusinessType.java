package com.px.mis.ec.entity;

public class BusinessType {
	
    private String busTypeId;//业务类型编号
    private String busTypeName;//业务类型名称
    private String memo;//备注

    public String getBusTypeId() {
        return busTypeId;
    }

    public void setBusTypeId(String busTypeId) {
        this.busTypeId = busTypeId == null ? null : busTypeId.trim();
    }

    public String getBusTypeName() {
        return busTypeName;
    }

    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName == null ? null : busTypeName.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}
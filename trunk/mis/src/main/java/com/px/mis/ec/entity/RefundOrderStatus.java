package com.px.mis.ec.entity;

public class RefundOrderStatus {
	
    private String refStatusId;//ÍË¿î×´Ì¬±àºÅ
    private String refStatusName;//ÍË¿î×´Ì¬Ãû³Æ
    private String memo;//±¸×¢

    public String getRefStatusId() {
        return refStatusId;
    }

    public void setRefStatusId(String refStatusId) {
        this.refStatusId = refStatusId == null ? null : refStatusId.trim();
    }

    public String getRefStatusName() {
        return refStatusName;
    }

    public void setRefStatusName(String refStatusName) {
        this.refStatusName = refStatusName == null ? null : refStatusName.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}
package com.px.mis.ec.entity;

public class ReturnStatus {
	
    private String returnStatusId;//ÍË»õ×´Ì¬±àÂë
    private String returnStatusName;//ÍË»õ×´Ì¬Ãû³Æ
    private String memo;//±¸×¢

    public String getReturnStatusId() {
        return returnStatusId;
    }

    public void setReturnStatusId(String returnStatusId) {
        this.returnStatusId = returnStatusId == null ? null : returnStatusId.trim();
    }

    public String getReturnStatusName() {
        return returnStatusName;
    }

    public void setReturnStatusName(String returnStatusName) {
        this.returnStatusName = returnStatusName == null ? null : returnStatusName.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}
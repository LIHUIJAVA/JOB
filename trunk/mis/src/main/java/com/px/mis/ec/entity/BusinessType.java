package com.px.mis.ec.entity;

public class BusinessType {
	
    private String busTypeId;//ҵ�����ͱ��
    private String busTypeName;//ҵ����������
    private String memo;//��ע

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
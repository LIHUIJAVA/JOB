package com.px.mis.ec.entity;

public class OrderDealSettings {
	
    private Integer settingId;//���ñ��
    private Integer orderShare;//���ض�����̯�����ۿ�
    private Integer tbOrderServ;//�����Ա����������
    private Integer autoMerge;//����ǰ�����Զ��ϲ�
    private Integer controlNum;//���ƿ�����
    private Integer verifyBatch;//���У����Ʒ����
    private Integer serviceNote;//�������ÿͷ���ע��ȡ
    private Integer batchPicking;//�����������
    private Integer orderIdCreate;//���������������ˮ����

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public Integer getOrderShare() {
        return orderShare;
    }

    public void setOrderShare(Integer orderShare) {
        this.orderShare = orderShare;
    }

    public Integer getTbOrderServ() {
        return tbOrderServ;
    }

    public void setTbOrderServ(Integer tbOrderServ) {
        this.tbOrderServ = tbOrderServ;
    }

    public Integer getAutoMerge() {
        return autoMerge;
    }

    public void setAutoMerge(Integer autoMerge) {
        this.autoMerge = autoMerge;
    }

    public Integer getControlNum() {
        return controlNum;
    }

    public void setControlNum(Integer controlNum) {
        this.controlNum = controlNum;
    }

    public Integer getVerifyBatch() {
        return verifyBatch;
    }

    public void setVerifyBatch(Integer verifyBatch) {
        this.verifyBatch = verifyBatch;
    }

    public Integer getServiceNote() {
        return serviceNote;
    }

    public void setServiceNote(Integer serviceNote) {
        this.serviceNote = serviceNote;
    }

    public Integer getBatchPicking() {
        return batchPicking;
    }

    public void setBatchPicking(Integer batchPicking) {
        this.batchPicking = batchPicking;
    }

    public Integer getOrderIdCreate() {
        return orderIdCreate;
    }

    public void setOrderIdCreate(Integer orderIdCreate) {
        this.orderIdCreate = orderIdCreate;
    }
}
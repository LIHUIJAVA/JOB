package com.px.mis.ec.entity;

public class OrderDealSettings {
	
    private Integer settingId;//设置编号
    private Integer orderShare;//下载订单分摊整单折扣
    private Integer tbOrderServ;//下载淘宝订单服务费
    private Integer autoMerge;//客审前订单自动合并
    private Integer controlNum;//控制可用量
    private Integer verifyBatch;//验货校验商品批次
    private Integer serviceNote;//下载启用客服备注提取
    private Integer batchPicking;//订单批量拣货
    private Integer orderIdCreate;//订单编号年月日流水生成

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
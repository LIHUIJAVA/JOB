package com.px.mis.whs.entity;

import java.math.BigDecimal;

//拣货子表
public class PickSnglSubTab {

    private Long orderNum;// 序号
    private String pickSnglNum;// 拣货单号

    private String bizTypId;// 业务类型编号,
    private String sellTypId;// 销售类型编号,
    private BigDecimal qty;// 数量,
    private BigDecimal bxRule;// 箱规,
    private String prdcDt;// 生产日期,
    private String invldtnDt;// 失效日期,
    private String spcModel;// 规格,
    private String crspdBarCd;// 对应条形码,
    private String whsEncd;// 仓库码,
    private String whsNm;// 仓库,
    private String invtyEncd;// 存货码,
    private String invtyNm;// 存货,
    private String batNum;// 批号,
    private String measrCorpId;// 计量单位码,
    private String measrCorpNm;// 计量单位,
    private String sellTypNm;// 销售类型,
    private String bizTypNm;// 业务类型,
    private String recvSendCateId;// 收发类别码,
    private String recvSendCateNm;// 收发类别,
    private String gdsBitEncd;// 货位码,
    private String gdsBitNm;// 货位名称,
    private String regnEncd;// 区域码,
    private String regnNm;// 区域名
    private String baoZhiQi;// 保质期

    public final Long getOrderNum() {
        return orderNum;
    }

    public final void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public final String getPickSnglNum() {
        return pickSnglNum;
    }

    public final void setPickSnglNum(String pickSnglNum) {
        this.pickSnglNum = pickSnglNum;
    }

    public final String getBizTypId() {
        return bizTypId;
    }

    public final void setBizTypId(String bizTypId) {
        this.bizTypId = bizTypId;
    }

    public final String getSellTypId() {
        return sellTypId;
    }

    public final void setSellTypId(String sellTypId) {
        this.sellTypId = sellTypId;
    }

    public final BigDecimal getQty() {
        return qty;
    }

    public final void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public final String getPrdcDt() {
        return prdcDt;
    }

    public final void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
    }

    public final String getInvldtnDt() {
        return invldtnDt;
    }

    public final void setInvldtnDt(String invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public final String getSpcModel() {
        return spcModel;
    }

    public final void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    public final String getCrspdBarCd() {
        return crspdBarCd;
    }

    public final void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
    }

    public final String getWhsEncd() {
        return whsEncd;
    }

    public final void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public final String getWhsNm() {
        return whsNm;
    }

    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public final String getInvtyEncd() {
        return invtyEncd;
    }

    public final void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    public final String getInvtyNm() {
        return invtyNm;
    }

    public final void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public final String getBatNum() {
        return batNum;
    }

    public final void setBatNum(String batNum) {
        this.batNum = batNum;
    }

    public final String getMeasrCorpId() {
        return measrCorpId;
    }

    public final void setMeasrCorpId(String measrCorpId) {
        this.measrCorpId = measrCorpId;
    }

    public final String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public final void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public final String getSellTypNm() {
        return sellTypNm;
    }

    public final void setSellTypNm(String sellTypNm) {
        this.sellTypNm = sellTypNm;
    }

    public final String getBizTypNm() {
        return bizTypNm;
    }

    public final void setBizTypNm(String bizTypNm) {
        this.bizTypNm = bizTypNm;
    }

    public final String getRecvSendCateId() {
        return recvSendCateId;
    }

    public final void setRecvSendCateId(String recvSendCateId) {
        this.recvSendCateId = recvSendCateId;
    }

    public final String getRecvSendCateNm() {
        return recvSendCateNm;
    }

    public final void setRecvSendCateNm(String recvSendCateNm) {
        this.recvSendCateNm = recvSendCateNm;
    }

    public final String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public final void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd;
    }

    public final String getGdsBitNm() {
        return gdsBitNm;
    }

    public final void setGdsBitNm(String gdsBitNm) {
        this.gdsBitNm = gdsBitNm;
    }

    public final String getRegnEncd() {
        return regnEncd;
    }

    public final void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd;
    }

    public final String getRegnNm() {
        return regnNm;
    }

    public final void setRegnNm(String regnNm) {
        this.regnNm = regnNm;
    }

    public final String getBaoZhiQi() {
        return baoZhiQi;
    }

    public final void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi;
    }

    public BigDecimal getBxRule() {
        return bxRule;
    }

    public void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

}

package com.px.mis.whs.entity;

import java.math.BigDecimal;

//����ӱ�
public class PickSnglSubTab {

    private Long orderNum;// ���
    private String pickSnglNum;// �������

    private String bizTypId;// ҵ�����ͱ��,
    private String sellTypId;// �������ͱ��,
    private BigDecimal qty;// ����,
    private BigDecimal bxRule;// ���,
    private String prdcDt;// ��������,
    private String invldtnDt;// ʧЧ����,
    private String spcModel;// ���,
    private String crspdBarCd;// ��Ӧ������,
    private String whsEncd;// �ֿ���,
    private String whsNm;// �ֿ�,
    private String invtyEncd;// �����,
    private String invtyNm;// ���,
    private String batNum;// ����,
    private String measrCorpId;// ������λ��,
    private String measrCorpNm;// ������λ,
    private String sellTypNm;// ��������,
    private String bizTypNm;// ҵ������,
    private String recvSendCateId;// �շ������,
    private String recvSendCateNm;// �շ����,
    private String gdsBitEncd;// ��λ��,
    private String gdsBitNm;// ��λ����,
    private String regnEncd;// ������,
    private String regnNm;// ������
    private String baoZhiQi;// ������

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

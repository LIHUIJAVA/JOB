package com.px.mis.whs.entity;

import java.math.BigDecimal;
import java.util.Date;

//�����
public class pick__sngl {
    private String formNum;//���ݺ�

    private String whsEncd;//�ֿ����

    private String whsNm;//�ֿ�����

    private String invtyEncd;//�������

    private String barCd;//������

    private String batNum;//����

    private Date prdcDt;//��������

    private String baoZhiQi;//������

    private Date invldtnDt;//ʧЧ����

    private Date pickDt;//�������

    private String gdsBit;//��λ

    private String sellTyp;//��������

    private String bizTyp;//ҵ������

    private BigDecimal qty;//����

    private String pickPers;//�����

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum == null ? null : formNum.trim();
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd == null ? null : whsEncd.trim();
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm == null ? null : whsNm.trim();
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd == null ? null : invtyEncd.trim();
    }

    public String getBarCd() {
        return barCd;
    }

    public void setBarCd(String barCd) {
        this.barCd = barCd == null ? null : barCd.trim();
    }

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum == null ? null : batNum.trim();
    }

    public Date getPrdcDt() {
        return prdcDt;
    }

    public void setPrdcDt(Date prdcDt) {
        this.prdcDt = prdcDt;
    }

    public String getBaoZhiQi() {
        return baoZhiQi;
    }

    public void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi == null ? null : baoZhiQi.trim();
    }

    public Date getInvldtnDt() {
        return invldtnDt;
    }

    public void setInvldtnDt(Date invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public Date getPickDt() {
        return pickDt;
    }

    public void setPickDt(Date pickDt) {
        this.pickDt = pickDt;
    }

    public String getGdsBit() {
        return gdsBit;
    }

    public void setGdsBit(String gdsBit) {
        this.gdsBit = gdsBit == null ? null : gdsBit.trim();
    }

    public String getSellTyp() {
        return sellTyp;
    }

    public void setSellTyp(String sellTyp) {
        this.sellTyp = sellTyp == null ? null : sellTyp.trim();
    }

    public String getBizTyp() {
        return bizTyp;
    }

    public void setBizTyp(String bizTyp) {
        this.bizTyp = bizTyp == null ? null : bizTyp.trim();
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getPickPers() {
        return pickPers;
    }

    public void setPickPers(String pickPers) {
        this.pickPers = pickPers == null ? null : pickPers.trim();
    }
}
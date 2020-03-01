package com.px.mis.whs.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//����
public class Regn {
    @JsonProperty("�������")
    private String regnEncd;// �������
    @JsonProperty("��������")
    private String regnNm;// ��������
    @JsonIgnore
    private String whsEncd;// �ֿ����
    @JsonIgnore
    private String whsNm;// �ֿ�ming
    @JsonProperty("��")
    private String longs;// ��
    @JsonProperty("��")
    private String wide;// ��
    @JsonProperty("�ض�����")
    private BigDecimal siteQty;// �ض�����
    @JsonProperty("��ע")
    private String memo;// ��ע
    @JsonProperty("������")
    private String setupPers;// ������
    @JsonProperty("����ʱ��")
    private String setupTm;// ����ʱ��
    @JsonProperty("�޸���")
    private String mdfr;// �޸���
    @JsonProperty("�޸�ʱ��")
    private String modiTm;// �޸�ʱ��
    @JsonIgnore
    private List<MovBitTab> movBitList;

    public final String getWhsNm() {
        return whsNm;
    }

    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public List<MovBitTab> getMovBitList() {
        return movBitList;
    }

    public void setMovBitList(List<MovBitTab> movBitList) {
        this.movBitList = movBitList;
    }

    public String getRegnEncd() {
        return regnEncd;
    }

    public void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd == null ? null : regnEncd.trim();
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd == null ? null : whsEncd.trim();
    }

    public String getRegnNm() {
        return regnNm;
    }

    public void setRegnNm(String regnNm) {
        this.regnNm = regnNm == null ? null : regnNm.trim();
    }

    public String getLongs() {
        return longs;
    }

    public void setLongs(String longs) {
        this.longs = longs == null ? null : longs.trim();
    }

    public String getWide() {
        return wide;
    }

    public void setWide(String wide) {
        this.wide = wide == null ? null : wide.trim();
    }

    public BigDecimal getSiteQty() {
        return siteQty;
    }

    public void setSiteQty(BigDecimal siteQty) {
        this.siteQty = siteQty;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers;
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }

}
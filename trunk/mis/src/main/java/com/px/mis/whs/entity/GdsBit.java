package com.px.mis.whs.entity;

import java.math.BigDecimal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//��λ
public class GdsBit {
    @JsonProperty("��λ����")
    private String gdsBitEncd;// ��λ����
    @JsonIgnore
    private String regnEncd;// �������
    @JsonIgnore
    private String whsEncd;// �ֿ����
    @JsonProperty("��λ����")
    private String gdsBitNm;// ��λ����
    //	@JsonProperty("������˾���")
    @JsonProperty("��λ���ͱ���")
    private String gdsBitTyp;// ��������
    // ��λ����
    @JsonProperty("��λ��������")
    private String gdsBitTypNm;// ��λ��������(���ܡ��ض�)
    @JsonProperty("��λ����")
//    @JsonIgnore
    private String gdsBitCd;// ��λ����
    @JsonProperty("��λ�����")
//    @JsonIgnore
    private BigDecimal gdsBitQty;// ��λ�����
    @JsonProperty("��ע")
    private String memo;// ��ע
//    @JsonProperty("��λ���ͱ���")
    @JsonIgnore
    private String gdsBitCdEncd;// ��λ�����
    @JsonProperty("������")
    private String setupPers;// ������
    @JsonProperty("����ʱ��")
    private String setupTm;// ����ʱ��
    @JsonProperty("�޸���")
    private String mdfr;// �޸���
    @JsonProperty("�޸�ʱ��")
    private String modiTm;// �޸�ʱ��
    @JsonIgnore
    private String gdsBitTypEncd;// ��λ���ͱ���
    @JsonIgnore
    private List<MovBitTab> movBitList;
    @JsonIgnore
    private String whsNm;// �ֿ�����
    @JsonIgnore
    private String regnNm;// ��������

    public final String getWhsNm() {
        return whsNm;
    }

    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public final String getRegnNm() {
        return regnNm;
    }

    public final void setRegnNm(String regnNm) {
        this.regnNm = regnNm;
    }

    /**
     * snow2019��5��31��
     *
     * @return whsEncd
     */
    public String getWhsEncd() {
        return whsEncd;
    }

    /**
     * snow2019��5��31��
     *
     * @param whsEncd Ҫ���õ� whsEncd
     */
    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getGdsBitTypNm() {
        return gdsBitTypNm;
    }

    public void setGdsBitTypNm(String gdsBitTypNm) {
        this.gdsBitTypNm = gdsBitTypNm;
    }

    public String getGdsBitTypEncd() {
        return gdsBitTypEncd;
    }

    public void setGdsBitTypEncd(String gdsBitTypEncd) {
        this.gdsBitTypEncd = gdsBitTypEncd;
    }

    public List<MovBitTab> getMovBitList() {
        return movBitList;
    }

    public void setMovBitList(List<MovBitTab> movBitList) {
        this.movBitList = movBitList;
    }

    public BigDecimal getGdsBitQty() {
        return gdsBitQty;
    }

    public void setGdsBitQty(BigDecimal gdsBitQty) {
        this.gdsBitQty = gdsBitQty;
    }

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd == null ? null : gdsBitEncd.trim();
    }

    public String getRegnEncd() {
        return regnEncd;
    }

    public void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd == null ? null : regnEncd.trim();
    }

    public String getGdsBitNm() {
        return gdsBitNm;
    }

    public void setGdsBitNm(String gdsBitNm) {
        this.gdsBitNm = gdsBitNm == null ? null : gdsBitNm.trim();
    }

    public String getGdsBitTyp() {
        return gdsBitTyp;
    }

    public void setGdsBitTyp(String gdsBitTyp) {
        this.gdsBitTyp = gdsBitTyp == null ? null : gdsBitTyp.trim();
    }

    public String getGdsBitCd() {
        return gdsBitCd;
    }

    public void setGdsBitCd(String gdsBitCd) {
        this.gdsBitCd = gdsBitCd == null ? null : gdsBitCd.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getGdsBitCdEncd() {
        return gdsBitCdEncd;
    }

    public void setGdsBitCdEncd(String gdsBitCdEncd) {
        this.gdsBitCdEncd = gdsBitCdEncd == null ? null : gdsBitCdEncd.trim();
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
package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//���̨��
public class InvtyStandReport {
    // ����
    @JsonProperty("���ݺ�")
    private String formNum;
    // ���ʱ��
    @JsonProperty("�������")
    private String chkTm;
    // ��������
    @JsonProperty("��������")
    private String formDt;
    // �ֿ����
    @JsonProperty("�ֿ����")
    private String whsEncd;
    // �ֿ�
    @JsonProperty("�ֿ�����")
    private String whsNm;
    //	�������
    @JsonProperty("�������")
    private String invtyEncd;
    //	 �������
    @JsonProperty("�������")
    private String invtyNm;
    // ��������
    @JsonProperty("��������")
    private String formType;
    // ��������
    @JsonProperty("�������ͱ���")
    private String outIntoWhsTypId;
    // ��������
    @JsonProperty("��������")
    private BigDecimal intoQty;
    // ��������
    @JsonProperty("��������")
    private BigDecimal outQty;
    // �������
    @JsonProperty("�������")
    private BigDecimal balance;

    // ����
    @JsonIgnore
    private BigDecimal qty;

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getOutIntoWhsTypId() {
        return outIntoWhsTypId;
    }

    public void setOutIntoWhsTypId(String outIntoWhsTypId) {
        this.outIntoWhsTypId = outIntoWhsTypId;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public String getFormDt() {
        return formDt;
    }

    public void setFormDt(String formDt) {
        this.formDt = formDt;
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public BigDecimal getIntoQty() {
        return intoQty;
    }

    public void setIntoQty(BigDecimal intoQty) {
        this.intoQty = intoQty;
    }

    public BigDecimal getOutQty() {
        return outQty;
    }

    public void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

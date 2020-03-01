package com.px.mis.whs.entity;

import java.math.BigDecimal;
import java.util.List;

import com.px.mis.purc.entity.InvtyDoc;

//��λ��
public class MovBitTab {
    private Integer id;// ����
    private String orderNum;// ����
    private String serialNum;// ���ŵ����
    private Long movBitEncd;// ��λ����

    private String invtyEncd;// �������

    private String whsEncd;// �ֿ����

    private String regnEncd;// �������

    private String gdsBitEncd;// ��λ����

    private BigDecimal qty;// ����

    private String gdsBitEncd1;// ԭʼ��λ����

    private BigDecimal qty1;// ԭʼ����

    private String gdsBitEncd2;// Ŀ���λ����

    private BigDecimal qty2;// ��λ����

    private String batNum;// ����

    private String prdcDt;// ��������

    private String intoDt;// �������

    private List<InvtyDoc> iDocList;/* ������� */

    // ��λ��
    private WhsDoc whsDoc;// �ֿ⵵��
    private Regn regn;// ����
    private InvtyDoc invtyDoc;/* ������� */
    private InvtyTab invtyTab;// ����

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum;
    }

    public String getPrdcDt() {
        return prdcDt;
    }

    public void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
    }

    public String getIntoDt() {
        return intoDt;
    }

    public void setIntoDt(String intoDt) {
        this.intoDt = intoDt;
    }

    public String getGdsBitEncd1() {
        return gdsBitEncd1;
    }

    public void setGdsBitEncd1(String gdsBitEncd1) {
        this.gdsBitEncd1 = gdsBitEncd1;
    }

    public BigDecimal getQty1() {
        return qty1;
    }

    public void setQty1(BigDecimal qty1) {
        this.qty1 = qty1;
    }

    public String getGdsBitEncd2() {
        return gdsBitEncd2;
    }

    public void setGdsBitEncd2(String gdsBitEncd2) {
        this.gdsBitEncd2 = gdsBitEncd2;
    }

    public BigDecimal getQty2() {
        return qty2;
    }

    public void setQty2(BigDecimal qty2) {
        this.qty2 = qty2;
    }

    public InvtyTab getInvtyTab() {
        return invtyTab;
    }

    public void setInvtyTab(InvtyTab invtyTab) {
        this.invtyTab = invtyTab;
    }

    public List<InvtyDoc> getiDocList() {
        return iDocList;
    }

    public void setiDocList(List<InvtyDoc> iDocList) {
        this.iDocList = iDocList;
    }

    public InvtyDoc getInvtyDoc() {
        return invtyDoc;
    }

    public void setInvtyDoc(InvtyDoc invtyDoc) {
        this.invtyDoc = invtyDoc;
    }

    public WhsDoc getWhsDoc() {
        return whsDoc;
    }

    public void setWhsDoc(WhsDoc whsDoc) {
        this.whsDoc = whsDoc;
    }

    public Regn getRegn() {
        return regn;
    }

    public void setRegn(Regn regn) {
        this.regn = regn;
    }

    public Long getMovBitEncd() {
        return movBitEncd;
    }

    public void setMovBitEncd(Long movBitEncd) {
        this.movBitEncd = movBitEncd;
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getRegnEncd() {
        return regnEncd;
    }

    public void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd;
    }

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public final Integer getId() {
        return id;
    }

    public final void setId(Integer id) {
        this.id = id;
    }

    public final String getOrderNum() {
        return orderNum;
    }

    public final void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public final String getSerialNum() {
        return serialNum;
    }

    public final void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

}

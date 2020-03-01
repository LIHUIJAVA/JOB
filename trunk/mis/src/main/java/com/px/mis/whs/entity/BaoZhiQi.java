package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaoZhiQi {
    /**
     * �ֿ����
     */
    @JsonProperty("�ֿ����")
    private String whs_encd;
    /**
     * �ֿ�����
     */
    @JsonProperty("�ֿ�����")

    private String whs_nm;
    /**
     * �������
     */
    @JsonProperty("�������")

    private String invty_encd;
    /**
     * �������
     */
    @JsonProperty("�������")

    private String invty_nm;
    /**
     * ����
     */
    @JsonProperty("����")

    private String bat_num;
    /**
     * ����ͺ�
     */
    @JsonProperty("����ͺ�")

    private String spc_model;
    @JsonProperty("���")
    private BigDecimal  bxRule;
    @JsonProperty("����")
    private BigDecimal  bxQty;
    /**
     * ��������λ
     */
    @JsonProperty("��������λ")

    private String measr_corp_nm;
    /**
     * �������
     */
    @JsonProperty("�������")

    private BigDecimal now_stok;
    /**
     * δ˰����
     */
    @JsonProperty("δ˰����")

    private BigDecimal un_tax_uprc;
    /**
     * ��˰����
     */
    @JsonProperty("��˰����")

    private BigDecimal cntn_tax_uprc;
    /**
     * δ˰���
     */
    @JsonProperty("δ˰���")

    private BigDecimal un_tax_amt;
    /**
     * ��˰���
     */
    @JsonProperty("��˰���")

    private BigDecimal cntn_tax_amt;

    /**
     * ��������
     */
    @JsonProperty("��������")

    private String prdcdt;
    /**
     * ������
     */
    @JsonProperty("������")

    private String bao_zhi_qi;
    /**
     * ʧЧ����
     */
    @JsonProperty("ʧЧ����")

    private String invldtndt;
    /**
     * ʣ������
     */
    @JsonProperty("ʣ������")

    private String overdueDays;
    /**
     * ״̬
     */
    @JsonProperty("״̬")

    private String stat;

    public BigDecimal getBxRule() {
        return bxRule;
    }

    public void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public BigDecimal getBxQty() {
        return bxQty;
    }

    public void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
    }

    public final String getWhs_encd() {
        return whs_encd;
    }

    public final void setWhs_encd(String whs_encd) {
        this.whs_encd = whs_encd;
    }

    public final String getWhs_nm() {
        return whs_nm;
    }

    public final void setWhs_nm(String whs_nm) {
        this.whs_nm = whs_nm;
    }

    public final String getInvty_encd() {
        return invty_encd;
    }

    public final void setInvty_encd(String invty_encd) {
        this.invty_encd = invty_encd;
    }

    public final String getInvty_nm() {
        return invty_nm;
    }

    public final void setInvty_nm(String invty_nm) {
        this.invty_nm = invty_nm;
    }

    public final String getBat_num() {
        return bat_num;
    }

    public final void setBat_num(String bat_num) {
        this.bat_num = bat_num;
    }

    public final String getSpc_model() {
        return spc_model;
    }

    public final void setSpc_model(String spc_model) {
        this.spc_model = spc_model;
    }

    public final String getMeasr_corp_nm() {
        return measr_corp_nm;
    }

    public final void setMeasr_corp_nm(String measr_corp_nm) {
        this.measr_corp_nm = measr_corp_nm;
    }

    public final BigDecimal getNow_stok() {
        return now_stok;
    }

    public final void setNow_stok(BigDecimal now_stok) {
        this.now_stok = now_stok;
    }

    public final BigDecimal getUn_tax_uprc() {
        return un_tax_uprc;
    }

    public final void setUn_tax_uprc(BigDecimal un_tax_uprc) {
        this.un_tax_uprc = un_tax_uprc;
    }

    public final BigDecimal getCntn_tax_uprc() {
        return cntn_tax_uprc;
    }

    public final void setCntn_tax_uprc(BigDecimal cntn_tax_uprc) {
        this.cntn_tax_uprc = cntn_tax_uprc;
    }

    public final BigDecimal getUn_tax_amt() {
        return un_tax_amt;
    }

    public final void setUn_tax_amt(BigDecimal un_tax_amt) {
        this.un_tax_amt = un_tax_amt;
    }

    public final BigDecimal getCntn_tax_amt() {
        return cntn_tax_amt;
    }

    public final void setCntn_tax_amt(BigDecimal cntn_tax_amt) {
        this.cntn_tax_amt = cntn_tax_amt;
    }

    public final String getPrdcdt() {
        return prdcdt;
    }

    public final void setPrdcdt(String prdcdt) {
        this.prdcdt = prdcdt;
    }

    public final String getBao_zhi_qi() {
        return bao_zhi_qi;
    }

    public final void setBao_zhi_qi(String bao_zhi_qi) {
        this.bao_zhi_qi = bao_zhi_qi;
    }

    public final String getInvldtndt() {
        return invldtndt;
    }

    public final void setInvldtndt(String invldtndt) {
        this.invldtndt = invldtndt;
    }

    public final String getOverdueDays() {
        return overdueDays;
    }

    public final void setOverdueDays(String overdueDays) {
        this.overdueDays = overdueDays;
    }

    public final String getStat() {
        return stat;
    }

    public final void setStat(String stat) {
        this.stat = stat;
    }
}

package com.px.mis.sell.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * �������ܱ�
 */
public class StatisticalCount {

	@JsonProperty("�ͻ����")

	private String custId;


	@JsonProperty("�ͻ����")

	private String custShtNm;

	@JsonProperty("�ͻ�����")

	private String custNm;


	@JsonProperty("�������")

	private String invtyEncd;

	@JsonProperty("�������")

	private String invtyNm;

	@JsonProperty("����ͺ�")

	private String spcModel;

	@JsonProperty("�˻�����")

	private BigDecimal rtnChuQty;
	@JsonProperty("�˻���˰���")

	private BigDecimal rtnNoTaxAmt;
	@JsonProperty("�˻���˰���")

	private BigDecimal rtnPrcTaxSum;

	@JsonProperty("����")

	private BigDecimal sellQty;
	@JsonProperty("������˰���")
	private BigDecimal sellNoTaxAmt;
	@JsonProperty("������˰���")
	private BigDecimal sellPrcTaxSum;

	@JsonProperty("��Ʊ����")

	private BigDecimal kpQty;
	@JsonProperty("��Ʊ��˰���")
	private BigDecimal kpAmt;
	@JsonProperty("��Ʊ��˰���")
	private BigDecimal kpAmtSum;

	@JsonProperty("��������")

	private BigDecimal sellOutQty;
	@JsonProperty("������˰���")
	private BigDecimal sellOutNoTaxAmt;
	@JsonProperty("���⺬˰���")
	private BigDecimal sellOutPrcTaxSum;

	@JsonProperty("����������")

	private BigDecimal rtnSellQty;
	@JsonProperty("��������˰���")

	private BigDecimal rtnSellNoTaxAmt;
	@JsonProperty("�����ۺ�˰���")

	private BigDecimal rtnSellPrcTaxSum;

    @Override
    public String toString() {
        return "StatisticalCount [custId=" + custId + ", custShtNm=" + custShtNm + ", custNm=" + custNm + ", invtyEncd="
                + invtyEncd + ", invtyNm=" + invtyNm + ", spcModel=" + spcModel + ", rtnChuQty=" + rtnChuQty
                + ", rtnNoTaxAmt=" + rtnNoTaxAmt + ", rtnPrcTaxSum=" + rtnPrcTaxSum + ", sellQty=" + sellQty
                + ", sellNoTaxAmt=" + sellNoTaxAmt + ", sellPrcTaxSum=" + sellPrcTaxSum + ", kpQty=" + kpQty
                + ", kpAmt=" + kpAmt + ", kpAmtSum=" + kpAmtSum + ", sellOutQty=" + sellOutQty + ", sellOutNoTaxAmt="
                + sellOutNoTaxAmt + ", sellOutPrcTaxSum=" + sellOutPrcTaxSum + ", rtnSellQty=" + rtnSellQty
                + ", rtnSellNoTaxAmt=" + rtnSellNoTaxAmt + ", rtnSellPrcTaxSum=" + rtnSellPrcTaxSum + "]";
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustShtNm() {
        return custShtNm;
    }

    public void setCustShtNm(String custShtNm) {
        this.custShtNm = custShtNm;
    }

    public String getCustNm() {
        return custNm;
    }

    public void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    public String getInvtyNm() {
        return invtyNm;
    }

    public void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public String getSpcModel() {
        return spcModel;
    }

    public void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    public BigDecimal getRtnChuQty() {
        return rtnChuQty;
    }

    public void setRtnChuQty(BigDecimal rtnChuQty) {
        this.rtnChuQty = rtnChuQty;
    }

    public BigDecimal getRtnNoTaxAmt() {
        return rtnNoTaxAmt;
    }

    public void setRtnNoTaxAmt(BigDecimal rtnNoTaxAmt) {
        this.rtnNoTaxAmt = rtnNoTaxAmt;
    }

    public BigDecimal getRtnPrcTaxSum() {
        return rtnPrcTaxSum;
    }

    public void setRtnPrcTaxSum(BigDecimal rtnPrcTaxSum) {
        this.rtnPrcTaxSum = rtnPrcTaxSum;
    }

    public BigDecimal getSellQty() {
        return sellQty;
    }

    public void setSellQty(BigDecimal sellQty) {
        this.sellQty = sellQty;
    }

    public BigDecimal getSellNoTaxAmt() {
        return sellNoTaxAmt;
    }

    public void setSellNoTaxAmt(BigDecimal sellNoTaxAmt) {
        this.sellNoTaxAmt = sellNoTaxAmt;
    }

    public BigDecimal getSellPrcTaxSum() {
        return sellPrcTaxSum;
    }

    public void setSellPrcTaxSum(BigDecimal sellPrcTaxSum) {
        this.sellPrcTaxSum = sellPrcTaxSum;
    }

    public BigDecimal getKpQty() {
        return kpQty;
    }

    public void setKpQty(BigDecimal kpQty) {
        this.kpQty = kpQty;
    }

    public BigDecimal getKpAmt() {
        return kpAmt;
    }

    public void setKpAmt(BigDecimal kpAmt) {
        this.kpAmt = kpAmt;
    }

    public BigDecimal getKpAmtSum() {
        return kpAmtSum;
    }

    public void setKpAmtSum(BigDecimal kpAmtSum) {
        this.kpAmtSum = kpAmtSum;
    }

    public BigDecimal getSellOutQty() {
        return sellOutQty;
    }

    public void setSellOutQty(BigDecimal sellOutQty) {
        this.sellOutQty = sellOutQty;
    }

    public BigDecimal getSellOutNoTaxAmt() {
        return sellOutNoTaxAmt;
    }

    public void setSellOutNoTaxAmt(BigDecimal sellOutNoTaxAmt) {
        this.sellOutNoTaxAmt = sellOutNoTaxAmt;
    }

    public BigDecimal getSellOutPrcTaxSum() {
        return sellOutPrcTaxSum;
    }

    public void setSellOutPrcTaxSum(BigDecimal sellOutPrcTaxSum) {
        this.sellOutPrcTaxSum = sellOutPrcTaxSum;
    }

    public BigDecimal getRtnSellQty() {
        return rtnSellQty;
    }

    public void setRtnSellQty(BigDecimal rtnSellQty) {
        this.rtnSellQty = rtnSellQty;
    }

    public BigDecimal getRtnSellNoTaxAmt() {
        return rtnSellNoTaxAmt;
    }

    public void setRtnSellNoTaxAmt(BigDecimal rtnSellNoTaxAmt) {
        this.rtnSellNoTaxAmt = rtnSellNoTaxAmt;
    }

    public BigDecimal getRtnSellPrcTaxSum() {
        return rtnSellPrcTaxSum;
    }

    public void setRtnSellPrcTaxSum(BigDecimal rtnSellPrcTaxSum) {
        this.rtnSellPrcTaxSum = rtnSellPrcTaxSum;
    }
}

package com.px.mis.sell.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * 发货汇总表
 */
public class StatisticalCount {

	@JsonProperty("客户编号")

	private String custId;


	@JsonProperty("客户简称")

	private String custShtNm;

	@JsonProperty("客户名称")

	private String custNm;


	@JsonProperty("存货编码")

	private String invtyEncd;

	@JsonProperty("存货名称")

	private String invtyNm;

	@JsonProperty("规格型号")

	private String spcModel;

	@JsonProperty("退货数量")

	private BigDecimal rtnChuQty;
	@JsonProperty("退货无税金额")

	private BigDecimal rtnNoTaxAmt;
	@JsonProperty("退货含税金额")

	private BigDecimal rtnPrcTaxSum;

	@JsonProperty("发货")

	private BigDecimal sellQty;
	@JsonProperty("发货无税金额")
	private BigDecimal sellNoTaxAmt;
	@JsonProperty("发货含税金额")
	private BigDecimal sellPrcTaxSum;

	@JsonProperty("开票数量")

	private BigDecimal kpQty;
	@JsonProperty("开票无税金额")
	private BigDecimal kpAmt;
	@JsonProperty("开票含税金额")
	private BigDecimal kpAmtSum;

	@JsonProperty("出库数量")

	private BigDecimal sellOutQty;
	@JsonProperty("出库无税金额")
	private BigDecimal sellOutNoTaxAmt;
	@JsonProperty("出库含税金额")
	private BigDecimal sellOutPrcTaxSum;

	@JsonProperty("净销售数量")

	private BigDecimal rtnSellQty;
	@JsonProperty("净销售无税金额")

	private BigDecimal rtnSellNoTaxAmt;
	@JsonProperty("净销售含税金额")

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

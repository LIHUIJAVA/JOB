package com.px.mis.sell.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliveryStatistic {
	@JsonProperty("单据编号")
	private	String sellSnglId;//单据编号
	@JsonProperty("单据日期")
	private	String	sellSnglDt;//单据日期
	@JsonProperty("客户编码")
	private	String	custId;//客户编码
	@JsonProperty("客户名称")
	private	String	custNm;//客户名称
	@JsonProperty("仓库名称")
	private	String	whsNm;//仓库名称
	@JsonProperty("存货编码")
	private	String	invtyEncd;//存货编码
	@JsonProperty("存货名称")
	private	String	invtyNm;//存货名称
	@JsonProperty("规格")
	private	String	spcModel;//规格
	@JsonProperty("主计量单位")
	private	String	measrCorpNm;//主计量单位
	@JsonProperty("发货数量")
	private	BigDecimal	qty;//发货数量
	@JsonProperty("发货价税合计")
	private	BigDecimal	prcTaxSum;//发货价税合计
	@JsonProperty("开票数量")
	private	BigDecimal	bllgQty;//开票数量
	@JsonProperty("出库数量")
	private	BigDecimal	outQty;//出库数量
	@JsonProperty("出库成本")
	private	BigDecimal	outNoTaxAmt;//出库成本
	@JsonProperty("净发货")
	private	BigDecimal	netDelivery;//净发货
	@JsonProperty("净退货")
	private	BigDecimal	netReturn;//净退货
	
	
	public String getSellSnglId() {
		
		return sellSnglId;
	}
	public void setSellSnglId(String sellSnglId) {
		this.sellSnglId = sellSnglId;
	}
	public String getSellSnglDt() {
		return sellSnglDt;
	}
	public void setSellSnglDt(String sellSnglDt) {
		this.sellSnglDt = sellSnglDt;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustNm() {
		return custNm;
	}
	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}
	public String getWhsNm() {
		return whsNm;
	}
	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
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
	public String getMeasrCorpNm() {
		return measrCorpNm;
	}
	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public BigDecimal getPrcTaxSum() {
		return prcTaxSum;
	}
	public void setPrcTaxSum(BigDecimal prcTaxSum) {
		this.prcTaxSum = prcTaxSum;
	}
	public BigDecimal getBllgQty() {
		return bllgQty;
	}
	public void setBllgQty(BigDecimal bllgQty) {
		this.bllgQty = bllgQty;
	}
	public BigDecimal getOutQty() {
		return outQty;
	}
	public void setOutQty(BigDecimal outQty) {
		this.outQty = outQty;
	}
	public BigDecimal getOutNoTaxAmt() {
		return outNoTaxAmt;
	}
	public void setOutNoTaxAmt(BigDecimal outNoTaxAmt) {
		this.outNoTaxAmt = outNoTaxAmt;
	}
	public BigDecimal getNetDelivery() {
		return netDelivery;
	}
	public void setNetDelivery(BigDecimal netDelivery) {
		this.netDelivery = netDelivery;
	}
	public BigDecimal getNetReturn() {
		return netReturn;
	}
	public void setNetReturn(BigDecimal netReturn) {
		this.netReturn = netReturn;
	}
	
	

}

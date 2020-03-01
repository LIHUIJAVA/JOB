package com.px.mis.account.entity;
//出入库调整单子表

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class OutIntoWhsAdjSnglSubTab {
	@JsonProperty("序号")
	private Integer ordrNum;//序号
	@JsonProperty("单据号")
	private String formNum;//单据号 出入库调整单主表标识
	@JsonProperty("被调整出入库子表标识")
	private Integer adjSubInd;//被调整出入库子表标识
	@JsonProperty("仓库编码")
	private String whsEncd;//仓库编码
	@JsonProperty("存货编码")
	private String invtyEncd;//存货编码
	@JsonProperty("批号")
	private String batNum;//批号
	@JsonProperty("金额")
	private BigDecimal amt;//金额
	@JsonProperty("明细子表备注")
	private String memo;//备注
	@JsonProperty("子备注")
	private String memos;//备注
	@JsonProperty("仓库名称")
	private String whsNm;//仓库名称
	@JsonProperty("存货名称")
	private String invtyNm;//存货名称；
	@JsonProperty("规格型号")
	private String spcModel;//规格型号
	@JsonProperty("箱规")
	private BigDecimal bxRule;//箱规
	@JsonProperty("计量单位名称")
	private String measrCorpNm;//计量单位名称
	@JsonProperty("来源子表序号")
	private Long toOrdrNum;//来源子表序号
	@JsonProperty("项目编码")
	private String projEncd;//项目编码
	@JsonProperty("项目名称")
    private String projNm;//项目名称
	
	public String getProjNm() {
		return projNm;
	}
	public void setProjNm(String projNm) {
		this.projNm = projNm;
	}
	
	public String getProjEncd() {
		return projEncd;
	}
	public void setProjEncd(String projEncd) {
		this.projEncd = projEncd;
	}
	public Long getToOrdrNum() {
		return toOrdrNum;
	}
	public void setToOrdrNum(Long toOrdrNum) {
		this.toOrdrNum = toOrdrNum;
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	
	public String getFormNum() {
		return formNum;
	}

	public void setFormNum(String formNum) {
		this.formNum = formNum;
	}

	public Integer getAdjSubInd() {
		return adjSubInd;
	}
	public void setAdjSubInd(Integer adjSubInd) {
		this.adjSubInd = adjSubInd;
	}
	public String getWhsEncd() {
		return whsEncd;
	}
	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getBatNum() {
		return batNum;
	}
	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public String getMemo() {
		return memos;
	}
	public void setMemo(String memo) {
		this.memos = memo;
	}
	public String getWhsNm() {
		return whsNm;
	}
	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
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
	public BigDecimal getBxRule() {
		return bxRule;
	}
	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}
	public String getMeasrCorpNm() {
		return measrCorpNm;
	}
	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
}

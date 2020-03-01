package com.px.mis.account.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 借方
 * @author lxya0
 *
 */
public class SubjectDebit {
	@JsonProperty("借方科目编号")
	private String subDebitId;//科目编号
	@JsonProperty("借方科目类型")
  	private String subDebitType;//科目类型2
	@JsonProperty("借方科目名称")
	private String subDebitNm;//科目名称
	@JsonProperty("借方金额")
	private BigDecimal subDebitMoney;//借方金额
	@JsonProperty("借方-贷方金额")
	private BigDecimal subDebitCreditMoney;//借方-贷方金额
	@JsonProperty("借方数量")
	private BigDecimal subDebitNum;//借方数量
	@JsonProperty("借方-贷方数量")
	private BigDecimal subDebitCreditNum;//借方-贷方数量
	@JsonProperty("借方科目方向")
	private Integer subDebitPath;//科目方向 借1贷2
	
	public SubjectDebit() {
		super();
	}
	public String getSubDebitId() {
		return subDebitId;
	}
	public void setSubDebitId(String subDebitId) {
		this.subDebitId = subDebitId;
	}
	public String getSubDebitType() {
		return subDebitType;
	}
	public void setSubDebitType(String subDebitType) {
		this.subDebitType = subDebitType;
	}
	public String getSubDebitNm() {
		return subDebitNm;
	}
	public void setSubDebitNm(String subDebitNm) {
		this.subDebitNm = subDebitNm;
	}
	public BigDecimal getSubDebitMoney() {
		return subDebitMoney;
	}
	public void setSubDebitMoney(BigDecimal subDebitMoney) {
		this.subDebitMoney = subDebitMoney;
	}
	public BigDecimal getSubDebitNum() {
		return subDebitNum;
	}
	public void setSubDebitNum(BigDecimal subDebitNum) {
		this.subDebitNum = subDebitNum;
	}
	public Integer getSubDebitPath() {
		return subDebitPath;
	}
	public void setSubDebitPath(Integer subDebitPath) {
		this.subDebitPath = subDebitPath;
	}
	public BigDecimal getSubDebitCreditMoney() {
		return subDebitCreditMoney;
	}
	public void setSubDebitCreditMoney(BigDecimal subDebitCreditMoney) {
		this.subDebitCreditMoney = subDebitCreditMoney;
	}
	public BigDecimal getSubDebitCreditNum() {
		return subDebitCreditNum;
	}
	public void setSubDebitCreditNum(BigDecimal subDebitCreditNum) {
		this.subDebitCreditNum = subDebitCreditNum;
	}
	public SubjectDebit(String subDebitId, String subDebitType, String subDebitNm, BigDecimal subDebitMoney,
			BigDecimal subDebitCreditMoney, BigDecimal subDebitNum, BigDecimal subDebitCreditNum,
			Integer subDebitPath) {
		super();
		this.subDebitId = subDebitId;
		this.subDebitType = subDebitType;
		this.subDebitNm = subDebitNm;
		this.subDebitMoney = subDebitMoney;
		this.subDebitCreditMoney = subDebitCreditMoney;
		this.subDebitNum = subDebitNum;
		this.subDebitCreditNum = subDebitCreditNum;
		this.subDebitPath = subDebitPath;
	}
	
	

	
	
	
}

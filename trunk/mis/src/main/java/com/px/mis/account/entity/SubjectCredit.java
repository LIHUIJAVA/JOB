package com.px.mis.account.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 贷方
 * @author lxya0
 *
 */
public class SubjectCredit {
	@JsonProperty("贷方科目编号")
  	private String subCreditId;//科目编号
	@JsonProperty("贷方科目类型")
  	private String subCreditType;//科目类型2
	@JsonProperty("贷方科目名称")
	private String subCreditNm;//科目名称
	@JsonProperty("贷方金额")
	private BigDecimal subCreditMoney;//贷方金额
	@JsonProperty("贷方-借方金额")
	private BigDecimal subCreditDebitMoney;//贷方-借方金额
	@JsonProperty("贷方数量")
	private BigDecimal subCreditNum;//贷方数量
	@JsonProperty("贷方-借方数量")
	private BigDecimal subCreditDebitNum;//贷方-借方数量
	@JsonProperty("贷方科目方向")
	private Integer subCreditPath;//科目方向 借1贷2
	
	public SubjectCredit() {
		super();
	}
	public String getSubCreditId() {
		return subCreditId;
	}
	public void setSubCreditId(String subCreditId) {
		this.subCreditId = subCreditId;
	}
	public String getSubCreditType() {
		return subCreditType;
	}
	public void setSubCreditType(String subCreditType) {
		this.subCreditType = subCreditType;
	}
	public String getSubCreditNm() {
		return subCreditNm;
	}
	public void setSubCreditNm(String subCreditNm) {
		this.subCreditNm = subCreditNm;
	}
	
	public BigDecimal getSubCreditMoney() {
		return subCreditMoney;
	}
	public void setSubCreditMoney(BigDecimal subCreditMoney) {
		this.subCreditMoney = subCreditMoney;
	}
	public BigDecimal getSubCreditNum() {
		return subCreditNum;
	}
	public void setSubCreditNum(BigDecimal subCreditNum) {
		this.subCreditNum = subCreditNum;
	}
	public Integer getSubCreditPath() {
		return subCreditPath;
	}
	public void setSubCreditPath(Integer subCreditPath) {
		this.subCreditPath = subCreditPath;
	}
	public BigDecimal getSubCreditDebitMoney() {
		return subCreditDebitMoney;
	}
	public void setSubCreditDebitMoney(BigDecimal subCreditDebitMoney) {
		this.subCreditDebitMoney = subCreditDebitMoney;
	}
	public BigDecimal getSubCreditDebitNum() {
		return subCreditDebitNum;
	}
	public void setSubCreditDebitNum(BigDecimal subCreditDebitNum) {
		this.subCreditDebitNum = subCreditDebitNum;
	}
	public SubjectCredit(String subCreditId, String subCreditType, String subCreditNm, BigDecimal subCreditMoney,
			BigDecimal subCreditDebitMoney, BigDecimal subCreditNum, BigDecimal subCreditDebitNum,
			Integer subCreditPath) {
		super();
		this.subCreditId = subCreditId;
		this.subCreditType = subCreditType;
		this.subCreditNm = subCreditNm;
		this.subCreditMoney = subCreditMoney;
		this.subCreditDebitMoney = subCreditDebitMoney;
		this.subCreditNum = subCreditNum;
		this.subCreditDebitNum = subCreditDebitNum;
		this.subCreditPath = subCreditPath;
	}
	
	
	
	
}

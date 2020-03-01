package com.px.mis.sell.entity;

import java.math.BigDecimal;

/**
 * 发货统计表
 */
public class SellStatisticalReport {
	/*
	 * 客户编号 部门 部门编号 业务员 业务员编号 客户名称 存货名称 存货编码 仓库名 仓库编码 存货分类 存货分类编码 箱规 批号 期初数量 期初金额
	 * 期初税额 期初价税合计 发货数量 发货金额(无税金额) 发货税额 发货价税合计 结存数量 结存金额 结存税额 结存价税合计 数量差异 金额差异 税额差异
	 * 价税合计差异
	 */

	private String custId;
	private String depName;
	private String depId;
	private String userName;
	private String accNum;
	private String custNm;
	private String invtyNm;
	private String invtyEncd;
	private String whsNm;
	private String whsEncd;
	private String invtyClsNm;
	private String invtyClsEncd;
	private String bxRule;
	private String batNum;
	private BigDecimal qiChuQty;
	private BigDecimal qiChuNoTaxAmt;
	private BigDecimal qiChuTaxAmt;
	private BigDecimal qiChuPrcTaxSum;
	private BigDecimal sellQty;
	private BigDecimal sellNoTaxAmt;
	private BigDecimal sellTaxAmt;
	private BigDecimal sellPrcTaxSum;
	private BigDecimal cumulativeQty;
	private BigDecimal cumulativeNoTaxAmt;
	private BigDecimal cumulativeTaxAmt;
	private BigDecimal cumulativePrcTaxSum;
	private BigDecimal differencesQty;
	private BigDecimal differencesNoTaxAmt;
	private BigDecimal differencesTaxAmt;
	private BigDecimal differencesPrcTaxSum;
	//开票的 
    private BigDecimal kPQty;
    private BigDecimal kPAmt;
    private BigDecimal kPTaxAmt;
    private BigDecimal kPAmtSum;
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAccNum() {
		return accNum;
	}
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}
	public String getCustNm() {
		return custNm;
	}
	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}
	public String getInvtyNm() {
		return invtyNm;
	}
	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getWhsNm() {
		return whsNm;
	}
	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}
	public String getWhsEncd() {
		return whsEncd;
	}
	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}
	public String getInvtyClsNm() {
		return invtyClsNm;
	}
	public void setInvtyClsNm(String invtyClsNm) {
		this.invtyClsNm = invtyClsNm;
	}
	public String getInvtyClsEncd() {
		return invtyClsEncd;
	}
	public void setInvtyClsEncd(String invtyClsEncd) {
		this.invtyClsEncd = invtyClsEncd;
	}
	public String getBxRule() {
		return bxRule;
	}
	public void setBxRule(String bxRule) {
		this.bxRule = bxRule;
	}
	public String getBatNum() {
		return batNum;
	}
	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}
	public BigDecimal getQiChuQty() {
		return qiChuQty;
	}
	public void setQiChuQty(BigDecimal qiChuQty) {
		this.qiChuQty = qiChuQty;
	}
	public BigDecimal getQiChuNoTaxAmt() {
		return qiChuNoTaxAmt;
	}
	public void setQiChuNoTaxAmt(BigDecimal qiChuNoTaxAmt) {
		this.qiChuNoTaxAmt = qiChuNoTaxAmt;
	}
	public BigDecimal getQiChuTaxAmt() {
		return qiChuTaxAmt;
	}
	public void setQiChuTaxAmt(BigDecimal qiChuTaxAmt) {
		this.qiChuTaxAmt = qiChuTaxAmt;
	}
	public BigDecimal getQiChuPrcTaxSum() {
		return qiChuPrcTaxSum;
	}
	public void setQiChuPrcTaxSum(BigDecimal qiChuPrcTaxSum) {
		this.qiChuPrcTaxSum = qiChuPrcTaxSum;
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
	public BigDecimal getSellTaxAmt() {
		return sellTaxAmt;
	}
	public void setSellTaxAmt(BigDecimal sellTaxAmt) {
		this.sellTaxAmt = sellTaxAmt;
	}
	public BigDecimal getSellPrcTaxSum() {
		return sellPrcTaxSum;
	}
	public void setSellPrcTaxSum(BigDecimal sellPrcTaxSum) {
		this.sellPrcTaxSum = sellPrcTaxSum;
	}
	public BigDecimal getCumulativeQty() {
		return cumulativeQty;
	}
	public void setCumulativeQty(BigDecimal cumulativeQty) {
		this.cumulativeQty = cumulativeQty;
	}
	public BigDecimal getCumulativeNoTaxAmt() {
		return cumulativeNoTaxAmt;
	}
	public void setCumulativeNoTaxAmt(BigDecimal cumulativeNoTaxAmt) {
		this.cumulativeNoTaxAmt = cumulativeNoTaxAmt;
	}
	public BigDecimal getCumulativeTaxAmt() {
		return cumulativeTaxAmt;
	}
	public void setCumulativeTaxAmt(BigDecimal cumulativeTaxAmt) {
		this.cumulativeTaxAmt = cumulativeTaxAmt;
	}
	public BigDecimal getCumulativePrcTaxSum() {
		return cumulativePrcTaxSum;
	}
	public void setCumulativePrcTaxSum(BigDecimal cumulativePrcTaxSum) {
		this.cumulativePrcTaxSum = cumulativePrcTaxSum;
	}
	public BigDecimal getDifferencesQty() {
		return differencesQty;
	}
	public void setDifferencesQty(BigDecimal differencesQty) {
		this.differencesQty = differencesQty;
	}
	public BigDecimal getDifferencesNoTaxAmt() {
		return differencesNoTaxAmt;
	}
	public void setDifferencesNoTaxAmt(BigDecimal differencesNoTaxAmt) {
		this.differencesNoTaxAmt = differencesNoTaxAmt;
	}
	public BigDecimal getDifferencesTaxAmt() {
		return differencesTaxAmt;
	}
	public void setDifferencesTaxAmt(BigDecimal differencesTaxAmt) {
		this.differencesTaxAmt = differencesTaxAmt;
	}
	public BigDecimal getDifferencesPrcTaxSum() {
		return differencesPrcTaxSum;
	}
	public void setDifferencesPrcTaxSum(BigDecimal differencesPrcTaxSum) {
		this.differencesPrcTaxSum = differencesPrcTaxSum;
	}
	public BigDecimal getkPQty() {
		return kPQty;
	}
	public void setkPQty(BigDecimal kPQty) {
		this.kPQty = kPQty;
	}
	public BigDecimal getkPAmt() {
		return kPAmt;
	}
	public void setkPAmt(BigDecimal kPAmt) {
		this.kPAmt = kPAmt;
	}
	public BigDecimal getkPTaxAmt() {
		return kPTaxAmt;
	}
	public void setkPTaxAmt(BigDecimal kPTaxAmt) {
		this.kPTaxAmt = kPTaxAmt;
	}
	public BigDecimal getkPAmtSum() {
		return kPAmtSum;
	}
	public void setkPAmtSum(BigDecimal kPAmtSum) {
		this.kPAmtSum = kPAmtSum;
	}

}

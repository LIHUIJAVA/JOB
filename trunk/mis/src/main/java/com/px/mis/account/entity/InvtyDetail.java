package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 明细帐实体类
 *
 */
public class InvtyDetail {
	public InvtyDetail() {
		
	}
	@JsonProperty("单据号")
	private int detailId; //主id
	@JsonProperty("科目id")
	private String subId;
	@JsonProperty("科目名称")
	private String subNm;
	@JsonProperty("科目借方")
	private String subDebitNm;
	@JsonProperty("科目贷方")
	private String subCreditNm;
	@JsonProperty("存货名称")
	private String invtyNm; //存货名称
	@JsonProperty("规格型号")
	private String spcModel; //规格型号
	@JsonProperty("计量单位")
    private String measrCorpNm; //计量单位
	@JsonProperty("存货编码")
    private String invtyEncd; //存货编码
	@JsonProperty("存货代码")
    private String invtyCd; //存货代码
	@JsonProperty("批号")
    private String batNum; //批号
	@JsonProperty("仓库编码")
    private String whsEncd; //仓库编码
	@JsonProperty("存货分类编码")
    private String invtyClsEncd; //分类
	@JsonProperty("存货分类名称")
    private String invtyClsNm; //分类
	
    private String level;
	@JsonProperty("仓库名称")
    private String whsNm; //仓库名称
	@JsonProperty("重量")
    private BigDecimal weight;//重量 
	@JsonProperty("箱规")
    private BigDecimal bxRule;//箱规
	@JsonProperty("期初总额")
    private BigDecimal amt;//期初总额
	@JsonProperty("期初数量")
    private BigDecimal qty;//期初数量
	@JsonProperty("期初单价")
    private BigDecimal uprc; //期初单价
	@JsonProperty("采购入库-数量")
    private BigDecimal purcQty;//采购入库-数量
	@JsonProperty("金额")
    private BigDecimal purcAmt;//金额
	@JsonProperty("暂估-数量")
    private BigDecimal purcTempQty;//暂估-数量
	@JsonProperty("暂估-金额")
    private BigDecimal purcTempAmt;//暂估-金额
	@JsonProperty("其他入数量")
    private BigDecimal othInQty;//其他入数量
	@JsonProperty("其他入金额")
    private BigDecimal othInAmt;//其他入金额 
	@JsonProperty("其他出数量")
    private BigDecimal othOutQty;//其他出数量
	@JsonProperty("其他出金额")
    private BigDecimal othOutAmt;//其他出金额
	@JsonProperty("含税均价")
    private BigDecimal cntnTaxUprc; //含税单价
	@JsonProperty("无税均价")
    private BigDecimal noTaxUprc; //无税单价
	@JsonProperty("调拨数量")
    private BigDecimal tranQty;//调拨数量
	@JsonProperty("调拨金额")
    private BigDecimal tranAmt;//调拨金额
	
	@JsonProperty("销售数量")
    private BigDecimal sellQty;//销售数量
	@JsonProperty("价税合计")
    private BigDecimal sellAmt;//价税合计
	@JsonProperty("销售收入")
    private BigDecimal sellInAmt;//销售收入
	@JsonProperty("出库数量")
    private BigDecimal outQty;//出库数量
	@JsonProperty("出库金额")
    private BigDecimal outAmt;//出库金额
	@JsonProperty("销售成本")
     private BigDecimal sellCost;//销售成本
	@JsonProperty("毛利")
    private BigDecimal gross;//毛利
	@JsonProperty("毛利率")
    private String grossRate;//毛利率
	@JsonProperty("期末数量")
    private BigDecimal finalQty;//期末数量
	@JsonProperty("期末金额")
    private BigDecimal finalAmt;//期末数量
	@JsonProperty("客户编码")
    private String custId;
	@JsonProperty("客户名称")
    private String custNm;
	@JsonProperty("部门编码")
    private String deptId;
	@JsonProperty("部门名称")
    private String deptName;
	@JsonProperty("操作人")
    private String accNum;
	@JsonInclude(Include.NON_NULL)
    private List<InvtyDetails> invtyDetailsList;//子表集合
    
    
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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public int getDetailId() {
		return detailId;
	}

	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}
	
	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getSubNm() {
		return subNm;
	}

	public void setSubNm(String subNm) {
		this.subNm = subNm;
	}

	public String getSubDebitNm() {
		return subDebitNm;
	}

	public void setSubDebitNm(String subDebitNm) {
		this.subDebitNm = subDebitNm;
	}

	public String getSubCreditNm() {
		return subCreditNm;
	}

	public void setSubCreditNm(String subCreditNm) {
		this.subCreditNm = subCreditNm;
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

	public String getInvtyEncd() {
		return invtyEncd;
	}

	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}

	public String getInvtyCd() {
		return invtyCd;
	}

	public void setInvtyCd(String invtyCd) {
		this.invtyCd = invtyCd;
	}

	public String getBatNum() {
		return batNum;
	}

	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}
	
	public List<InvtyDetails> getInvtyDetailsList() {
		return invtyDetailsList;
	}

	public void setInvtyDetailsList(List<InvtyDetails> invtyDetailsList) {
		this.invtyDetailsList = invtyDetailsList;
	}
	
	public String getWhsEncd() {
		return whsEncd;
	}

	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}
	
	public String getInvtyClsEncd() {
		return invtyClsEncd;
	}

	public void setInvtyClsEncd(String invtyClsEncd) {
		this.invtyClsEncd = invtyClsEncd;
	}
	
	public String getInvtyClsNm() {
		return invtyClsNm;
	}

	public void setInvtyClsNm(String invtyClsNm) {
		this.invtyClsNm = invtyClsNm;
	}
	@JsonIgnore
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getWhsNm() {
		return whsNm;
	}

	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getBxRule() {
		return bxRule;
	}

	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getUprc() {
		return uprc;
	}

	public void setUprc(BigDecimal uprc) {
		this.uprc = uprc;
	}
	
	public BigDecimal getPurcQty() {
		return purcQty;
	}

	public void setPurcQty(BigDecimal purcQty) {
		this.purcQty = purcQty;
	}

	public BigDecimal getPurcAmt() {
		return purcAmt;
	}

	public void setPurcAmt(BigDecimal purcAmt) {
		this.purcAmt = purcAmt;
	}

	public BigDecimal getPurcTempQty() {
		return purcTempQty;
	}

	public void setPurcTempQty(BigDecimal purcTempQty) {
		this.purcTempQty = purcTempQty;
	}

	public BigDecimal getPurcTempAmt() {
		return purcTempAmt;
	}

	public void setPurcTempAmt(BigDecimal purcTempAmt) {
		this.purcTempAmt = purcTempAmt;
	}

	public BigDecimal getOthInQty() {
		return othInQty;
	}

	public void setOthInQty(BigDecimal othInQty) {
		this.othInQty = othInQty;
	}

	public BigDecimal getOthInAmt() {
		return othInAmt;
	}

	public void setOthInAmt(BigDecimal othInAmt) {
		this.othInAmt = othInAmt;
	}

	public BigDecimal getOthOutQty() {
		return othOutQty;
	}

	public void setOthOutQty(BigDecimal othOutQty) {
		this.othOutQty = othOutQty;
	}

	public BigDecimal getOthOutAmt() {
		return othOutAmt;
	}

	public void setOthOutAmt(BigDecimal othOutAmt) {
		this.othOutAmt = othOutAmt;
	}

	public BigDecimal getCntnTaxUprc() {
		return cntnTaxUprc;
	}

	public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
		this.cntnTaxUprc = cntnTaxUprc;
	}

	public BigDecimal getNoTaxUprc() {
		return noTaxUprc;
	}

	public void setNoTaxUprc(BigDecimal noTaxUprc) {
		this.noTaxUprc = noTaxUprc;
	}

	public BigDecimal getTranQty() {
		return tranQty;
	}

	public void setTranQty(BigDecimal tranQty) {
		this.tranQty = tranQty;
	}

	public BigDecimal getTranAmt() {
		return tranAmt;
	}

	public void setTranAmt(BigDecimal tranAmt) {
		this.tranAmt = tranAmt;
	}

	public BigDecimal getSellQty() {
		return sellQty;
	}

	public void setSellQty(BigDecimal sellQty) {
		this.sellQty = sellQty;
	}

	public BigDecimal getSellAmt() {
		return sellAmt;
	}

	public void setSellAmt(BigDecimal sellAmt) {
		this.sellAmt = sellAmt;
	}

	public BigDecimal getSellCost() {
		return sellCost;
	}

	public void setSellCost(BigDecimal sellCost) {
		this.sellCost = sellCost;
	}

	public BigDecimal getGross() {
		return gross;
	}

	public void setGross(BigDecimal gross) {
		this.gross = gross;
	}

	public String getGrossRate() {
		return grossRate;
	}

	public void setGrossRate(String grossRate) {
		this.grossRate = grossRate;
	}

	public BigDecimal getFinalQty() {
		return finalQty;
	}

	public void setFinalQty(BigDecimal finalQty) {
		this.finalQty = finalQty;
	}

	public BigDecimal getFinalAmt() {
		return finalAmt;
	}

	public void setFinalAmt(BigDecimal finalAmt) {
		this.finalAmt = finalAmt;
	}
	
	public BigDecimal getOutQty() {
		return outQty;
	}

	public void setOutQty(BigDecimal outQty) {
		this.outQty = outQty;
	}

	public BigDecimal getOutAmt() {
		return outAmt;
	}

	public void setOutAmt(BigDecimal outAmt) {
		this.outAmt = outAmt;
	}
	
	
	public BigDecimal getSellInAmt() {
		return sellInAmt;
	}

	public void setSellInAmt(BigDecimal sellInAmt) {
		this.sellInAmt = sellInAmt;
	}

	public InvtyDetail( String invtyNm, String spcModel, String measrCorpNm,
			String invtyEncd, String invtyCd, String batNum) {
		super();
		this.invtyNm = invtyNm;
		this.spcModel = spcModel;
		this.measrCorpNm = measrCorpNm;
		this.invtyEncd = invtyEncd;
		this.invtyCd = invtyCd;
		this.batNum = batNum;
	}
    
	public InvtyDetail(String invtyNm, String spcModel, String measrCorpNm, String invtyEncd, String invtyCd,
			   String batNum, String whsEncd, String invtyClsEncd, String invtyClsNm, String whsNm 
			   ) {
			  super();
			  this.invtyNm = invtyNm;
			  this.spcModel = spcModel;
			  this.measrCorpNm = measrCorpNm;
			  this.invtyEncd = invtyEncd;
			  this.invtyCd = invtyCd;
			  this.batNum = batNum;
			  this.whsEncd = whsEncd;
			  this.invtyClsEncd = invtyClsEncd;
			  this.invtyClsNm = invtyClsNm;
			  this.whsNm = whsNm;
			  
			 }
    
}

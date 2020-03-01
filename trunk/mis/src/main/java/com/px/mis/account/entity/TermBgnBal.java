package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

//期初余额实体类
public class TermBgnBal {
	@JsonProperty("是否记账")
	private Integer isNtBookEntry;// 是否记账
	@JsonProperty("仓库编码")
	private String whsEncd;// 仓库编码
	@JsonProperty("仓库名称")
	private String whsNm;// 仓库名称
	@JsonProperty("存货编码")
	private String invtyEncd;// 存货编码
	@JsonProperty("存货名称")
	private String invtyNm;// 存货名称
	@JsonProperty("规格型号")
	private String spcModel;// 规格型号
	@JsonProperty("计量单位")
	private String measrCorpNm;// 计量单位名称
	@JsonProperty("数量")
	private BigDecimal qty;// 数量
	@JsonProperty("无税单价")
	private BigDecimal noTaxUprc;// 无税单价
	@JsonProperty("无税金额")
	private BigDecimal noTaxAmt;// 无税金额
	@JsonProperty("批次")
	private String batNum;// 批号
	@JsonProperty("生产日期")
	private String prdcDt;// 生产日期
	@JsonProperty("保质期")
	private String baoZhiQi;// 保质期
	@JsonProperty("失效日期")
	private String invldtnDt;// 失效日期
	@JsonProperty("存货科目编码")
	private String invtySubjId;// 存货科目编码

	/* 以下为导出忽略的字段 */
	@JsonProperty("序号")

	private Integer ordrNum;// 序号
	@JsonProperty("部门编码")

	private String deptId;// 部门编码
	@JsonProperty("箱数")

	private BigDecimal bxQty;// 箱数

	@JsonProperty("税额")
	private BigDecimal taxAmt;// 税额

	@JsonProperty("税率")
	private BigDecimal taxRate;// 税率

	@JsonProperty("含税单价")
	private BigDecimal cntnTaxUprc;// 含税单价

	@JsonProperty("价税合计")
	private BigDecimal prcTaxSum;// 价税合计

	@JsonProperty("国际批次")
	private String intlBat;// 国际批次

	@JsonProperty("记账人")
	private String bookEntryPers;// 记账人

	@JsonProperty("记账时间")
	private String bookEntryTm;// 记账时间

	@JsonProperty("创建人")
	private String setupPers;// 创建人

	@JsonProperty("创建时间")
	private String setupTm;// 创建时间

	@JsonProperty("修改人")
	private String modiPers;// 修改人

	@JsonProperty("修改时间")
	private String modiTm;// 修改时间

	@JsonProperty("项目编码")
	private String projEncd;// 项目编码

	@JsonProperty("备注")
	private String memo;// 备注

	@JsonProperty("项目名称")
	private String projNm;// 项目名称

	@JsonProperty("箱规")
	private BigDecimal bxRule;// 箱规

	@JsonProperty("部门名称")
	private String deptName;// 部门名称

	@JsonProperty("科目名称")
	private String subjNm;// 科目名称

	public Integer getIsNtBookEntry() {
		return isNtBookEntry;
	}

	public void setIsNtBookEntry(Integer isNtBookEntry) {
		this.isNtBookEntry = isNtBookEntry;
	}

	public String getWhsEncd() {
		return whsEncd;
	}

	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
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

	public BigDecimal getNoTaxUprc() {
		return noTaxUprc;
	}

	public void setNoTaxUprc(BigDecimal noTaxUprc) {
		this.noTaxUprc = noTaxUprc;
	}

	public BigDecimal getNoTaxAmt() {
		return noTaxAmt;
	}

	public void setNoTaxAmt(BigDecimal noTaxAmt) {
		this.noTaxAmt = noTaxAmt;
	}

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

	public String getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public String getInvldtnDt() {
		return invldtnDt;
	}

	public void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}

	public String getInvtySubjId() {
		return invtySubjId;
	}

	public void setInvtySubjId(String invtySubjId) {
		this.invtySubjId = invtySubjId;
	}
    @JsonIgnore
	public Integer getOrdrNum() {
		return ordrNum;
	}

	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	@JsonIgnore
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	@JsonIgnore
	public BigDecimal getBxQty() {
		return bxQty;
	}

	public void setBxQty(BigDecimal bxQty) {
		this.bxQty = bxQty;
	}
	@JsonIgnore
	public BigDecimal getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}
	@JsonIgnore
	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	@JsonIgnore
	public BigDecimal getCntnTaxUprc() {
		return cntnTaxUprc;
	}

	public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
		this.cntnTaxUprc = cntnTaxUprc;
	}
	@JsonIgnore
	public BigDecimal getPrcTaxSum() {
		return prcTaxSum;
	}

	public void setPrcTaxSum(BigDecimal prcTaxSum) {
		this.prcTaxSum = prcTaxSum;
	}
	@JsonIgnore
	public String getIntlBat() {
		return intlBat;
	}

	public void setIntlBat(String intlBat) {
		this.intlBat = intlBat;
	}
	@JsonIgnore
	public String getBookEntryPers() {
		return bookEntryPers;
	}

	public void setBookEntryPers(String bookEntryPers) {
		this.bookEntryPers = bookEntryPers;
	}
	@JsonIgnore
	public String getBookEntryTm() {
		return bookEntryTm;
	}

	public void setBookEntryTm(String bookEntryTm) {
		this.bookEntryTm = bookEntryTm;
	}
	@JsonIgnore
	public String getSetupPers() {
		return setupPers;
	}

	public void setSetupPers(String setupPers) {
		this.setupPers = setupPers;
	}
	@JsonIgnore
	public String getSetupTm() {
		return setupTm;
	}

	public void setSetupTm(String setupTm) {
		this.setupTm = setupTm;
	}
	@JsonIgnore
	public String getModiPers() {
		return modiPers;
	}

	public void setModiPers(String modiPers) {
		this.modiPers = modiPers;
	}
	@JsonIgnore
	public String getModiTm() {
		return modiTm;
	}

	public void setModiTm(String modiTm) {
		this.modiTm = modiTm;
	}
	@JsonIgnore
	public String getProjEncd() {
		return projEncd;
	}

	public void setProjEncd(String projEncd) {
		this.projEncd = projEncd;
	}
	@JsonIgnore
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	@JsonIgnore
	public String getProjNm() {
		return projNm;
	}

	public void setProjNm(String projNm) {
		this.projNm = projNm;
	}
	@JsonIgnore
	public BigDecimal getBxRule() {
		return bxRule;
	}

	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}
	@JsonIgnore
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	@JsonIgnore
	public String getSubjNm() {
		return subjNm;
	}

	public void setSubjNm(String subjNm) {
		this.subjNm = subjNm;
	}

	

}

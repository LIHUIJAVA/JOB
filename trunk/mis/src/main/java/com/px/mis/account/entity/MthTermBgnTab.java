package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//各月期初表实体类；
public class MthTermBgnTab {
	private Integer ordrNum;//序号
	private String acctYr;//会计年
	private String acctiMth;//会计月
	private String whsEncd;//仓库编码
	private String invtyEncd;//存货编码
	private BigDecimal qty;//数量
	private String bat;//批次
	private BigDecimal uprc;//单价
	private BigDecimal amt;//金额
	private BigDecimal cntnTaxUprc;//含税单价
	private BigDecimal prcTaxSum;//价税合计
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date prdcDt;//生产日期
	private String quaGuarPer;//保质期
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date invldtnDt;//失效日期
	
	private String whsNm;//仓库名称
	private String invtyNm;//存货名称
	
	public MthTermBgnTab() {
	}
	
	public Integer getOrdrNum() {
		return ordrNum;
	}

	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}

	public String getAcctYr() {
		return acctYr;
	}
	public void setAcctYr(String acctYr) {
		this.acctYr = acctYr;
	}
	public String getAcctiMth() {
		return acctiMth;
	}
	public void setAcctiMth(String acctiMth) {
		this.acctiMth = acctiMth;
	}
	public String getWhsEncd() {
		return whsEncd;
	}
	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public String getBat() {
		return bat;
	}
	public void setBat(String bat) {
		this.bat = bat;
	}
	public BigDecimal getUprc() {
		return uprc;
	}
	public void setUprc(BigDecimal uprc) {
		this.uprc = uprc;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public BigDecimal getCntnTaxUprc() {
		return cntnTaxUprc;
	}
	public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
		this.cntnTaxUprc = cntnTaxUprc;
	}
	public BigDecimal getPrcTaxSum() {
		return prcTaxSum;
	}
	public void setPrcTaxSum(BigDecimal prcTaxSum) {
		this.prcTaxSum = prcTaxSum;
	}
	public Date getPrdcDt() {
		return prdcDt;
	}
	public void setPrdcDt(Date prdcDt) {
		this.prdcDt = prdcDt;
	}
	public String getQuaGuarPer() {
		return quaGuarPer;
	}
	public void setQuaGuarPer(String quaGuarPer) {
		this.quaGuarPer = quaGuarPer;
	}
	public Date getInvldtnDt() {
		return invldtnDt;
	}
	public void setInvldtnDt(Date invldtnDt) {
		this.invldtnDt = invldtnDt;
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
	public String getInvtyNm() {
		return invtyNm;
	}
	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}
	
}

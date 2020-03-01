package com.px.mis.account.entity;

import java.math.BigDecimal;
//本月入库成本表
public class CurmthIntoWhsCostTab {
	private Integer ordrNum;//序号
	private String acctYr;//会计年
	private String acctiMth;//会计月
	private Integer pursInvSubTabId;//采购发票子表id
	private String invtyEncd;//存货编码
	private String bat;//批次
	private BigDecimal qty;//数量
	private BigDecimal uncntnTaxUprc;//不含税单价
	private BigDecimal uncntnTaxAmt;//不含税金额
	private BigDecimal cntnTaxUprc;//含税单价
	private BigDecimal cntnTaxAmt;//含税金额
	
	private String pursInvNm;//采购发票号
	private String invtyNm;//存货名称
	public CurmthIntoWhsCostTab() {
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
	public Integer getPursInvSubTabId() {
		return pursInvSubTabId;
	}
	public void setPursInvSubTabId(Integer pursInvSubTabId) {
		this.pursInvSubTabId = pursInvSubTabId;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getBat() {
		return bat;
	}
	public void setBat(String bat) {
		this.bat = bat;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public BigDecimal getUncntnTaxUprc() {
		return uncntnTaxUprc;
	}
	public void setUncntnTaxUprc(BigDecimal uncntnTaxUprc) {
		this.uncntnTaxUprc = uncntnTaxUprc;
	}
	public BigDecimal getUncntnTaxAmt() {
		return uncntnTaxAmt;
	}
	public void setUncntnTaxAmt(BigDecimal uncntnTaxAmt) {
		this.uncntnTaxAmt = uncntnTaxAmt;
	}
	public BigDecimal getCntnTaxUprc() {
		return cntnTaxUprc;
	}
	public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
		this.cntnTaxUprc = cntnTaxUprc;
	}
	public BigDecimal getCntnTaxAmt() {
		return cntnTaxAmt;
	}
	public void setCntnTaxAmt(BigDecimal cntnTaxAmt) {
		this.cntnTaxAmt = cntnTaxAmt;
	}
	public String getPursInvNm() {
		return pursInvNm;
	}
	public void setPursInvNm(String pursInvNm) {
		this.pursInvNm = pursInvNm;
	}
	public String getInvtyNm() {
		return invtyNm;
	}
	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}
	
}

package com.px.mis.account.entity;

import java.math.BigDecimal;
//�������ɱ���
public class CurmthIntoWhsCostTab {
	private Integer ordrNum;//���
	private String acctYr;//�����
	private String acctiMth;//�����
	private Integer pursInvSubTabId;//�ɹ���Ʊ�ӱ�id
	private String invtyEncd;//�������
	private String bat;//����
	private BigDecimal qty;//����
	private BigDecimal uncntnTaxUprc;//����˰����
	private BigDecimal uncntnTaxAmt;//����˰���
	private BigDecimal cntnTaxUprc;//��˰����
	private BigDecimal cntnTaxAmt;//��˰���
	
	private String pursInvNm;//�ɹ���Ʊ��
	private String invtyNm;//�������
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

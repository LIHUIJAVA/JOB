package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//�����ڳ���ʵ���ࣻ
public class MthTermBgnTab {
	private Integer ordrNum;//���
	private String acctYr;//�����
	private String acctiMth;//�����
	private String whsEncd;//�ֿ����
	private String invtyEncd;//�������
	private BigDecimal qty;//����
	private String bat;//����
	private BigDecimal uprc;//����
	private BigDecimal amt;//���
	private BigDecimal cntnTaxUprc;//��˰����
	private BigDecimal prcTaxSum;//��˰�ϼ�
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date prdcDt;//��������
	private String quaGuarPer;//������
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date invldtnDt;//ʧЧ����
	
	private String whsNm;//�ֿ�����
	private String invtyNm;//�������
	
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

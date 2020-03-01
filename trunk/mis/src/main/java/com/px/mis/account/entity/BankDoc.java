package com.px.mis.account.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

//银行档案表实体类；
public class BankDoc {
	private String bankEncd;//银行编码
	private String bankNm;//银行名称
	private Integer indvAcctIsFixlen;//个人账号是否定长
	private Integer indvAcctNumLen;//个人账号长度
	private Integer autoOutIndvNumLen;//自动带出的个人账号长度
	private String corpEncd;//单位编码
	private Integer compAcctIsFixLen;//企业账号是否定长
	private Integer compAcctNumLen;//企业账号长度
	private Integer bankId;//银行标识
	private Integer isSysData;//是否系统预制
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date pubufts;//时间戳
	
	public String getBankEncd() {
		return bankEncd;
	}
	public void setBankEncd(String bankEncd) {
		this.bankEncd = bankEncd;
	}
	public String getBankNm() {
		return bankNm;
	}
	public void setBankNm(String bankNm) {
		this.bankNm = bankNm;
	}
	public Integer getIndvAcctIsFixlen() {
		return indvAcctIsFixlen;
	}
	public void setIndvAcctIsFixlen(Integer indvAcctIsFixlen) {
		this.indvAcctIsFixlen = indvAcctIsFixlen;
	}
	public Integer getIndvAcctNumLen() {
		return indvAcctNumLen;
	}
	public void setIndvAcctNumLen(Integer indvAcctNumLen) {
		this.indvAcctNumLen = indvAcctNumLen;
	}
	public Integer getAutoOutIndvNumLen() {
		return autoOutIndvNumLen;
	}
	public void setAutoOutIndvNumLen(Integer autoOutIndvNumLen) {
		this.autoOutIndvNumLen = autoOutIndvNumLen;
	}
	public String getCorpEncd() {
		return corpEncd;
	}
	public void setCorpEncd(String corpEncd) {
		this.corpEncd = corpEncd;
	}
	public Integer getCompAcctIsFixLen() {
		return compAcctIsFixLen;
	}
	public void setCompAcctIsFixLen(Integer compAcctIsFixLen) {
		this.compAcctIsFixLen = compAcctIsFixLen;
	}
	public Integer getCompAcctNumLen() {
		return compAcctNumLen;
	}
	public void setCompAcctNumLen(Integer compAcctNumLen) {
		this.compAcctNumLen = compAcctNumLen;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public Integer getIsSysData() {
		return isSysData;
	}
	public void setIsSysData(Integer isSysData) {
		this.isSysData = isSysData;
	}
	public Date getPubufts() {
		return pubufts;
	}
	public void setPubufts(Date pubufts) {
		this.pubufts = pubufts;
	}
	
}

package com.px.mis.account.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

//���е�����ʵ���ࣻ
public class BankDoc {
	private String bankEncd;//���б���
	private String bankNm;//��������
	private Integer indvAcctIsFixlen;//�����˺��Ƿ񶨳�
	private Integer indvAcctNumLen;//�����˺ų���
	private Integer autoOutIndvNumLen;//�Զ������ĸ����˺ų���
	private String corpEncd;//��λ����
	private Integer compAcctIsFixLen;//��ҵ�˺��Ƿ񶨳�
	private Integer compAcctNumLen;//��ҵ�˺ų���
	private Integer bankId;//���б�ʶ
	private Integer isSysData;//�Ƿ�ϵͳԤ��
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date pubufts;//ʱ���
	
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

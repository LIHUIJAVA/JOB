package com.px.mis.account.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//��ƾ֤������ʵ����
public class VouchSupvnTab {
	private Integer ordrNum;//���
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date mutuExclTm;//����ʱ��
	private String emp;//��Ա
	private String mutuExclTyp;//��������
	public VouchSupvnTab() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public Date getMutuExclTm() {
		return mutuExclTm;
	}
	public void setMutuExclTm(Date mutuExclTm) {
		this.mutuExclTm = mutuExclTm;
	}
	public String getEmp() {
		return emp;
	}
	public void setEmp(String emp) {
		this.emp = emp;
	}
	public String getMutuExclTyp() {
		return mutuExclTyp;
	}
	public void setMutuExclTyp(String mutuExclTyp) {
		this.mutuExclTyp = mutuExclTyp;
	}
	
}

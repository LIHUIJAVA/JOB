package com.px.mis.account.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//制凭证并发表实体类
public class VouchSupvnTab {
	private Integer ordrNum;//序号
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date mutuExclTm;//互斥时间
	private String emp;//人员
	private String mutuExclTyp;//操作类型
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

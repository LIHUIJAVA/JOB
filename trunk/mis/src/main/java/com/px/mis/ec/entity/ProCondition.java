package com.px.mis.ec.entity;
//促销条件表
public class ProCondition {
	private Integer no;//序号
	private Integer proConditionEncd;//促销条件编码
	private String proConditionName;//促销条件名称
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public Integer getProConditionEncd() {
		return proConditionEncd;
	}
	public void setProConditionEncd(Integer proConditionEncd) {
		this.proConditionEncd = proConditionEncd;
	}
	public String getProConditionName() {
		return proConditionName;
	}
	public void setProConditionName(String proConditionName) {
		this.proConditionName = proConditionName;
	}
	@Override
	public String toString() {
		return "ProCondition [no=" + no + ", proConditionEncd=" + proConditionEncd + ", proConditionName="
				+ proConditionName + "]";
	}
	
}

package com.px.mis.ec.entity;
//����������
public class ProCondition {
	private Integer no;//���
	private Integer proConditionEncd;//������������
	private String proConditionName;//������������
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

package com.px.mis.account.entity;

import java.math.BigDecimal;
//����ʽ�ռ�ù滮��
public class InvtyCapOcupLaytTab {
	private Integer ordrNum;//���
	private String deptId;//���ű��
	private String invtyEncd;//�������
	private String invtyBigClsEncd;//����������
	private BigDecimal planAmt;//�ƻ����
	private String whsEncd;//�ֿ����
	private String memo;//��ע
	
	private String deptNm;//��������
	private String invtyNm;//�������
	private String invtyBigClsNm;//�����������
	private String whsNm;//�ֿ�����
	public InvtyCapOcupLaytTab() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getInvtyBigClsEncd() {
		return invtyBigClsEncd;
	}
	public void setInvtyBigClsEncd(String invtyBigClsEncd) {
		this.invtyBigClsEncd = invtyBigClsEncd;
	}
	public BigDecimal getPlanAmt() {
		return planAmt;
	}
	public void setPlanAmt(BigDecimal planAmt) {
		this.planAmt = planAmt;
	}
	public String getWhsEncd() {
		return whsEncd;
	}
	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getInvtyNm() {
		return invtyNm;
	}
	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}
	public String getInvtyBigClsNm() {
		return invtyBigClsNm;
	}
	public void setInvtyBigClsNm(String invtyBigClsNm) {
		this.invtyBigClsNm = invtyBigClsNm;
	}
	public String getWhsNm() {
		return whsNm;
	}
	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}
	
}

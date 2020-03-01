package com.px.mis.account.entity;

import java.math.BigDecimal;
//存货资金占用规划表
public class InvtyCapOcupLaytTab {
	private Integer ordrNum;//序号
	private String deptId;//部门编号
	private String invtyEncd;//存货编码
	private String invtyBigClsEncd;//存货大类编码
	private BigDecimal planAmt;//计划金额
	private String whsEncd;//仓库编码
	private String memo;//备注
	
	private String deptNm;//部门名称
	private String invtyNm;//存货名称
	private String invtyBigClsNm;//存货分类名称
	private String whsNm;//仓库名称
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

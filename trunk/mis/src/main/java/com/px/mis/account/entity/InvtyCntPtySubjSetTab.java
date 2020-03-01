package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//存货对方科目设置表
public class InvtyCntPtySubjSetTab {
	@JsonProperty("序号")
	//@JsonIgnore
	private Integer ordrNum;// 序号
	//@JsonIgnore
	@JsonProperty("部门编码")
	private String deptId;// 部门编码
	@JsonProperty("收发类别编码")
	private String recvSendCateId;// 收发类别编码
	@JsonProperty("收发类别名称")
	private String recvSendCateNm;// 收发类别名称
	@JsonProperty("存货编码")
	//@JsonIgnore
	private String invtyEncd;// 存货编码
	//@JsonIgnore
	@JsonProperty("存货名称")
	private String invtyNm;// 存货名称
	@JsonProperty("存货分类编码")
	private String invtyBigClsEncd;// 存货大类编码
	@JsonProperty("存货分类名称")
	private String invtyBigClsNm;// 存货大类名称
	@JsonProperty("对方科目编码")
	private String cntPtySubjId;// 对方科目编码
	@JsonProperty("对方科目名称")
	private String cntPtySubjNm;// 对方科目名称
	@JsonProperty("暂估科目编码")
	private String teesSubjEncd;// 暂估科目编码
	@JsonProperty("暂估科目名称")
	private String teesSubjNm;// 暂估科目名称
	@JsonProperty("备注")
	private String memo;// 备注

	//@JsonIgnore
	@JsonProperty("部门名称")
	private String deptNm;// 部门名称

	public InvtyCntPtySubjSetTab() {
	}

	public Integer getOrdrNum() {
		return ordrNum;
	}

	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getRecvSendCateId() {
		return recvSendCateId;
	}

	public void setRecvSendCateId(String recvSendCateId) {
		this.recvSendCateId = recvSendCateId;
	}

	public String getCntPtySubjId() {
		return cntPtySubjId;
	}

	public void setCntPtySubjId(String cntPtySubjId) {
		this.cntPtySubjId = cntPtySubjId;
	}

	public String getTeesSubjEncd() {
		return teesSubjEncd;
	}

	public void setTeesSubjEncd(String teesSubjEncd) {
		this.teesSubjEncd = teesSubjEncd;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public String getDeptNm() {
		return deptNm;
	}

	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}

	public String getRecvSendCateNm() {
		return recvSendCateNm;
	}

	public void setRecvSendCateNm(String recvSendCateNm) {
		this.recvSendCateNm = recvSendCateNm;
	}

	public String getCntPtySubjNm() {
		return cntPtySubjNm;
	}

	public void setCntPtySubjNm(String cntPtySubjNm) {
		this.cntPtySubjNm = cntPtySubjNm;
	}

	public String getTeesSubjNm() {
		return teesSubjNm;
	}

	public void setTeesSubjNm(String teesSubjNm) {
		this.teesSubjNm = teesSubjNm;
	}

}

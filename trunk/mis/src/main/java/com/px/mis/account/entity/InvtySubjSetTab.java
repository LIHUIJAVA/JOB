package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//存货科目设置表实体
public class InvtySubjSetTab {

	//@JsonIgnore
	@JsonProperty("序号")
	private Integer ordrNum;//序号
	@JsonProperty("存货分类编码")
	private String invtyBigClsEncd;//存货大类编号
	@JsonProperty("存货分类名称")
	private String invtyClsNm;//存货分类名称
	@JsonProperty("存货科目编码")
	private String invtySubjId;//存货科目编号
	@JsonProperty("存货科目名称")
	private String invtySubjNm;//存货科目名称
	@JsonProperty("委托代销科目编码")
	private String entrsAgnSubjId;//委托代销科目编号
	@JsonProperty("委托代销科目名称")
	private String entrsAgnSubjNm;//委托代销科目名称
	@JsonProperty("备注")
	private String memo;//备注
	//JsonIgnore
	//@JsonIgnore
	@JsonProperty("分期收款发出商品科目编号")
	private String amtblRecvId;//分期收款发出商品科目编号
	//@JsonIgnore
	@JsonProperty("分期收款发出商品科目名称")
	private String amtblRecvNm;//分期收款发出商品科目名称
	
	public String getAmtblRecvNm() {
		return amtblRecvNm;
	}
	public void setAmtblRecvNm(String amtblRecvNm) {
		this.amtblRecvNm = amtblRecvNm;
	}
	public String getAmtblRecvId() {
		return amtblRecvId;
	}
	public void setAmtblRecvId(String amtblRecvId) {
		this.amtblRecvId = amtblRecvId;
	}
	public InvtySubjSetTab() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getInvtyBigClsEncd() {
		return invtyBigClsEncd;
	}
	public void setInvtyBigClsEncd(String invtyBigClsEncd) {
		this.invtyBigClsEncd = invtyBigClsEncd;
	}
	public String getInvtySubjId() {
		return invtySubjId;
	}
	public void setInvtySubjId(String invtySubjId) {
		this.invtySubjId = invtySubjId;
	}
	public String getEntrsAgnSubjId() {
		return entrsAgnSubjId;
	}
	public void setEntrsAgnSubjId(String entrsAgnSubjId) {
		this.entrsAgnSubjId = entrsAgnSubjId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getInvtyClsNm() {
		return invtyClsNm;
	}
	public void setInvtyClsNm(String invtyClsNm) {
		this.invtyClsNm = invtyClsNm;
	}
	public String getInvtySubjNm() {
		return invtySubjNm;
	}
	public void setInvtySubjNm(String invtySubjNm) {
		this.invtySubjNm = invtySubjNm;
	}
	public String getEntrsAgnSubjNm() {
		return entrsAgnSubjNm;
	}
	public void setEntrsAgnSubjNm(String entrsAgnSubjNm) {
		this.entrsAgnSubjNm = entrsAgnSubjNm;
	}
	
}

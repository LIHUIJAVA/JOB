package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//存货应付科目表实体
public class InvtyPayblSubj {
	@JsonProperty("序号")
	//@JsonIgnore
	private Integer incrsId;// 自增id
	@JsonProperty("供应商分类编码")
	private String provrClsEncd;// 供应商分类编码
	@JsonProperty("供应商分类名称")
	private String provrClsNm;// 供应商分类名称
	@JsonProperty("应付科目编码")
	private String payblSubjEncd;// 应付科目编码
	@JsonProperty("应付科目名称")
	private String payblSubjNm;// 应付科目名称
	@JsonProperty("预付科目编码")
	private String prepySubjEncd;// 预付科目编码
	@JsonProperty("预付科目名称")
	private String prepySubjNm;// 预付科目名称
	/** 以下为忽略 **/


	public InvtyPayblSubj() {
	}

	public Integer getIncrsId() {
		return incrsId;
	}

	public void setIncrsId(Integer incrsId) {
		this.incrsId = incrsId;
	}

	public String getProvrClsEncd() {
		return provrClsEncd;
	}

	public void setProvrClsEncd(String provrClsEncd) {
		this.provrClsEncd = provrClsEncd;
	}

	public String getPayblSubjEncd() {
		return payblSubjEncd;
	}

	public void setPayblSubjEncd(String payblSubjEncd) {
		this.payblSubjEncd = payblSubjEncd;
	}

	public String getPrepySubjEncd() {
		return prepySubjEncd;
	}

	public void setPrepySubjEncd(String prepySubjEncd) {
		this.prepySubjEncd = prepySubjEncd;
	}

	public String getProvrClsNm() {
		return provrClsNm;
	}

	public void setProvrClsNm(String provrClsNm) {
		this.provrClsNm = provrClsNm;
	}

	public String getPayblSubjNm() {
		return payblSubjNm;
	}

	public void setPayblSubjNm(String payblSubjNm) {
		this.payblSubjNm = payblSubjNm;
	}

	public String getPrepySubjNm() {
		return prepySubjNm;
	}

	public void setPrepySubjNm(String prepySubjNm) {
		this.prepySubjNm = prepySubjNm;
	}

}

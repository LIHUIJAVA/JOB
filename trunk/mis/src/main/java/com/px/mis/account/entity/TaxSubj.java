package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//税金科目表实体
public class TaxSubj {
	//@JsonIgnore
	@JsonProperty("编号")
	private Integer autoId;//自动id
	@JsonProperty("供应商分类编码")
	private String provrClsEncd;//供应商分类编码；
	@JsonProperty("供应商分类名称")
	private String provrClsNm;//供应商分类名称
	@JsonProperty("采购科目编码")
	private String pursSubjEncd;//采购科目编码；
	@JsonProperty("采购科目名称")
	private String pursSubjNm;//采购科目名称
	@JsonProperty("采购税金编码")
	private String pursTaxEncd;//采购税金编码；
	@JsonProperty("采购税金科目")
	private String pursTaxNm;//采购税金名称
	
	//

	
	

	public TaxSubj() {
	}
	public Integer getAutoId() {
		return autoId;
	}
	public void setAutoId(Integer autoId) {
		this.autoId = autoId;
	}
	public String getProvrClsEncd() {
		return provrClsEncd;
	}
	public void setProvrClsEncd(String provrClsEncd) {
		this.provrClsEncd = provrClsEncd;
	}
	public String getPursSubjEncd() {
		return pursSubjEncd;
	}
	public void setPursSubjEncd(String pursSubjEncd) {
		this.pursSubjEncd = pursSubjEncd;
	}
	public String getPursTaxEncd() {
		return pursTaxEncd;
	}
	public void setPursTaxEncd(String pursTaxEncd) {
		this.pursTaxEncd = pursTaxEncd;
	}
	public String getProvrClsNm() {
		return provrClsNm;
	}
	public void setProvrClsNm(String provrClsNm) {
		this.provrClsNm = provrClsNm;
	}
	public String getPursSubjNm() {
		return pursSubjNm;
	}
	public void setPursSubjNm(String pursSubjNm) {
		this.pursSubjNm = pursSubjNm;
	}
	public String getPursTaxNm() {
		return pursTaxNm;
	}
	public void setPursTaxNm(String pursTaxNm) {
		this.pursTaxNm = pursTaxNm;
	}
	
}

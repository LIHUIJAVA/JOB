package com.px.mis.account.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
//凭证类别档案表实体
public class VouchCateDoc {
	
	@JsonInclude(value = Include.NON_NULL)
	private Integer ordrNum;//序号
	private String vouchCateWor;//凭证类别字；
	private String vouchCateNm;//凭证类别名称
	@JsonInclude(value = Include.NON_NULL)
	private String vouchCateSortNum;//凭证类别排序号
	private String lmtMode;//限制类型
	private List<VouchCateDocSubTab> lmtSubjList;//受限科目编号
	private String memo;//备注
	
	public VouchCateDoc() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getVouchCateWor() {
		return vouchCateWor;
	}
	public void setVouchCateWor(String vouchCateWor) {
		this.vouchCateWor = vouchCateWor;
	}
	public String getVouchCateNm() {
		return vouchCateNm;
	}
	public void setVouchCateNm(String vouchCateNm) {
		this.vouchCateNm = vouchCateNm;
	}
	public String getVouchCateSortNum() {
		return vouchCateSortNum;
	}
	public void setVouchCateSortNum(String vouchCateSortNum) {
		this.vouchCateSortNum = vouchCateSortNum;
	}
	public String getLmtMode() {
		return lmtMode;
	}
	public void setLmtMode(String lmtMode) {
		this.lmtMode = lmtMode;
	}
	
	public List<VouchCateDocSubTab> getLmtSubjList() {
		return lmtSubjList;
	}
	public void setLmtSubjList(List<VouchCateDocSubTab> lmtSubjList) {
		this.lmtSubjList = lmtSubjList;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}

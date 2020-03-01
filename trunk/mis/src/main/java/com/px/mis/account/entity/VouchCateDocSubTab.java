package com.px.mis.account.entity;
//凭证类别档案子表实体
public class VouchCateDocSubTab {
	private Integer ordrNum;//序号
	private String vouchCateWor;//凭证类别字
	private String lmtSubjId;//受限科目编号 
	
	
	public VouchCateDocSubTab() {
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
	public String getLmtSubjId() {
		return lmtSubjId;
	}
	public void setLmtSubjId(String lmtSubjId) {
		this.lmtSubjId = lmtSubjId;
	}
	@Override
	public String toString() {
		return "VouchCateDocSubTab [ordrNum=" + ordrNum + ", vouchCateWor=" + vouchCateWor + ", lmtSubjId=" + lmtSubjId
				+ "]";
	}
}

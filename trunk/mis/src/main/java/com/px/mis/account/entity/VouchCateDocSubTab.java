package com.px.mis.account.entity;
//ƾ֤��𵵰��ӱ�ʵ��
public class VouchCateDocSubTab {
	private Integer ordrNum;//���
	private String vouchCateWor;//ƾ֤�����
	private String lmtSubjId;//���޿�Ŀ��� 
	
	
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

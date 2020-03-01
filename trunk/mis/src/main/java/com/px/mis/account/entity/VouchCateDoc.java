package com.px.mis.account.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
//ƾ֤��𵵰���ʵ��
public class VouchCateDoc {
	
	@JsonInclude(value = Include.NON_NULL)
	private Integer ordrNum;//���
	private String vouchCateWor;//ƾ֤����֣�
	private String vouchCateNm;//ƾ֤�������
	@JsonInclude(value = Include.NON_NULL)
	private String vouchCateSortNum;//ƾ֤��������
	private String lmtMode;//��������
	private List<VouchCateDocSubTab> lmtSubjList;//���޿�Ŀ���
	private String memo;//��ע
	
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

package com.px.mis.account.entity;
//�����������ӱ�

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class OutIntoWhsAdjSnglSubTab {
	@JsonProperty("���")
	private Integer ordrNum;//���
	@JsonProperty("���ݺ�")
	private String formNum;//���ݺ� ���������������ʶ
	@JsonProperty("������������ӱ��ʶ")
	private Integer adjSubInd;//������������ӱ��ʶ
	@JsonProperty("�ֿ����")
	private String whsEncd;//�ֿ����
	@JsonProperty("�������")
	private String invtyEncd;//�������
	@JsonProperty("����")
	private String batNum;//����
	@JsonProperty("���")
	private BigDecimal amt;//���
	@JsonProperty("��ϸ�ӱ�ע")
	private String memo;//��ע
	@JsonProperty("�ӱ�ע")
	private String memos;//��ע
	@JsonProperty("�ֿ�����")
	private String whsNm;//�ֿ�����
	@JsonProperty("�������")
	private String invtyNm;//������ƣ�
	@JsonProperty("����ͺ�")
	private String spcModel;//����ͺ�
	@JsonProperty("���")
	private BigDecimal bxRule;//���
	@JsonProperty("������λ����")
	private String measrCorpNm;//������λ����
	@JsonProperty("��Դ�ӱ����")
	private Long toOrdrNum;//��Դ�ӱ����
	@JsonProperty("��Ŀ����")
	private String projEncd;//��Ŀ����
	@JsonProperty("��Ŀ����")
    private String projNm;//��Ŀ����
	
	public String getProjNm() {
		return projNm;
	}
	public void setProjNm(String projNm) {
		this.projNm = projNm;
	}
	
	public String getProjEncd() {
		return projEncd;
	}
	public void setProjEncd(String projEncd) {
		this.projEncd = projEncd;
	}
	public Long getToOrdrNum() {
		return toOrdrNum;
	}
	public void setToOrdrNum(Long toOrdrNum) {
		this.toOrdrNum = toOrdrNum;
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	
	public String getFormNum() {
		return formNum;
	}

	public void setFormNum(String formNum) {
		this.formNum = formNum;
	}

	public Integer getAdjSubInd() {
		return adjSubInd;
	}
	public void setAdjSubInd(Integer adjSubInd) {
		this.adjSubInd = adjSubInd;
	}
	public String getWhsEncd() {
		return whsEncd;
	}
	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getBatNum() {
		return batNum;
	}
	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public String getMemo() {
		return memos;
	}
	public void setMemo(String memo) {
		this.memos = memo;
	}
	public String getWhsNm() {
		return whsNm;
	}
	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}
	public String getInvtyNm() {
		return invtyNm;
	}
	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}
	public String getSpcModel() {
		return spcModel;
	}
	public void setSpcModel(String spcModel) {
		this.spcModel = spcModel;
	}
	public BigDecimal getBxRule() {
		return bxRule;
	}
	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}
	public String getMeasrCorpNm() {
		return measrCorpNm;
	}
	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
}

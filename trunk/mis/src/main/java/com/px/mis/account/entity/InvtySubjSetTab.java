package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//�����Ŀ���ñ�ʵ��
public class InvtySubjSetTab {

	//@JsonIgnore
	@JsonProperty("���")
	private Integer ordrNum;//���
	@JsonProperty("����������")
	private String invtyBigClsEncd;//���������
	@JsonProperty("�����������")
	private String invtyClsNm;//�����������
	@JsonProperty("�����Ŀ����")
	private String invtySubjId;//�����Ŀ���
	@JsonProperty("�����Ŀ����")
	private String invtySubjNm;//�����Ŀ����
	@JsonProperty("ί�д�����Ŀ����")
	private String entrsAgnSubjId;//ί�д�����Ŀ���
	@JsonProperty("ί�д�����Ŀ����")
	private String entrsAgnSubjNm;//ί�д�����Ŀ����
	@JsonProperty("��ע")
	private String memo;//��ע
	//JsonIgnore
	//@JsonIgnore
	@JsonProperty("�����տ����Ʒ��Ŀ���")
	private String amtblRecvId;//�����տ����Ʒ��Ŀ���
	//@JsonIgnore
	@JsonProperty("�����տ����Ʒ��Ŀ����")
	private String amtblRecvNm;//�����տ����Ʒ��Ŀ����
	
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

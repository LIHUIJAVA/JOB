package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//����Է���Ŀ���ñ�
public class InvtyCntPtySubjSetTab {
	@JsonProperty("���")
	//@JsonIgnore
	private Integer ordrNum;// ���
	//@JsonIgnore
	@JsonProperty("���ű���")
	private String deptId;// ���ű���
	@JsonProperty("�շ�������")
	private String recvSendCateId;// �շ�������
	@JsonProperty("�շ��������")
	private String recvSendCateNm;// �շ��������
	@JsonProperty("�������")
	//@JsonIgnore
	private String invtyEncd;// �������
	//@JsonIgnore
	@JsonProperty("�������")
	private String invtyNm;// �������
	@JsonProperty("����������")
	private String invtyBigClsEncd;// ����������
	@JsonProperty("�����������")
	private String invtyBigClsNm;// �����������
	@JsonProperty("�Է���Ŀ����")
	private String cntPtySubjId;// �Է���Ŀ����
	@JsonProperty("�Է���Ŀ����")
	private String cntPtySubjNm;// �Է���Ŀ����
	@JsonProperty("�ݹ���Ŀ����")
	private String teesSubjEncd;// �ݹ���Ŀ����
	@JsonProperty("�ݹ���Ŀ����")
	private String teesSubjNm;// �ݹ���Ŀ����
	@JsonProperty("��ע")
	private String memo;// ��ע

	//@JsonIgnore
	@JsonProperty("��������")
	private String deptNm;// ��������

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

package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//���Ӧ����Ŀ��ʵ��
public class InvtyPayblSubj {
	@JsonProperty("���")
	//@JsonIgnore
	private Integer incrsId;// ����id
	@JsonProperty("��Ӧ�̷������")
	private String provrClsEncd;// ��Ӧ�̷������
	@JsonProperty("��Ӧ�̷�������")
	private String provrClsNm;// ��Ӧ�̷�������
	@JsonProperty("Ӧ����Ŀ����")
	private String payblSubjEncd;// Ӧ����Ŀ����
	@JsonProperty("Ӧ����Ŀ����")
	private String payblSubjNm;// Ӧ����Ŀ����
	@JsonProperty("Ԥ����Ŀ����")
	private String prepySubjEncd;// Ԥ����Ŀ����
	@JsonProperty("Ԥ����Ŀ����")
	private String prepySubjNm;// Ԥ����Ŀ����
	/** ����Ϊ���� **/


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

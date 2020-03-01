package com.px.mis.account.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * �跽
 * @author lxya0
 *
 */
public class SubjectDebit {
	@JsonProperty("�跽��Ŀ���")
	private String subDebitId;//��Ŀ���
	@JsonProperty("�跽��Ŀ����")
  	private String subDebitType;//��Ŀ����2
	@JsonProperty("�跽��Ŀ����")
	private String subDebitNm;//��Ŀ����
	@JsonProperty("�跽���")
	private BigDecimal subDebitMoney;//�跽���
	@JsonProperty("�跽-�������")
	private BigDecimal subDebitCreditMoney;//�跽-�������
	@JsonProperty("�跽����")
	private BigDecimal subDebitNum;//�跽����
	@JsonProperty("�跽-��������")
	private BigDecimal subDebitCreditNum;//�跽-��������
	@JsonProperty("�跽��Ŀ����")
	private Integer subDebitPath;//��Ŀ���� ��1��2
	
	public SubjectDebit() {
		super();
	}
	public String getSubDebitId() {
		return subDebitId;
	}
	public void setSubDebitId(String subDebitId) {
		this.subDebitId = subDebitId;
	}
	public String getSubDebitType() {
		return subDebitType;
	}
	public void setSubDebitType(String subDebitType) {
		this.subDebitType = subDebitType;
	}
	public String getSubDebitNm() {
		return subDebitNm;
	}
	public void setSubDebitNm(String subDebitNm) {
		this.subDebitNm = subDebitNm;
	}
	public BigDecimal getSubDebitMoney() {
		return subDebitMoney;
	}
	public void setSubDebitMoney(BigDecimal subDebitMoney) {
		this.subDebitMoney = subDebitMoney;
	}
	public BigDecimal getSubDebitNum() {
		return subDebitNum;
	}
	public void setSubDebitNum(BigDecimal subDebitNum) {
		this.subDebitNum = subDebitNum;
	}
	public Integer getSubDebitPath() {
		return subDebitPath;
	}
	public void setSubDebitPath(Integer subDebitPath) {
		this.subDebitPath = subDebitPath;
	}
	public BigDecimal getSubDebitCreditMoney() {
		return subDebitCreditMoney;
	}
	public void setSubDebitCreditMoney(BigDecimal subDebitCreditMoney) {
		this.subDebitCreditMoney = subDebitCreditMoney;
	}
	public BigDecimal getSubDebitCreditNum() {
		return subDebitCreditNum;
	}
	public void setSubDebitCreditNum(BigDecimal subDebitCreditNum) {
		this.subDebitCreditNum = subDebitCreditNum;
	}
	public SubjectDebit(String subDebitId, String subDebitType, String subDebitNm, BigDecimal subDebitMoney,
			BigDecimal subDebitCreditMoney, BigDecimal subDebitNum, BigDecimal subDebitCreditNum,
			Integer subDebitPath) {
		super();
		this.subDebitId = subDebitId;
		this.subDebitType = subDebitType;
		this.subDebitNm = subDebitNm;
		this.subDebitMoney = subDebitMoney;
		this.subDebitCreditMoney = subDebitCreditMoney;
		this.subDebitNum = subDebitNum;
		this.subDebitCreditNum = subDebitCreditNum;
		this.subDebitPath = subDebitPath;
	}
	
	

	
	
	
}

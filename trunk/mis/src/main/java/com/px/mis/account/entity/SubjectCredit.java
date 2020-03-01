package com.px.mis.account.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ����
 * @author lxya0
 *
 */
public class SubjectCredit {
	@JsonProperty("������Ŀ���")
  	private String subCreditId;//��Ŀ���
	@JsonProperty("������Ŀ����")
  	private String subCreditType;//��Ŀ����2
	@JsonProperty("������Ŀ����")
	private String subCreditNm;//��Ŀ����
	@JsonProperty("�������")
	private BigDecimal subCreditMoney;//�������
	@JsonProperty("����-�跽���")
	private BigDecimal subCreditDebitMoney;//����-�跽���
	@JsonProperty("��������")
	private BigDecimal subCreditNum;//��������
	@JsonProperty("����-�跽����")
	private BigDecimal subCreditDebitNum;//����-�跽����
	@JsonProperty("������Ŀ����")
	private Integer subCreditPath;//��Ŀ���� ��1��2
	
	public SubjectCredit() {
		super();
	}
	public String getSubCreditId() {
		return subCreditId;
	}
	public void setSubCreditId(String subCreditId) {
		this.subCreditId = subCreditId;
	}
	public String getSubCreditType() {
		return subCreditType;
	}
	public void setSubCreditType(String subCreditType) {
		this.subCreditType = subCreditType;
	}
	public String getSubCreditNm() {
		return subCreditNm;
	}
	public void setSubCreditNm(String subCreditNm) {
		this.subCreditNm = subCreditNm;
	}
	
	public BigDecimal getSubCreditMoney() {
		return subCreditMoney;
	}
	public void setSubCreditMoney(BigDecimal subCreditMoney) {
		this.subCreditMoney = subCreditMoney;
	}
	public BigDecimal getSubCreditNum() {
		return subCreditNum;
	}
	public void setSubCreditNum(BigDecimal subCreditNum) {
		this.subCreditNum = subCreditNum;
	}
	public Integer getSubCreditPath() {
		return subCreditPath;
	}
	public void setSubCreditPath(Integer subCreditPath) {
		this.subCreditPath = subCreditPath;
	}
	public BigDecimal getSubCreditDebitMoney() {
		return subCreditDebitMoney;
	}
	public void setSubCreditDebitMoney(BigDecimal subCreditDebitMoney) {
		this.subCreditDebitMoney = subCreditDebitMoney;
	}
	public BigDecimal getSubCreditDebitNum() {
		return subCreditDebitNum;
	}
	public void setSubCreditDebitNum(BigDecimal subCreditDebitNum) {
		this.subCreditDebitNum = subCreditDebitNum;
	}
	public SubjectCredit(String subCreditId, String subCreditType, String subCreditNm, BigDecimal subCreditMoney,
			BigDecimal subCreditDebitMoney, BigDecimal subCreditNum, BigDecimal subCreditDebitNum,
			Integer subCreditPath) {
		super();
		this.subCreditId = subCreditId;
		this.subCreditType = subCreditType;
		this.subCreditNm = subCreditNm;
		this.subCreditMoney = subCreditMoney;
		this.subCreditDebitMoney = subCreditDebitMoney;
		this.subCreditNum = subCreditNum;
		this.subCreditDebitNum = subCreditDebitNum;
		this.subCreditPath = subCreditPath;
	}
	
	
	
	
}

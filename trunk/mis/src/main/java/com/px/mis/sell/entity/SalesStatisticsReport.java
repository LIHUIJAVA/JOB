package com.px.mis.sell.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * �����ۺ�ͳ�Ʊ�
 */
public class SalesStatisticsReport {
	@JsonProperty("�������")
	private String invtyEncd; // �������
	@JsonProperty("�������")
	private String invtyNm; // �������
//	private String sellSnglDt; // ����
//	private String sellSnglId; // ���۵���
	@JsonProperty("��������")
	private BigDecimal sellQty; // ��������
	@JsonProperty("�������")
	private BigDecimal sellnoTaxAmt; // �������
	@JsonProperty("������˰�ϼ�")
	private BigDecimal sellprcTaxSum; // ������˰�ϼ�
//	private String sellOutWhsDt; // ��������
//	private String sellOutWhsId; // ���ⵥ��
	@JsonProperty("��������")
	private BigDecimal sellOutQty; // ��������
	@JsonProperty("������")
	private BigDecimal sellOutNoTaxAmt; // ������
	@JsonProperty("�����˰�ϼ�")
	private BigDecimal sellOutPrcTaxSum; // �����˰�ϼ�
//	private String invSellSnglNum; // ��Ʊ��
//	private String invBllgDt; // ��Ʊʱ��
	@JsonProperty("��Ʊ����")
	private BigDecimal invQty; // ��Ʊ����
	@JsonProperty("��Ʊ���")
	private BigDecimal invNoTaxAmt; // ��Ʊ���
	@JsonProperty("��Ʊ��˰�ϼ�")
	private BigDecimal invPrcTaxSum; // ��Ʊ��˰�ϼ�
//	private String documentsTypes; // ��������
	@JsonProperty("�ͻ����")
	private String custId; // �ͻ����
	@JsonProperty("����")
	private String depName; // ����
	@JsonProperty("���ű���")
	private String depId; // ���ű���
	@JsonProperty("ҵ��Ա")
	private String userName; // ҵ��Ա
	@JsonProperty("ҵ��Ա����")
	private String accNum; // ҵ��Ա����
	@JsonProperty("�ͻ�����")
	private String custNm; // �ͻ�����
	

	public final BigDecimal getSellQty() {
		return sellQty;
	}

	public final void setSellQty(BigDecimal sellQty) {
		this.sellQty = sellQty;
	}

	public final BigDecimal getSellnoTaxAmt() {
		return sellnoTaxAmt;
	}

	public final void setSellnoTaxAmt(BigDecimal sellnoTaxAmt) {
		this.sellnoTaxAmt = sellnoTaxAmt;
	}

	public final BigDecimal getSellprcTaxSum() {
		return sellprcTaxSum;
	}

	public final void setSellprcTaxSum(BigDecimal sellprcTaxSum) {
		this.sellprcTaxSum = sellprcTaxSum;
	}

	public final BigDecimal getSellOutQty() {
		return sellOutQty;
	}

	public final void setSellOutQty(BigDecimal sellOutQty) {
		this.sellOutQty = sellOutQty;
	}

	public final BigDecimal getSellOutNoTaxAmt() {
		return sellOutNoTaxAmt;
	}

	public final void setSellOutNoTaxAmt(BigDecimal sellOutNoTaxAmt) {
		this.sellOutNoTaxAmt = sellOutNoTaxAmt;
	}

	public final BigDecimal getSellOutPrcTaxSum() {
		return sellOutPrcTaxSum;
	}

	public final void setSellOutPrcTaxSum(BigDecimal sellOutPrcTaxSum) {
		this.sellOutPrcTaxSum = sellOutPrcTaxSum;
	}

	public final BigDecimal getInvQty() {
		return invQty;
	}

	public final void setInvQty(BigDecimal invQty) {
		this.invQty = invQty;
	}

	public final BigDecimal getInvNoTaxAmt() {
		return invNoTaxAmt;
	}

	public final void setInvNoTaxAmt(BigDecimal invNoTaxAmt) {
		this.invNoTaxAmt = invNoTaxAmt;
	}

	public final BigDecimal getInvPrcTaxSum() {
		return invPrcTaxSum;
	}

	public final void setInvPrcTaxSum(BigDecimal invPrcTaxSum) {
		this.invPrcTaxSum = invPrcTaxSum;
	}

	public final String getCustId() {
		return custId;
	}

	public final void setCustId(String custId) {
		this.custId = custId;
	}

	public final String getDepName() {
		return depName;
	}

	public final void setDepName(String depName) {
		this.depName = depName;
	}

	public final String getDepId() {
		return depId;
	}

	public final void setDepId(String depId) {
		this.depId = depId;
	}

	public final String getUserName() {
		return userName;
	}

	public final void setUserName(String userName) {
		this.userName = userName;
	}

	public final String getAccNum() {
		return accNum;
	}

	public final void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public final String getCustNm() {
		return custNm;
	}

	public final void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public final String getInvtyEncd() {
		return invtyEncd;
	}

	public final void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}

	public final String getInvtyNm() {
		return invtyNm;
	}

	public final void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}

}

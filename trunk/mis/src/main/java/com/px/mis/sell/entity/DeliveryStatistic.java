package com.px.mis.sell.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliveryStatistic {
	@JsonProperty("���ݱ��")
	private	String sellSnglId;//���ݱ��
	@JsonProperty("��������")
	private	String	sellSnglDt;//��������
	@JsonProperty("�ͻ�����")
	private	String	custId;//�ͻ�����
	@JsonProperty("�ͻ�����")
	private	String	custNm;//�ͻ�����
	@JsonProperty("�ֿ�����")
	private	String	whsNm;//�ֿ�����
	@JsonProperty("�������")
	private	String	invtyEncd;//�������
	@JsonProperty("�������")
	private	String	invtyNm;//�������
	@JsonProperty("���")
	private	String	spcModel;//���
	@JsonProperty("��������λ")
	private	String	measrCorpNm;//��������λ
	@JsonProperty("��������")
	private	BigDecimal	qty;//��������
	@JsonProperty("������˰�ϼ�")
	private	BigDecimal	prcTaxSum;//������˰�ϼ�
	@JsonProperty("��Ʊ����")
	private	BigDecimal	bllgQty;//��Ʊ����
	@JsonProperty("��������")
	private	BigDecimal	outQty;//��������
	@JsonProperty("����ɱ�")
	private	BigDecimal	outNoTaxAmt;//����ɱ�
	@JsonProperty("������")
	private	BigDecimal	netDelivery;//������
	@JsonProperty("���˻�")
	private	BigDecimal	netReturn;//���˻�
	
	
	public String getSellSnglId() {
		
		return sellSnglId;
	}
	public void setSellSnglId(String sellSnglId) {
		this.sellSnglId = sellSnglId;
	}
	public String getSellSnglDt() {
		return sellSnglDt;
	}
	public void setSellSnglDt(String sellSnglDt) {
		this.sellSnglDt = sellSnglDt;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustNm() {
		return custNm;
	}
	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}
	public String getWhsNm() {
		return whsNm;
	}
	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
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
	public String getMeasrCorpNm() {
		return measrCorpNm;
	}
	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public BigDecimal getPrcTaxSum() {
		return prcTaxSum;
	}
	public void setPrcTaxSum(BigDecimal prcTaxSum) {
		this.prcTaxSum = prcTaxSum;
	}
	public BigDecimal getBllgQty() {
		return bllgQty;
	}
	public void setBllgQty(BigDecimal bllgQty) {
		this.bllgQty = bllgQty;
	}
	public BigDecimal getOutQty() {
		return outQty;
	}
	public void setOutQty(BigDecimal outQty) {
		this.outQty = outQty;
	}
	public BigDecimal getOutNoTaxAmt() {
		return outNoTaxAmt;
	}
	public void setOutNoTaxAmt(BigDecimal outNoTaxAmt) {
		this.outNoTaxAmt = outNoTaxAmt;
	}
	public BigDecimal getNetDelivery() {
		return netDelivery;
	}
	public void setNetDelivery(BigDecimal netDelivery) {
		this.netDelivery = netDelivery;
	}
	public BigDecimal getNetReturn() {
		return netReturn;
	}
	public void setNetReturn(BigDecimal netReturn) {
		this.netReturn = netReturn;
	}
	
	

}

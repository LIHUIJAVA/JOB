package com.px.mis.account.entity;

import java.math.BigDecimal;

public class SellOutWhsPool {
	private String custId;//�ͻ����
	private String custNm;//�ͻ�
	
	private String invtyNm; //�������
	private String spcModel; //����ͺ�
    private String measrCorpNm; //������λ
    private String invtyEncd; //������
    private BigDecimal amt;//�ڳ��ܶ�
    private BigDecimal qty;//�ڳ�����
    
    
    private BigDecimal inQty;//��������
    private BigDecimal inMoeny;//������
   
    private BigDecimal sendQty;//��������
    private BigDecimal sendMoeny;//�������
    
    private BigDecimal othQty;//�������
    private BigDecimal othMoeny;//�����
    
    private Integer isNtBllg;//�Ƿ�Ʊ

    private Integer isNtStl;//�Ƿ����
    
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
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public BigDecimal getInQty() {
		return inQty;
	}
	public void setInQty(BigDecimal inQty) {
		this.inQty = inQty;
	}
	public BigDecimal getInMoeny() {
		return inMoeny;
	}
	public void setInMoeny(BigDecimal inMoeny) {
		this.inMoeny = inMoeny;
	}
	public BigDecimal getSendQty() {
		return sendQty;
	}
	public void setSendQty(BigDecimal sendQty) {
		this.sendQty = sendQty;
	}
	public BigDecimal getSendMoeny() {
		return sendMoeny;
	}
	public void setSendMoeny(BigDecimal sendMoeny) {
		this.sendMoeny = sendMoeny;
	}
	public BigDecimal getOthQty() {
		return othQty;
	}
	public void setOthQty(BigDecimal othQty) {
		this.othQty = othQty;
	}
	public BigDecimal getOthMoeny() {
		return othMoeny;
	}
	public void setOthMoeny(BigDecimal othMoeny) {
		this.othMoeny = othMoeny;
	}
	public Integer getIsNtBllg() {
		return isNtBllg;
	}
	public void setIsNtBllg(Integer isNtBllg) {
		this.isNtBllg = isNtBllg;
	}
	public Integer getIsNtStl() {
		return isNtStl;
	}
	public void setIsNtStl(Integer isNtStl) {
		this.isNtStl = isNtStl;
	}
    
    
}

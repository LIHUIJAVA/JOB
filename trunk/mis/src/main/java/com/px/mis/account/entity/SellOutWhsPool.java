package com.px.mis.account.entity;

import java.math.BigDecimal;

public class SellOutWhsPool {
	private String custId;//客户编号
	private String custNm;//客户
	
	private String invtyNm; //存货名称
	private String spcModel; //规格型号
    private String measrCorpNm; //计量单位
    private String invtyEncd; //存货编号
    private BigDecimal amt;//期初总额
    private BigDecimal qty;//期初数量
    
    
    private BigDecimal inQty;//收入数量
    private BigDecimal inMoeny;//收入金额
   
    private BigDecimal sendQty;//发出数量
    private BigDecimal sendMoeny;//发出金额
    
    private BigDecimal othQty;//结存数量
    private BigDecimal othMoeny;//结存金额
    
    private Integer isNtBllg;//是否开票

    private Integer isNtStl;//是否结算
    
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

package com.px.mis.account.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvtyDetails {
	@JsonProperty("子表序号")
	private String invtyDetailsId; 
	@JsonProperty("记账时间")
    private String bookOkDt; //记账时间
	@JsonProperty("凭证编码")
    private String makeVouchId;//凭证编码
	@JsonProperty("凭证摘要")
    private String makeVouchAbst;//凭证摘要
	@JsonProperty("收发类型编码")
    private String recvSendCateId;//收发类型编码
	@JsonProperty("收发类型名称")
    private String recvSendCateNm;//收发类型名称
	@JsonProperty("收入单价")
    private BigDecimal inUnitPrice; //收入单价
	@JsonProperty("收入数量")
    private BigDecimal inQty;//收入数量
	@JsonProperty("收入金额")
    private BigDecimal inMoeny;//收入金额
	@JsonProperty("发出单价")
    private BigDecimal sendUnitPrice;//发出单价
	@JsonProperty("发出数量")
    private BigDecimal sendQty;//发出数量
	@JsonProperty("发出金额")
    private BigDecimal sendMoeny;//发出金额
	@JsonProperty("结算单价")
    private BigDecimal othUnitPrice;//结算单价
	@JsonProperty("结算数量")
    private BigDecimal othQty;//结算数量
	@JsonProperty("结算金额")
    private BigDecimal othMoeny;//结算金额
	
	@JsonProperty("主表id")
    private int detailId; //主表id
	
	@JsonProperty("单据号")
    private String formNum; //单据号
	
	@JsonProperty("是否开票")
    private Integer isNtBllg;//是否开票
	@JsonProperty("上级单据号")
    private String sellOrdrInd; //上级单据号
	@JsonProperty("上级子表标识")
	private int toOrdrNum; //上级子表标识
	
	
	public int getToOrdrNum() {
		return toOrdrNum;
	}
	public void setToOrdrNum(int toOrdrNum) {
		this.toOrdrNum = toOrdrNum;
	}
	public String getInvtyDetailsId() {
		return invtyDetailsId;
	}
	public void setInvtyDetailsId(String invtyDetailsId) {
		this.invtyDetailsId = invtyDetailsId;
	}
	public int getDetailId() {
		return detailId;
	}
	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}
	public String getBookOkDt() {
		return bookOkDt;
	}
	public void setBookOkDt(String bookOkDt) {
		this.bookOkDt = bookOkDt;
	}
	public String getMakeVouchId() {
		return makeVouchId;
	}
	public void setMakeVouchId(String makeVouchId) {
		this.makeVouchId = makeVouchId;
	}
	public String getMakeVouchAbst() {
		return makeVouchAbst;
	}
	public void setMakeVouchAbst(String makeVouchAbst) {
		this.makeVouchAbst = makeVouchAbst;
	}
	public String getRecvSendCateId() {
		return recvSendCateId;
	}
	public void setRecvSendCateId(String recvSendCateId) {
		this.recvSendCateId = recvSendCateId;
	}
	public String getRecvSendCateNm() {
		return recvSendCateNm;
	}
	public void setRecvSendCateNm(String recvSendCateNm) {
		this.recvSendCateNm = recvSendCateNm;
	}
	public BigDecimal getInUnitPrice() {
		return inUnitPrice;
	}
	public void setInUnitPrice(BigDecimal inUnitPrice) {
		this.inUnitPrice = inUnitPrice;
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
	public BigDecimal getSendUnitPrice() {
		return sendUnitPrice;
	}
	public void setSendUnitPrice(BigDecimal sendUnitPrice) {
		this.sendUnitPrice = sendUnitPrice;
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
	public BigDecimal getOthUnitPrice() {
		return othUnitPrice;
	}
	public void setOthUnitPrice(BigDecimal othUnitPrice) {
		this.othUnitPrice = othUnitPrice;
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
	
	public String getFormNum() {
		return formNum;
	}
	public void setFormNum(String formNum) {
		this.formNum = formNum;
	}
	
	public Integer getIsNtBllg() {
		return isNtBllg;
	}
	public void setIsNtBllg(Integer isNtBllg) {
		this.isNtBllg = isNtBllg;
	}
	
	
	public String getSellOrdrInd() {
		return sellOrdrInd;
	}
	public void setSellOrdrInd(String sellOrdrInd) {
		this.sellOrdrInd = sellOrdrInd;
	}
	public InvtyDetails() {}
	
	public InvtyDetails(String makeVouchAbst, BigDecimal othUnitPrice,BigDecimal othQty, BigDecimal othMoeny) {
		super();
		this.makeVouchAbst = makeVouchAbst;
		this.othUnitPrice = othUnitPrice;
		this.othQty = othQty;
		this.othMoeny = othMoeny;
	}
	public InvtyDetails(String makeVouchAbst, BigDecimal inQty, BigDecimal inMoeny, BigDecimal sendQty, BigDecimal sendMoeny, 
			BigDecimal othUnitPrice,BigDecimal othQty, BigDecimal othMoeny) {
		super();
		
		this.makeVouchAbst = makeVouchAbst;
		this.inQty = inQty;
		this.inMoeny = inMoeny;
		this.sendQty = sendQty;
		this.sendMoeny = sendMoeny;
		this.othUnitPrice = othUnitPrice;
		this.othQty = othQty;
		this.othMoeny = othMoeny;
	}
	public InvtyDetails(String bookOkDt, String makeVouchId, String makeVouchAbst, String recvSendCateId,
			String recvSendCateNm, BigDecimal inUnitPrice, BigDecimal inQty, BigDecimal inMoeny,
			BigDecimal sendUnitPrice, BigDecimal sendQty, BigDecimal sendMoeny, BigDecimal othUnitPrice,
			BigDecimal othQty, BigDecimal othMoeny, String formNum,int detailsId,int toOrdrNum) {
		super();
		this.bookOkDt = bookOkDt;
		this.makeVouchId = makeVouchId;
		this.makeVouchAbst = makeVouchAbst;
		this.recvSendCateId = recvSendCateId;
		this.recvSendCateNm = recvSendCateNm;
		this.inUnitPrice = inUnitPrice;
		this.inQty = inQty;
		this.inMoeny = inMoeny;
		this.sendUnitPrice = sendUnitPrice;
		this.sendQty = sendQty;
		this.sendMoeny = sendMoeny;
		this.othUnitPrice = othUnitPrice;
		this.othQty = othQty;
		this.othMoeny = othMoeny;
		this.formNum = formNum;
		this.detailId = detailsId;
		this.toOrdrNum = toOrdrNum;
	}
	
	
	public InvtyDetails(BigDecimal inQty, BigDecimal inMoeny, BigDecimal sendQty, BigDecimal sendMoeny,
			BigDecimal othQty, BigDecimal othMoeny) {
		super();
		
		this.inQty = inQty;
		this.inMoeny = inMoeny;
		this.sendQty = sendQty;
		this.sendMoeny = sendMoeny;
		this.othQty = othQty;
		this.othMoeny = othMoeny;
	
	}
	

	
}

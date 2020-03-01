package com.px.mis.account.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvtyDetails {
	@JsonProperty("�ӱ����")
	private String invtyDetailsId; 
	@JsonProperty("����ʱ��")
    private String bookOkDt; //����ʱ��
	@JsonProperty("ƾ֤����")
    private String makeVouchId;//ƾ֤����
	@JsonProperty("ƾ֤ժҪ")
    private String makeVouchAbst;//ƾ֤ժҪ
	@JsonProperty("�շ����ͱ���")
    private String recvSendCateId;//�շ����ͱ���
	@JsonProperty("�շ���������")
    private String recvSendCateNm;//�շ���������
	@JsonProperty("���뵥��")
    private BigDecimal inUnitPrice; //���뵥��
	@JsonProperty("��������")
    private BigDecimal inQty;//��������
	@JsonProperty("������")
    private BigDecimal inMoeny;//������
	@JsonProperty("��������")
    private BigDecimal sendUnitPrice;//��������
	@JsonProperty("��������")
    private BigDecimal sendQty;//��������
	@JsonProperty("�������")
    private BigDecimal sendMoeny;//�������
	@JsonProperty("���㵥��")
    private BigDecimal othUnitPrice;//���㵥��
	@JsonProperty("��������")
    private BigDecimal othQty;//��������
	@JsonProperty("������")
    private BigDecimal othMoeny;//������
	
	@JsonProperty("����id")
    private int detailId; //����id
	
	@JsonProperty("���ݺ�")
    private String formNum; //���ݺ�
	
	@JsonProperty("�Ƿ�Ʊ")
    private Integer isNtBllg;//�Ƿ�Ʊ
	@JsonProperty("�ϼ����ݺ�")
    private String sellOrdrInd; //�ϼ����ݺ�
	@JsonProperty("�ϼ��ӱ��ʶ")
	private int toOrdrNum; //�ϼ��ӱ��ʶ
	
	
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

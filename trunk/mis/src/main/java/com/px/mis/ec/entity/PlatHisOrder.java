package com.px.mis.ec.entity;

import java.util.Date;

public class PlatHisOrder {
	
    private String orderId;//�������
    private String storeId;//���̱��
    private String storeName;//��������
    private Date payTime;//����ʱ��
    private Integer waif;//���
    private Integer isAudit;//�Ƿ����
    private String auditHint;//�����ʾ
    private Integer goodNum;//��Ʒ����
    private Long goodMoney;//��Ʒ���
    private Long payMoney;//ʵ�����
    private String buyerNote;//�������
    private String sellerNote;//���ұ�ע
    private String recAddress;//�ջ�����ϸ��ַ
    private String buyerId;//��һ�Ա��
    private String recName;//�ջ�������
    private String recMobile;//�ջ����ֻ���
    private String ecOrderId;//���̶�����
    private Integer isInvoice;//�Ƿ�Ʊ
    private String invoiceTitle;//���ұ�ע����
    private Integer noteFlag;//�Ƿ�ر�
    private Integer isClose;//�Ƿ�ر�
    private Integer isShip;//�Ƿ񷢻�
    private Long adjustMoney;//���ҵ������
    private Long discountMoney;//ϵͳ�Żݽ��
    private Long goodPrice;//��Ʒ����
    private Long payPrice;//ʵ�����
    private Integer orderStatus;//����״̬
    private Integer returnStatus;//�˻�״̬
    private Integer hasGift;//�Ƿ���Ʒ
    private String memo;//��ע

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId == null ? null : storeId.trim();
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getWaif() {
        return waif;
    }

    public void setWaif(Integer waif) {
        this.waif = waif;
    }

    public Integer getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(Integer isAudit) {
        this.isAudit = isAudit;
    }

    public String getAuditHint() {
        return auditHint;
    }

    public void setAuditHint(String auditHint) {
        this.auditHint = auditHint == null ? null : auditHint.trim();
    }

    public Integer getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(Integer goodNum) {
        this.goodNum = goodNum;
    }

    public Long getGoodMoney() {
        return goodMoney;
    }

    public void setGoodMoney(Long goodMoney) {
        this.goodMoney = goodMoney;
    }

    public Long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Long payMoney) {
        this.payMoney = payMoney;
    }

    public String getBuyerNote() {
        return buyerNote;
    }

    public void setBuyerNote(String buyerNote) {
        this.buyerNote = buyerNote == null ? null : buyerNote.trim();
    }

    public String getSellerNote() {
        return sellerNote;
    }

    public void setSellerNote(String sellerNote) {
        this.sellerNote = sellerNote == null ? null : sellerNote.trim();
    }

    public String getRecAddress() {
        return recAddress;
    }

    public void setRecAddress(String recAddress) {
        this.recAddress = recAddress == null ? null : recAddress.trim();
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId == null ? null : buyerId.trim();
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName == null ? null : recName.trim();
    }

    public String getRecMobile() {
        return recMobile;
    }

    public void setRecMobile(String recMobile) {
        this.recMobile = recMobile == null ? null : recMobile.trim();
    }

    public String getEcOrderId() {
        return ecOrderId;
    }

    public void setEcOrderId(String ecOrderId) {
        this.ecOrderId = ecOrderId == null ? null : ecOrderId.trim();
    }

    public Integer getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(Integer isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle == null ? null : invoiceTitle.trim();
    }

    public Integer getNoteFlag() {
        return noteFlag;
    }

    public void setNoteFlag(Integer noteFlag) {
        this.noteFlag = noteFlag;
    }

    public Integer getIsClose() {
        return isClose;
    }

    public void setIsClose(Integer isClose) {
        this.isClose = isClose;
    }

    public Integer getIsShip() {
        return isShip;
    }

    public void setIsShip(Integer isShip) {
        this.isShip = isShip;
    }

    public Long getAdjustMoney() {
        return adjustMoney;
    }

    public void setAdjustMoney(Long adjustMoney) {
        this.adjustMoney = adjustMoney;
    }

    public Long getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Long discountMoney) {
        this.discountMoney = discountMoney;
    }

    public Long getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(Long goodPrice) {
        this.goodPrice = goodPrice;
    }

    public Long getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Long payPrice) {
        this.payPrice = payPrice;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(Integer returnStatus) {
        this.returnStatus = returnStatus;
    }

    public Integer getHasGift() {
        return hasGift;
    }

    public void setHasGift(Integer hasGift) {
        this.hasGift = hasGift;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}
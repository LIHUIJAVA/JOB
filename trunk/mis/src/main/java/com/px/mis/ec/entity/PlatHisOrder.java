package com.px.mis.ec.entity;

import java.util.Date;

public class PlatHisOrder {
	
    private String orderId;//订单编号
    private String storeId;//店铺编号
    private String storeName;//店铺名称
    private Date payTime;//付款时间
    private Integer waif;//旗标
    private Integer isAudit;//是否客审
    private String auditHint;//审核提示
    private Integer goodNum;//商品数量
    private Long goodMoney;//商品金额
    private Long payMoney;//实付金额
    private String buyerNote;//买家留言
    private String sellerNote;//卖家备注
    private String recAddress;//收货人详细地址
    private String buyerId;//买家会员号
    private String recName;//收货人姓名
    private String recMobile;//收货人手机号
    private String ecOrderId;//电商订单号
    private Integer isInvoice;//是否开票
    private String invoiceTitle;//卖家备注旗帜
    private Integer noteFlag;//是否关闭
    private Integer isClose;//是否关闭
    private Integer isShip;//是否发货
    private Long adjustMoney;//卖家调整金额
    private Long discountMoney;//系统优惠金额
    private Long goodPrice;//商品单价
    private Long payPrice;//实付金额
    private Integer orderStatus;//订单状态
    private Integer returnStatus;//退货状态
    private Integer hasGift;//是否含赠品
    private String memo;//备注

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
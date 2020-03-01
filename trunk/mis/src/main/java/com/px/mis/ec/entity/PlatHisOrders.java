package com.px.mis.ec.entity;

public class PlatHisOrders {
	
    private Long no;//序号
    private String goodId;//商品编号
    private Integer goodNum;//商品数量
    private Long goodMoney;//商品金额
    private Long payMoney;//实付金额
    private String goodSku;//商品sku
    private String orderId;//订单编号
    private String bachNo;//批号
    private String expressCom;//快递公司
    private String deliverWhs;//发货仓库
    private String proActId;//促销活动编号
    private Long discountMoney;//系统优惠金额
    private String adjustMoney;//买家调整金额
    private String memo;//备注

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId == null ? null : goodId.trim();
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

    public String getGoodSku() {
        return goodSku;
    }

    public void setGoodSku(String goodSku) {
        this.goodSku = goodSku == null ? null : goodSku.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getBachNo() {
        return bachNo;
    }

    public void setBachNo(String bachNo) {
        this.bachNo = bachNo == null ? null : bachNo.trim();
    }

    public String getExpressCom() {
        return expressCom;
    }

    public void setExpressCom(String expressCom) {
        this.expressCom = expressCom == null ? null : expressCom.trim();
    }

    public String getDeliverWhs() {
        return deliverWhs;
    }

    public void setDeliverWhs(String deliverWhs) {
        this.deliverWhs = deliverWhs == null ? null : deliverWhs.trim();
    }

    public String getProActId() {
        return proActId;
    }

    public void setProActId(String proActId) {
        this.proActId = proActId == null ? null : proActId.trim();
    }

    public Long getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Long discountMoney) {
        this.discountMoney = discountMoney;
    }

    public String getAdjustMoney() {
        return adjustMoney;
    }

    public void setAdjustMoney(String adjustMoney) {
        this.adjustMoney = adjustMoney == null ? null : adjustMoney.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}
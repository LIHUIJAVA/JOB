package com.px.mis.ec.entity;

public class PlatHisOrders {
	
    private Long no;//���
    private String goodId;//��Ʒ���
    private Integer goodNum;//��Ʒ����
    private Long goodMoney;//��Ʒ���
    private Long payMoney;//ʵ�����
    private String goodSku;//��Ʒsku
    private String orderId;//�������
    private String bachNo;//����
    private String expressCom;//��ݹ�˾
    private String deliverWhs;//�����ֿ�
    private String proActId;//��������
    private Long discountMoney;//ϵͳ�Żݽ��
    private String adjustMoney;//��ҵ������
    private String memo;//��ע

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
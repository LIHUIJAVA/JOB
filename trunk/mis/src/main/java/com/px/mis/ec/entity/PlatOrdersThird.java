package com.px.mis.ec.entity;

import java.math.BigDecimal;

/**
 * 第三方平台商品关系映射
 *
 */
public class PlatOrdersThird {
	
    private Long no;//序号
    private String goodId;//商品编号
    private Integer itemTotal;//商品数量 原单数量
    private BigDecimal goodMoney;//商品金额
    private BigDecimal payMoney;//实付金额
    private String skuName;//平台商品名称
    private String skuId;//商品sku
    private String orderId;//订单编号
    private String batchNo;//批号
    private String expressCom;//快递公司
    private String proActId;//促销活动编号
    private BigDecimal discountMoney;//系统优惠金额
    private BigDecimal adjustMoney;//卖家调整金额
    private String memo;//备注
    private BigDecimal unitPrice;//商品单价
    private BigDecimal payPrice;//实付单价
    private String newStoreId;//发货仓库编码 
    private BigDecimal sellerPrice;//结算单价
    private String ecOrderId;//平台订单号
    private String invId;//存货编码
    private Integer invNum;//存货数量 
    private Integer splitNum;//拆分数量
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
		this.goodId = goodId;
	}
	public Integer getItemTotal() {
		return itemTotal;
	}
	public void setItemTotal(Integer itemTotal) {
		this.itemTotal = itemTotal;
	}
	public BigDecimal getGoodMoney() {
		return goodMoney;
	}
	public void setGoodMoney(BigDecimal goodMoney) {
		this.goodMoney = goodMoney;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getExpressCom() {
		return expressCom;
	}
	public void setExpressCom(String expressCom) {
		this.expressCom = expressCom;
	}
	public String getProActId() {
		return proActId;
	}
	public void setProActId(String proActId) {
		this.proActId = proActId;
	}
	public BigDecimal getDiscountMoney() {
		return discountMoney;
	}
	public void setDiscountMoney(BigDecimal discountMoney) {
		this.discountMoney = discountMoney;
	}
	public BigDecimal getAdjustMoney() {
		return adjustMoney;
	}
	public void setAdjustMoney(BigDecimal adjustMoney) {
		this.adjustMoney = adjustMoney;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}
	public String getNewStoreId() {
		return newStoreId;
	}
	public void setNewStoreId(String newStoreId) {
		this.newStoreId = newStoreId;
	}
	public BigDecimal getSellerPrice() {
		return sellerPrice;
	}
	public void setSellerPrice(BigDecimal sellerPrice) {
		this.sellerPrice = sellerPrice;
	}
	public String getEcOrderId() {
		return ecOrderId;
	}
	public void setEcOrderId(String ecOrderId) {
		this.ecOrderId = ecOrderId;
	}
	public String getInvId() {
		return invId;
	}
	public void setInvId(String invId) {
		this.invId = invId;
	}
	public Integer getInvNum() {
		return invNum;
	}
	public void setInvNum(Integer invNum) {
		this.invNum = invNum;
	}
	public Integer getSplitNum() {
		return splitNum;
	}
	public void setSplitNum(Integer splitNum) {
		this.splitNum = splitNum;
	}
	
	
    
    

	
}
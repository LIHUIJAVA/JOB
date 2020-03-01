package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class GoodRecord {

	private Integer id;// 商品档案id
	private String goodId;// 商品编号
	private String storeId;// 店铺编号
	private String ecId;// 电商平台编号
	private String goodName;// 存货商品名称
	private String goodSku;// 商品sku
	private String ecGoodId;// 平台商品编号
	private String ecGoodName;// 平台商品名称
	private String goodMode;// 规格型号
	private BigDecimal upsetPrice;// 最低售价
	private String safeInv;// 安全库存
	private String skuProp;// sku属性
	private String onlineStatus;// 线上状态
	private String operator;// 操作人
	private String operatTime;// 操作时间
	private Integer isSecSale;// 是否二销
	private String memo;// 备注

	private Long no;// 商品项的主键id

	private String storeName;// 店铺名称

	private String ecName;// 电商平台名称

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getEcName() {
		return ecName;
	}

	public void setEcName(String ecName) {
		this.ecName = ecName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId == null ? null : goodId.trim();
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId == null ? null : storeId.trim();
	}

	public String getEcId() {
		return ecId;
	}

	public void setEcId(String ecId) {
		this.ecId = ecId == null ? null : ecId.trim();
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName == null ? null : goodName.trim();
	}

	public String getGoodSku() {
		return goodSku;
	}

	public void setGoodSku(String goodSku) {
		this.goodSku = goodSku == null ? null : goodSku.trim();
	}

	public String getEcGoodId() {
		return ecGoodId;
	}

	public void setEcGoodId(String ecGoodId) {
		this.ecGoodId = ecGoodId == null ? null : ecGoodId.trim();
	}

	public String getEcGoodName() {
		return ecGoodName;
	}

	public void setEcGoodName(String ecGoodName) {
		this.ecGoodName = ecGoodName == null ? null : ecGoodName.trim();
	}

	public String getGoodMode() {
		return goodMode;
	}

	public void setGoodMode(String goodMode) {
		this.goodMode = goodMode == null ? null : goodMode.trim();
	}

	public BigDecimal getUpsetPrice() {
		return upsetPrice;
	}

	public void setUpsetPrice(BigDecimal upsetPrice) {
		this.upsetPrice = upsetPrice;
	}

	public String getSafeInv() {
		return safeInv;
	}

	public void setSafeInv(String safeInv) {
		this.safeInv = safeInv == null ? null : safeInv.trim();
	}

	public String getSkuProp() {
		return skuProp;
	}

	public void setSkuProp(String skuProp) {
		this.skuProp = skuProp == null ? null : skuProp.trim();
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus == null ? null : onlineStatus.trim();
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator == null ? null : operator.trim();
	}

	public String getOperatTime() {
		return operatTime;
	}

	public void setOperatTime(String operatTime) {
		this.operatTime = operatTime == null ? null : operatTime.trim();
	}

	public Integer getIsSecSale() {
		return isSecSale;
	}

	public void setIsSecSale(Integer isSecSale) {
		this.isSecSale = isSecSale;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}

	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	@Override
	public String toString() {
		return "GoodRecord [id=" + id + ", goodId=" + goodId + ", storeId=" + storeId + ", ecId=" + ecId + ", goodName="
				+ goodName + ", goodSku=" + goodSku + ", ecGoodId=" + ecGoodId + ", ecGoodName=" + ecGoodName
				+ ", goodMode=" + goodMode + ", upsetPrice=" + upsetPrice + ", safeInv=" + safeInv + ", skuProp="
				+ skuProp + ", onlineStatus=" + onlineStatus + ", operator=" + operator + ", operatTime=" + operatTime
				+ ", isSecSale=" + isSecSale + ", memo=" + memo + ", no=" + no + "]";
	}

}
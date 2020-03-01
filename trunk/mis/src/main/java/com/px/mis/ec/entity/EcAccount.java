package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class EcAccount {
	private String storeId;//店铺id
	private String storeName;//店铺名称
	private String billNo;//业务单号
	private String ecOrderId;//电商订单号
	private String orderType;//单据类型
	private String shOrderId;//商户订单号
	private String goodNo;//商品编号
	private String goodName;//商品名称
	private String startTime;//费用发生时间
	private String jifeiTime;//计费时间
	private String jiesuanTime;//结算时间
	private String costType;//费用项
	private BigDecimal money;//金额
	private Integer moneyType;//收支类型0收1支
	private String memo;//备注
	private String shopId;//店铺号
	private int direction;//收支方向0收入1支出
	private String costDate;//账单日期
	private int checkResult;//勾兑结果0未勾兑1已勾兑
	private String checkerId;//勾兑人id
	private String checkerName;//勾兑人
	private String  checkTime;//勾兑时间
	private int isCheckType;//是否勾兑项0否1是，为1时参与汇总统计'
	private String creator;//创建人
	private String createTime;//创建时间
	
	
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getEcOrderId() {
		return ecOrderId;
	}
	public void setEcOrderId(String ecOrderId) {
		this.ecOrderId = ecOrderId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getShOrderId() {
		return shOrderId;
	}
	public void setShOrderId(String shOrderId) {
		this.shOrderId = shOrderId;
	}
	public String getGoodNo() {
		return goodNo;
	}
	public void setGoodNo(String goodNo) {
		this.goodNo = goodNo;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getJifeiTime() {
		return jifeiTime;
	}
	public void setJifeiTime(String jifeiTime) {
		this.jifeiTime = jifeiTime;
	}
	public String getJiesuanTime() {
		return jiesuanTime;
	}
	public void setJiesuanTime(String jiesuanTime) {
		this.jiesuanTime = jiesuanTime;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public Integer getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public String getCostDate() {
		return costDate;
	}
	public void setCostDate(String costDate) {
		this.costDate = costDate;
	}
	public int getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(int checkResult) {
		this.checkResult = checkResult;
	}
	public String getCheckerId() {
		return checkerId;
	}
	public void setCheckerId(String checkerId) {
		this.checkerId = checkerId;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public int getIsCheckType() {
		return isCheckType;
	}
	public void setIsCheckType(int isCheckType) {
		this.isCheckType = isCheckType;
	}
	
}

package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class EcAccount {
	private String storeId;//����id
	private String storeName;//��������
	private String billNo;//ҵ�񵥺�
	private String ecOrderId;//���̶�����
	private String orderType;//��������
	private String shOrderId;//�̻�������
	private String goodNo;//��Ʒ���
	private String goodName;//��Ʒ����
	private String startTime;//���÷���ʱ��
	private String jifeiTime;//�Ʒ�ʱ��
	private String jiesuanTime;//����ʱ��
	private String costType;//������
	private BigDecimal money;//���
	private Integer moneyType;//��֧����0��1֧
	private String memo;//��ע
	private String shopId;//���̺�
	private int direction;//��֧����0����1֧��
	private String costDate;//�˵�����
	private int checkResult;//���ҽ��0δ����1�ѹ���
	private String checkerId;//������id
	private String checkerName;//������
	private String  checkTime;//����ʱ��
	private int isCheckType;//�Ƿ񹴶���0��1�ǣ�Ϊ1ʱ�������ͳ��'
	private String creator;//������
	private String createTime;//����ʱ��
	
	
	
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

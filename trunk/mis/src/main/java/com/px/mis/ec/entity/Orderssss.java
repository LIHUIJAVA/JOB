package com.px.mis.ec.entity;

import java.math.BigDecimal;

/**
 * �����б�
 * @author Mr.Lu
 *
 */
public class Orderssss {
	private String ecOrderId;//ƽ̨������  
	private String orderId;//�������
	private String storeId;//���̱��
	private String storeName;//��������
	private String tradeDt;//����ʱ��
	private String payTime;//����ʱ��
    private String goodId;//��Ʒ���
    private String goodSku;//��Ʒsku
    private String goodName;//ƽ̨��Ʒ����
    private BigDecimal goodPrice;//��Ʒ����
    private Integer goodNum;//��Ʒ���� ԭ������
    private BigDecimal goodMoney;//��Ʒ���
    private BigDecimal discountMoney;//ϵͳ�Żݽ��
    private BigDecimal adjustMoney;//���ҵ������
    private BigDecimal payMoney;//ʵ�����
    private String invId;//�������
    private String invName;//�������
    private Integer invNum;//�������
    private String ptoCode;//ĸ������
    private String ptoName;//ĸ������
    private String batchNo;//����
    private String prdcDt;//��������
    private String invldtnDt;//ʧЧ����
    private String deliverWhs;//�����ֿ���� 
    private String deliverWhsName;//�����ֿ�����
    private String expressCode;//��ݹ�˾����
    private String expressName;//��ݹ�˾����
    private String expressNo;//��ݵ���
    private int canRefNum;//��������
    private BigDecimal canRefMoney;//���˽��
    private int isGift;//�Ƿ���Ʒ��0��1��
    private String recName;//�ջ�������
    private String recMobile;//�ջ����ֻ���
    private String province;//ʡ
    private String city;//��
    private String county;//��
    private String town;//��
    private String address;//��ַ
    private String memo;//��ע
    
    
	public String getExpressNo() {
		return expressNo;
	}
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getExpressCode() {
		return expressCode;
	}
	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	public String getExpressName() {
		return expressName;
	}
	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
	public String getEcOrderId() {
		return ecOrderId;
	}
	public void setEcOrderId(String ecOrderId) {
		this.ecOrderId = ecOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getTradeDt() {
		return tradeDt;
	}
	public void setTradeDt(String tradeDt) {
		this.tradeDt = tradeDt;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	public String getGoodSku() {
		return goodSku;
	}
	public void setGoodSku(String goodSku) {
		this.goodSku = goodSku;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public BigDecimal getGoodPrice() {
		return goodPrice;
	}
	public void setGoodPrice(BigDecimal goodPrice) {
		this.goodPrice = goodPrice;
	}
	public Integer getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(Integer goodNum) {
		this.goodNum = goodNum;
	}
	public BigDecimal getGoodMoney() {
		return goodMoney;
	}
	public void setGoodMoney(BigDecimal goodMoney) {
		this.goodMoney = goodMoney;
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
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public String getInvId() {
		return invId;
	}
	public void setInvId(String invId) {
		this.invId = invId;
	}
	public String getInvName() {
		return invName;
	}
	public void setInvName(String invName) {
		this.invName = invName;
	}
	public Integer getInvNum() {
		return invNum;
	}
	public void setInvNum(Integer invNum) {
		this.invNum = invNum;
	}
	public String getPtoCode() {
		return ptoCode;
	}
	public void setPtoCode(String ptoCode) {
		this.ptoCode = ptoCode;
	}
	public String getPtoName() {
		return ptoName;
	}
	public void setPtoName(String ptoName) {
		this.ptoName = ptoName;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getPrdcDt() {
		return prdcDt;
	}
	public void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
	}
	public String getInvldtnDt() {
		return invldtnDt;
	}
	public void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}
	public String getDeliverWhs() {
		return deliverWhs;
	}
	public void setDeliverWhs(String deliverWhs) {
		this.deliverWhs = deliverWhs;
	}
	public String getDeliverWhsName() {
		return deliverWhsName;
	}
	public void setDeliverWhsName(String deliverWhsName) {
		this.deliverWhsName = deliverWhsName;
	}
	public int getCanRefNum() {
		return canRefNum;
	}
	public void setCanRefNum(int canRefNum) {
		this.canRefNum = canRefNum;
	}
	public BigDecimal getCanRefMoney() {
		return canRefMoney;
	}
	public void setCanRefMoney(BigDecimal canRefMoney) {
		this.canRefMoney = canRefMoney;
	}
	public int getIsGift() {
		return isGift;
	}
	public void setIsGift(int isGift) {
		this.isGift = isGift;
	}
	public String getRecName() {
		return recName;
	}
	public void setRecName(String recName) {
		this.recName = recName;
	}
	public String getRecMobile() {
		return recMobile;
	}
	public void setRecMobile(String recMobile) {
		this.recMobile = recMobile;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

}

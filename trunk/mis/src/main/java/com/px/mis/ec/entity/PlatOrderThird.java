package com.px.mis.ec.entity;

import java.math.BigDecimal;
import java.util.List;


/**
 * ������ƽ̨������ϵӳ��
 * @author lxya0
 *
 */
public class PlatOrderThird {
	
    private String orderCode;//�������
    private String storeCode;//���̱��
    private String storeName;//��������
    private String paymentConfirmTime;//����ʱ��
    private Integer waif;//���
    private Integer isAudit;//�Ƿ����
    private String auditHint;//�����ʾ
    private Integer goodNum;//��Ʒ����
    private BigDecimal orderTotalPrice;//��Ʒ���
    private BigDecimal orderPayment;//ʵ�����
    private String orderRemark;//�������
    private String venderRemark;//���ұ�ע
    private String fullAddress;//�ջ�����ϸ��ַ
    private String pin;//��һ�Ա��
    private String fullname;//�ջ�������
    private String mobile;//�ջ����ֻ���
    private String ecOrderId;//���̶�����
    private Integer isInvoice;//�Ƿ�Ʊ
    private String invoiceTitle;//��Ʊ̧ͷ
    private Integer noteFlag;//���ұ�ע����
    private Integer isClose;//�Ƿ�ر�
    private Integer isShip;//�Ƿ񷢻�
    private BigDecimal adjustMoney;//���ҵ������
    private BigDecimal discountMoney;//ϵͳ�Żݽ��
    private Integer orderStatus;//����״̬
    private Integer returnStatus;//�˻�״̬
    private Integer hasGift;//�Ƿ���Ʒ
    private String memo;//��ע
    private Integer adjustStatus;//����״̬
    private String orderStartTime;//�µ�ʱ��
    private String bizTypId;//ҵ�����ͱ��
    private String sellTypId;//�������ͱ��
    private String recvSendCateId;//�շ������
    private BigDecimal orderSellerPrice;//������ ����ʵ��������
    private String province;
    private String provinceId;
    private String city;
    private String cityId;
    private String county;
    private String countyId;
    private String town;
    private String townId;
    private BigDecimal freightPrice;//�˷�
    private String deliverWhs;//�����ֿ�
    private String deliveryType;//�ͻ������ڣ����ͣ�1-ֻ�������ͻ�(˫���ա����ղ�����);2-ֻ˫���ա������ͻ�(�����ղ�����);3-�����ա�˫��������վ����ͻ�;����ֵ-���ء�����ʱ�䡱��
    private String venderCode;//�̼�ID�̼ұ���
    private int deliverSelf;//�Ƿ��Է��� 0:ƽ̨�ַ��� 1���Է���
    private int sellerDiscount; //�����Ż���ϸ
    private List<PlatOrdersThird> platOrdersList; //��Ʒ��ϸ����
    private List<CouponDetail> couponDetailList; //�Ż���ϸ����
    private List<PlatOrderPaymethod> payMethodList; //���ʽ����
    
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getPaymentConfirmTime() {
		return paymentConfirmTime;
	}
	public void setPaymentConfirmTime(String paymentConfirmTime) {
		this.paymentConfirmTime = paymentConfirmTime;
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
		this.auditHint = auditHint;
	}
	public Integer getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(Integer goodNum) {
		this.goodNum = goodNum;
	}
	public BigDecimal getOrderTotalPrice() {
		return orderTotalPrice;
	}
	public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}
	public BigDecimal getOrderPayment() {
		return orderPayment;
	}
	public void setOrderPayment(BigDecimal orderPayment) {
		this.orderPayment = orderPayment;
	}
	public String getOrderRemark() {
		return orderRemark;
	}
	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}
	public String getVenderRemark() {
		return venderRemark;
	}
	public void setVenderRemark(String venderRemark) {
		this.venderRemark = venderRemark;
	}
	public String getFullAddress() {
		return fullAddress;
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEcOrderId() {
		return ecOrderId;
	}
	public void setEcOrderId(String ecOrderId) {
		this.ecOrderId = ecOrderId;
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
		this.invoiceTitle = invoiceTitle;
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
	public BigDecimal getAdjustMoney() {
		return adjustMoney;
	}
	public void setAdjustMoney(BigDecimal adjustMoney) {
		this.adjustMoney = adjustMoney;
	}
	public BigDecimal getDiscountMoney() {
		return discountMoney;
	}
	public void setDiscountMoney(BigDecimal discountMoney) {
		this.discountMoney = discountMoney;
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
		this.memo = memo;
	}
	public Integer getAdjustStatus() {
		return adjustStatus;
	}
	public void setAdjustStatus(Integer adjustStatus) {
		this.adjustStatus = adjustStatus;
	}
	public String getOrderStartTime() {
		return orderStartTime;
	}
	public void setOrderStartTime(String orderStartTime) {
		this.orderStartTime = orderStartTime;
	}
	public String getBizTypId() {
		return bizTypId;
	}
	public void setBizTypId(String bizTypId) {
		this.bizTypId = bizTypId;
	}
	public String getSellTypId() {
		return sellTypId;
	}
	public void setSellTypId(String sellTypId) {
		this.sellTypId = sellTypId;
	}
	public String getRecvSendCateId() {
		return recvSendCateId;
	}
	public void setRecvSendCateId(String recvSendCateId) {
		this.recvSendCateId = recvSendCateId;
	}
	public BigDecimal getOrderSellerPrice() {
		return orderSellerPrice;
	}
	public void setOrderSellerPrice(BigDecimal orderSellerPrice) {
		this.orderSellerPrice = orderSellerPrice;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getTownId() {
		return townId;
	}
	public void setTownId(String townId) {
		this.townId = townId;
	}
	public BigDecimal getFreightPrice() {
		return freightPrice;
	}
	public void setFreightPrice(BigDecimal freightPrice) {
		this.freightPrice = freightPrice;
	}
	public String getDeliverWhs() {
		return deliverWhs;
	}
	public void setDeliverWhs(String deliverWhs) {
		this.deliverWhs = deliverWhs;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	public String getVenderCode() {
		return venderCode;
	}
	public void setVenderCode(String venderCode) {
		this.venderCode = venderCode;
	}
	public int getDeliverSelf() {
		return deliverSelf;
	}
	public void setDeliverSelf(int deliverSelf) {
		this.deliverSelf = deliverSelf;
	}
	
	public int getSellerDiscount() {
		return sellerDiscount;
	}
	public void setSellerDiscount(int sellerDiscount) {
		this.sellerDiscount = sellerDiscount;
	}
	
	public List<PlatOrdersThird> getPlatOrdersList() {
		return platOrdersList;
	}
	public void setPlatOrdersList(List<PlatOrdersThird> platOrdersList) {
		this.platOrdersList = platOrdersList;
	}
	public List<CouponDetail> getCouponDetailList() {
		return couponDetailList;
	}
	public void setCouponDetailList(List<CouponDetail> couponDetailList) {
		this.couponDetailList = couponDetailList;
	}
	
	public List<PlatOrderPaymethod> getPayMethodList() {
		return payMethodList;
	}
	public void setPayMethodList(List<PlatOrderPaymethod> payMethodList) {
		this.payMethodList = payMethodList;
	}
	
	
    
    

	
    
    
}
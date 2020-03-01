package com.px.mis.ec.entity;

import java.math.BigDecimal;
import java.util.List;


/**
 * 第三方平台订单关系映射
 * @author lxya0
 *
 */
public class PlatOrderThird {
	
    private String orderCode;//订单编号
    private String storeCode;//店铺编号
    private String storeName;//店铺名称
    private String paymentConfirmTime;//付款时间
    private Integer waif;//旗标
    private Integer isAudit;//是否客审
    private String auditHint;//审核提示
    private Integer goodNum;//商品数量
    private BigDecimal orderTotalPrice;//商品金额
    private BigDecimal orderPayment;//实付金额
    private String orderRemark;//买家留言
    private String venderRemark;//卖家备注
    private String fullAddress;//收货人详细地址
    private String pin;//买家会员号
    private String fullname;//收货人姓名
    private String mobile;//收货人手机号
    private String ecOrderId;//电商订单号
    private Integer isInvoice;//是否开票
    private String invoiceTitle;//发票抬头
    private Integer noteFlag;//卖家备注旗帜
    private Integer isClose;//是否关闭
    private Integer isShip;//是否发货
    private BigDecimal adjustMoney;//卖家调整金额
    private BigDecimal discountMoney;//系统优惠金额
    private Integer orderStatus;//订单状态
    private Integer returnStatus;//退货状态
    private Integer hasGift;//是否含赠品
    private String memo;//备注
    private Integer adjustStatus;//调整状态
    private String orderStartTime;//下单时间
    private String bizTypId;//业务类型编号
    private String sellTypId;//销售类型编号
    private String recvSendCateId;//收发类别编号
    private BigDecimal orderSellerPrice;//结算金额 订单实际收入金额
    private String province;
    private String provinceId;
    private String city;
    private String cityId;
    private String county;
    private String countyId;
    private String town;
    private String townId;
    private BigDecimal freightPrice;//运费
    private String deliverWhs;//发货仓库
    private String deliveryType;//送货（日期）类型（1-只工作日送货(双休日、假日不用送);2-只双休日、假日送货(工作日不用送);3-工作日、双休日与假日均可送货;其他值-返回“任意时间”）
    private String venderCode;//商家ID商家编码
    private int deliverSelf;//是否自发货 0:平台仓发货 1：自发货
    private int sellerDiscount; //订单优惠明细
    private List<PlatOrdersThird> platOrdersList; //商品明细集合
    private List<CouponDetail> couponDetailList; //优惠明细集合
    private List<PlatOrderPaymethod> payMethodList; //付款方式集合
    
	
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
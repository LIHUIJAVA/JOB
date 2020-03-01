package com.px.mis.ec.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.print.DocFlavor.STRING;

public class PlatOrder implements Cloneable{
	
    private String orderId;//订单编号
    private String storeId;//店铺编号
    private String storeName;//店铺名称
    private String payTime;//付款时间
    private Integer waif;//旗标
    private Integer isAudit;//是否客审
    private String auditHint;//审核提示
    private Integer goodNum;//商品数量
    private BigDecimal goodMoney;//商品金额
    private BigDecimal payMoney;//实付金额
    private String buyerNote;//买家留言
    private String sellerNote;//卖家备注
    private String recAddress;//收货人详细地址
    private String buyerId;//买家会员号
    private String recName;//收货人姓名
    private String recMobile;//收货人手机号
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
    private String tradeDt;//交易时间
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
    private String deliverWhsNm;//发货仓库名称
    private String deliveryType;//送货（日期）类型（1-只工作日送货(双休日、假日不用送);2-只双休日、假日送货(工作日不用送);3-工作日、双休日与假日均可送货;其他值-返回“任意时间”）
    private String venderId;//商家ID商家编码
    private int deliverSelf;//是否自发货 0:平台仓发货 1：自发货
    
    
    
    private String expressCode;//快递公司编号
    private String expressName;//快递公司名称
    private String expressNo;//快递单号
    private BigDecimal weight;//重量
    private String auditTime;//审核时间
    private String shipTime;//发货时间
    private String closeTime;//关闭时间
    private String downloadTime;//下载时间
    private List<PlatOrders> platOrdersList;
    
    private String expressTemplate;//快递单打印模板编号
    
    private Integer canMatchActive;//是否可以匹配活动 为1不可以
    
    public String getDeliverWhsNm() {
		return deliverWhsNm;
	}

	public void setDeliverWhsNm(String deliverWhsNm) {
		this.deliverWhsNm = deliverWhsNm;
	}

	public int getDeliverSelf() {
		return deliverSelf;
	}

	public Integer getCanMatchActive() {
		return canMatchActive;
	}

	public void setCanMatchActive(Integer canMatchActive) {
		this.canMatchActive = canMatchActive;
	}

	public String getExpressTemplate() {
		return expressTemplate;
	}

	public void setExpressTemplate(String expressTemplate) {
		this.expressTemplate = expressTemplate;
	}

	public String getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(String downloadTime) {
		this.downloadTime = downloadTime;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getShipTime() {
		return shipTime;
	}

	public void setShipTime(String shipTime) {
		this.shipTime = shipTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public void setDeliverSelf(int deliverSelf) {
		this.deliverSelf = deliverSelf;
	}

	public String getOrderId() {
        return orderId;
    }

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getVenderId() {
		return venderId;
	}

	public void setVenderId(String venderId) {
		this.venderId = venderId;
	}

	public BigDecimal getFreightPrice() {
		return freightPrice;
	}

	public String getDeliverWhs() {
		return deliverWhs;
	}

	public void setDeliverWhs(String deliverWhs) {
		this.deliverWhs = deliverWhs;
	}

	public void setFreightPrice(BigDecimal freightPrice) {
		this.freightPrice = freightPrice;
	}

	public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getStoreId() {
        return storeId;
    }

	public BigDecimal getOrderSellerPrice() {
		return orderSellerPrice;
	}

	public void setOrderSellerPrice(BigDecimal orderSellerPrice) {
		this.orderSellerPrice = orderSellerPrice;
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

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
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

	public void setAuditHint(String auditHint) {
        this.auditHint = auditHint == null ? null : auditHint.trim();
    }

    public Integer getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(Integer goodNum) {
        this.goodNum = goodNum;
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

	public List<PlatOrders> getPlatOrdersList() {
		return platOrdersList;
	}

	public void setPlatOrdersList(List<PlatOrders> platOrdersList) {
		this.platOrdersList = platOrdersList;
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

	public Integer getAdjustStatus() {
		return adjustStatus;
	}

	public void setAdjustStatus(Integer adjustStatus) {
		this.adjustStatus = adjustStatus;
	}

	public String getTradeDt() {
		return tradeDt;
	}

	public void setTradeDt(String tradeDt) {
		this.tradeDt = tradeDt;
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

	@Override
	public String toString() {
		return "PlatOrder [orderId=" + orderId + ", storeId=" + storeId + ", storeName=" + storeName + ", payTime="
				+ payTime + ", waif=" + waif + ", isAudit=" + isAudit + ", auditHint=" + auditHint + ", goodNum="
				+ goodNum + ", goodMoney=" + goodMoney + ", payMoney=" + payMoney + ", buyerNote=" + buyerNote
				+ ", sellerNote=" + sellerNote + ", recAddress=" + recAddress + ", buyerId=" + buyerId + ", recName="
				+ recName + ", recMobile=" + recMobile + ", ecOrderId=" + ecOrderId + ", isInvoice=" + isInvoice
				+ ", invoiceTitle=" + invoiceTitle + ", noteFlag=" + noteFlag + ", isClose=" + isClose + ", isShip="
				+ isShip + ", adjustMoney=" + adjustMoney + ", discountMoney=" + discountMoney + ", orderStatus="
				+ orderStatus + ", returnStatus=" + returnStatus + ", hasGift=" + hasGift + ", memo=" + memo
				+ ", adjustStatus=" + adjustStatus + ", tradeDt=" + tradeDt + ", bizTypId=" + bizTypId + ", sellTypId="
				+ sellTypId + ", recvSendCateId=" + recvSendCateId + ", platOrdersList=" + platOrdersList + "]";
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	
    
    
}
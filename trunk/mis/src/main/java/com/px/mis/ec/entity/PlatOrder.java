package com.px.mis.ec.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.print.DocFlavor.STRING;

public class PlatOrder implements Cloneable{
	
    private String orderId;//�������
    private String storeId;//���̱��
    private String storeName;//��������
    private String payTime;//����ʱ��
    private Integer waif;//���
    private Integer isAudit;//�Ƿ����
    private String auditHint;//�����ʾ
    private Integer goodNum;//��Ʒ����
    private BigDecimal goodMoney;//��Ʒ���
    private BigDecimal payMoney;//ʵ�����
    private String buyerNote;//�������
    private String sellerNote;//���ұ�ע
    private String recAddress;//�ջ�����ϸ��ַ
    private String buyerId;//��һ�Ա��
    private String recName;//�ջ�������
    private String recMobile;//�ջ����ֻ���
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
    private String tradeDt;//����ʱ��
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
    private String deliverWhsNm;//�����ֿ�����
    private String deliveryType;//�ͻ������ڣ����ͣ�1-ֻ�������ͻ�(˫���ա����ղ�����);2-ֻ˫���ա������ͻ�(�����ղ�����);3-�����ա�˫��������վ����ͻ�;����ֵ-���ء�����ʱ�䡱��
    private String venderId;//�̼�ID�̼ұ���
    private int deliverSelf;//�Ƿ��Է��� 0:ƽ̨�ַ��� 1���Է���
    
    
    
    private String expressCode;//��ݹ�˾���
    private String expressName;//��ݹ�˾����
    private String expressNo;//��ݵ���
    private BigDecimal weight;//����
    private String auditTime;//���ʱ��
    private String shipTime;//����ʱ��
    private String closeTime;//�ر�ʱ��
    private String downloadTime;//����ʱ��
    private List<PlatOrders> platOrdersList;
    
    private String expressTemplate;//��ݵ���ӡģ����
    
    private Integer canMatchActive;//�Ƿ����ƥ�� Ϊ1������
    
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
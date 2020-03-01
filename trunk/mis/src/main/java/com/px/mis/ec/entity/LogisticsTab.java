package com.px.mis.ec.entity;

import java.math.BigDecimal;
import java.util.List;

import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.whs.entity.ExpressCorp;

//物流表
public class LogisticsTab {
	private Integer ordrNum;//物流编号
	private String expressCode;//快递单号
	private String saleEncd;//销售单号
	private String orderId;//订单编号
	private String outWhsId;//销售出库单号
	private String ecOrderId;//电商订单号
	private String createDate;//创建时间
	private BigDecimal goodNum;//整单商品件数
	private BigDecimal goodMoney;//整单金额
	private BigDecimal payMoney;//整单实付金额
	private String buyerNote;//买家留言
	private String sellerNote;//卖家备注
	private String recAddress;//收货人详细地址
	private String recName;//收货人姓名
	private String recMobile;//收货人手机号
	private String memo;//备注
	private String bizTypId;//业务类型编号
	private String sellTypId;//销售类型编号
	private String deliverWhs;//发货仓库编码
	private String recvSendCateId;//收发类别编号
	private String gdFlowEncd;//物流公司编号
	private String expressEncd;//快递公司编号
	private Integer adjustStatus;//调整状态
    private Integer isPick;//是否拣货完成
    private Integer isShip;//是否发货
    private Integer isBackPlatform;//是否回传平台
    private String packageDate;//揽件时间
    private BigDecimal volume;//体积
    private BigDecimal weight;//重量
    private BigDecimal freight;//运费
    private int deliverSelf;//是否自发货
    private String bigShotCode;//大头笔编码
    private String bigShotName;//大头笔名称
    private String gatherCenterName;//集包地名称
    private String gatherCenterCode;//集包地代码
    private String branchName;//目的地网点名称
    private String branchCode;//目的地网点编码
    private String secondSectionCode;//二段码
    private String thirdSectionCode;//三段码
    private String shipDate;//发货时间
    
    private String templateId;//打印模板id
    private int isPrint;//是否打印 0未打印 1已打印
    private String printTime;//打印时间
    
    private String storeId;//店铺id
    private String storeName;//店铺名称
    private String platId;//平台id
    
    private List<SellSnglSub> sellSnglSubMap;
    private List<PlatOrders> platOrdersList;//订单项集合
    private List<ExpressCorp> expresslist;//快递公司list 
    
    private String expressName;//快递公司名称
    private String whsName;//仓库名称
    private String shiperName;//发货人
    private String shipAddress;//发货地址
    private String shiperPhone;//发货人电话
    private String shipProvince;
    private String shipCity;
    private String shipCounty;
    
    
    
	public String getShiperName() {
		return shiperName;
	}

	public void setShiperName(String shiperName) {
		this.shiperName = shiperName;
	}

	public String getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	public String getShiperPhone() {
		return shiperPhone;
	}

	public void setShiperPhone(String shiperPhone) {
		this.shiperPhone = shiperPhone;
	}

	public String getShipProvince() {
		return shipProvince;
	}

	public void setShipProvince(String shipProvince) {
		this.shipProvince = shipProvince;
	}

	public String getShipCity() {
		return shipCity;
	}

	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	public String getShipCounty() {
		return shipCounty;
	}

	public void setShipCounty(String shipCounty) {
		this.shipCounty = shipCounty;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public LogisticsTab() {
	}

	public String getWhsName() {
		return whsName;
	}

	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getPlatId() {
		return platId;
	}

	public void setPlatId(String platId) {
		this.platId = platId;
	}

	public int getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(int isPrint) {
		this.isPrint = isPrint;
	}

	public String getPrintTime() {
		return printTime;
	}

	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Integer getOrdrNum() {
		return ordrNum;
	}
	
	public List<ExpressCorp> getExpresslist() {
		return expresslist;
	}
	public void setExpresslist(List<ExpressCorp> expresslist) {
		this.expresslist = expresslist;
	}
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public List<SellSnglSub> getSellSnglSubMap() {
		return sellSnglSubMap;
	}
	public void setSellSnglSubMap(List<SellSnglSub> sellSnglSubMap) {
		this.sellSnglSubMap = sellSnglSubMap;
	}
	public String getBigShotCode() {
		return bigShotCode;
	}
	public void setBigShotCode(String bigShotCode) {
		this.bigShotCode = bigShotCode;
	}
	public String getBigShotName() {
		return bigShotName;
	}
	public void setBigShotName(String bigShotName) {
		this.bigShotName = bigShotName;
	}
	public String getGatherCenterName() {
		return gatherCenterName;
	}
	public void setGatherCenterName(String gatherCenterName) {
		this.gatherCenterName = gatherCenterName;
	}
	public String getGatherCenterCode() {
		return gatherCenterCode;
	}
	public void setGatherCenterCode(String gatherCenterCode) {
		this.gatherCenterCode = gatherCenterCode;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getSecondSectionCode() {
		return secondSectionCode;
	}
	public void setSecondSectionCode(String secondSectionCode) {
		this.secondSectionCode = secondSectionCode;
	}
	public String getThirdSectionCode() {
		return thirdSectionCode;
	}
	public void setThirdSectionCode(String thirdSectionCode) {
		this.thirdSectionCode = thirdSectionCode;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	
	public int getDeliverSelf() {
		return deliverSelf;
	}
	public void setDeliverSelf(int deliverSelf) {
		this.deliverSelf = deliverSelf;
	}
	public String getExpressCode() {
		return expressCode;
	}
	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	public String getSaleEncd() {
		return saleEncd;
	}
	public void setSaleEncd(String saleEncd) {
		this.saleEncd = saleEncd;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOutWhsId() {
		return outWhsId;
	}
	public void setOutWhsId(String outWhsId) {
		this.outWhsId = outWhsId;
	}
	public String getEcOrderId() {
		return ecOrderId;
	}
	public void setEcOrderId(String ecOrderId) {
		this.ecOrderId = ecOrderId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public BigDecimal getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(BigDecimal goodNum) {
		this.goodNum = goodNum;
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
	public String getBuyerNote() {
		return buyerNote;
	}
	public void setBuyerNote(String buyerNote) {
		this.buyerNote = buyerNote;
	}
	public String getSellerNote() {
		return sellerNote;
	}
	public void setSellerNote(String sellerNote) {
		this.sellerNote = sellerNote;
	}
	public String getRecAddress() {
		return recAddress;
	}
	public void setRecAddress(String recAddress) {
		this.recAddress = recAddress;
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
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public String getDeliverWhs() {
		return deliverWhs;
	}
	public void setDeliverWhs(String deliverWhs) {
		this.deliverWhs = deliverWhs;
	}
	public String getRecvSendCateId() {
		return recvSendCateId;
	}
	public void setRecvSendCateId(String recvSendCateId) {
		this.recvSendCateId = recvSendCateId;
	}
	public String getGdFlowEncd() {
		return gdFlowEncd;
	}
	public void setGdFlowEncd(String gdFlowEncd) {
		this.gdFlowEncd = gdFlowEncd;
	}
	public String getExpressEncd() {
		return expressEncd;
	}
	public void setExpressEncd(String expressEncd) {
		this.expressEncd = expressEncd;
	}
	public Integer getAdjustStatus() {
		return adjustStatus;
	}
	public void setAdjustStatus(Integer adjustStatus) {
		this.adjustStatus = adjustStatus;
	}
	public Integer getIsPick() {
		return isPick;
	}
	public void setIsPick(Integer isPick) {
		this.isPick = isPick;
	}
	public Integer getIsShip() {
		return isShip;
	}
	public void setIsShip(Integer isShip) {
		this.isShip = isShip;
	}
	public Integer getIsBackPlatform() {
		return isBackPlatform;
	}
	public void setIsBackPlatform(Integer isBackPlatform) {
		this.isBackPlatform = isBackPlatform;
	}
	public String getPackageDate() {
		return packageDate;
	}
	public void setPackageDate(String packageDate) {
		this.packageDate = packageDate;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public BigDecimal getFreight() {
		return freight;
	}
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}
	public List<PlatOrders> getPlatOrdersList() {
		return platOrdersList;
	}
	public void setPlatOrdersList(List<PlatOrders> platOrdersList) {
		this.platOrdersList = platOrdersList;
	}
	@Override
	public String toString() {
		return "LogisticsTab [ordrNum=" + ordrNum + ", expressCode=" + expressCode + ", saleEncd=" + saleEncd
				+ ", orderId=" + orderId + ", outWhsId=" + outWhsId + ", ecOrderId=" + ecOrderId + ", createDate="
				+ createDate + ", goodNum=" + goodNum + ", goodMoney=" + goodMoney + ", payMoney=" + payMoney
				+ ", buyerNote=" + buyerNote + ", sellerNote=" + sellerNote + ", recAddress=" + recAddress
				+ ", recName=" + recName + ", recMobile=" + recMobile + ", memo=" + memo + ", bizTypId=" + bizTypId
				+ ", sellTypId=" + sellTypId + ", deliverWhs=" + deliverWhs + ", recvSendCateId=" + recvSendCateId
				+ ", gdFlowEncd=" + gdFlowEncd + ", expressEncd=" + expressEncd + ", adjustStatus=" + adjustStatus
				+ ", isPick=" + isPick + ", isShip=" + isShip + ", isBackPlatform=" + isBackPlatform + ", packageDate="
				+ packageDate + ", volume=" + volume + ", weight=" + weight + ", freight=" + freight
				+ ", platOrdersList=" + platOrdersList + "]";
	}
	
	
	
	
}

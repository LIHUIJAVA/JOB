package com.px.mis.ec.entity;

import java.math.BigDecimal;
import java.util.List;

import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.whs.entity.ExpressCorp;

//������
public class LogisticsTab {
	private Integer ordrNum;//�������
	private String expressCode;//��ݵ���
	private String saleEncd;//���۵���
	private String orderId;//�������
	private String outWhsId;//���۳��ⵥ��
	private String ecOrderId;//���̶�����
	private String createDate;//����ʱ��
	private BigDecimal goodNum;//������Ʒ����
	private BigDecimal goodMoney;//�������
	private BigDecimal payMoney;//����ʵ�����
	private String buyerNote;//�������
	private String sellerNote;//���ұ�ע
	private String recAddress;//�ջ�����ϸ��ַ
	private String recName;//�ջ�������
	private String recMobile;//�ջ����ֻ���
	private String memo;//��ע
	private String bizTypId;//ҵ�����ͱ��
	private String sellTypId;//�������ͱ��
	private String deliverWhs;//�����ֿ����
	private String recvSendCateId;//�շ������
	private String gdFlowEncd;//������˾���
	private String expressEncd;//��ݹ�˾���
	private Integer adjustStatus;//����״̬
    private Integer isPick;//�Ƿ������
    private Integer isShip;//�Ƿ񷢻�
    private Integer isBackPlatform;//�Ƿ�ش�ƽ̨
    private String packageDate;//����ʱ��
    private BigDecimal volume;//���
    private BigDecimal weight;//����
    private BigDecimal freight;//�˷�
    private int deliverSelf;//�Ƿ��Է���
    private String bigShotCode;//��ͷ�ʱ���
    private String bigShotName;//��ͷ������
    private String gatherCenterName;//����������
    private String gatherCenterCode;//�����ش���
    private String branchName;//Ŀ�ĵ���������
    private String branchCode;//Ŀ�ĵ��������
    private String secondSectionCode;//������
    private String thirdSectionCode;//������
    private String shipDate;//����ʱ��
    
    private String templateId;//��ӡģ��id
    private int isPrint;//�Ƿ��ӡ 0δ��ӡ 1�Ѵ�ӡ
    private String printTime;//��ӡʱ��
    
    private String storeId;//����id
    private String storeName;//��������
    private String platId;//ƽ̨id
    
    private List<SellSnglSub> sellSnglSubMap;
    private List<PlatOrders> platOrdersList;//�������
    private List<ExpressCorp> expresslist;//��ݹ�˾list 
    
    private String expressName;//��ݹ�˾����
    private String whsName;//�ֿ�����
    private String shiperName;//������
    private String shipAddress;//������ַ
    private String shiperPhone;//�����˵绰
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

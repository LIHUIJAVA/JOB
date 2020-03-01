package com.px.mis.ec.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal ;

/**
 * �����б���
 * @author Mr.Li
 *
 */
public class exportOrders {

	@JsonProperty("ƽ̨������")
	private String  poEcOrderId ; // ƽ̨������,
	@JsonProperty("������")
	private String  poOrderId ; // ������,
	@JsonProperty("���̱��")
	private String  poStoreId ; // ���̱��,
	@JsonProperty("��������")
	private String  poStoreName ; // ��������,
	@JsonProperty("�µ�ʱ��")
	private String  poTradeDt ; // �µ�ʱ��,
	@JsonProperty("����ʱ��")
	private String  poPayTime ; // ����ʱ��,
	@JsonProperty("����ʱ��")
	private String  poShipTime ; // ����ʱ��,
	@JsonProperty("��ݹ�˾����")
	private String  poExpressCode ; // ��ݹ�˾����,
	@JsonProperty("��ݵ���")
	private String  poExpressNo ; // ��ݵ���,
	@JsonProperty("�ջ�������")
	private String  poRecName ; // �ջ�������,
	@JsonProperty("�ֻ���")
	private String  poRecMobile ; // �ֻ���,
	@JsonProperty("ʡ")
	private String  poProvince ; // ʡ,
	@JsonProperty("��")
	private String  poCity ; // ��,
	@JsonProperty("��")
	private String  poCounty ; // ��,
	@JsonProperty("��")
	private String  poTown ; // ��,
	@JsonProperty("�ջ���ַ")
	private String  poRecAddress ; // �ջ���ַ,
	@JsonProperty("������ע")
	private String  poMemo ; // ������ע,
	@JsonProperty("��Ʒ���")
	private BigDecimal  poGoodMoney ; // ��Ʒ���,
	@JsonProperty("ʵ�����")
	private BigDecimal  poPayMoney ; // ʵ�����,
	@JsonProperty("�������")
	private BigDecimal  poAdjustMoney ; // �������,
	@JsonProperty("�Żݽ��")
	private BigDecimal  poDiscountMoney ; // �Żݽ��,
	@JsonProperty("�����ֿ����")
	private String  poDeliverWhs ; // �����ֿ����,
	@JsonProperty("����")
	private BigDecimal  poWeight ; // ����,
	@JsonProperty("�Ƿ��Է���")
	private Integer poDeliverSelf ; // �Ƿ��Է���,
	@JsonProperty("�˷�")
	private BigDecimal  poFreightPrice ; // �˷�,
	@JsonProperty("������")
	private BigDecimal  poOrderSellerPrice ; // ������,
	@JsonProperty("������Ʒ")
	private Integer poHasGift ; // ������Ʒ,
	@JsonProperty("��һ�Ա��")
	private String  poBuyerId ; // ��һ�Ա��,
	@JsonProperty("��ұ�ע")
	private String  poBuyerNote ; // ��ұ�ע,
	@JsonProperty("���ұ�ע")
	private String  poSellerNote ; // ���ұ�ע,
	@JsonProperty("������Ʒ����")
	private Integer poGoodNum ; // ������Ʒ����,
	@JsonProperty("�Ƿ񷢻�")
	private Integer isShip ; // �Ƿ񷢻�,

	@JsonProperty("spu")
	private String  posGoodId ; // spu,
	@JsonProperty("��Ʒsku")
	private String  posGoodSku ; // ��Ʒsku,
	@JsonProperty("��Ʒ����")
	private String  posGoodName ; // ��Ʒ����,
	@JsonProperty("��ϸ����")
	private BigDecimal  posGoodPrice ; // ��ϸ����,
	@JsonProperty("sku����")
	private Integer posGoodNum ; // sku����,
	@JsonProperty("��ϸ��Ʒ���")
	private BigDecimal  posGoodMoney ; // ��ϸ��Ʒ���,
	@JsonProperty("��ϸ�Żݽ��")
	private BigDecimal  posDiscountMoney ; // ��ϸ�Żݽ��,
	@JsonProperty("��ϸ�������")
	private BigDecimal  posAdjustMoney ; // ��ϸ�������,
	@JsonProperty("��ϸʵ�����")
	private BigDecimal  posPayMoney ; // ��ϸʵ�����,
	@JsonProperty("�������")
	private String  posInvId ; // �������,
	@JsonProperty("�������")
	private Integer posInvNum ; // �������,
	@JsonProperty("ĸ������")
	private String  posPtoCode ; // ĸ������,
	@JsonProperty("ĸ������")
	private String  posPtoName ; // ĸ������,
	@JsonProperty("����")
	private String  posBatchNo ; // ����,
	@JsonProperty("��������")
	private String  posPrdcDt ; // ��������,
	@JsonProperty("ʧЧ����")
	private String  posInvldtnDt ; // ʧЧ����,
//	@JsonProperty("�����ֿ����")
//	private String  posDeliverWhs ; // �����ֿ����,
	@JsonProperty("��������")
	private Integer posCanRefNum ; // ��������,
	@JsonProperty("���˽��")
	private BigDecimal  posCanRefMoney ; // ���˽��,
	@JsonProperty("�Ƿ���Ʒ")
	private Integer posIsGift ; // �Ƿ���Ʒ,
	@JsonProperty("��ϸ��ע")
	private String  posMemo ; // ��ϸ��ע,
	@JsonProperty("��ϸʵ������")
	private BigDecimal  posPayPrice ; // ��ϸʵ������,
	@JsonProperty("���㵥��")
	private BigDecimal  posSellerPrice ; // ���㵥��

	@JsonProperty("�������")
	private String  iInvtyNm ; // �������,
	@JsonProperty("��ݹ�˾����")
	private String  cExpressNm ; // ��ݹ�˾����,
	@JsonProperty("�����ֿ�����")
	private String  wWhsNm ; // �����ֿ�����,

	public String getPoEcOrderId() {
		return poEcOrderId;
	}

	public void setPoEcOrderId(String poEcOrderId) {
		this.poEcOrderId = poEcOrderId;
	}

	public String getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}

	public String getPoStoreId() {
		return poStoreId;
	}

	public void setPoStoreId(String poStoreId) {
		this.poStoreId = poStoreId;
	}

	public String getPoShipTime() {
		return poShipTime;
	}

	public void setPoShipTime(String poShipTime) {
		this.poShipTime = poShipTime;
	}

	public Integer getIsShip() {
		return isShip;
	}

	public void setIsShip(Integer isShip) {
		this.isShip = isShip;
	}

	public String getPoStoreName() {
		return poStoreName;
	}

	public void setPoStoreName(String poStoreName) {
		this.poStoreName = poStoreName;
	}

	public String getPoTradeDt() {
		return poTradeDt;
	}

	public void setPoTradeDt(String poTradeDt) {
		this.poTradeDt = poTradeDt;
	}

	public String getPoPayTime() {
		return poPayTime;
	}

	public void setPoPayTime(String poPayTime) {
		this.poPayTime = poPayTime;
	}

	public String getPoExpressCode() {
		return poExpressCode;
	}

	public void setPoExpressCode(String poExpressCode) {
		this.poExpressCode = poExpressCode;
	}

	public String getPoExpressNo() {
		return poExpressNo;
	}

	public void setPoExpressNo(String poExpressNo) {
		this.poExpressNo = poExpressNo;
	}

	public String getPoRecName() {
		return poRecName;
	}

	public void setPoRecName(String poRecName) {
		this.poRecName = poRecName;
	}

	public String getPoRecMobile() {
		return poRecMobile;
	}

	public void setPoRecMobile(String poRecMobile) {
		this.poRecMobile = poRecMobile;
	}

	public String getPoProvince() {
		return poProvince;
	}

	public void setPoProvince(String poProvince) {
		this.poProvince = poProvince;
	}

	public String getPoCity() {
		return poCity;
	}

	public void setPoCity(String poCity) {
		this.poCity = poCity;
	}

	public String getPoCounty() {
		return poCounty;
	}

	public void setPoCounty(String poCounty) {
		this.poCounty = poCounty;
	}

	public String getPoTown() {
		return poTown;
	}

	public void setPoTown(String poTown) {
		this.poTown = poTown;
	}

	public String getPoRecAddress() {
		return poRecAddress;
	}

	public void setPoRecAddress(String poRecAddress) {
		this.poRecAddress = poRecAddress;
	}

	public String getPoMemo() {
		return poMemo;
	}

	public void setPoMemo(String poMemo) {
		this.poMemo = poMemo;
	}

	public BigDecimal getPoGoodMoney() {
		return poGoodMoney;
	}

	public void setPoGoodMoney(BigDecimal poGoodMoney) {
		this.poGoodMoney = poGoodMoney;
	}

	public BigDecimal getPoPayMoney() {
		return poPayMoney;
	}

	public void setPoPayMoney(BigDecimal poPayMoney) {
		this.poPayMoney = poPayMoney;
	}

	public BigDecimal getPoAdjustMoney() {
		return poAdjustMoney;
	}

	public void setPoAdjustMoney(BigDecimal poAdjustMoney) {
		this.poAdjustMoney = poAdjustMoney;
	}

	public BigDecimal getPoDiscountMoney() {
		return poDiscountMoney;
	}

	public void setPoDiscountMoney(BigDecimal poDiscountMoney) {
		this.poDiscountMoney = poDiscountMoney;
	}

	public String getPoDeliverWhs() {
		return poDeliverWhs;
	}

	public void setPoDeliverWhs(String poDeliverWhs) {
		this.poDeliverWhs = poDeliverWhs;
	}

	public BigDecimal getPoWeight() {
		return poWeight;
	}

	public void setPoWeight(BigDecimal poWeight) {
		this.poWeight = poWeight;
	}

	public Integer getPoDeliverSelf() {
		return poDeliverSelf;
	}

	public void setPoDeliverSelf(Integer poDeliverSelf) {
		this.poDeliverSelf = poDeliverSelf;
	}

	public BigDecimal getPoFreightPrice() {
		return poFreightPrice;
	}

	public void setPoFreightPrice(BigDecimal poFreightPrice) {
		this.poFreightPrice = poFreightPrice;
	}

	public BigDecimal getPoOrderSellerPrice() {
		return poOrderSellerPrice;
	}

	public void setPoOrderSellerPrice(BigDecimal poOrderSellerPrice) {
		this.poOrderSellerPrice = poOrderSellerPrice;
	}

	public Integer getPoHasGift() {
		return poHasGift;
	}

	public void setPoHasGift(Integer poHasGift) {
		this.poHasGift = poHasGift;
	}

	public String getPoBuyerId() {
		return poBuyerId;
	}

	public void setPoBuyerId(String poBuyerId) {
		this.poBuyerId = poBuyerId;
	}

	public String getPoBuyerNote() {
		return poBuyerNote;
	}

	public void setPoBuyerNote(String poBuyerNote) {
		this.poBuyerNote = poBuyerNote;
	}

	public String getPoSellerNote() {
		return poSellerNote;
	}

	public void setPoSellerNote(String poSellerNote) {
		this.poSellerNote = poSellerNote;
	}

	public Integer getPoGoodNum() {
		return poGoodNum;
	}

	public void setPoGoodNum(Integer poGoodNum) {
		this.poGoodNum = poGoodNum;
	}

	public String getPosGoodId() {
		return posGoodId;
	}

	public void setPosGoodId(String posGoodId) {
		this.posGoodId = posGoodId;
	}

	public String getPosGoodSku() {
		return posGoodSku;
	}

	public void setPosGoodSku(String posGoodSku) {
		this.posGoodSku = posGoodSku;
	}

	public String getPosGoodName() {
		return posGoodName;
	}

	public void setPosGoodName(String posGoodName) {
		this.posGoodName = posGoodName;
	}

	public BigDecimal getPosGoodPrice() {
		return posGoodPrice;
	}

	public void setPosGoodPrice(BigDecimal posGoodPrice) {
		this.posGoodPrice = posGoodPrice;
	}

	public Integer getPosGoodNum() {
		return posGoodNum;
	}

	public void setPosGoodNum(Integer posGoodNum) {
		this.posGoodNum = posGoodNum;
	}

	public BigDecimal getPosGoodMoney() {
		return posGoodMoney;
	}

	public void setPosGoodMoney(BigDecimal posGoodMoney) {
		this.posGoodMoney = posGoodMoney;
	}

	public BigDecimal getPosDiscountMoney() {
		return posDiscountMoney;
	}

	public void setPosDiscountMoney(BigDecimal posDiscountMoney) {
		this.posDiscountMoney = posDiscountMoney;
	}

	public BigDecimal getPosAdjustMoney() {
		return posAdjustMoney;
	}

	public void setPosAdjustMoney(BigDecimal posAdjustMoney) {
		this.posAdjustMoney = posAdjustMoney;
	}

	public BigDecimal getPosPayMoney() {
		return posPayMoney;
	}

	public void setPosPayMoney(BigDecimal posPayMoney) {
		this.posPayMoney = posPayMoney;
	}

	public String getPosInvId() {
		return posInvId;
	}

	public void setPosInvId(String posInvId) {
		this.posInvId = posInvId;
	}

	public Integer getPosInvNum() {
		return posInvNum;
	}

	public void setPosInvNum(Integer posInvNum) {
		this.posInvNum = posInvNum;
	}

	public String getPosPtoCode() {
		return posPtoCode;
	}

	public void setPosPtoCode(String posPtoCode) {
		this.posPtoCode = posPtoCode;
	}

	public String getPosPtoName() {
		return posPtoName;
	}

	public void setPosPtoName(String posPtoName) {
		this.posPtoName = posPtoName;
	}

	public String getPosBatchNo() {
		return posBatchNo;
	}

	public void setPosBatchNo(String posBatchNo) {
		this.posBatchNo = posBatchNo;
	}

	public String getPosPrdcDt() {
		return posPrdcDt;
	}

	public void setPosPrdcDt(String posPrdcDt) {
		this.posPrdcDt = posPrdcDt;
	}

	public String getPosInvldtnDt() {
		return posInvldtnDt;
	}

	public void setPosInvldtnDt(String posInvldtnDt) {
		this.posInvldtnDt = posInvldtnDt;
	}

	public Integer getPosCanRefNum() {
		return posCanRefNum;
	}

	public void setPosCanRefNum(Integer posCanRefNum) {
		this.posCanRefNum = posCanRefNum;
	}

	public BigDecimal getPosCanRefMoney() {
		return posCanRefMoney;
	}

	public void setPosCanRefMoney(BigDecimal posCanRefMoney) {
		this.posCanRefMoney = posCanRefMoney;
	}

	public Integer getPosIsGift() {
		return posIsGift;
	}

	public void setPosIsGift(Integer posIsGift) {
		this.posIsGift = posIsGift;
	}

	public String getPosMemo() {
		return posMemo;
	}

	public void setPosMemo(String posMemo) {
		this.posMemo = posMemo;
	}

	public BigDecimal getPosPayPrice() {
		return posPayPrice;
	}

	public void setPosPayPrice(BigDecimal posPayPrice) {
		this.posPayPrice = posPayPrice;
	}

	public BigDecimal getPosSellerPrice() {
		return posSellerPrice;
	}

	public void setPosSellerPrice(BigDecimal posSellerPrice) {
		this.posSellerPrice = posSellerPrice;
	}

	public String getiInvtyNm() {
		return iInvtyNm;
	}

	public void setiInvtyNm(String iInvtyNm) {
		this.iInvtyNm = iInvtyNm;
	}

	public String getcExpressNm() {
		return cExpressNm;
	}

	public void setcExpressNm(String cExpressNm) {
		this.cExpressNm = cExpressNm;
	}

	public String getwWhsNm() {
		return wWhsNm;
	}

	public void setwWhsNm(String wWhsNm) {
		this.wWhsNm = wWhsNm;
	}
}

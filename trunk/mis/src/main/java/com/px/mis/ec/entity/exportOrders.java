package com.px.mis.ec.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal ;

/**
 * 订单列表导出
 * @author Mr.Li
 *
 */
public class exportOrders {

	@JsonProperty("平台订单号")
	private String  poEcOrderId ; // 平台订单号,
	@JsonProperty("订单号")
	private String  poOrderId ; // 订单号,
	@JsonProperty("店铺编号")
	private String  poStoreId ; // 店铺编号,
	@JsonProperty("店铺名称")
	private String  poStoreName ; // 店铺名称,
	@JsonProperty("下单时间")
	private String  poTradeDt ; // 下单时间,
	@JsonProperty("付款时间")
	private String  poPayTime ; // 付款时间,
	@JsonProperty("发货时间")
	private String  poShipTime ; // 付款时间,
	@JsonProperty("快递公司编码")
	private String  poExpressCode ; // 快递公司编码,
	@JsonProperty("快递单号")
	private String  poExpressNo ; // 快递单号,
	@JsonProperty("收货人姓名")
	private String  poRecName ; // 收货人姓名,
	@JsonProperty("手机号")
	private String  poRecMobile ; // 手机号,
	@JsonProperty("省")
	private String  poProvince ; // 省,
	@JsonProperty("市")
	private String  poCity ; // 市,
	@JsonProperty("区")
	private String  poCounty ; // 区,
	@JsonProperty("镇")
	private String  poTown ; // 镇,
	@JsonProperty("收货地址")
	private String  poRecAddress ; // 收货地址,
	@JsonProperty("订单备注")
	private String  poMemo ; // 订单备注,
	@JsonProperty("商品金额")
	private BigDecimal  poGoodMoney ; // 商品金额,
	@JsonProperty("实付金额")
	private BigDecimal  poPayMoney ; // 实付金额,
	@JsonProperty("调整金额")
	private BigDecimal  poAdjustMoney ; // 调整金额,
	@JsonProperty("优惠金额")
	private BigDecimal  poDiscountMoney ; // 优惠金额,
	@JsonProperty("发货仓库编码")
	private String  poDeliverWhs ; // 发货仓库编码,
	@JsonProperty("重量")
	private BigDecimal  poWeight ; // 重量,
	@JsonProperty("是否自发货")
	private Integer poDeliverSelf ; // 是否自发货,
	@JsonProperty("运费")
	private BigDecimal  poFreightPrice ; // 运费,
	@JsonProperty("结算金额")
	private BigDecimal  poOrderSellerPrice ; // 结算金额,
	@JsonProperty("包含赠品")
	private Integer poHasGift ; // 包含赠品,
	@JsonProperty("买家会员号")
	private String  poBuyerId ; // 买家会员号,
	@JsonProperty("买家备注")
	private String  poBuyerNote ; // 买家备注,
	@JsonProperty("卖家备注")
	private String  poSellerNote ; // 卖家备注,
	@JsonProperty("整单商品数量")
	private Integer poGoodNum ; // 整单商品数量,
	@JsonProperty("是否发货")
	private Integer isShip ; // 是否发货,

	@JsonProperty("spu")
	private String  posGoodId ; // spu,
	@JsonProperty("商品sku")
	private String  posGoodSku ; // 商品sku,
	@JsonProperty("商品名称")
	private String  posGoodName ; // 商品名称,
	@JsonProperty("明细单价")
	private BigDecimal  posGoodPrice ; // 明细单价,
	@JsonProperty("sku数量")
	private Integer posGoodNum ; // sku数量,
	@JsonProperty("明细商品金额")
	private BigDecimal  posGoodMoney ; // 明细商品金额,
	@JsonProperty("明细优惠金额")
	private BigDecimal  posDiscountMoney ; // 明细优惠金额,
	@JsonProperty("明细调整金额")
	private BigDecimal  posAdjustMoney ; // 明细调整金额,
	@JsonProperty("明细实付金额")
	private BigDecimal  posPayMoney ; // 明细实付金额,
	@JsonProperty("存货编码")
	private String  posInvId ; // 存货编码,
	@JsonProperty("存货数量")
	private Integer posInvNum ; // 存货数量,
	@JsonProperty("母件编码")
	private String  posPtoCode ; // 母件编码,
	@JsonProperty("母件名称")
	private String  posPtoName ; // 母件名称,
	@JsonProperty("批号")
	private String  posBatchNo ; // 批号,
	@JsonProperty("生产日期")
	private String  posPrdcDt ; // 生产日期,
	@JsonProperty("失效日期")
	private String  posInvldtnDt ; // 失效日期,
//	@JsonProperty("发货仓库编码")
//	private String  posDeliverWhs ; // 发货仓库编码,
	@JsonProperty("可退数量")
	private Integer posCanRefNum ; // 可退数量,
	@JsonProperty("可退金额")
	private BigDecimal  posCanRefMoney ; // 可退金额,
	@JsonProperty("是否赠品")
	private Integer posIsGift ; // 是否赠品,
	@JsonProperty("明细备注")
	private String  posMemo ; // 明细备注,
	@JsonProperty("明细实付单价")
	private BigDecimal  posPayPrice ; // 明细实付单价,
	@JsonProperty("结算单价")
	private BigDecimal  posSellerPrice ; // 结算单价

	@JsonProperty("存货名称")
	private String  iInvtyNm ; // 存货名称,
	@JsonProperty("快递公司名称")
	private String  cExpressNm ; // 快递公司名称,
	@JsonProperty("发货仓库名称")
	private String  wWhsNm ; // 发货仓库名称,

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

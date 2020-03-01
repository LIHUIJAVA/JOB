package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

//现存量报表

/**
 * @author Administrator
 */
public class NowStokReport {
	@JsonProperty("待发货数量")
	private BigDecimal sellWhsQty;// 待发货数量
	@JsonProperty("待到货数量")
	private BigDecimal intoWhsQty;// 待到货数量
	@JsonProperty("调拨在途数量")
	private BigDecimal inTransitQty;// 调拨在途数量
	@JsonProperty("预计入库数量")
	private BigDecimal othIntoQty;// 预计入库数量
	@JsonProperty("预计出库数量")
	private BigDecimal othOutQty;// 预计出库数量
	@JsonProperty("调拨待入数量")
	private BigDecimal cannibIntoQty;// 调拨待入数量
	@JsonProperty("调拨待出数量")
	private BigDecimal cannibOutQty;// 调拨待出数量
	@JsonProperty("现存量")
	private BigDecimal nowStok;// 现存量
	@JsonProperty("失效日期")
	private String invldtnDt;// 失效日期
	@JsonProperty("生产日期")
	private String prdcDt;// 生产日期
	@JsonProperty("批号")
	private String batNum;// 批号
	@JsonProperty("可用量")
	private BigDecimal avalQty;// 可用量
	@JsonProperty("未税单价")
	private String unTaxUprc;// 未税单价
	@JsonProperty("含税单价")
	private String cntnTaxUprc;// 含税单价
	@JsonProperty("未税金额")
	private String unTaxAmt;// 未税金额
	@JsonProperty("含税金额")
	private String cntnTaxAmt;// 含税金额
	@JsonProperty("存货编码")
	private String invtyEncd;// 存货编码
	@JsonProperty("仓库编码")
	private String whsEncd;// 仓库编码
	@JsonProperty("保质期")
	private String baoZhiQi;// 保质期
	@JsonProperty("主计量单位名称")
	private String measrCorpNm;// 主计量单位名称
	@JsonProperty("最低售价")
	private String loSellPrc;// 最低售价
	@JsonProperty("主计量单位编码")
	private String measrCorpId;// 主计量单位编码
	@JsonProperty("重量")
	private String weight;// 重量
	@JsonProperty("箱规")
	private String bxRule;// 箱规
	@JsonProperty("体积")
	private String vol;// 体积
	@JsonProperty("规格型号")
	private String spcModel;// 规格型号
	@JsonProperty("存货名称")
	private String invtyNm;// 存货名称
	@JsonProperty("参考售价")
	private String refSellPrc;// 参考售价
	@JsonProperty("参考成本")
	private String refCost;// 参考成本
	@JsonProperty("仓库名称")
	private String whsNm;// 仓库名称
	@JsonProperty("存货代码")
	private String invtyCd;// 存货代码

	public BigDecimal getSellWhsQty() {
		return sellWhsQty;
	}

	public void setSellWhsQty(BigDecimal sellWhsQty) {
		this.sellWhsQty = sellWhsQty;
	}

	public BigDecimal getIntoWhsQty() {
		return intoWhsQty;
	}

	public void setIntoWhsQty(BigDecimal intoWhsQty) {
		this.intoWhsQty = intoWhsQty;
	}

	public BigDecimal getInTransitQty() {
		return inTransitQty;
	}

	public void setInTransitQty(BigDecimal inTransitQty) {
		this.inTransitQty = inTransitQty;
	}

	public BigDecimal getOthIntoQty() {
		return othIntoQty;
	}

	public void setOthIntoQty(BigDecimal othIntoQty) {
		this.othIntoQty = othIntoQty;
	}

	public BigDecimal getOthOutQty() {
		return othOutQty;
	}

	public void setOthOutQty(BigDecimal othOutQty) {
		this.othOutQty = othOutQty;
	}

	public BigDecimal getCannibIntoQty() {
		return cannibIntoQty;
	}

	public void setCannibIntoQty(BigDecimal cannibIntoQty) {
		this.cannibIntoQty = cannibIntoQty;
	}

	public BigDecimal getCannibOutQty() {
		return cannibOutQty;
	}

	public void setCannibOutQty(BigDecimal cannibOutQty) {
		this.cannibOutQty = cannibOutQty;
	}

	public BigDecimal getNowStok() {
		return nowStok;
	}

	public void setNowStok(BigDecimal nowStok) {
		this.nowStok = nowStok;
	}

	public String getInvldtnDt() {
		return invldtnDt;
	}

	public void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}

	public String getPrdcDt() {
		return prdcDt;
	}

	public void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
	}

	public String getBatNum() {
		return batNum;
	}

	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}

	public BigDecimal getAvalQty() {
		return avalQty;
	}

	public void setAvalQty(BigDecimal avalQty) {
		this.avalQty = avalQty;
	}

	public String getUnTaxUprc() {
		return unTaxUprc;
	}

	public void setUnTaxUprc(String unTaxUprc) {
		this.unTaxUprc = unTaxUprc;
	}

	public String getCntnTaxUprc() {
		return cntnTaxUprc;
	}

	public void setCntnTaxUprc(String cntnTaxUprc) {
		this.cntnTaxUprc = cntnTaxUprc;
	}

	public String getUnTaxAmt() {
		return unTaxAmt;
	}

	public void setUnTaxAmt(String unTaxAmt) {
		this.unTaxAmt = unTaxAmt;
	}

	public String getCntnTaxAmt() {
		return cntnTaxAmt;
	}

	public void setCntnTaxAmt(String cntnTaxAmt) {
		this.cntnTaxAmt = cntnTaxAmt;
	}

	public String getInvtyEncd() {
		return invtyEncd;
	}

	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}

	public String getWhsEncd() {
		return whsEncd;
	}

	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}

	public String getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public String getMeasrCorpNm() {
		return measrCorpNm;
	}

	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}

	public String getLoSellPrc() {
		return loSellPrc;
	}

	public void setLoSellPrc(String loSellPrc) {
		this.loSellPrc = loSellPrc;
	}

	public String getMeasrCorpId() {
		return measrCorpId;
	}

	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getBxRule() {
		return bxRule;
	}

	public void setBxRule(String bxRule) {
		this.bxRule = bxRule;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	public String getSpcModel() {
		return spcModel;
	}

	public void setSpcModel(String spcModel) {
		this.spcModel = spcModel;
	}

	public String getInvtyNm() {
		return invtyNm;
	}

	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}

	public String getRefSellPrc() {
		return refSellPrc;
	}

	public void setRefSellPrc(String refSellPrc) {
		this.refSellPrc = refSellPrc;
	}

	public String getRefCost() {
		return refCost;
	}

	public void setRefCost(String refCost) {
		this.refCost = refCost;
	}

	public String getWhsNm() {
		return whsNm;
	}

	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}

	public String getInvtyCd() {
		return invtyCd;
	}

	public void setInvtyCd(String invtyCd) {
		this.invtyCd = invtyCd;
	}

}
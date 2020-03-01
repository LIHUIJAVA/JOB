package com.px.mis.sell.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 采购现存量查询
 */
public class ProcurementNowStokReport {
	@JsonProperty("仓库编码")
	private String whsEncd; // 仓库编码,
	@JsonProperty("存货编码")
	private String invtyEncd; // 存货编码,
	@JsonProperty("批号")
	private String batNum; // 批号,
	@JsonProperty("箱规")
	private String bxQty; // 箱规,
	@JsonProperty("入库数量")
	private BigDecimal intoWhsQty; // 入库数量,
	@JsonProperty("调拨入库数量")
	private BigDecimal intoCannibQty; // 调拨入库数量,
	@JsonProperty("待发货数量")
	private BigDecimal sellOutWhsQty; // 待发货数量,
	@JsonProperty("调拨出库数量")
	private BigDecimal outCannibQty; // 调拨出库数量,
	@JsonProperty("现存量")
	private BigDecimal nowStok; // invty_tab.now_stok 现存量,
	@JsonProperty("可用量")
	private BigDecimal avalQty; // invty_tab.aval_qty 可用量,
	@JsonProperty("生产日期")
	private String prdcDt; // 生产日期,
	@JsonProperty("保质期")
	private String baoZhQi; // 保质期,
	@JsonProperty("失效日期")
	private String invldtnDt; // 失效日期,
	@JsonProperty("参考成本")
	private BigDecimal refCost; // invty_doc.ref_cost 参考成本,
	@JsonProperty("仓库")
	private String whsNm; // whs_doc.whs_nm 仓库,
	@JsonProperty("存货")
	private String invtyNm; // invty_doc.invty_nm 存货,
	@JsonProperty("规格")
	private String spcModel; // invty_doc.spc_model 规格,
	@JsonProperty("计量单位")
	private String measrCorpNm; // measr_corp_doc.measr_corp_nm 计量单位

	public final String getWhsEncd() {
		return whsEncd;
	}

	public final void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}

	public final String getInvtyEncd() {
		return invtyEncd;
	}

	public final void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}

	public final String getBatNum() {
		return batNum;
	}

	public final void setBatNum(String batNum) {
		this.batNum = batNum;
	}

	public final String getBxQty() {
		return bxQty;
	}

	public final void setBxQty(String bxQty) {
		this.bxQty = bxQty;
	}

	public final BigDecimal getIntoWhsQty() {
		return intoWhsQty;
	}

	public final void setIntoWhsQty(BigDecimal intoWhsQty) {
		this.intoWhsQty = intoWhsQty;
	}

	public final BigDecimal getIntoCannibQty() {
		return intoCannibQty;
	}

	public final void setIntoCannibQty(BigDecimal intoCannibQty) {
		this.intoCannibQty = intoCannibQty;
	}

	public final BigDecimal getSellOutWhsQty() {
		return sellOutWhsQty;
	}

	public final void setSellOutWhsQty(BigDecimal sellOutWhsQty) {
		this.sellOutWhsQty = sellOutWhsQty;
	}

	public final BigDecimal getOutCannibQty() {
		return outCannibQty;
	}

	public final void setOutCannibQty(BigDecimal outCannibQty) {
		this.outCannibQty = outCannibQty;
	}

	public final BigDecimal getNowStok() {
		return nowStok;
	}

	public final void setNowStok(BigDecimal nowStok) {
		this.nowStok = nowStok;
	}

	public final BigDecimal getAvalQty() {
		return avalQty;
	}

	public final void setAvalQty(BigDecimal avalQty) {
		this.avalQty = avalQty;
	}

	public final String getPrdcDt() {
		return prdcDt;
	}

	public final void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
	}

	public final String getBaoZhQi() {
		return baoZhQi;
	}

	public final void setBaoZhQi(String baoZhQi) {
		this.baoZhQi = baoZhQi;
	}

	public final String getInvldtnDt() {
		return invldtnDt;
	}

	public final void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}

	public final BigDecimal getRefCost() {
		return refCost;
	}

	public final void setRefCost(BigDecimal refCost) {
		this.refCost = refCost;
	}

	public final String getWhsNm() {
		return whsNm;
	}

	public final void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}

	public final String getInvtyNm() {
		return invtyNm;
	}

	public final void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}

	public final String getSpcModel() {
		return spcModel;
	}

	public final void setSpcModel(String spcModel) {
		this.spcModel = spcModel;
	}

	public final String getMeasrCorpNm() {
		return measrCorpNm;
	}

	public final void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}

}

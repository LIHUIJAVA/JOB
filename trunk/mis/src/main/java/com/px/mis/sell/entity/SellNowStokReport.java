package com.px.mis.sell.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

//销售现存量报表
public class SellNowStokReport {
	// 仓库编码1
	@JsonProperty("仓库编码")
	private String whsEncd;
	// 仓库名称
	@JsonProperty("仓库名称")
	private String whsNm;
	// 存货编码1
	@JsonProperty("存货编码")
	private String invtyEncd;
	@JsonProperty("存货代码")
	private String invtyCd;//存货代码
	//数量
	@JsonProperty("数量")
	private BigDecimal qty;
	// 存货名称
	@JsonProperty("存货名称")
	private String invtyNm;
	// 规格型号
	@JsonProperty("规格型号")
	private String spcModel;// 规格型号
	// 主计量单位
	@JsonProperty("主计量单位")
	private String measrCorpNm;// 计量单位名称
	// 箱规
	@JsonProperty("箱规")
	private BigDecimal bxRule;// 箱规
	// 结存数量
	@JsonProperty("结存数量")
	private BigDecimal nowStok;// 现存量
	// 可用数量
	@JsonProperty("可用数量")
	private BigDecimal avalQty;// 可用量
	// 待入库数量
	@JsonProperty("待入库数量")
	private BigDecimal intoWhsQty;// 可用量
	// 待发货数量
	@JsonProperty("待发货数量")
	private BigDecimal sellWhsQty;// 可用量
	// 调拨待入数量
	@JsonProperty("调拨待入数量")
	private BigDecimal outIntoQty;// 可用量
	// 批号1
	@JsonProperty("批次")
	private String batNum;// 批号
	// 生产日期
	@JsonProperty("生产日期")
	private String prdcDt;// 生产日期
	// 保质期
	@JsonProperty("保质期")
	private String baoZhiQi;// 保质期
	// 失效日期
	@JsonProperty("失效日期")
	private String invldtnDt;// 失效日期
	@JsonProperty("剩余天数")
	private String overdueDays;//剩余天数
	// 参考售价
	@JsonProperty("参考售价")
	private BigDecimal refSellPrc;// 参考售价
	// 最低售价
	@JsonProperty("最低售价")
	private BigDecimal loSellPrc;// 最低售价
	// 调拨待出数量
	@JsonProperty("调拨待出数量")
	private BigDecimal outWhsQty;// 可用量
	// 单位重量
	@JsonProperty("单位重量")
	private BigDecimal weight;// 重量
	// 单位体积
	@JsonProperty("单位体积")
	private String vol;// 体积
	// 平均无税单价
	@JsonProperty("无税单价")
	private BigDecimal noTaxUprc;
	// 无税金额
	@JsonProperty("无税金额")
	private BigDecimal noTaxAmt;
	// 平均含税单价
//	@JsonProperty("平均含税单价")
//	private BigDecimal cntnTaxUprc;
//	// 价税合计
//	@JsonProperty("价税合计")
//	private BigDecimal prcTaxSum;
//	// 税额
//	@JsonProperty("税额")
//	private BigDecimal taxAmt;


	public String getWhsEncd() {
		return whsEncd;
	}

	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}

	public String getWhsNm() {
		return whsNm;
	}

	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}

	public String getInvtyEncd() {
		return invtyEncd;
	}

	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}

	public String getInvtyCd() {
		return invtyCd;
	}

	public void setInvtyCd(String invtyCd) {
		this.invtyCd = invtyCd;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public String getInvtyNm() {
		return invtyNm;
	}

	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}

	public String getSpcModel() {
		return spcModel;
	}

	public void setSpcModel(String spcModel) {
		this.spcModel = spcModel;
	}

	public String getMeasrCorpNm() {
		return measrCorpNm;
	}

	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}

	public BigDecimal getBxRule() {
		return bxRule;
	}

	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}

	public BigDecimal getNowStok() {
		return nowStok;
	}

	public void setNowStok(BigDecimal nowStok) {
		this.nowStok = nowStok;
	}

	public BigDecimal getAvalQty() {
		return avalQty;
	}

	public void setAvalQty(BigDecimal avalQty) {
		this.avalQty = avalQty;
	}

	public BigDecimal getIntoWhsQty() {
		return intoWhsQty;
	}

	public void setIntoWhsQty(BigDecimal intoWhsQty) {
		this.intoWhsQty = intoWhsQty;
	}

	public BigDecimal getSellWhsQty() {
		return sellWhsQty;
	}

	public void setSellWhsQty(BigDecimal sellWhsQty) {
		this.sellWhsQty = sellWhsQty;
	}

	public BigDecimal getOutIntoQty() {
		return outIntoQty;
	}

	public void setOutIntoQty(BigDecimal outIntoQty) {
		this.outIntoQty = outIntoQty;
	}

	public String getBatNum() {
		return batNum;
	}

	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}

	public String getPrdcDt() {
		return prdcDt;
	}

	public void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
	}

	public String getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public String getInvldtnDt() {
		return invldtnDt;
	}

	public void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}

	public String getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(String overdueDays) {
		this.overdueDays = overdueDays;
	}

	public BigDecimal getRefSellPrc() {
		return refSellPrc;
	}

	public void setRefSellPrc(BigDecimal refSellPrc) {
		this.refSellPrc = refSellPrc;
	}

	public BigDecimal getLoSellPrc() {
		return loSellPrc;
	}

	public void setLoSellPrc(BigDecimal loSellPrc) {
		this.loSellPrc = loSellPrc;
	}

	public BigDecimal getOutWhsQty() {
		return outWhsQty;
	}

	public void setOutWhsQty(BigDecimal outWhsQty) {
		this.outWhsQty = outWhsQty;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	public BigDecimal getNoTaxUprc() {
		return noTaxUprc;
	}

	public void setNoTaxUprc(BigDecimal noTaxUprc) {
		this.noTaxUprc = noTaxUprc;
	}

	public BigDecimal getNoTaxAmt() {
		return noTaxAmt;
	}

	public void setNoTaxAmt(BigDecimal noTaxAmt) {
		this.noTaxAmt = noTaxAmt;
	}
}

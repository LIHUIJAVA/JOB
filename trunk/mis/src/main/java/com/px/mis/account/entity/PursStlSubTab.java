package com.px.mis.account.entity;

import java.math.BigDecimal;
//采购结算子表
public class PursStlSubTab {
	private Integer incrsId;//自增id
	private String stlSnglId;//结算单号
	private String invtyEncd;//存货编码
	private String whsEncd;//仓库编码
	private BigDecimal qty;//数量
	private BigDecimal bxQty;//箱数
	private BigDecimal taxRate;//税率
	private BigDecimal taxAmt;//税额
	private BigDecimal noTaxUprc;//无税单价
	private BigDecimal noTaxAmt;//无税金额
	private BigDecimal cntnTaxUprc;//含税单价
	private BigDecimal prcTaxSum;//价税合计
	private String batNum;//批号
	private String prdcDt;//生产日期
	private String baoZhiQi;//保质期
	private String invldtnDt;//失效日期
	private String intlBat;//国际批次
	private String memo;//备注
	
	public String getIntlBat() {
		return intlBat;
	}
	public void setIntlBat(String intlBat) {
		this.intlBat = intlBat;
	}
	public Integer getIncrsId() {
		return incrsId;
	}
	public void setIncrsId(Integer incrsId) {
		this.incrsId = incrsId;
	}
	public String getStlSnglId() {
		return stlSnglId;
	}
	public void setStlSnglId(String stlSnglId) {
		this.stlSnglId = stlSnglId;
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
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public BigDecimal getBxQty() {
		return bxQty;
	}
	public void setBxQty(BigDecimal bxQty) {
		this.bxQty = bxQty;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public BigDecimal getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
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
	public BigDecimal getCntnTaxUprc() {
		return cntnTaxUprc;
	}
	public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
		this.cntnTaxUprc = cntnTaxUprc;
	}
	public BigDecimal getPrcTaxSum() {
		return prcTaxSum;
	}
	public void setPrcTaxSum(BigDecimal prcTaxSum) {
		this.prcTaxSum = prcTaxSum;
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
	public String getInvldtnDt() {
		return invldtnDt;
	}
	public void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getBaoZhiQi() {
		return baoZhiQi;
	}
	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}
	
	 //联查存货档案字段、计量单位名称、仓库名称
    private String invtyNm;//存货名称
    private String spcModel;//规格型号
    private String invtyCd;//存货代码
    private BigDecimal bxRule;//箱规
    private String baoZhiQiDt;//保质期天数
    private BigDecimal iptaxRate;//进项税率
    private BigDecimal optaxRate;//销项税率
    private BigDecimal refCost;//参考成本
    private BigDecimal refSellPrc;//参考售价
    private String measrCorpNm;//计量单位名称
    private String whsNm;//仓库名称
    private String crspdBarCd;//对应条形码

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
	public String getInvtyCd() {
		return invtyCd;
	}
	public void setInvtyCd(String invtyCd) {
		this.invtyCd = invtyCd;
	}
	public BigDecimal getBxRule() {
		return bxRule;
	}
	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}
	public String getBaoZhiQiDt() {
		return baoZhiQiDt;
	}
	public void setBaoZhiQiDt(String baoZhiQiDt) {
		this.baoZhiQiDt = baoZhiQiDt;
	}
	public BigDecimal getIptaxRate() {
		return iptaxRate;
	}
	public void setIptaxRate(BigDecimal iptaxRate) {
		this.iptaxRate = iptaxRate;
	}
	public BigDecimal getOptaxRate() {
		return optaxRate;
	}
	public void setOptaxRate(BigDecimal optaxRate) {
		this.optaxRate = optaxRate;
	}

	public BigDecimal getRefCost() {
		return refCost;
	}
	public void setRefCost(BigDecimal refCost) {
		this.refCost = refCost;
	}
	public BigDecimal getRefSellPrc() {
		return refSellPrc;
	}
	public void setRefSellPrc(BigDecimal refSellPrc) {
		this.refSellPrc = refSellPrc;
	}

	public String getMeasrCorpNm() {
		return measrCorpNm;
	}
	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}
	public String getWhsNm() {
		return whsNm;
	}
	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}
	public String getCrspdBarCd() {
		return crspdBarCd;
	}
	public void setCrspdBarCd(String crspdBarCd) {
		this.crspdBarCd = crspdBarCd;
	}
}

package com.px.mis.purc.entity;

import java.math.BigDecimal;

public class PursOrdrSub implements Comparable<PursOrdrSub>{
	
    private Long ordrNum;//序号
    private String pursOrdrId;//采购订单编号
    private String invtyEncd;//存货编码
    private BigDecimal noTaxUprc;//无税单价
    private BigDecimal noTaxAmt;//无税金额
	private BigDecimal taxAmt;//税额
    private BigDecimal cntnTaxUprc;//含税单价
    private BigDecimal prcTaxSum;//价税合计
    private BigDecimal taxRate;//税率
    private BigDecimal discntAmt;//折扣额(目前未用到)
    private BigDecimal qty;//数量
    private BigDecimal bxQty;//箱数
    private String planToGdsDt;//计划到货时间
    private String measrCorpId;//计量单位编号
    private Integer isComplimentary;//是否赠品
	//联查存货档案字段、计量单位名称
    private String invtyNm;//存货名称
    private String spcModel;//规格型号
    private String invtyCd;//存货代码
    private BigDecimal bxRule;//箱规
    private BigDecimal iptaxRate;//进项税率
    private BigDecimal optaxRate;//销项税率
    private BigDecimal highestPurPrice;//最高进价
    private BigDecimal loSellPrc;//最低售价
    private BigDecimal refCost;//参考成本
    private BigDecimal refSellPrc;//参考售价
    private BigDecimal ltstCost;//最新成本
    private String measrCorpNm;//计量单位名称
    private String crspdBarCd;//对应条形码
    
    private BigDecimal unToGdsQty;//未到货数量
    private BigDecimal unApplPayQty;//未申请付款数量
    private BigDecimal unApplPayAmt;//未申请付款金额
    private String baoZhiQi;//保质期
    private String formTypName;//单据类型名称
    private String memo;//表体备注
    
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}

	public String getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public BigDecimal getUnApplPayAmt() {
		return unApplPayAmt;
	}

	public void setUnApplPayAmt(BigDecimal unApplPayAmt) {
		this.unApplPayAmt = unApplPayAmt;
	}

	public BigDecimal getUnApplPayQty() {
		return unApplPayQty;
	}

	public void setUnApplPayQty(BigDecimal unApplPayQty) {
		this.unApplPayQty = unApplPayQty;
	}

	public Long getOrdrNum() {
		return ordrNum;
	}

	public void setOrdrNum(Long ordrNum) {
		this.ordrNum = ordrNum;
	}

	public BigDecimal getUnToGdsQty() {
		return unToGdsQty;
	}

	public void setUnToGdsQty(BigDecimal unToGdsQty) {
		this.unToGdsQty = unToGdsQty;
	}

	public String getPursOrdrId() {
		return pursOrdrId;
	}

	public void setPursOrdrId(String pursOrdrId) {
		this.pursOrdrId = pursOrdrId;
	}

	public String getInvtyEncd() {
		return invtyEncd;
	}

	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
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

	public BigDecimal getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
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

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getDiscntAmt() {
		return discntAmt;
	}

	public void setDiscntAmt(BigDecimal discntAmt) {
		this.discntAmt = discntAmt;
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

	public String getPlanToGdsDt() {
		return planToGdsDt;
	}

	public void setPlanToGdsDt(String planToGdsDt) {
		this.planToGdsDt = planToGdsDt;
	}

	public String getMeasrCorpId() {
		return measrCorpId;
	}

	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
	}

	public Integer getIsComplimentary() {
		return isComplimentary;
	}

	public void setIsComplimentary(Integer isComplimentary) {
		this.isComplimentary = isComplimentary;
	}

	public String getCrspdBarCd() {
		return crspdBarCd;
	}

	public void setCrspdBarCd(String crspdBarCd) {
		this.crspdBarCd = crspdBarCd;
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

	public BigDecimal getHighestPurPrice() {
		return highestPurPrice;
	}

	public void setHighestPurPrice(BigDecimal highestPurPrice) {
		this.highestPurPrice = highestPurPrice;
	}

	public BigDecimal getLoSellPrc() {
		return loSellPrc;
	}

	public void setLoSellPrc(BigDecimal loSellPrc) {
		this.loSellPrc = loSellPrc;
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

	public BigDecimal getLtstCost() {
		return ltstCost;
	}

	public void setLtstCost(BigDecimal ltstCost) {
		this.ltstCost = ltstCost;
	}
	
	@Override
	public int compareTo(PursOrdrSub pursOrdrSub) {
		return this.invtyEncd.compareTo(pursOrdrSub.getInvtyEncd());
	}

}
package com.px.mis.account.entity;

import java.math.BigDecimal;
//销售专用发票子表
public class SellInvSubTab {
	private Integer ordrNum;//序号
	private String sellInvNum;//销售发票号
	private String invtyEncd;//存货编码
	private String whsEncd;//仓库编号
	private BigDecimal qty;//数量
	private String measrCorpId;//计量单位编号
	private BigDecimal noTaxUprc;//无税单价
	private BigDecimal noTaxAmt;//无税金额
	private BigDecimal cntnTaxUprc;//含税单价
	private BigDecimal prcTaxSum;//价税合计
	private BigDecimal taxAmt;//税额
	private BigDecimal taxRate;//税率
	private String batNum;//批号
	private Integer isComplimentary;//是否是赠品
	private String delvSnglNum;//发货单号
	private String invtyFstLvlCls;//存货一级分类
	
	private String memo;//备注
	
	private String whsNm;//仓库
	private String invtyNm;//存货名称
	private String invtyCd;//存货代码
	private String spcModel;//规格型号
	private String measrCorpNm;//计量单位
	private String baoZhiQiDt;//保质期天数
	private String crspdBarCd;//对应条形码
	private BigDecimal bxRule; //箱规
	private String intlBat;//国际批次
	private Integer isNtRtnGoods;//是否退货
	private BigDecimal bxQty;//箱数
	
	
	public String getInvtyCd() {
		return invtyCd;
	}
	public void setInvtyCd(String invtyCd) {
		this.invtyCd = invtyCd;
	}
	public String getBaoZhiQiDt() {
		return baoZhiQiDt;
	}
	public void setBaoZhiQiDt(String baoZhiQiDt) {
		this.baoZhiQiDt = baoZhiQiDt;
	}
	public String getCrspdBarCd() {
		return crspdBarCd;
	}
	public void setCrspdBarCd(String crspdBarCd) {
		this.crspdBarCd = crspdBarCd;
	}
	public BigDecimal getBxRule() {
		return bxRule;
	}
	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}
	public String getIntlBat() {
		return intlBat;
	}
	public void setIntlBat(String intlBat) {
		this.intlBat = intlBat;
	}
	public Integer getIsNtRtnGoods() {
		return isNtRtnGoods;
	}
	public void setIsNtRtnGoods(Integer isNtRtnGoods) {
		this.isNtRtnGoods = isNtRtnGoods;
	}
	public BigDecimal getBxQty() {
		return bxQty;
	}
	public void setBxQty(BigDecimal bxQty) {
		this.bxQty = bxQty;
	}
	public SellInvSubTab() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getSellInvNum() {
		return sellInvNum;
	}
	public void setSellInvNum(String sellInvNum) {
		this.sellInvNum = sellInvNum;
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
	public String getMeasrCorpId() {
		return measrCorpId;
	}
	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
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
	public BigDecimal getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public String getBatNum() {
		return batNum;
	}
	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}
	public Integer getIsComplimentary() {
		return isComplimentary;
	}
	public void setIsComplimentary(Integer isComplimentary) {
		this.isComplimentary = isComplimentary;
	}
	public String getDelvSnglNum() {
		return delvSnglNum;
	}
	public void setDelvSnglNum(String delvSnglNum) {
		this.delvSnglNum = delvSnglNum;
	}
	public String getInvtyFstLvlCls() {
		return invtyFstLvlCls;
	}
	public void setInvtyFstLvlCls(String invtyFstLvlCls) {
		this.invtyFstLvlCls = invtyFstLvlCls;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getWhsNm() {
		return whsNm;
	}
	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
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
	
}

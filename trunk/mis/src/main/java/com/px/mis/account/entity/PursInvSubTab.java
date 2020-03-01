package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//采购专用发票子表
public class PursInvSubTab {
	private Integer ordrNum;//序号
	private String pursInvNum;//采购发票号
	private String invtyFstLvlCls;//存货一级分类
	private String crspdIntoWhsSnglNum;//对应入库单号
	private String intoWhsSnglSubtabId;//入库单子表id ?是否是int类型
	private String invtyEncd;//存货编码
	private String whsEncd;//仓库编号
	private BigDecimal qty;//数量
	private BigDecimal noTaxUprc;//无税单价 单价
	private BigDecimal noTaxAmt;//无税金额 金额
	private BigDecimal taxAmt;//税额
	private BigDecimal cntnTaxUprc;//含税单价
	private BigDecimal prcTaxSum;//价税合计
	private BigDecimal taxRate;//税率
	private String batNum;//批号 批次
	private String intlBat;//国际批次
	private String measrCorpId;//计量单位编号
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date stlDt;//结算日期
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date stlTm;//结算时间
	private String memo;//备注
	
	private String whsNm;//仓库
	private String invtyNm;//存货名称
	private String invtyCd;//存货代码
	private String spcModel;//规格型号
	private BigDecimal bxRule;//箱规
	private BigDecimal bxQty;//箱数
	private Integer isNtRtnGoods;//是否退货
	private String measrCorpNm;//计量单位
	private String baoZhiQiDt;//保质期天数
	private String crspdBarCd;//对应条形码
	private String gift;//赠品
	public PursInvSubTab() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getPursInvNum() {
		return pursInvNum;
	}
	public void setPursInvNum(String pursInvNum) {
		this.pursInvNum = pursInvNum;
	}
	public String getInvtyFstLvlCls() {
		return invtyFstLvlCls;
	}
	public void setInvtyFstLvlCls(String invtyFstLvlCls) {
		this.invtyFstLvlCls = invtyFstLvlCls;
	}
	public String getCrspdIntoWhsSnglNum() {
		return crspdIntoWhsSnglNum;
	}
	public void setCrspdIntoWhsSnglNum(String crspdIntoWhsSnglNum) {
		this.crspdIntoWhsSnglNum = crspdIntoWhsSnglNum;
	}
	public String getIntoWhsSnglSubtabId() {
		return intoWhsSnglSubtabId;
	}
	public void setIntoWhsSnglSubtabId(String intoWhsSnglSubtabId) {
		this.intoWhsSnglSubtabId = intoWhsSnglSubtabId;
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
	public String getBatNum() {
		return batNum;
	}
	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}
	public String getMeasrCorpId() {
		return measrCorpId;
	}
	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
	}
	public Date getStlDt() {
		return stlDt;
	}
	public void setStlDt(Date stlDt) {
		this.stlDt = stlDt;
	}
	public Date getStlTm() {
		return stlTm;
	}
	public void setStlTm(Date stlTm) {
		this.stlTm = stlTm;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public String getGift() {
		return gift;
	}
	public void setGift(String gift) {
		this.gift = gift;
	}
	public String getIntlBat() {
		return intlBat;
	}
	public void setIntlBat(String intlBat) {
		this.intlBat = intlBat;
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
	public BigDecimal getBxRule() {
		return bxRule;
	}
	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}
	public BigDecimal getBxQty() {
		return bxQty;
	}
	public void setBxQty(BigDecimal bxQty) {
		this.bxQty = bxQty;
	}
	public Integer getIsNtRtnGoods() {
		return isNtRtnGoods;
	}
	public void setIsNtRtnGoods(Integer isNtRtnGoods) {
		this.isNtRtnGoods = isNtRtnGoods;
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
	
}

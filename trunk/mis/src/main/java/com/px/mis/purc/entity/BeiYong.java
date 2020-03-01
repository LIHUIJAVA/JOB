package com.px.mis.purc.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
//销售单主表
public class BeiYong {
	@JsonProperty("单号")
    private String formId;//单号
	@JsonProperty("日期")
    private String formDt;//日期
	@JsonProperty("用户编号")
    private String accNum;//用户编号    
	@JsonProperty("用户名称")
    private String userName;//用户名称    
	@JsonProperty("业务类型编号")
    private String bizTypId;//业务类型编号
	@JsonProperty("业务类型名称")
    private String bizTypNm;//业务类型名称
	@JsonProperty("客户编号")
    private String custId;//客户编号
	@JsonProperty("客户名称")
    private String custNm;//客户名称
	@JsonProperty("部门编号")
    private String deptId;//部门编号    
	@JsonProperty("部门名称")
    private String deptName;//部门名称
	@JsonProperty("销售类型编号")
    private String sellTypId;//销售类型编号
	@JsonProperty("销售类型名称")
    private String sellTypNm;//销售类型名称
	@JsonProperty("收发类别编号")
    private String recvSendCateId;//收发类别编号
	@JsonProperty("收发类别名称")
    private String recvSendCateNm;//收发类别名称
	@JsonProperty("仓库编码")
    private String whsEncd;//仓库编码
	@JsonProperty("仓库名称")
    private String whsNm;//仓库名称
	@JsonProperty("存货编码")
    private String invtyEncd;//存货编码
	@JsonProperty("存货名称")
    private String invtyNm;//存货名称
	@JsonProperty("规格型号")
    private String spcModel;//规格型号
	@JsonProperty("存货代码")
    private String invtyCd;//存货代码
	@JsonProperty("条形码(对应条形码)")
    private String crspdBarCd;//条形码(对应条形码)
	@JsonProperty("主计量单位编码")
    private String measrCorpId;//主计量单位编码
	@JsonProperty("主计量单位名称")
    private String measrCorpNm;//主计量单位名称
	@JsonProperty("数量")
    private BigDecimal qty;//数量
	@JsonProperty("箱数")
    private BigDecimal bxQty;//箱数
	@JsonProperty("箱规")
    private BigDecimal bxRule;//箱规
	@JsonProperty("含税单价")
    private BigDecimal cntnTaxUprc;//含税单价
	@JsonProperty("价税合计")
    private BigDecimal prcTaxSum;//价税合计
	@JsonProperty("无税单价")
    private BigDecimal noTaxUprc;//无税单价
	@JsonProperty("无税金额")
    private BigDecimal noTaxAmt;//无税金额
	@JsonProperty("税额")
    private BigDecimal taxAmt;//税额
	@JsonProperty("税率")
    private BigDecimal taxRate;//税率
	@JsonProperty("批次")
    private String batNum;//批次
	@JsonProperty("国际批次")
    private String intlBat;//国际批次
	@JsonProperty("成本")
    private BigDecimal ChengBen;//成本
	@JsonProperty("毛利")
    private BigDecimal MaoLi;//毛利
	@JsonProperty("生产日期")
    private String prdcDt;//生产日期
	@JsonProperty("失效日期")
    private String invldtnDt;//失效日期
	@JsonProperty("保质期")
    private String baoZhiQi;//保质期
    
    
    
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
	@JsonIgnore
	public BigDecimal getChengBen() {
		return ChengBen;
	}
	@JsonIgnore
	public void setChengBen(BigDecimal ChengBen) {
		this.ChengBen = ChengBen;
	}
	@JsonIgnore
	public BigDecimal getMaoLi() {
		return MaoLi;
	}
	@JsonIgnore
	public void setMaoLi(BigDecimal MaoLi) {
		this.MaoLi = MaoLi;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFormDt() {
		return formDt;
	}
	public void setFormDt(String formDt) {
		this.formDt = formDt;
	}
	public String getAccNum() {
		return accNum;
	}
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBizTypId() {
		return bizTypId;
	}
	public void setBizTypId(String bizTypId) {
		this.bizTypId = bizTypId;
	}
	public String getBizTypNm() {
		return bizTypNm;
	}
	public void setBizTypNm(String bizTypNm) {
		this.bizTypNm = bizTypNm;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustNm() {
		return custNm;
	}
	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getSellTypId() {
		return sellTypId;
	}
	public void setSellTypId(String sellTypId) {
		this.sellTypId = sellTypId;
	}
	public String getSellTypNm() {
		return sellTypNm;
	}
	public void setSellTypNm(String sellTypNm) {
		this.sellTypNm = sellTypNm;
	}
	public String getRecvSendCateId() {
		return recvSendCateId;
	}
	public void setRecvSendCateId(String recvSendCateId) {
		this.recvSendCateId = recvSendCateId;
	}
	public String getRecvSendCateNm() {
		return recvSendCateNm;
	}
	public void setRecvSendCateNm(String recvSendCateNm) {
		this.recvSendCateNm = recvSendCateNm;
	}
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
	public String getCrspdBarCd() {
		return crspdBarCd;
	}
	public void setCrspdBarCd(String crspdBarCd) {
		this.crspdBarCd = crspdBarCd;
	}
	public String getMeasrCorpId() {
		return measrCorpId;
	}
	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
	}
	public String getMeasrCorpNm() {
		return measrCorpNm;
	}
	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
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
	public String getIntlBat() {
		return intlBat;
	}
	public void setIntlBat(String intlBat) {
		this.intlBat = intlBat;
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
	public String getBaoZhiQi() {
		return baoZhiQi;
	}
	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}
	
}
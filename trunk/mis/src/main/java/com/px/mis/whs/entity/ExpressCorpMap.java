package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//快递公司
public class ExpressCorpMap {

    //	private String expressEncd;// 快递公司编号
//
//	private String expressNm;// 快递公司名称
//
//	private String expressTyp;// 快递类型
//
//	private String expOrderId;// 快递单号
//
//	private String sellHomeInfo;// 卖家信息
//
//	private String buyHomeInfo;// 买家信息
//
//	private Integer isNtSprtGdsToPay;// 是否支持货到付款
//
//	private BigDecimal gdsToPayServCost;// 货到付款服务费
//
//	private Integer isNtStpUse;// 是否停用
//
//	private BigDecimal firstWet;// 首重
//
//	private BigDecimal firstWetStrPrice;// 首重起价
//
//	private Integer printCnt;// 打印次数
//
//	private String memo;// 备注
//
//	private Integer isNtChk;// 是否审核
//
//	private String setupPers;// 创建人
//
//	private String setupTm;// 创建时间
//
//	private String mdfr;// 修改人
//
//	private String modiTm;// 修改时间
//
//	private String chkr;// 审核人
//
//	private String chkTm;// 审核时间
//
//	private String deliver;// 发货人姓名
//
//	private String deliverPhone;// 发货人手机
//
//	private String deliverMobile;// 发货人座机
//
//	private String companyCode;// 快递公司代码
//
//	private String province;// 发货地省
//
//	private String city;// 发货地市
//
//	private String country;// 发货地区
//
//	private String detailedAddress;// 详细地址
    @JsonProperty("快递公司编码")
    private String expressEncd;// 快递公司编号
    @JsonProperty("快递公司名称")
    private String expressNm;// 快递公司名称
    //	@JsonProperty("快递类型")
    @JsonIgnore
    private String expressTyp;// 快递类型
    //	@JsonProperty("快递单号")
    @JsonIgnore
    private String expOrderId;// 快递单号
    //	@JsonProperty("卖家信息")
    @JsonIgnore
    private String sellHomeInfo;// 卖家信息
    //	@JsonProperty("买家信息")
    @JsonIgnore
    private String buyHomeInfo;// 买家信息
    //	@JsonProperty("是否支持货到付款")
    @JsonIgnore
    private Integer isNtSprtGdsToPay;// 是否支持货到付款
    //	@JsonProperty("货到付款服务费")
    @JsonIgnore
    private BigDecimal gdsToPayServCost;// 货到付款服务费
    @JsonProperty("是否停用")
    private Integer isNtStpUse;// 是否停用
    @JsonProperty("首重")
    private BigDecimal firstWet;// 首重
    @JsonProperty("首重起价")
    private BigDecimal firstWetStrPrice;// 首重起价
    //	@JsonProperty("打印次数")
    @JsonIgnore
    private Integer printCnt;// 打印次数
    @JsonProperty("备注")
    private String memo;// 备注
    //	@JsonProperty("是否审核")
    @JsonIgnore
    private Integer isNtChk;// 是否审核
    @JsonProperty("创建人")
    private String setupPers;// 创建人
    @JsonProperty("创建时间")
    private String setupTm;// 创建时间
    @JsonProperty("修改人")
    private String mdfr;// 修改人
    @JsonProperty("修改时间")
    private String modiTm;// 修改时间
    //	@JsonProperty("审核人")
    @JsonIgnore
    private String chkr;// 审核人
    //	@JsonProperty("审核时间")
    @JsonIgnore
    private String chkTm;// 审核时间
    @JsonProperty("发货人姓名")
    private String deliver;// 发货人姓名
    @JsonProperty("发货人手机")
    private String deliverPhone;// 发货人手机
    @JsonProperty("发货人座机")
    private String deliverMobile;// 发货人座机
    @JsonProperty("快递公司代码")
    private String companyCode;// 快递公司代码
    @JsonProperty("发货地省")
    private String province;// 发货地省
    @JsonProperty("发货地市")
    private String city;// 发货地市
    @JsonProperty("发货地区")
    private String country;// 发货地区
    @JsonProperty("详细地址")
    private String detailedAddress;// 详细地址

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getDeliverPhone() {
        return deliverPhone;
    }

    public void setDeliverPhone(String deliverPhone) {
        this.deliverPhone = deliverPhone;
    }

    public String getDeliverMobile() {
        return deliverMobile;
    }

    public void setDeliverMobile(String deliverMobile) {
        this.deliverMobile = deliverMobile;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getExpressEncd() {
        return expressEncd;
    }

    public void setExpressEncd(String expressEncd) {
        this.expressEncd = expressEncd == null ? null : expressEncd.trim();
    }

    public String getExpressNm() {
        return expressNm;
    }

    public void setExpressNm(String expressNm) {
        this.expressNm = expressNm == null ? null : expressNm.trim();
    }

    public String getExpressTyp() {
        return expressTyp;
    }

    public void setExpressTyp(String expressTyp) {
        this.expressTyp = expressTyp == null ? null : expressTyp.trim();
    }

    public String getExpOrderId() {
        return expOrderId;
    }

    public void setExpOrderId(String expOrderId) {
        this.expOrderId = expOrderId == null ? null : expOrderId.trim();
    }

    public String getSellHomeInfo() {
        return sellHomeInfo;
    }

    public void setSellHomeInfo(String sellHomeInfo) {
        this.sellHomeInfo = sellHomeInfo == null ? null : sellHomeInfo.trim();
    }

    public String getBuyHomeInfo() {
        return buyHomeInfo;
    }

    public void setBuyHomeInfo(String buyHomeInfo) {
        this.buyHomeInfo = buyHomeInfo == null ? null : buyHomeInfo.trim();
    }

    public Integer getIsNtSprtGdsToPay() {
        return isNtSprtGdsToPay;
    }

    public void setIsNtSprtGdsToPay(Integer isNtSprtGdsToPay) {
        this.isNtSprtGdsToPay = isNtSprtGdsToPay;
    }

    public BigDecimal getGdsToPayServCost() {
        return gdsToPayServCost;
    }

    public void setGdsToPayServCost(BigDecimal gdsToPayServCost) {
        this.gdsToPayServCost = gdsToPayServCost;
    }

    public Integer getIsNtStpUse() {
        return isNtStpUse;
    }

    public void setIsNtStpUse(Integer isNtStpUse) {
        this.isNtStpUse = isNtStpUse;
    }

    public BigDecimal getFirstWet() {
        return firstWet;
    }

    public void setFirstWet(BigDecimal firstWet) {
        this.firstWet = firstWet;
    }

    public BigDecimal getFirstWetStrPrice() {
        return firstWetStrPrice;
    }

    public void setFirstWetStrPrice(BigDecimal firstWetStrPrice) {
        this.firstWetStrPrice = firstWetStrPrice;
    }

    public Integer getPrintCnt() {
        return printCnt;
    }

    public void setPrintCnt(Integer printCnt) {
        this.printCnt = printCnt;
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers == null ? null : setupPers.trim();
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr;
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public Integer getIsNtChk() {
        return isNtChk;
    }

    public void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }
}
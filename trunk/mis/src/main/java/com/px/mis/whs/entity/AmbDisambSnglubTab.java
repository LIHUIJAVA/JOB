package com.px.mis.whs.entity;

import java.math.BigDecimal;

//组装拆卸单子表
public class AmbDisambSnglubTab {

    private String formNum;// 单据号

    private String subKitEncd;// 子配套件编码

    private Long ordrNum;// 序号

    private String whsEncd;// 仓库编码

    private BigDecimal subQty;// 子件数量

    private BigDecimal momQty;// 母件数量

    private BigDecimal momSubRatio;// 母子比例

    private BigDecimal taxRate;// 税率

    private BigDecimal scntnTaxUprc;// 含税单价

    private BigDecimal sunTaxUprc;// 未税单价

    private BigDecimal scntnTaxAmt;// 含税金额

    private BigDecimal sunTaxAmt;// 未税金额

    private String sbatNum;// 批号

    private String sprdcDt;// 生产日期

    private String baoZhiQi;// 保质期

    private String sinvldtnDt;// 失效日期

//	private InvtyDoc invtyDoc;

    // 联查存货档案字段、计量单位名称、仓库名称
    private String invtyNm;// 存货名称
    private String spcModel;// 规格型号
    private String invtyCd;// 存货代码
    private BigDecimal bxRule;// 箱规
    private String baoZhiQiDt;// 保质期天数
    private BigDecimal iptaxRate;// 进项税率
    private BigDecimal optaxRate;// 销项税率
    private BigDecimal highestPurPrice;// 最高进价
    private BigDecimal loSellPrc;// 最低售价
    private BigDecimal refCost;// 参考成本
    private BigDecimal refSellPrc;// 参考售价
    private BigDecimal ltstCost;// 最新成本
    private String measrCorpNm;// 计量单位名称
    private String whsNm;// 仓库名称
    private String crspdBarCd;// 对应条形码
    private BigDecimal sbxQty;// 箱数
    private String projEncd;// 项目编码
    private String projNm;// 项目名称
    private BigDecimal staxAmt;//税额

    public BigDecimal getStaxAmt() {
        return staxAmt;
    }

    public void setStaxAmt(BigDecimal staxAmt) {
        this.staxAmt = staxAmt;
    }

    public String getProjNm() {
        return projNm;
    }

    public void setProjNm(String projNm) {
        this.projNm = projNm;
    }

    public String getProjEncd() {
        return projEncd;
    }

    public void setProjEncd(String projEncd) {
        this.projEncd = projEncd;
    }

    public final BigDecimal getSbxQty() {
        return sbxQty;
    }

    public final void setSbxQty(BigDecimal sbxQty) {
        this.sbxQty = sbxQty;
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

    public String getSprdcDt() {
        return sprdcDt;
    }

    public void setSprdcDt(String sprdcDt) {
        this.sprdcDt = sprdcDt;
    }

    public String getSinvldtnDt() {
        return sinvldtnDt;
    }

    public void setSinvldtnDt(String sinvldtnDt) {
        this.sinvldtnDt = sinvldtnDt;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public String getSubKitEncd() {
        return subKitEncd;
    }

    public void setSubKitEncd(String subKitEncd) {
        this.subKitEncd = subKitEncd == null ? null : subKitEncd.trim();
    }

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd == null ? null : whsEncd.trim();
    }

    public BigDecimal getSubQty() {
        return subQty;
    }

    public void setSubQty(BigDecimal subQty) {
        this.subQty = subQty;
    }

    public BigDecimal getMomQty() {
        return momQty;
    }

    public void setMomQty(BigDecimal momQty) {
        this.momQty = momQty;
    }

    public BigDecimal getMomSubRatio() {
        return momSubRatio;
    }

    public void setMomSubRatio(BigDecimal momSubRatio) {
        this.momSubRatio = momSubRatio;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getScntnTaxUprc() {
        return scntnTaxUprc;
    }

    public void setScntnTaxUprc(BigDecimal scntnTaxUprc) {
        this.scntnTaxUprc = scntnTaxUprc;
    }

    public BigDecimal getSunTaxUprc() {
        return sunTaxUprc;
    }

    public void setSunTaxUprc(BigDecimal sunTaxUprc) {
        this.sunTaxUprc = sunTaxUprc;
    }

    public BigDecimal getScntnTaxAmt() {
        return scntnTaxAmt;
    }

    public void setScntnTaxAmt(BigDecimal scntnTaxAmt) {
        this.scntnTaxAmt = scntnTaxAmt;
    }

    public BigDecimal getSunTaxAmt() {
        return sunTaxAmt;
    }

    public void setSunTaxAmt(BigDecimal sunTaxAmt) {
        this.sunTaxAmt = sunTaxAmt;
    }

    public String getSbatNum() {
        return sbatNum;
    }

    public void setSbatNum(String sbatNum) {
        this.sbatNum = sbatNum == null ? null : sbatNum.trim();
    }

    public String getBaoZhiQi() {
        return baoZhiQi;
    }

    public void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi == null ? null : baoZhiQi.trim();
    }

}
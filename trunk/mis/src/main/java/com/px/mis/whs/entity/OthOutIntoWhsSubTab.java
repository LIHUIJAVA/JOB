package com.px.mis.whs.entity;

import java.math.BigDecimal;


import com.px.mis.purc.entity.InvtyDoc;

//其他出入库单子表
public class OthOutIntoWhsSubTab {
    private Long ordrNum;//序号

    private String formNum;//单号

    private String invtyEncd;//存货编码

    private BigDecimal qty;//数量

    private BigDecimal taxRate;//税率

    private BigDecimal cntnTaxUprc;//含税单价

    private BigDecimal unTaxUprc;//未税单价[单价(根据批次带入单价)]

    private BigDecimal bookEntryUprc;//记账单价

    private BigDecimal cntnTaxAmt;//含税金额

    private BigDecimal unTaxAmt;//未税金额

    private String intlBat;//国际批号
    private String batNum;//批号

    private String prdcDt;//生产日期

    private String baoZhiQi;//保质期

    private String invldtnDt;//失效日期

    private String gdsBitEncd;//货位编码

    private InvtyDoc invtyDoc;

    // 联查存货档案字段、计量单位名称、仓库名称
    private String invtyNm;// 存货名称
    private String spcModel;// 规格型号
    private String invtyCd;// 存货代码
    private BigDecimal bxRule;// 箱规
    private BigDecimal bxQty;// 箱规

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
    private String invtyClsEncd;//存货分类名称
    private String invtyClsNm;//存货分类名称
    private String measrCorpId;//计量单位编号
    private String memos;//备注
    private String projEncd;// 项目编码
    private String projNm;// 项目名称
    private BigDecimal taxAmt;//税额

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
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


    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }

    public BigDecimal getBxQty() {
        return bxQty;
    }

    public void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
    }

    public String getInvtyClsEncd() {
        return invtyClsEncd;
    }

    public void setInvtyClsEncd(String invtyClsEncd) {
        this.invtyClsEncd = invtyClsEncd;
    }

    public String getInvtyClsNm() {
        return invtyClsNm;
    }

    public void setInvtyClsNm(String invtyClsNm) {
        this.invtyClsNm = invtyClsNm;
    }

    public String getMeasrCorpId() {
        return measrCorpId;
    }

    public void setMeasrCorpId(String measrCorpId) {
        this.measrCorpId = measrCorpId;
    }

    public InvtyDoc getInvtyDoc() {
        return invtyDoc;
    }

    public void setInvtyDoc(InvtyDoc invtyDoc) {
        this.invtyDoc = invtyDoc;
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

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd;
    }

    public String getIntlBat() {
        return intlBat;
    }

    public void setIntlBat(String intlBat) {
        this.intlBat = intlBat;
    }


    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum == null ? null : formNum.trim();
    }


    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd == null ? null : invtyEncd.trim();
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getCntnTaxUprc() {
        return cntnTaxUprc;
    }

    public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
        this.cntnTaxUprc = cntnTaxUprc;
    }

    public BigDecimal getUnTaxUprc() {
        return unTaxUprc;
    }

    public void setUnTaxUprc(BigDecimal unTaxUprc) {
        this.unTaxUprc = unTaxUprc;
    }

    public BigDecimal getBookEntryUprc() {
        return bookEntryUprc;
    }

    public void setBookEntryUprc(BigDecimal bookEntryUprc) {
        this.bookEntryUprc = bookEntryUprc;
    }

    public BigDecimal getCntnTaxAmt() {
        return cntnTaxAmt;
    }

    public void setCntnTaxAmt(BigDecimal cntnTaxAmt) {
        this.cntnTaxAmt = cntnTaxAmt;
    }

    public BigDecimal getUnTaxAmt() {
        return unTaxAmt;
    }

    public void setUnTaxAmt(BigDecimal unTaxAmt) {
        this.unTaxAmt = unTaxAmt;
    }

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum == null ? null : batNum.trim();
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


}
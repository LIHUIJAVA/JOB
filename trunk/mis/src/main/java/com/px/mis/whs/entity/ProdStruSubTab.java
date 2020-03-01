package com.px.mis.whs.entity;

import java.math.BigDecimal;

//产品结构子表
public class ProdStruSubTab {


    private String subEncd;//子件编码
    private String ordrNumSub;//序号
    private String ordrNum;//产品结构主标识
    private String momEncd;//母件编码
    private String subNm;//子件名称
    private String subSpc;//子件规格
    private String measrCorp;//计量单位
    private BigDecimal bxRule;//箱规
    private BigDecimal subQty;//子件数量
    private BigDecimal momQty;//母件数量
    private String memo;//备注

    private String smeasrCorpNm;//计量单位名称
    private String sbaoZhiQiDt;// 保质期天数
    private BigDecimal soptaxRate;// 销项税率
    private String sinvtyCd;// 存货代码
    private BigDecimal srefCost;// 参考成本
    private String scrspdBarCd;// 对应条形码

    public String getSubEncd() {
        return subEncd;
    }

    public void setSubEncd(String subEncd) {
        this.subEncd = subEncd;
    }

    public String getOrdrNumSub() {
        return ordrNumSub;
    }

    public void setOrdrNumSub(String ordrNumSub) {
        this.ordrNumSub = ordrNumSub;
    }

    public String getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(String ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getMomEncd() {
        return momEncd;
    }

    public void setMomEncd(String momEncd) {
        this.momEncd = momEncd;
    }

    public String getSubNm() {
        return subNm;
    }

    public void setSubNm(String subNm) {
        this.subNm = subNm;
    }

    public String getSubSpc() {
        return subSpc;
    }

    public void setSubSpc(String subSpc) {
        this.subSpc = subSpc;
    }

    public String getMeasrCorp() {
        return measrCorp;
    }

    public void setMeasrCorp(String measrCorp) {
        this.measrCorp = measrCorp;
    }

    public BigDecimal getBxRule() {
        return bxRule;
    }

    public void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSmeasrCorpNm() {
        return smeasrCorpNm;
    }

    public void setSmeasrCorpNm(String smeasrCorpNm) {
        this.smeasrCorpNm = smeasrCorpNm;
    }

    public String getSbaoZhiQiDt() {
        return sbaoZhiQiDt;
    }

    public void setSbaoZhiQiDt(String sbaoZhiQiDt) {
        this.sbaoZhiQiDt = sbaoZhiQiDt;
    }

    public BigDecimal getSoptaxRate() {
        return soptaxRate;
    }

    public void setSoptaxRate(BigDecimal soptaxRate) {
        this.soptaxRate = soptaxRate;
    }

    public String getSinvtyCd() {
        return sinvtyCd;
    }

    public void setSinvtyCd(String sinvtyCd) {
        this.sinvtyCd = sinvtyCd;
    }

    public BigDecimal getSrefCost() {
        return srefCost;
    }

    public void setSrefCost(BigDecimal srefCost) {
        this.srefCost = srefCost;
    }

    public String getScrspdBarCd() {
        return scrspdBarCd;
    }

    public void setScrspdBarCd(String scrspdBarCd) {
        this.scrspdBarCd = scrspdBarCd;
    }
}
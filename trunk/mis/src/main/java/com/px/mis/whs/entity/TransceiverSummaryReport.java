package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

//收发汇总报表
public class TransceiverSummaryReport {
    /**
     * 仓库编码
     */
    @JsonProperty("仓库编码")
    private String whsEncd;// 仓库编码
    /**
     * 存货编码
     */
    @JsonProperty("存货编码")
    private String invtyEncd;// 存货编
    /**
     * 批号
     */
    @JsonProperty("批号")
    private String batNum;// 批号

    /**
     * 存货名称
     */
    @JsonProperty("存货名称")
    private String invtyNm; // 存货名称
    /**
     * 仓库名称
     */
    @JsonProperty("仓库名称")
    private String whsNm; // 仓库名称
    @JsonProperty("规格")
    private String spcModel;// 规格
    @JsonProperty("计量单位名称")
    private String measrCorpNm;// 计量单位
    @JsonProperty("存货分类名称")
    private String invtyClsNm;// 存货分类名称

    /**
     * 期初
     */
    @JsonProperty("期初数量")
    private BigDecimal qiChuQty;// 量
    @JsonProperty("期初无税金额")
    private BigDecimal qiChuAmt;// 无税金额
    @JsonProperty("期初价税合计")
    private BigDecimal qiChuSum;// 价税合计含税

    /**
     * 进量
     */
    @JsonProperty("入库数量")
    private BigDecimal intoQty;// 量
    @JsonProperty("入库无税金额")
    private BigDecimal intoAmt;// 无税金额
    @JsonProperty("入库价税合计")
    private BigDecimal intoSum;// 价税合计含税

    /**
     * 出量
     */
    @JsonProperty("出库数量")
    private BigDecimal outQty;// 量
    @JsonProperty("出库无税金额")
    private BigDecimal outAmt;// 无税金额
    @JsonProperty("出库价税合计")
    private BigDecimal outSum;// 价税合计含税

    /**
     * 结存
     */
    @JsonProperty("结存数量")
    private BigDecimal jieCunQty;// 量
    @JsonProperty("结存无税金额")
    private BigDecimal jieChuAmt;// 无税金额
    @JsonProperty("结存价税合计")
    private BigDecimal jieChuSum;// 价税合计含税
    @JsonProperty("生产日期")
    private String prdcDt;// 生产日期
    @JsonProperty("保质期")
    private String baoZhiQi;// 保质期
    @JsonProperty("失效日期")
    private String invldtnDt;// 失效日期
    @JsonProperty("箱规")
    private String bxRule; // 箱规

    /**
     * @return 仓库编码
     * @date 2019年4月15日
     */
    public final String getWhsEncd() {
        return whsEncd;
    }

    /**
     * @param 仓库编码
     * @date 2019年4月15日
     */
    public final void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    /**
     * @return 调拨：减少、增加可用量现存量别名
     * @date 2019年4月15日
     */
    public final String getInvtyEncd() {
        return invtyEncd;
    }

    /**
     * @param 调拨：减少、增加可用量现存量别名
     * @date 2019年4月15日
     */
    public final void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    /**
     * @return 批号
     * @date 2019年4月15日
     */
    public final String getBatNum() {
        return batNum;
    }

    /**
     * @param 批号
     * @date 2019年4月15日
     */
    public final void setBatNum(String batNum) {
        this.batNum = batNum;
    }

    /**
     * @return 期初
     * @date 2019年4月15日
     */
    public final BigDecimal getQiChuQty() {
        return qiChuQty;
    }

    /**
     * @param 期初
     * @date 2019年4月15日
     */
    public final void setQiChuQty(BigDecimal qiChuQty) {
        this.qiChuQty = qiChuQty;
    }

    /**
     * @return 结存
     * @date 2019年4月15日
     */
    public final BigDecimal getJieCunQty() {
        return jieCunQty;
    }

    /**
     * @param 结存
     * @date 2019年4月15日
     */
    public final void setJieCunQty(BigDecimal jieCunQty) {
        this.jieCunQty = jieCunQty;
    }

    /**
     * @return hj存货名称
     * @date 2019年4月15日
     */
    public final String getInvtyNm() {
        return invtyNm;
    }

    /**
     * @param hj存货名称
     * @date 2019年4月15日
     */
    public final void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    /**
     * @return 仓库名称
     * @date 2019年4月15日
     */
    public final String getWhsNm() {
        return whsNm;
    }

    /**
     * @param 仓库名称
     * @date 2019年4月15日
     */
    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
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

    public String getInvtyClsNm() {
        return invtyClsNm;
    }

    public void setInvtyClsNm(String invtyClsNm) {
        this.invtyClsNm = invtyClsNm;
    }

    public BigDecimal getQiChuAmt() {
        return qiChuAmt;
    }

    public void setQiChuAmt(BigDecimal qiChuAmt) {
        this.qiChuAmt = qiChuAmt;
    }

    public BigDecimal getQiChuSum() {
        return qiChuSum;
    }

    public void setQiChuSum(BigDecimal qiChuSum) {
        this.qiChuSum = qiChuSum;
    }

    public BigDecimal getIntoQty() {
        return intoQty;
    }

    public void setIntoQty(BigDecimal intoQty) {
        this.intoQty = intoQty;
    }

    public BigDecimal getIntoAmt() {
        return intoAmt;
    }

    public void setIntoAmt(BigDecimal intoAmt) {
        this.intoAmt = intoAmt;
    }

    public BigDecimal getIntoSum() {
        return intoSum;
    }

    public void setIntoSum(BigDecimal intoSum) {
        this.intoSum = intoSum;
    }

    public BigDecimal getOutQty() {
        return outQty;
    }

    public void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    public BigDecimal getOutAmt() {
        return outAmt;
    }

    public void setOutAmt(BigDecimal outAmt) {
        this.outAmt = outAmt;
    }

    public BigDecimal getOutSum() {
        return outSum;
    }

    public void setOutSum(BigDecimal outSum) {
        this.outSum = outSum;
    }

    public BigDecimal getJieChuAmt() {
        return jieChuAmt;
    }

    public void setJieChuAmt(BigDecimal jieChuAmt) {
        this.jieChuAmt = jieChuAmt;
    }

    public BigDecimal getJieChuSum() {
        return jieChuSum;
    }

    public void setJieChuSum(BigDecimal jieChuSum) {
        this.jieChuSum = jieChuSum;
    }

    public final String getPrdcDt() {
        return prdcDt;
    }

    public final void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
    }

    public final String getBaoZhiQi() {
        return baoZhiQi;
    }

    public final void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi;
    }

    public final String getInvldtnDt() {
        return invldtnDt;
    }

    public final void setInvldtnDt(String invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public final String getBxRule() {
        return bxRule;
    }

    public final void setBxRule(String bxRule) {
        this.bxRule = bxRule;
    }

}
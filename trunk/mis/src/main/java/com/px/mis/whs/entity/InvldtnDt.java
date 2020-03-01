package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvldtnDt {
    /**
     * 仓库编码
     */
    @JsonProperty("仓库编码")

    private String whsEncd;
    /**
     * 仓库名称
     */
    @JsonProperty("仓库名称")

    private String whsNm;
    /**
     * 存货编码
     */
    @JsonProperty("存货编码")

    private String invtyEncd;

    /**
     * 存货代码
     */
    @JsonProperty("存货代码")

    private String invtyCd;

    /**
     * 存货名称
     */
    @JsonProperty("存货名称")

    private String invtyNm;
    /**
     * 批次
     */
    @JsonProperty("批次")

    private String batNum;
    /**
     * 规格型号
     */
    @JsonProperty("规格型号")

    private String spcModel;
    /**
     * 主计量单位
     */
    @JsonProperty("主计量单位名称")

    private String measrCorpNm;
    /**
     * 主计量单位
     */
    @JsonProperty("主计量单位编码")

    private String measrCorpId;

    /**
     * 结存数量
     */
    @JsonProperty("结存数量")

    private BigDecimal nowStok;

    /**
     * 生产日期
     */
    @JsonProperty("生产日期")

    private String prdcDt;
    /**
     * 保质期
     */
    @JsonProperty("保质期")

    private String baoZhiQi;
    /**
     * 失效日期
     */
    @JsonProperty("失效日期")

    private String invldtnDt;

    public final String getWhsEncd() {
        return whsEncd;
    }

    public final void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public final String getWhsNm() {
        return whsNm;
    }

    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public final String getInvtyEncd() {
        return invtyEncd;
    }

    public final void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    public final String getInvtyCd() {
        return invtyCd;
    }

    public final void setInvtyCd(String invtyCd) {
        this.invtyCd = invtyCd;
    }

    public final String getInvtyNm() {
        return invtyNm;
    }

    public final void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public final String getBatNum() {
        return batNum;
    }

    public final void setBatNum(String batNum) {
        this.batNum = batNum;
    }

    public final String getSpcModel() {
        return spcModel;
    }

    public final void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    public final String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public final void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public final String getMeasrCorpId() {
        return measrCorpId;
    }

    public final void setMeasrCorpId(String measrCorpId) {
        this.measrCorpId = measrCorpId;
    }

    public final BigDecimal getNowStok() {
        return nowStok;
    }

    public final void setNowStok(BigDecimal nowStok) {
        this.nowStok = nowStok;
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

    @Override
    public String toString() {
        return String.format(
                "InvldtnDt [whsEncd=%s, whsNm=%s, invtyEncd=%s, invtyCd=%s, invtyNm=%s, batNum=%s, spcModel=%s, measrCorpNm=%s, measrCorpId=%s, nowStok=%s, prdcDt=%s, baoZhiQi=%s, invldtnDt=%s]",
                whsEncd, whsNm, invtyEncd, invtyCd, invtyNm, batNum, spcModel, measrCorpNm, measrCorpId, nowStok,
                prdcDt, baoZhiQi, invldtnDt);
    }

}

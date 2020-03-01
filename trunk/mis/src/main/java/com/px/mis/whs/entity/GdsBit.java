package com.px.mis.whs.entity;

import java.math.BigDecimal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//货位
public class GdsBit {
    @JsonProperty("货位编码")
    private String gdsBitEncd;// 货位编码
    @JsonIgnore
    private String regnEncd;// 区域编码
    @JsonIgnore
    private String whsEncd;// 仓库编码
    @JsonProperty("货位名称")
    private String gdsBitNm;// 货位名称
    //	@JsonProperty("物流公司编号")
    @JsonProperty("货位类型编码")
    private String gdsBitTyp;// 货架类型
    // 货位类型
    @JsonProperty("货位类型名称")
    private String gdsBitTypNm;// 货位类型名称(货架、地堆)
    @JsonProperty("货位代码")
//    @JsonIgnore
    private String gdsBitCd;// 货位代码
    @JsonProperty("货位存放量")
//    @JsonIgnore
    private BigDecimal gdsBitQty;// 货位存放量
    @JsonProperty("备注")
    private String memo;// 备注
//    @JsonProperty("货位类型编码")
    @JsonIgnore
    private String gdsBitCdEncd;// 货位码编码
    @JsonProperty("创建人")
    private String setupPers;// 创建人
    @JsonProperty("创建时间")
    private String setupTm;// 创建时间
    @JsonProperty("修改人")
    private String mdfr;// 修改人
    @JsonProperty("修改时间")
    private String modiTm;// 修改时间
    @JsonIgnore
    private String gdsBitTypEncd;// 货位类型编码
    @JsonIgnore
    private List<MovBitTab> movBitList;
    @JsonIgnore
    private String whsNm;// 仓库名称
    @JsonIgnore
    private String regnNm;// 区域名称

    public final String getWhsNm() {
        return whsNm;
    }

    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public final String getRegnNm() {
        return regnNm;
    }

    public final void setRegnNm(String regnNm) {
        this.regnNm = regnNm;
    }

    /**
     * snow2019年5月31日
     *
     * @return whsEncd
     */
    public String getWhsEncd() {
        return whsEncd;
    }

    /**
     * snow2019年5月31日
     *
     * @param whsEncd 要设置的 whsEncd
     */
    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getGdsBitTypNm() {
        return gdsBitTypNm;
    }

    public void setGdsBitTypNm(String gdsBitTypNm) {
        this.gdsBitTypNm = gdsBitTypNm;
    }

    public String getGdsBitTypEncd() {
        return gdsBitTypEncd;
    }

    public void setGdsBitTypEncd(String gdsBitTypEncd) {
        this.gdsBitTypEncd = gdsBitTypEncd;
    }

    public List<MovBitTab> getMovBitList() {
        return movBitList;
    }

    public void setMovBitList(List<MovBitTab> movBitList) {
        this.movBitList = movBitList;
    }

    public BigDecimal getGdsBitQty() {
        return gdsBitQty;
    }

    public void setGdsBitQty(BigDecimal gdsBitQty) {
        this.gdsBitQty = gdsBitQty;
    }

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd == null ? null : gdsBitEncd.trim();
    }

    public String getRegnEncd() {
        return regnEncd;
    }

    public void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd == null ? null : regnEncd.trim();
    }

    public String getGdsBitNm() {
        return gdsBitNm;
    }

    public void setGdsBitNm(String gdsBitNm) {
        this.gdsBitNm = gdsBitNm == null ? null : gdsBitNm.trim();
    }

    public String getGdsBitTyp() {
        return gdsBitTyp;
    }

    public void setGdsBitTyp(String gdsBitTyp) {
        this.gdsBitTyp = gdsBitTyp == null ? null : gdsBitTyp.trim();
    }

    public String getGdsBitCd() {
        return gdsBitCd;
    }

    public void setGdsBitCd(String gdsBitCd) {
        this.gdsBitCd = gdsBitCd == null ? null : gdsBitCd.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getGdsBitCdEncd() {
        return gdsBitCdEncd;
    }

    public void setGdsBitCdEncd(String gdsBitCdEncd) {
        this.gdsBitCdEncd = gdsBitCdEncd == null ? null : gdsBitCdEncd.trim();
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers;
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
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

}
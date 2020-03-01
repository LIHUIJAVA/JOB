package com.px.mis.whs.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//区域
public class Regn {
    @JsonProperty("区域编码")
    private String regnEncd;// 区域编码
    @JsonProperty("区域名称")
    private String regnNm;// 区域名称
    @JsonIgnore
    private String whsEncd;// 仓库编码
    @JsonIgnore
    private String whsNm;// 仓库ming
    @JsonProperty("长")
    private String longs;// 长
    @JsonProperty("宽")
    private String wide;// 宽
    @JsonProperty("地堆数量")
    private BigDecimal siteQty;// 地堆数量
    @JsonProperty("备注")
    private String memo;// 备注
    @JsonProperty("创建人")
    private String setupPers;// 创建人
    @JsonProperty("创建时间")
    private String setupTm;// 创建时间
    @JsonProperty("修改人")
    private String mdfr;// 修改人
    @JsonProperty("修改时间")
    private String modiTm;// 修改时间
    @JsonIgnore
    private List<MovBitTab> movBitList;

    public final String getWhsNm() {
        return whsNm;
    }

    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public List<MovBitTab> getMovBitList() {
        return movBitList;
    }

    public void setMovBitList(List<MovBitTab> movBitList) {
        this.movBitList = movBitList;
    }

    public String getRegnEncd() {
        return regnEncd;
    }

    public void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd == null ? null : regnEncd.trim();
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd == null ? null : whsEncd.trim();
    }

    public String getRegnNm() {
        return regnNm;
    }

    public void setRegnNm(String regnNm) {
        this.regnNm = regnNm == null ? null : regnNm.trim();
    }

    public String getLongs() {
        return longs;
    }

    public void setLongs(String longs) {
        this.longs = longs == null ? null : longs.trim();
    }

    public String getWide() {
        return wide;
    }

    public void setWide(String wide) {
        this.wide = wide == null ? null : wide.trim();
    }

    public BigDecimal getSiteQty() {
        return siteQty;
    }

    public void setSiteQty(BigDecimal siteQty) {
        this.siteQty = siteQty;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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
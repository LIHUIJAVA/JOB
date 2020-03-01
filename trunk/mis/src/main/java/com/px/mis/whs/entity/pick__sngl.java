package com.px.mis.whs.entity;

import java.math.BigDecimal;
import java.util.Date;

//拣货单
public class pick__sngl {
    private String formNum;//单据号

    private String whsEncd;//仓库编码

    private String whsNm;//仓库名称

    private String invtyEncd;//存货编码

    private String barCd;//条形码

    private String batNum;//批号

    private Date prdcDt;//生产日期

    private String baoZhiQi;//保质期

    private Date invldtnDt;//失效日期

    private Date pickDt;//拣货日期

    private String gdsBit;//货位

    private String sellTyp;//销售类型

    private String bizTyp;//业务类型

    private BigDecimal qty;//数量

    private String pickPers;//拣货人

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum == null ? null : formNum.trim();
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd == null ? null : whsEncd.trim();
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm == null ? null : whsNm.trim();
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd == null ? null : invtyEncd.trim();
    }

    public String getBarCd() {
        return barCd;
    }

    public void setBarCd(String barCd) {
        this.barCd = barCd == null ? null : barCd.trim();
    }

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum == null ? null : batNum.trim();
    }

    public Date getPrdcDt() {
        return prdcDt;
    }

    public void setPrdcDt(Date prdcDt) {
        this.prdcDt = prdcDt;
    }

    public String getBaoZhiQi() {
        return baoZhiQi;
    }

    public void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi == null ? null : baoZhiQi.trim();
    }

    public Date getInvldtnDt() {
        return invldtnDt;
    }

    public void setInvldtnDt(Date invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public Date getPickDt() {
        return pickDt;
    }

    public void setPickDt(Date pickDt) {
        this.pickDt = pickDt;
    }

    public String getGdsBit() {
        return gdsBit;
    }

    public void setGdsBit(String gdsBit) {
        this.gdsBit = gdsBit == null ? null : gdsBit.trim();
    }

    public String getSellTyp() {
        return sellTyp;
    }

    public void setSellTyp(String sellTyp) {
        this.sellTyp = sellTyp == null ? null : sellTyp.trim();
    }

    public String getBizTyp() {
        return bizTyp;
    }

    public void setBizTyp(String bizTyp) {
        this.bizTyp = bizTyp == null ? null : bizTyp.trim();
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getPickPers() {
        return pickPers;
    }

    public void setPickPers(String pickPers) {
        this.pickPers = pickPers == null ? null : pickPers.trim();
    }
}
package com.px.mis.whs.entity;

import java.math.BigDecimal;
import java.util.List;

import com.px.mis.purc.entity.InvtyDoc;

//移位表
public class MovBitTab {
    private Integer id;// 编码
    private String orderNum;// 单号
    private String serialNum;// 单号的序号
    private Long movBitEncd;// 移位编码

    private String invtyEncd;// 存货编码

    private String whsEncd;// 仓库编码

    private String regnEncd;// 区域编码

    private String gdsBitEncd;// 货位编码

    private BigDecimal qty;// 数量

    private String gdsBitEncd1;// 原始货位编码

    private BigDecimal qty1;// 原始数量

    private String gdsBitEncd2;// 目标货位编码

    private BigDecimal qty2;// 移位数量

    private String batNum;// 批号

    private String prdcDt;// 生产日期

    private String intoDt;// 入库日期

    private List<InvtyDoc> iDocList;/* 存货档案 */

    // 移位表
    private WhsDoc whsDoc;// 仓库档案
    private Regn regn;// 区域
    private InvtyDoc invtyDoc;/* 存货档案 */
    private InvtyTab invtyTab;// 库存表

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum;
    }

    public String getPrdcDt() {
        return prdcDt;
    }

    public void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
    }

    public String getIntoDt() {
        return intoDt;
    }

    public void setIntoDt(String intoDt) {
        this.intoDt = intoDt;
    }

    public String getGdsBitEncd1() {
        return gdsBitEncd1;
    }

    public void setGdsBitEncd1(String gdsBitEncd1) {
        this.gdsBitEncd1 = gdsBitEncd1;
    }

    public BigDecimal getQty1() {
        return qty1;
    }

    public void setQty1(BigDecimal qty1) {
        this.qty1 = qty1;
    }

    public String getGdsBitEncd2() {
        return gdsBitEncd2;
    }

    public void setGdsBitEncd2(String gdsBitEncd2) {
        this.gdsBitEncd2 = gdsBitEncd2;
    }

    public BigDecimal getQty2() {
        return qty2;
    }

    public void setQty2(BigDecimal qty2) {
        this.qty2 = qty2;
    }

    public InvtyTab getInvtyTab() {
        return invtyTab;
    }

    public void setInvtyTab(InvtyTab invtyTab) {
        this.invtyTab = invtyTab;
    }

    public List<InvtyDoc> getiDocList() {
        return iDocList;
    }

    public void setiDocList(List<InvtyDoc> iDocList) {
        this.iDocList = iDocList;
    }

    public InvtyDoc getInvtyDoc() {
        return invtyDoc;
    }

    public void setInvtyDoc(InvtyDoc invtyDoc) {
        this.invtyDoc = invtyDoc;
    }

    public WhsDoc getWhsDoc() {
        return whsDoc;
    }

    public void setWhsDoc(WhsDoc whsDoc) {
        this.whsDoc = whsDoc;
    }

    public Regn getRegn() {
        return regn;
    }

    public void setRegn(Regn regn) {
        this.regn = regn;
    }

    public Long getMovBitEncd() {
        return movBitEncd;
    }

    public void setMovBitEncd(Long movBitEncd) {
        this.movBitEncd = movBitEncd;
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getRegnEncd() {
        return regnEncd;
    }

    public void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd;
    }

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public final Integer getId() {
        return id;
    }

    public final void setId(Integer id) {
        this.id = id;
    }

    public final String getOrderNum() {
        return orderNum;
    }

    public final void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public final String getSerialNum() {
        return serialNum;
    }

    public final void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

}

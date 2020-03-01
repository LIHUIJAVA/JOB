package com.px.mis.whs.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.SellOutWhs;

//库存表
public class InvtyTab {
    @JsonIgnore
    private Long ordrNum;// 序号
    @JsonProperty("仓库编码")
    private String whsEncd;// 仓库编码
    // 调拨：减少、增加可用量现存量别名
    @JsonIgnore
    private String tranOutWhsEncd;// 转出仓库编码
    @JsonIgnore
    private String tranInWhsEncd;// 转入仓库编码
    @JsonProperty("存货编码")
    private String invtyEncd;// 存货编
    @JsonProperty("批号")
    private String batNum;// 批号
    @JsonProperty("现存数量")
    private BigDecimal nowStok;// 现存量
    @JsonProperty("可用数量")
    private BigDecimal avalQty;// 可用量

    // 调拨的数量：别名
    @JsonIgnore
    private BigDecimal cannibQty;// 调拨数量
    @JsonIgnore
    private BigDecimal recvQty;// 实收数量
    @JsonProperty("生产日期")
    private String prdcDt;// 生产日期
    @JsonProperty("保质期")
    private String baoZhiQi;// 保质期
    @JsonProperty("失效日期")
    private String invldtnDt;// 失效日期
    @JsonProperty("含税单价")
    private BigDecimal cntnTaxUprc;// 含税单价
    @JsonProperty("未税单价")
    private BigDecimal unTaxUprc;// 未税单价
    //	@JsonProperty("存货编码")
    @JsonIgnore
    private BigDecimal bookEntryUprc;// 记账单价
    @JsonProperty("税率")
    private BigDecimal taxRate;// 税率
    @JsonProperty("含税金额")
    private BigDecimal cntnTaxAmt;// 含税金额
    @JsonProperty("未税金额")
    private BigDecimal unTaxAmt;// 未税金额
    @JsonIgnore
    private String regnEncd;// 区域编码
    @JsonIgnore
    private String gdsBitEncd;// 货位编码
    @JsonIgnore
    private String gdsBitNm;// 货位名称
    @JsonIgnore
    private List<InvtyDoc> iDocList;

    // 保质期预警
    @JsonIgnore
    private InvtyDoc invtyDoc;
    @JsonIgnore
    private WhsDoc whsDoc;

    // 出入库流水账
    @JsonIgnore
    private IntoWhs intoWhs;// 采购入
    @JsonIgnore
    private SellOutWhs sellOutWhs;// 销售出
    @JsonIgnore
    private OthOutIntoWhs outIntoWhs;// 其他出入库

    // hj
    @JsonProperty("存货名称")
    private String invtyNm; // 存货名称
    @JsonProperty("仓库名称")
    private String whsNm; // 仓库名称

    //	@JsonProperty("计量单位名称")
    @JsonIgnore
    private String measrCorpNm;// 计量单位名称
    @JsonIgnore
    private String setupTm;
    //	@JsonProperty("规格型号")
    @JsonIgnore
    private String spcModel;// 规格型号
    @JsonIgnore
    private BigDecimal bookQty;// 账面数

    // 货位移位实体
    @JsonIgnore
    private List<MovBitTab> movBitTab;
    @JsonIgnore
    private String barCd;// 盘点货位名称

    public String getBarCd() {
        return barCd;
    }

    public void setBarCd(String barCd) {
        this.barCd = barCd;
    }

    public String getGdsBitNm() {
        return gdsBitNm;
    }

    public void setGdsBitNm(String gdsBitNm) {
        this.gdsBitNm = gdsBitNm;
    }

    public final List<MovBitTab> getMovBitTab() {
        return movBitTab;
    }

    public final void setMovBitTab(List<MovBitTab> movBitTab) {
        this.movBitTab = movBitTab;
    }

    public BigDecimal getBookQty() {
        return bookQty;
    }

    public void setBookQty(BigDecimal bookQty) {
        this.bookQty = bookQty;
    }

    public String getSpcModel() {
        return spcModel;
    }

    public void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public String getInvtyNm() {
        return invtyNm;
    }

    public void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public BigDecimal getCannibQty() {
        return cannibQty;
    }

    public void setCannibQty(BigDecimal cannibQty) {
        this.cannibQty = cannibQty;
    }

    public BigDecimal getRecvQty() {
        return recvQty;
    }

    public void setRecvQty(BigDecimal recvQty) {
        this.recvQty = recvQty;
    }

    public String getTranOutWhsEncd() {
        return tranOutWhsEncd;
    }

    public void setTranOutWhsEncd(String tranOutWhsEncd) {
        this.tranOutWhsEncd = tranOutWhsEncd;
    }

    public String getTranInWhsEncd() {
        return tranInWhsEncd;
    }

    public void setTranInWhsEncd(String tranInWhsEncd) {
        this.tranInWhsEncd = tranInWhsEncd;
    }

    public OthOutIntoWhs getOutIntoWhs() {
        return outIntoWhs;
    }

    public void setOutIntoWhs(OthOutIntoWhs outIntoWhs) {
        this.outIntoWhs = outIntoWhs;
    }

    public IntoWhs getIntoWhs() {
        return intoWhs;
    }

    public void setIntoWhs(IntoWhs intoWhs) {
        this.intoWhs = intoWhs;
    }

    public SellOutWhs getSellOutWhs() {
        return sellOutWhs;
    }

    public void setSellOutWhs(SellOutWhs sellOutWhs) {
        this.sellOutWhs = sellOutWhs;
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

    public List<InvtyDoc> getiDocList() {
        return iDocList;
    }

    public void setiDocList(List<InvtyDoc> iDocList) {
        this.iDocList = iDocList;
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

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getCntnTaxAmt() {
        return cntnTaxAmt;
    }

    public void setCntnTaxAmt(BigDecimal cntnTaxAmt) {
        this.cntnTaxAmt = cntnTaxAmt;
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

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd == null ? null : invtyEncd.trim();
    }

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum == null ? null : batNum.trim();
    }

    public BigDecimal getNowStok() {
        return nowStok;
    }

    public void setNowStok(BigDecimal nowStok) {
        this.nowStok = nowStok;
    }

    public BigDecimal getAvalQty() {
        return avalQty;
    }

    public void setAvalQty(BigDecimal avalQty) {
        this.avalQty = avalQty;
    }

    public String getBaoZhiQi() {
        return baoZhiQi;
    }

    public void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi;
    }

    public String getPrdcDt() {
        return prdcDt;
    }

    public void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
    }

    public String getInvldtnDt() {
        return invldtnDt;
    }

    public void setInvldtnDt(String invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public BigDecimal getUnTaxAmt() {
        return unTaxAmt;
    }

    public void setUnTaxAmt(BigDecimal unTaxAmt) {
        this.unTaxAmt = unTaxAmt;
    }

}
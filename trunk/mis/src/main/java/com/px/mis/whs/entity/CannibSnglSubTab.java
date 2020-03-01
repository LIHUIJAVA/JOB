package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.px.mis.purc.entity.InvtyDoc;

//调拨单子表
public class CannibSnglSubTab {
    private Long ordrNum;// 序号

    private String formNum;// 单据号

    private String invtyId;// 存货编号
    private String invtyEncd;// 计量单位名称

    private BigDecimal cannibQty;// 调拨数量

    private BigDecimal recvQty;// 实收数量

    private String batNum;// 批号

    private String invldtnDt;// 失效日期

    private String baoZhiQi;// 保质期

    private String prdcDt;// 生产日期

    private BigDecimal taxRate;// 税率

    private BigDecimal cntnTaxUprc;// 含税单价

    private BigDecimal unTaxUprc;// 未税单价

    private BigDecimal cntnTaxAmt;// 含税金额

    private BigDecimal unTaxAmt;// 未税金额

    // 存货档案
    private InvtyDoc invtyDoc;

    // 联查存货档案字段、计量单位名称等
    private String invtyNm;// 存货名称
    private String spcModel;// 规格型号
    private BigDecimal bxRule;// 箱规
    private BigDecimal bxQty;// 箱数
    private String measrCorpNm;// 计量单位名称

    // 联查存货档案字段、计量单位名称、仓库名称
//	private String invtyNm;// 存货名称
//	private String spcModel;// 规格型号
    private String invtyCd;// 存货代码
    //	private BigDecimal bxRule;// 箱规
    private String baoZhiQiDt;// 保质期天数
    //	private BigDecimal iptaxRate;// 进项税率
//	private BigDecimal optaxRate;// 销项税率
//	private BigDecimal highestPurPrice;// 最高进价
//	private BigDecimal loSellPrc;// 最低售价
//	private BigDecimal refCost;// 参考成本
//	private BigDecimal refSellPrc;// 参考售价
//	private BigDecimal ltstCost;// 最新成本
//	private String measrCorpNm;// 计量单位名称
//	private String whsNm;// 仓库名称
    private String crspdBarCd;// 对应条形码
    private String projEncd;// 项目编码
    private String projNm;// 项目名称
    private BigDecimal taxAmt;//税额

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
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

    /**
     * @return invtyDoc
     * @date 2019年4月15日
     */
    public final InvtyDoc getInvtyDoc() {
        return invtyDoc;
    }

    /**
     * @param invtyDoc
     * @date 2019年4月15日
     */
    public final void setInvtyDoc(InvtyDoc invtyDoc) {
        this.invtyDoc = invtyDoc;
    }

    /**
     * @return invtyCd
     * @date 2019年4月15日
     */
    public final String getInvtyCd() {
        return invtyCd;
    }

    /**
     * @param invtyCd
     * @date 2019年4月15日
     */
    public final void setInvtyCd(String invtyCd) {
        this.invtyCd = invtyCd;
    }

    /**
     * @return baoZhiQiDt
     * @date 2019年4月15日
     */
    public final String getBaoZhiQiDt() {
        return baoZhiQiDt;
    }

    /**
     * @param baoZhiQiDt
     * @date 2019年4月15日
     */
    public final void setBaoZhiQiDt(String baoZhiQiDt) {
        this.baoZhiQiDt = baoZhiQiDt;
    }

    /**
     * @return crspdBarCd
     * @date 2019年4月15日
     */
    public final String getCrspdBarCd() {
        return crspdBarCd;
    }

    /**
     * @param crspdBarCd
     * @date 2019年4月15日
     */
    public final void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
    }

    public BigDecimal getBxQty() {
        return bxQty;
    }

    public void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
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

    public BigDecimal getBxRule() {
        return bxRule;
    }

    public void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
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

    public String getInvtyId() {
        return invtyId;
    }

    public void setInvtyId(String invtyId) {
        this.invtyId = invtyId == null ? null : invtyId.trim();
    }

    public BigDecimal getCannibQty() {
        return cannibQty;
    }

    public void setCannibQty(BigDecimal cannibQty) {
        this.cannibQty = cannibQty;
    }

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum == null ? null : batNum.trim();
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

    public String getPrdcDt() {
        return prdcDt;
    }

    public void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
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

    public BigDecimal getRecvQty() {
        return recvQty;
    }

    public void setRecvQty(BigDecimal recvQty) {
        this.recvQty = recvQty;
    }

}
package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 出入库流水报表
 */
public class OutIngWaterReport {
    /**
     * 单号 其他出入
     */
    @JsonProperty("单据号")
    private String formNum;
    /**
     * 日期
     */
    @JsonProperty("单据日期")
    private String formDt;
    /**
     * 备注
     */
    @JsonProperty("备注")
    private String memo;
    /**
     * 创建人
     */
    @JsonProperty("创建人")
    private String setupPers;
    /**
     * 审核人
     */
    @JsonProperty("审核人")
    private String chkr;
    /**
     * 审核时间
     */
    @JsonProperty("审核时间")
    private String chkTm;
    /**
     * 批号
     */
    @JsonProperty("批号")
    private String batNum;
    /**
     * 数量
     */
//	@JsonProperty("数量")
    @JsonIgnore
    private BigDecimal qty;
    /**
     * 无税单价<!-- 无税单价 -->;
     */
//	@JsonProperty("无税单价 ")
    @JsonIgnore
    private BigDecimal noTaxUprc;
    /**
     * 数量
     */
    @JsonProperty("入库数量")
    private BigDecimal intoQty;
    /**
     * 无税单价<!-- 无税单价 -->;
     */
    @JsonProperty("入库无税单价")
    private BigDecimal intoNoTaxUprc;
    /**
     * 数量
     */
    @JsonProperty("出库数量")
    private BigDecimal outQty;
    /**
     * 无税单价<!-- 无税单价 -->;
     */
    @JsonProperty("出库无税单价")
    private BigDecimal outNoTaxUprc;
    /**
     * 出入库类型名称
     */
    @JsonProperty("出入库类型名称")
    private String outIntoWhsTypNm;
    /**
     * 仓库名称
     */
    @JsonProperty("仓库名称")
    private String whsNm;
    /**
     * 收发类别名称
     */
    @JsonProperty("收发类别名称")
    private String recvSendCateNm;
    /**
     * 供应商 //客户名称<!-- 客户名称 -->
     */
    @JsonProperty("供应商客户名称")
    private String custNm;
    /**
     * 存货编码
     */
    @JsonProperty("存货编码")
    private String invtyEncd;
    /**
     * 存货名称
     */
    @JsonProperty("存货名称")
    private String invtyNm;
    /**
     * 规格型号
     */
    @JsonProperty("规格型号")
    private String spcModel;
    /**
     * <!-- 计量单位名称 -->
     */
    @JsonProperty("计量单位名称")
    private String measrCorpNm;
    /**
     * 出入库类型id
     */
    @JsonProperty("出入库类型编码")
    private String outIntoWhsTypEncd;

    public final String getOutIntoWhsTypEncd() {
        return outIntoWhsTypEncd;
    }

    public final void setOutIntoWhsTypEncd(String outIntoWhsTypEncd) {
        this.outIntoWhsTypEncd = outIntoWhsTypEncd;
    }

    /**
     * @return 单号
     * @date 2019年4月15日
     */
    public final String getFormNum() {
        return formNum;
    }

    /**
     * @param 单号
     * @date 2019年4月15日
     */
    public final void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    /**
     * @return 日期
     * @date 2019年4月15日
     */
    public final String getFormDt() {
        return formDt;
    }

    /**
     * @param 日期
     * @date 2019年4月15日
     */
    public final void setFormDt(String formDt) {
        this.formDt = formDt;
    }

    /**
     * @return 备注
     * @date 2019年4月15日
     */
    public final String getMemo() {
        return memo;
    }

    /**
     * @param 备注
     * @date 2019年4月15日
     */
    public final void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @return 创建人
     * @date 2019年4月15日
     */
    public final String getSetupPers() {
        return setupPers;
    }

    /**
     * @param 创建人
     * @date 2019年4月15日
     */
    public final void setSetupPers(String setupPers) {
        this.setupPers = setupPers;
    }

    /**
     * @return 审核人
     * @date 2019年4月15日
     */
    public final String getChkr() {
        return chkr;
    }

    /**
     * @param 审核人
     * @date 2019年4月15日
     */
    public final void setChkr(String chkr) {
        this.chkr = chkr;
    }

    /**
     * @return 审核时间
     * @date 2019年4月15日
     */
    public final String getChkTm() {
        return chkTm;
    }

    /**
     * @param 审核时间
     * @date 2019年4月15日
     */
    public final void setChkTm(String chkTm) {
        this.chkTm = chkTm;
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
     * @return 数量
     * @date 2019年4月15日
     */
    public final BigDecimal getQty() {
        return qty;
    }

    /**
     * @param 数量
     * @date 2019年4月15日
     */
    public final void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * @return 无税单价<! - - 无税单价-->;
     * @date 2019年4月15日
     */
    public final BigDecimal getNoTaxUprc() {
        return noTaxUprc;
    }

    /**
     * @param 无税单价<!--无税单价-->;
     * @date 2019年4月15日
     */
    public final void setNoTaxUprc(BigDecimal noTaxUprc) {
        this.noTaxUprc = noTaxUprc;
    }

    /**
     * @return 数量
     * @date 2019年4月15日
     */
    public final BigDecimal getIntoQty() {
        return intoQty;
    }

    /**
     * @param 数量
     * @date 2019年4月15日
     */
    public final void setIntoQty(BigDecimal intoQty) {
        this.intoQty = intoQty;
    }

    /**
     * @return 无税单价<! - - 无税单价-->;
     * @date 2019年4月15日
     */
    public final BigDecimal getIntoNoTaxUprc() {
        return intoNoTaxUprc;
    }

    /**
     * @param 无税单价<!--无税单价-->;
     * @date 2019年4月15日
     */
    public final void setIntoNoTaxUprc(BigDecimal intoNoTaxUprc) {
        this.intoNoTaxUprc = intoNoTaxUprc;
    }

    /**
     * @return 数量
     * @date 2019年4月15日
     */
    public final BigDecimal getOutQty() {
        return outQty;
    }

    /**
     * @param 数量
     * @date 2019年4月15日
     */
    public final void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    /**
     * @return 无税单价<! - - 无税单价-->;
     * @date 2019年4月15日
     */
    public final BigDecimal getOutNoTaxUprc() {
        return outNoTaxUprc;
    }

    /**
     * @param 无税单价<!--无税单价-->;
     * @date 2019年4月15日
     */
    public final void setOutNoTaxUprc(BigDecimal outNoTaxUprc) {
        this.outNoTaxUprc = outNoTaxUprc;
    }

    /**
     * @return 出入库类型名称
     * @date 2019年4月15日
     */
    public final String getOutIntoWhsTypNm() {
        return outIntoWhsTypNm;
    }

    /**
     * @param 出入库类型名称
     * @date 2019年4月15日
     */
    public final void setOutIntoWhsTypNm(String outIntoWhsTypNm) {
        this.outIntoWhsTypNm = outIntoWhsTypNm;
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

    /**
     * @return 收发类别名称
     * @date 2019年4月15日
     */
    public final String getRecvSendCateNm() {
        return recvSendCateNm;
    }

    /**
     * @param 收发类别名称
     * @date 2019年4月15日
     */
    public final void setRecvSendCateNm(String recvSendCateNm) {
        this.recvSendCateNm = recvSendCateNm;
    }

    /**
     * @return 供应商客户名称<! - - 客户名称-->
     * @date 2019年4月15日
     */
    public final String getCustNm() {
        return custNm;
    }

    /**
     * @param 供应商客户名称<!--客户名称-->
     * @date 2019年4月15日
     */
    public final void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    /**
     * @return 存货编码
     * @date 2019年4月15日
     */
    public final String getInvtyEncd() {
        return invtyEncd;
    }

    /**
     * @param 存货编码
     * @date 2019年4月15日
     */
    public final void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    /**
     * @return 存货名称
     * @date 2019年4月15日
     */
    public final String getInvtyNm() {
        return invtyNm;
    }

    /**
     * @param 存货名称
     * @date 2019年4月15日
     */
    public final void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    /**
     * @return 规格型号
     * @date 2019年4月15日
     */
    public final String getSpcModel() {
        return spcModel;
    }

    /**
     * @param 规格型号
     * @date 2019年4月15日
     */
    public final void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    /**
     * @return <!--计量单位名称-->
     * @date 2019年4月15日
     */
    public final String getMeasrCorpNm() {
        return measrCorpNm;
    }

    /**
     * @param <!--计量单位名称-->
     * @date 2019年4月15日
     */
    public final void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

}
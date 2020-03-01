package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//库存台账
public class InvtyStandReport {
    // 单号
    @JsonProperty("单据号")
    private String formNum;
    // 审核时间
    @JsonProperty("审核日期")
    private String chkTm;
    // 单据日期
    @JsonProperty("单据日期")
    private String formDt;
    // 仓库编码
    @JsonProperty("仓库编码")
    private String whsEncd;
    // 仓库
    @JsonProperty("仓库名称")
    private String whsNm;
    //	存货编码
    @JsonProperty("存货编码")
    private String invtyEncd;
    //	 存货名称
    @JsonProperty("存货名称")
    private String invtyNm;
    // 单据类型
    @JsonProperty("单据类型")
    private String formType;
    // 单据类型
    @JsonProperty("单据类型编码")
    private String outIntoWhsTypId;
    // 收入数量
    @JsonProperty("收入数量")
    private BigDecimal intoQty;
    // 发出数量
    @JsonProperty("发出数量")
    private BigDecimal outQty;
    // 结存数量
    @JsonProperty("结存数量")
    private BigDecimal balance;

    // 数量
    @JsonIgnore
    private BigDecimal qty;

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getOutIntoWhsTypId() {
        return outIntoWhsTypId;
    }

    public void setOutIntoWhsTypId(String outIntoWhsTypId) {
        this.outIntoWhsTypId = outIntoWhsTypId;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public String getFormDt() {
        return formDt;
    }

    public void setFormDt(String formDt) {
        this.formDt = formDt;
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public BigDecimal getIntoQty() {
        return intoQty;
    }

    public void setIntoQty(BigDecimal intoQty) {
        this.intoQty = intoQty;
    }

    public BigDecimal getOutQty() {
        return outQty;
    }

    public void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

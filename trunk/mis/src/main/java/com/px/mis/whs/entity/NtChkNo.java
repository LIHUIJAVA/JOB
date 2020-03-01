package com.px.mis.whs.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//未审单据
public class NtChkNo {
    @JsonProperty("单据号")
    private String formNum;// 单据号
    @JsonProperty("单据类型编码")
    private String formTypEncd;// 单据类型编码
    @JsonProperty("单据类型名称")
    private String formTypName;// 单据类型名称
    @JsonProperty("单据日期")
    private String formDt;// 单据日期
    @JsonProperty("创建人")
    private String setupPers;// 创建人
    @JsonProperty("创建时间")
    private String setupTm;// 创建时间

    public final String getFormDt() {
        return formDt;
    }

    public final void setFormDt(String formDt) {
        this.formDt = formDt;
    }

    public final String getSetupPers() {
        return setupPers;
    }

    public final void setSetupPers(String setupPers) {
        this.setupPers = setupPers;
    }

    public final String getSetupTm() {
        return setupTm;
    }

    public final void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public final String getFormNum() {
        return formNum;
    }

    public final void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public final String getFormTypEncd() {
        return formTypEncd;
    }

    public final void setFormTypEncd(String formTypEncd) {
        this.formTypEncd = formTypEncd;
    }

    public final String getFormTypName() {
        return formTypName;
    }

    public final void setFormTypName(String formTypName) {
        this.formTypName = formTypName;
    }

    @Override
    public String toString() {
        return String.format(
                "NtChkNo [formNum=%s, formTypEncd=%s, formTypName=%s, formDt=%s, setupPers=%s, setupTm=%s]", formNum,
                formTypEncd, formTypName, formDt, setupPers, setupTm);
    }

}
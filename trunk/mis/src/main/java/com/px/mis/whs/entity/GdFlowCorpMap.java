package com.px.mis.whs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//物流公司
public class GdFlowCorpMap {
//	private String gdFlowEncd;// 物流公司编号
//
//	private String gdFlowNm;// 物流公司名称
//
//	private String traffMode;// 运输方式
//
//	private String traffVehic;// 运输车辆
//
//	private String traffFormNum;// 运输单号
//
//	private Integer printCnt;// 打印次数
//
//	private String memo;// 备注
//
//	private Integer isNtChk;// 是否审核
//
//	private String setupPers;// 创建人
//
//	private String setupTm;// 创建时间
//
//	private String mdfr;// 修改人
//
//	private String modiTm;// 修改时间
//
//	private String chkr;// 审核人
//
//	private String chkTm;// 审核时间

    @JsonProperty("物流公司编码")
    private String gdFlowEncd;// 物流公司编号
    @JsonProperty("物流公司名称")
    private String gdFlowNm;// 物流公司名称
    @JsonProperty("运输方式")
    private String traffMode;// 运输方式
    @JsonProperty("运输车辆")
    private String traffVehic;// 运输车辆
    //	@JsonProperty("运输单号")
    @JsonIgnore
    private String traffFormNum;// 运输单号
    //	@JsonProperty("打印次数")
    @JsonIgnore
    private Integer printCnt;// 打印次数
    @JsonProperty("备注")
    private String memo;// 备注
    //	@JsonProperty("是否审核")
    @JsonIgnore
    private Integer isNtChk;// 是否审核
    @JsonProperty("创建人")
    private String setupPers;// 创建人
    @JsonProperty("创建时间")
    private String setupTm;// 创建时间
    @JsonProperty("修改人")
    private String mdfr;// 修改人
    @JsonProperty("修改时间")
    private String modiTm;// 修改时间
    //	@JsonProperty("审核人")
    @JsonIgnore
    private String chkr;// 审核人
    //	@JsonProperty("审核时间")
    @JsonIgnore
    private String chkTm;// 审核时间

    public Integer getPrintCnt() {
        return printCnt;
    }

    public void setPrintCnt(Integer printCnt) {
        this.printCnt = printCnt;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getIsNtChk() {
        return isNtChk;
    }

    public void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public String getGdFlowEncd() {
        return gdFlowEncd;
    }

    public void setGdFlowEncd(String gdFlowEncd) {
        this.gdFlowEncd = gdFlowEncd == null ? null : gdFlowEncd.trim();
    }

    public String getGdFlowNm() {
        return gdFlowNm;
    }

    public void setGdFlowNm(String gdFlowNm) {
        this.gdFlowNm = gdFlowNm == null ? null : gdFlowNm.trim();
    }

    public String getTraffMode() {
        return traffMode;
    }

    public void setTraffMode(String traffMode) {
        this.traffMode = traffMode == null ? null : traffMode.trim();
    }

    public String getTraffVehic() {
        return traffVehic;
    }

    public void setTraffVehic(String traffVehic) {
        this.traffVehic = traffVehic == null ? null : traffVehic.trim();
    }

    public String getTraffFormNum() {
        return traffFormNum;
    }

    public void setTraffFormNum(String traffFormNum) {
        this.traffFormNum = traffFormNum == null ? null : traffFormNum.trim();
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers == null ? null : setupPers.trim();
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr;
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

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

}
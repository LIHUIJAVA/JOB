package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//调拨单
public class CannibSnglMap {
//	private String formNum;// 单据号
//	private String formDt;// 单据日期
//	private String cannibDt;// 调拨日期
//	private String tranOutWhsEncd;// 转出仓库编码
//	private String tranInWhsEncd;// 转入仓库编码
//	private String tranOutWhsEncdName;// 转出仓库名称
//	private String tranInWhsEncdName;// 转入仓库名称
//	private String tranOutDeptEncd;// 转出部门编码
//	private String tranInDeptEncd;// 转入部门编码
//	private String cannibStatus;// 调拨状态
//	private String memo;// 备注
//	private Integer isNtWms;// 是否向WMS上传
//	private Integer isNtChk;// 是否审核
//	private Integer isNtCmplt;// 是否完成
//	private Integer isNtClos;// 是否关闭
//	private Integer printCnt;// 打印次数
//	private String setupPers;// 创建人
//	private String setupTm;// 创建时间
//	private String mdfr;// 修改人
//	private String modiTm;// 修改时间
//	private String chkr;// 审核人
//	private String chkTm;// 审核时间
//	private String formTypEncd;// 单据类型编码
//	private String formTypName;// 单据类型名称
//	private String tranOutDeptNm;// 转出部门名称
//	private String tranInDeptNm;// 转入部门名称
//
////子
//
//	private Long ordrNum;// 序号
//	private String invtyId;// 存货编号
//	private BigDecimal cannibQty;// 调拨数量
//	private BigDecimal recvQty;// 实收数量
//	private String batNum;// 批号
//	private String invldtnDt;// 失效日期
//	private String baoZhiQi;// 保质期
//	private String prdcDt;// 生产日期
//	private BigDecimal taxRate;// 税率
//	private BigDecimal cntnTaxUprc;// 含税单价
//	private BigDecimal unTaxUprc;// 未税单价
//	private BigDecimal cntnTaxAmt;// 含税金额
//	private BigDecimal unTaxAmt;// 未税金额
//	// 联查存货档案字段、计量单位名称等
//	private String invtyNm;// 存货名称
//	private String spcModel;// 规格型号
//	private BigDecimal bxRule;// 箱规
//	private BigDecimal bxQty;// 箱数
//	private String measrCorpNm;// 计量单位名称
//	private String invtyCd;// 存货代码
//	// 联查存货档案字段、计量单位名称、仓库名称
////	private String invtyNm;// 存货名称
////	private String spcModel;// 规格型号
////	private BigDecimal bxRule;// 箱规
////	private String measrCorpNm;// 计量单位名称
//	private String crspdBarCd;// 对应条形码
//	

    @JsonProperty("单据号")
    private String formNum;// 单据号
    @JsonProperty("单据日期")
    private String formDt;// 单据日期
//    @JsonProperty("调拨日期")
    @JsonIgnore
    private String cannibDt;// 调拨日期
    @JsonProperty("转出仓库编码")
    private String tranOutWhsEncd;// 转出仓库编码
    @JsonProperty("转入仓库编码")
    private String tranInWhsEncd;// 转入仓库编码
    @JsonProperty("转出仓库名称")
    private String tranOutWhsEncdName;// 转出仓库名称
    @JsonProperty("转入仓库名称")
    private String tranInWhsEncdName;// 转入仓库名称
//    @JsonProperty("转出部门编码")
    @JsonIgnore
    private String tranOutDeptEncd;// 转出部门编码
//    @JsonProperty("转入部门编码")
    @JsonIgnore
    private String tranInDeptEncd;// 转入部门编码
//    @JsonProperty("调拨状态")
    @JsonIgnore
    private String cannibStatus;// 调拨状态
    @JsonProperty("备注")
    private String memo;// 备注
    //	@JsonProperty("是否向WMS上传")
    @JsonIgnore
    private Integer isNtWms;// 是否向WMS上传
    @JsonProperty("是否审核")
    private Integer isNtChk;// 是否审核
    //	@JsonProperty("是否完成")
    @JsonIgnore
    private Integer isNtCmplt;// 是否完成
    //	@JsonProperty("是否关闭")
    @JsonIgnore
    private Integer isNtClos;// 是否关闭
    //	@JsonProperty("打印次数")
    @JsonIgnore
    private Integer printCnt;// 打印次数
    @JsonProperty("创建人")
    private String setupPers;// 创建人
    @JsonProperty("创建时间")
    private String setupTm;// 创建时间
    @JsonProperty("修改人")
    private String mdfr;// 修改人
    @JsonProperty("修改时间")
    private String modiTm;// 修改时间
    @JsonProperty("审核人")
    private String chkr;// 审核人
    @JsonProperty("审核时间")
    private String chkTm;// 审核时间
    @JsonProperty("单据类型编码")
    private String formTypEncd;// 单据类型编码
    @JsonProperty("单据类型名称")
    private String formTypName;// 单据类型名称
//    @JsonProperty("转出部门名称")
    @JsonIgnore
    private String tranOutDeptNm;// 转出部门名称
//    @JsonProperty("转入部门名称")
    @JsonIgnore
    private String tranInDeptNm;// 转入部门名称
    //	@JsonProperty("序号")
    @JsonIgnore
    private Long ordrNum;// 序号
    @JsonProperty("存货编码")
    private String invtyId;// 存货编号
    @JsonProperty("调拨数量")
    private BigDecimal cannibQty;// 调拨数量
    //	@JsonProperty("实收数量")
    @JsonIgnore
    private BigDecimal recvQty;// 实收数量
    @JsonProperty("批号")
    private String batNum;// 批号
    @JsonProperty("失效日期")
    private String invldtnDt;// 失效日期
    @JsonProperty("保质期")
    private String baoZhiQi;// 保质期
    @JsonProperty("生产日期")
    private String prdcDt;// 生产日期
    @JsonProperty("税率")
    private BigDecimal taxRate;// 税率
    @JsonProperty("含税单价")
    private BigDecimal cntnTaxUprc;// 含税单价
    @JsonProperty("未税单价")
    private BigDecimal unTaxUprc;// 未税单价
    @JsonProperty("含税金额")
    private BigDecimal cntnTaxAmt;// 含税金额
    @JsonProperty("未税金额")
    private BigDecimal unTaxAmt;// 未税金额
    @JsonProperty("税额")
    private BigDecimal taxAmt;//税额
    @JsonProperty("存货名称")
    private String invtyNm;// 存货名称
    @JsonProperty("规格型号")
    private String spcModel;// 规格型号
    @JsonProperty("箱规")
    private BigDecimal bxRule;// 箱规
    @JsonProperty("箱数")
    private BigDecimal bxQty;// 箱数
    @JsonProperty("计量单位名称")
    private String measrCorpNm;// 计量单位名称
    @JsonProperty("存货代码")
    private String invtyCd;// 存货代码
    @JsonProperty("对应条形码")
    private String crspdBarCd;// 对应条形码
    @JsonProperty("项目编码")
    private String projEncd;// 项目编码

    @JsonProperty("项目名称")
    private String projNm;// 项目名称

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getProjEncd() {
        return projEncd;
    }

    public void setProjEncd(String projEncd) {
        this.projEncd = projEncd;
    }

    public String getProjNm() {
        return projNm;
    }

    public void setProjNm(String projNm) {
        this.projNm = projNm;
    }


    public final String getTranOutDeptNm() {
        return tranOutDeptNm;
    }

    public final void setTranOutDeptNm(String tranOutDeptNm) {
        this.tranOutDeptNm = tranOutDeptNm;
    }

    public final String getTranInDeptNm() {
        return tranInDeptNm;
    }

    public final void setTranInDeptNm(String tranInDeptNm) {
        this.tranInDeptNm = tranInDeptNm;
    }

    public final String getFormNum() {
        return formNum;
    }

    public final void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public final String getFormDt() {
        return formDt;
    }

    public final void setFormDt(String formDt) {
        this.formDt = formDt;
    }

    public final String getCannibDt() {
        return cannibDt;
    }

    public final void setCannibDt(String cannibDt) {
        this.cannibDt = cannibDt;
    }

    public final String getTranOutWhsEncd() {
        return tranOutWhsEncd;
    }

    public final void setTranOutWhsEncd(String tranOutWhsEncd) {
        this.tranOutWhsEncd = tranOutWhsEncd;
    }

    public final String getTranInWhsEncd() {
        return tranInWhsEncd;
    }

    public final void setTranInWhsEncd(String tranInWhsEncd) {
        this.tranInWhsEncd = tranInWhsEncd;
    }

    public final String getTranOutWhsEncdName() {
        return tranOutWhsEncdName;
    }

    public final void setTranOutWhsEncdName(String tranOutWhsEncdName) {
        this.tranOutWhsEncdName = tranOutWhsEncdName;
    }

    public final String getTranInWhsEncdName() {
        return tranInWhsEncdName;
    }

    public final void setTranInWhsEncdName(String tranInWhsEncdName) {
        this.tranInWhsEncdName = tranInWhsEncdName;
    }

    public final String getTranOutDeptEncd() {
        return tranOutDeptEncd;
    }

    public final void setTranOutDeptEncd(String tranOutDeptEncd) {
        this.tranOutDeptEncd = tranOutDeptEncd;
    }

    public final String getTranInDeptEncd() {
        return tranInDeptEncd;
    }

    public final void setTranInDeptEncd(String tranInDeptEncd) {
        this.tranInDeptEncd = tranInDeptEncd;
    }

    public final String getCannibStatus() {
        return cannibStatus;
    }

    public final void setCannibStatus(String cannibStatus) {
        this.cannibStatus = cannibStatus;
    }

    public final String getMemo() {
        return memo;
    }

    public final void setMemo(String memo) {
        this.memo = memo;
    }

    public final Integer getIsNtWms() {
        return isNtWms;
    }

    public final void setIsNtWms(Integer isNtWms) {
        this.isNtWms = isNtWms;
    }

    public final Integer getIsNtChk() {
        return isNtChk;
    }

    public final void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public final Integer getIsNtCmplt() {
        return isNtCmplt;
    }

    public final void setIsNtCmplt(Integer isNtCmplt) {
        this.isNtCmplt = isNtCmplt;
    }

    public final Integer getIsNtClos() {
        return isNtClos;
    }

    public final void setIsNtClos(Integer isNtClos) {
        this.isNtClos = isNtClos;
    }

    public final Integer getPrintCnt() {
        return printCnt;
    }

    public final void setPrintCnt(Integer printCnt) {
        this.printCnt = printCnt;
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

    public final String getMdfr() {
        return mdfr;
    }

    public final void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public final String getModiTm() {
        return modiTm;
    }

    public final void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }

    public final String getChkr() {
        return chkr;
    }

    public final void setChkr(String chkr) {
        this.chkr = chkr;
    }

    public final String getChkTm() {
        return chkTm;
    }

    public final void setChkTm(String chkTm) {
        this.chkTm = chkTm;
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

    public final Long getOrdrNum() {
        return ordrNum;
    }

    public final void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public final String getInvtyId() {
        return invtyId;
    }

    public final void setInvtyId(String invtyId) {
        this.invtyId = invtyId;
    }

    public final BigDecimal getCannibQty() {
        return cannibQty;
    }

    public final void setCannibQty(BigDecimal cannibQty) {
        this.cannibQty = cannibQty;
    }

    public final BigDecimal getRecvQty() {
        return recvQty;
    }

    public final void setRecvQty(BigDecimal recvQty) {
        this.recvQty = recvQty;
    }

    public final String getBatNum() {
        return batNum;
    }

    public final void setBatNum(String batNum) {
        this.batNum = batNum;
    }

    public final String getInvldtnDt() {
        return invldtnDt;
    }

    public final void setInvldtnDt(String invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public final String getBaoZhiQi() {
        return baoZhiQi;
    }

    public final void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi;
    }

    public final String getPrdcDt() {
        return prdcDt;
    }

    public final void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
    }

    public final BigDecimal getTaxRate() {
        return taxRate;
    }

    public final void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public final BigDecimal getCntnTaxUprc() {
        return cntnTaxUprc;
    }

    public final void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
        this.cntnTaxUprc = cntnTaxUprc;
    }

    public final BigDecimal getUnTaxUprc() {
        return unTaxUprc;
    }

    public final void setUnTaxUprc(BigDecimal unTaxUprc) {
        this.unTaxUprc = unTaxUprc;
    }

    public final BigDecimal getCntnTaxAmt() {
        return cntnTaxAmt;
    }

    public final void setCntnTaxAmt(BigDecimal cntnTaxAmt) {
        this.cntnTaxAmt = cntnTaxAmt;
    }

    public final BigDecimal getUnTaxAmt() {
        return unTaxAmt;
    }

    public final void setUnTaxAmt(BigDecimal unTaxAmt) {
        this.unTaxAmt = unTaxAmt;
    }

    public final String getInvtyNm() {
        return invtyNm;
    }

    public final void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public final String getSpcModel() {
        return spcModel;
    }

    public final void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    public final BigDecimal getBxRule() {
        return bxRule;
    }

    public final void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public final BigDecimal getBxQty() {
        return bxQty;
    }

    public final void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
    }

    public final String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public final void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public final String getInvtyCd() {
        return invtyCd;
    }

    public final void setInvtyCd(String invtyCd) {
        this.invtyCd = invtyCd;
    }

    public final String getCrspdBarCd() {
        return crspdBarCd;
    }

    public final void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
    }

    @Override
    public String toString() {
        return String.format(
                "CannibSnglMap [formNum=%s, formDt=%s, cannibDt=%s, tranOutWhsEncd=%s, tranInWhsEncd=%s, tranOutWhsEncdName=%s, tranInWhsEncdName=%s, tranOutDeptEncd=%s, tranInDeptEncd=%s, cannibStatus=%s, memo=%s, isNtWms=%s, isNtChk=%s, isNtCmplt=%s, isNtClos=%s, printCnt=%s, setupPers=%s, setupTm=%s, mdfr=%s, modiTm=%s, chkr=%s, chkTm=%s, formTypEncd=%s, formTypName=%s, ordrNum=%s, invtyId=%s, cannibQty=%s, recvQty=%s, batNum=%s, invldtnDt=%s, baoZhiQi=%s, prdcDt=%s, taxRate=%s, cntnTaxUprc=%s, unTaxUprc=%s, cntnTaxAmt=%s, unTaxAmt=%s, invtyNm=%s, spcModel=%s, bxRule=%s, bxQty=%s, measrCorpNm=%s, invtyCd=%s, crspdBarCd=%s]",
                formNum, formDt, cannibDt, tranOutWhsEncd, tranInWhsEncd, tranOutWhsEncdName, tranInWhsEncdName,
                tranOutDeptEncd, tranInDeptEncd, cannibStatus, memo, isNtWms, isNtChk, isNtCmplt, isNtClos, printCnt,
                setupPers, setupTm, mdfr, modiTm, chkr, chkTm, formTypEncd, formTypName, ordrNum, invtyId, cannibQty,
                recvQty, batNum, invldtnDt, baoZhiQi, prdcDt, taxRate, cntnTaxUprc, unTaxUprc, cntnTaxAmt, unTaxAmt,
                invtyNm, spcModel, bxRule, bxQty, measrCorpNm, invtyCd, crspdBarCd);
    }

}
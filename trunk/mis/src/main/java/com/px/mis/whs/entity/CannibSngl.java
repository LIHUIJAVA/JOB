package com.px.mis.whs.entity;

import java.util.List;

//调拨单
public class CannibSngl {
    private String formNum;// 单据号
    private Long ordrNum;// 序号
    private String formDt;// 单据日期
    private String cannibDt;// 调拨日期
    private String cannibDt1;// 调拨日期 临时值
    private String cannibDt2;// 调拨日期 临时值
    private String tranOutWhsEncd;// 转出仓库编码
    private String tranInWhsEncd;// 转入仓库编码
    private String tranOutWhsEncdName;// 转出仓库名称
    private String tranInWhsEncdName;// 转入仓库名称
    private String tranOutDeptEncd;// 转出部门编码
    private String tranInDeptEncd;// 转入部门编码
    private String cannibStatus;// 调拨状态
    private String memo;// 备注
    private Integer isNtWms;// 是否向WMS上传
    private Integer isNtChk;// 是否审核
    private Integer isNtCmplt;// 是否完成
    private Integer isNtClos;// 是否关闭
    private Integer printCnt;// 打印次数
    private String setupPers;// 创建人
    private String setupTm;// 创建时间
    private String mdfr;// 修改人
    private String modiTm;// 修改时间
    private String chkr;// 审核人
    private String chkTm;// 审核时间
    private List<CannibSnglSubTab> checkSnglList;
    private String formTypEncd;// 单据类型编码
    private String formTypName;//单据类型名称
    private String tranOutDeptNm;// 转出部门名称
    private String tranInDeptNm;// 转入部门名称

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

    public String getFormTypName() {
        return formTypName;
    }

    public void setFormTypName(String formTypName) {
        this.formTypName = formTypName;
    }


    public String getFormTypEncd() {
        return formTypEncd;
    }

    public void setFormTypEncd(String formTypEncd) {
        this.formTypEncd = formTypEncd;
    }

    public String getTranOutWhsEncdName() {
        return tranOutWhsEncdName;
    }

    public void setTranOutWhsEncdName(String tranOutWhsEncdName) {
        this.tranOutWhsEncdName = tranOutWhsEncdName;
    }

    public String getTranInWhsEncdName() {
        return tranInWhsEncdName;
    }

    public void setTranInWhsEncdName(String tranInWhsEncdName) {
        this.tranInWhsEncdName = tranInWhsEncdName;
    }

    public final List<CannibSnglSubTab> getCheckSnglList() {
        return checkSnglList;
    }

    public final void setCheckSnglList(List<CannibSnglSubTab> checkSnglList) {
        this.checkSnglList = checkSnglList;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum == null ? null : formNum.trim();
    }

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getTranOutWhsEncd() {
        return tranOutWhsEncd;
    }

    public void setTranOutWhsEncd(String tranOutWhsEncd) {
        this.tranOutWhsEncd = tranOutWhsEncd == null ? null : tranOutWhsEncd.trim();
    }

    public String getTranInWhsEncd() {
        return tranInWhsEncd;
    }

    public void setTranInWhsEncd(String tranInWhsEncd) {
        this.tranInWhsEncd = tranInWhsEncd == null ? null : tranInWhsEncd.trim();
    }

    public String getTranOutDeptEncd() {
        return tranOutDeptEncd;
    }

    public void setTranOutDeptEncd(String tranOutDeptEncd) {
        this.tranOutDeptEncd = tranOutDeptEncd == null ? null : tranOutDeptEncd.trim();
    }

    public String getTranInDeptEncd() {
        return tranInDeptEncd;
    }

    public void setTranInDeptEncd(String tranInDeptEncd) {
        this.tranInDeptEncd = tranInDeptEncd == null ? null : tranInDeptEncd.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Integer getIsNtWms() {
        return isNtWms;
    }

    public void setIsNtWms(Integer isNtWms) {
        this.isNtWms = isNtWms;
    }

    public Integer getIsNtChk() {
        return isNtChk;
    }

    public void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public Integer getIsNtCmplt() {
        return isNtCmplt;
    }

    public void setIsNtCmplt(Integer isNtCmplt) {
        this.isNtCmplt = isNtCmplt;
    }

    public Integer getIsNtClos() {
        return isNtClos;
    }

    public void setIsNtClos(Integer isNtClos) {
        this.isNtClos = isNtClos;
    }

    public Integer getPrintCnt() {
        return printCnt;
    }

    public void setPrintCnt(Integer printCnt) {
        this.printCnt = printCnt;
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
        this.mdfr = mdfr == null ? null : mdfr.trim();
    }

    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr == null ? null : chkr.trim();
    }

    public String getCannibStatus() {
        return cannibStatus;
    }

    public void setCannibStatus(String cannibStatus) {
        this.cannibStatus = cannibStatus;
    }

    public String getFormDt() {
        return formDt;
    }

    public void setFormDt(String formDt) {
        this.formDt = formDt;
    }

    public String getCannibDt() {
        return cannibDt;
    }

    public void setCannibDt(String cannibDt) {
        this.cannibDt = cannibDt;
    }

    public String getCannibDt1() {
        return cannibDt1;
    }

    public void setCannibDt1(String cannibDt1) {
        this.cannibDt1 = cannibDt1;
    }

    public String getCannibDt2() {
        return cannibDt2;
    }

    public void setCannibDt2(String cannibDt2) {
        this.cannibDt2 = cannibDt2;
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
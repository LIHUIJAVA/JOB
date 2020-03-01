package com.px.mis.whs.entity;

import java.util.List;

//其他出入库单
public class OthOutIntoWhs {
    private String formNum;// 单号

    private Long ordrNum;// 序号

    private String formDt;// 单据日期
    private String formDt1;// 单据日期起
    private String formDt2;// 单据日期终

    private String whsEncd;// 仓库编码

    private String srcFormNum;// 来源单据号

    private String outIntoWhsTyp;// 出入库类型

    private String outStatus;// 出库状态

    private String inStatus;// 入库状态

    private String memo;// 备注

    private Integer isNtWms;// 是否向WMS上传

    private Integer isNtChk;// 是否审核

    private Integer isNtBookEntry;// 是否记账

    private Integer isNtCmplt;// 是否完成

    private Integer isNtClos;// 是否关闭

    private Integer printCnt;// 打印次数

    private String setupPers;// 创建人

    private String setupTm;// 创建时间

    private String mdfr;// 修改人

    private String modiTm;// 修改时间

    private String chkr;// 审核人

    private String chkTm;// 审核时间
    private String chkTm1;// 审核时间起
    private String chkTm2;// 审核时间终

    private String bookEntryPers;// 记账人

    private String bookEntryTm;// 记账时间

    private String operator;// 操作人
    private String operatorId;// 操作人编码

    private List<OthOutIntoWhsSubTab> outIntoSubList;
    // 出入库类型 1 0
//    private Integer inOut;
    // 出入库流水账
    private String outIntoWhsTypId;// 出入库类型编码
    private String outIntoWhsTypNm;// 出入库类型名称

    private String whsNm;// 仓库名称
    private String formTypEncd;// 单据类型编码
    private String formTypName;// 单据类型名称

    private Integer isNtMakeVouch;// 是否生成凭证
    private String makVouchPers;// 制凭证人
    private String makVouchTm;// 制凭证时间
    private String recvSendCateId;// 收发类别id
    private String recvSendCateNm;// 收发类别名称

    private String  outWhsEncd;
    private String   inWhsEncd;
    private String   outWhsNm;
    private String   inWhsNm;

    public String getOutWhsEncd() {
        return outWhsEncd;
    }

    public void setOutWhsEncd(String outWhsEncd) {
        this.outWhsEncd = outWhsEncd;
    }

    public String getInWhsEncd() {
        return inWhsEncd;
    }

    public void setInWhsEncd(String inWhsEncd) {
        this.inWhsEncd = inWhsEncd;
    }

    public String getOutWhsNm() {
        return outWhsNm;
    }

    public void setOutWhsNm(String outWhsNm) {
        this.outWhsNm = outWhsNm;
    }

    public String getInWhsNm() {
        return inWhsNm;
    }

    public void setInWhsNm(String inWhsNm) {
        this.inWhsNm = inWhsNm;
    }

    public String getRecvSendCateId() {
        return recvSendCateId;
    }

    public void setRecvSendCateId(String recvSendCateId) {
        this.recvSendCateId = recvSendCateId;
    }

    public String getRecvSendCateNm() {
        return recvSendCateNm;
    }

    public void setRecvSendCateNm(String recvSendCateNm) {
        this.recvSendCateNm = recvSendCateNm;
    }

    public Integer getIsNtMakeVouch() {
        return isNtMakeVouch;
    }

    public void setIsNtMakeVouch(Integer isNtMakeVouch) {
        this.isNtMakeVouch = isNtMakeVouch;
    }

    public String getMakVouchPers() {
        return makVouchPers;
    }

    public void setMakVouchPers(String makVouchPers) {
        this.makVouchPers = makVouchPers;
    }

    public String getMakVouchTm() {
        return makVouchTm;
    }

    public void setMakVouchTm(String makVouchTm) {
        this.makVouchTm = makVouchTm;
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

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getChkTm1() {
        return chkTm1;
    }

    public void setChkTm1(String chkTm1) {
        this.chkTm1 = chkTm1;
    }

    public String getChkTm2() {
        return chkTm2;
    }

    public void setChkTm2(String chkTm2) {
        this.chkTm2 = chkTm2;
    }

    public String getOutIntoWhsTypId() {
        return outIntoWhsTypId;
    }

    public void setOutIntoWhsTypId(String outIntoWhsTypId) {
        this.outIntoWhsTypId = outIntoWhsTypId;
    }

    public String getOutIntoWhsTypNm() {
        return outIntoWhsTypNm;
    }

    public void setOutIntoWhsTypNm(String outIntoWhsTypNm) {
        this.outIntoWhsTypNm = outIntoWhsTypNm;
    }

    public List<OthOutIntoWhsSubTab> getOutIntoSubList() {
        return outIntoSubList;
    }

    public void setOutIntoSubList(List<OthOutIntoWhsSubTab> outIntoSubList) {
        this.outIntoSubList = outIntoSubList;
    }

    public String getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(String outStatus) {
        this.outStatus = outStatus;
    }

    public String getInStatus() {
        return inStatus;
    }

    public void setInStatus(String inStatus) {
        this.inStatus = inStatus;
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

    public String getSrcFormNum() {
        return srcFormNum;
    }

    public void setSrcFormNum(String srcFormNum) {
        this.srcFormNum = srcFormNum == null ? null : srcFormNum.trim();
    }

    public String getOutIntoWhsTyp() {
        return outIntoWhsTyp;
    }

    public void setOutIntoWhsTyp(String outIntoWhsTyp) {
        this.outIntoWhsTyp = outIntoWhsTyp == null ? null : outIntoWhsTyp.trim();
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

    public Integer getIsNtBookEntry() {
        return isNtBookEntry;
    }

    public void setIsNtBookEntry(Integer isNtBookEntry) {
        this.isNtBookEntry = isNtBookEntry;
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

    public String getBookEntryPers() {
        return bookEntryPers;
    }

    public void setBookEntryPers(String bookEntryPers) {
        this.bookEntryPers = bookEntryPers == null ? null : bookEntryPers.trim();
    }

    public String getFormDt() {
        return formDt;
    }

    public void setFormDt(String formDt) {
        this.formDt = formDt;
    }

    public String getFormDt1() {
        return formDt1;
    }

    public void setFormDt1(String formDt1) {
        this.formDt1 = formDt1;
    }

    public String getFormDt2() {
        return formDt2;
    }

    public void setFormDt2(String formDt2) {
        this.formDt2 = formDt2;
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

    public String getBookEntryTm() {
        return bookEntryTm;
    }

    public void setBookEntryTm(String bookEntryTm) {
        this.bookEntryTm = bookEntryTm;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

}
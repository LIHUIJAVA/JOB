package com.px.mis.whs.entity;


import java.util.List;

//盘点损益
public class CheckPrftLoss {
    private String checkFormNum;//盘点单号

    private String srcFormNum;//来源单据号

    private Long ordrNum;//序号

    private String checkDt;//盘点日期
    private String checkDt1;//盘点日期
    private String checkDt2;//盘点日期

    private String bookDt;//账面日期

    private String whsEncd;//仓库编码

    private String checkBat;//盘点批号

    private String memo;//备注

    private Integer isNtWms;//是否向WMS上传

    private Integer isNtChk;//是否审核

    private Integer isNtBookEntry;//是否记账

    private Integer isNtCmplt;//是否完成

    private Integer isNtClos;//是否关闭

    private Integer printCnt;//打印次数

    private String setupPers;//创建人

    private String setupTm;//创建时间

    private String mdfr;//修改人

    private String modiTm;//修改时间

    private String chkr;//审核人

    private String chkTm;//审核日期

    private String bookEntryPers;//记账人

    private String bookEntryTm;//记账时间
    private String whsNm;//仓库名称

    private List<CheckPrftLossSubTab> cPrftLossList;
    private String formTypEncd;// 单据类型编码
    private String formTypName;//单据类型名称

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


    public final String getWhsNm() {
        return whsNm;
    }

    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public String getSrcFormNum() {
        return srcFormNum;
    }

    public void setSrcFormNum(String srcFormNum) {
        this.srcFormNum = srcFormNum;
    }

    public String getCheckFormNum() {
        return checkFormNum;
    }

    public void setCheckFormNum(String checkFormNum) {
        this.checkFormNum = checkFormNum;
    }

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getCheckDt() {
        return checkDt;
    }

    public void setCheckDt(String checkDt) {
        this.checkDt = checkDt;
    }

    public String getCheckDt1() {
        return checkDt1;
    }

    public void setCheckDt1(String checkDt1) {
        this.checkDt1 = checkDt1;
    }

    public String getCheckDt2() {
        return checkDt2;
    }

    public void setCheckDt2(String checkDt2) {
        this.checkDt2 = checkDt2;
    }

    public String getBookDt() {
        return bookDt;
    }

    public void setBookDt(String bookDt) {
        this.bookDt = bookDt;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getCheckBat() {
        return checkBat;
    }

    public void setCheckBat(String checkBat) {
        this.checkBat = checkBat;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
        this.setupPers = setupPers;
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }

    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr;
    }

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public String getBookEntryPers() {
        return bookEntryPers;
    }

    public void setBookEntryPers(String bookEntryPers) {
        this.bookEntryPers = bookEntryPers;
    }

    public String getBookEntryTm() {
        return bookEntryTm;
    }

    public void setBookEntryTm(String bookEntryTm) {
        this.bookEntryTm = bookEntryTm;
    }

    public List<CheckPrftLossSubTab> getcPrftLossList() {
        return cPrftLossList;
    }

    public void setcPrftLossList(List<CheckPrftLossSubTab> cPrftLossList) {
        this.cPrftLossList = cPrftLossList;
    }


}
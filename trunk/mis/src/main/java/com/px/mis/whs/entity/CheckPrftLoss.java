package com.px.mis.whs.entity;


import java.util.List;

//�̵�����
public class CheckPrftLoss {
    private String checkFormNum;//�̵㵥��

    private String srcFormNum;//��Դ���ݺ�

    private Long ordrNum;//���

    private String checkDt;//�̵�����
    private String checkDt1;//�̵�����
    private String checkDt2;//�̵�����

    private String bookDt;//��������

    private String whsEncd;//�ֿ����

    private String checkBat;//�̵�����

    private String memo;//��ע

    private Integer isNtWms;//�Ƿ���WMS�ϴ�

    private Integer isNtChk;//�Ƿ����

    private Integer isNtBookEntry;//�Ƿ����

    private Integer isNtCmplt;//�Ƿ����

    private Integer isNtClos;//�Ƿ�ر�

    private Integer printCnt;//��ӡ����

    private String setupPers;//������

    private String setupTm;//����ʱ��

    private String mdfr;//�޸���

    private String modiTm;//�޸�ʱ��

    private String chkr;//�����

    private String chkTm;//�������

    private String bookEntryPers;//������

    private String bookEntryTm;//����ʱ��
    private String whsNm;//�ֿ�����

    private List<CheckPrftLossSubTab> cPrftLossList;
    private String formTypEncd;// �������ͱ���
    private String formTypName;//������������

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
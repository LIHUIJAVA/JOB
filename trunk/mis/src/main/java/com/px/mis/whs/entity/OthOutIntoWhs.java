package com.px.mis.whs.entity;

import java.util.List;

//��������ⵥ
public class OthOutIntoWhs {
    private String formNum;// ����

    private Long ordrNum;// ���

    private String formDt;// ��������
    private String formDt1;// ����������
    private String formDt2;// ����������

    private String whsEncd;// �ֿ����

    private String srcFormNum;// ��Դ���ݺ�

    private String outIntoWhsTyp;// ���������

    private String outStatus;// ����״̬

    private String inStatus;// ���״̬

    private String memo;// ��ע

    private Integer isNtWms;// �Ƿ���WMS�ϴ�

    private Integer isNtChk;// �Ƿ����

    private Integer isNtBookEntry;// �Ƿ����

    private Integer isNtCmplt;// �Ƿ����

    private Integer isNtClos;// �Ƿ�ر�

    private Integer printCnt;// ��ӡ����

    private String setupPers;// ������

    private String setupTm;// ����ʱ��

    private String mdfr;// �޸���

    private String modiTm;// �޸�ʱ��

    private String chkr;// �����

    private String chkTm;// ���ʱ��
    private String chkTm1;// ���ʱ����
    private String chkTm2;// ���ʱ����

    private String bookEntryPers;// ������

    private String bookEntryTm;// ����ʱ��

    private String operator;// ������
    private String operatorId;// �����˱���

    private List<OthOutIntoWhsSubTab> outIntoSubList;
    // ��������� 1 0
//    private Integer inOut;
    // �������ˮ��
    private String outIntoWhsTypId;// ��������ͱ���
    private String outIntoWhsTypNm;// �������������

    private String whsNm;// �ֿ�����
    private String formTypEncd;// �������ͱ���
    private String formTypName;// ������������

    private Integer isNtMakeVouch;// �Ƿ�����ƾ֤
    private String makVouchPers;// ��ƾ֤��
    private String makVouchTm;// ��ƾ֤ʱ��
    private String recvSendCateId;// �շ����id
    private String recvSendCateNm;// �շ��������

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
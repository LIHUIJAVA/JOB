package com.px.mis.whs.entity;

import java.util.List;

//������
public class CannibSngl {
    private String formNum;// ���ݺ�
    private Long ordrNum;// ���
    private String formDt;// ��������
    private String cannibDt;// ��������
    private String cannibDt1;// �������� ��ʱֵ
    private String cannibDt2;// �������� ��ʱֵ
    private String tranOutWhsEncd;// ת���ֿ����
    private String tranInWhsEncd;// ת��ֿ����
    private String tranOutWhsEncdName;// ת���ֿ�����
    private String tranInWhsEncdName;// ת��ֿ�����
    private String tranOutDeptEncd;// ת�����ű���
    private String tranInDeptEncd;// ת�벿�ű���
    private String cannibStatus;// ����״̬
    private String memo;// ��ע
    private Integer isNtWms;// �Ƿ���WMS�ϴ�
    private Integer isNtChk;// �Ƿ����
    private Integer isNtCmplt;// �Ƿ����
    private Integer isNtClos;// �Ƿ�ر�
    private Integer printCnt;// ��ӡ����
    private String setupPers;// ������
    private String setupTm;// ����ʱ��
    private String mdfr;// �޸���
    private String modiTm;// �޸�ʱ��
    private String chkr;// �����
    private String chkTm;// ���ʱ��
    private List<CannibSnglSubTab> checkSnglList;
    private String formTypEncd;// �������ͱ���
    private String formTypName;//������������
    private String tranOutDeptNm;// ת����������
    private String tranInDeptNm;// ת�벿������

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
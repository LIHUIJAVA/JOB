package com.px.mis.whs.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//δ�󵥾�
public class NtChkNo {
    @JsonProperty("���ݺ�")
    private String formNum;// ���ݺ�
    @JsonProperty("�������ͱ���")
    private String formTypEncd;// �������ͱ���
    @JsonProperty("������������")
    private String formTypName;// ������������
    @JsonProperty("��������")
    private String formDt;// ��������
    @JsonProperty("������")
    private String setupPers;// ������
    @JsonProperty("����ʱ��")
    private String setupTm;// ����ʱ��

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
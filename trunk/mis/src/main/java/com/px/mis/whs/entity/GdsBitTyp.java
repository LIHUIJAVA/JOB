package com.px.mis.whs.entity;

//��λ����
public class GdsBitTyp {
    private String gdsBitTypEncd;//��λ���ͱ���

    private String gdsBitEncd;//���ܱ���

    private String gdsBitTypNm;//��λ��������(���ܡ��ض�)

    public String getGdsBitTypEncd() {
        return gdsBitTypEncd;
    }

    public void setGdsBitTypEncd(String gdsBitTypEncd) {
        this.gdsBitTypEncd = gdsBitTypEncd == null ? null : gdsBitTypEncd.trim();
    }

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd == null ? null : gdsBitEncd.trim();
    }

    public String getGdsBitTypNm() {
        return gdsBitTypNm;
    }

    public void setGdsBitTypNm(String gdsBitTypNm) {
        this.gdsBitTypNm = gdsBitTypNm == null ? null : gdsBitTypNm.trim();
    }
}
package com.px.mis.whs.entity;

//货位类型
public class GdsBitTyp {
    private String gdsBitTypEncd;//货位类型编码

    private String gdsBitEncd;//货架编码

    private String gdsBitTypNm;//货位类型名称(货架、地堆)

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
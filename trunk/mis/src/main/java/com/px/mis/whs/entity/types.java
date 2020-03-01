package com.px.mis.whs.entity;

//类型
public class types {
    private Long typId;//类型编号

    private String momKitEncd;//母配套件编码

    private String typNm;//类型名称（套件、散件）

    public Long getTypId() {
        return typId;
    }

    public void setTypId(Long typId) {
        this.typId = typId;
    }

    public String getMomKitEncd() {
        return momKitEncd;
    }

    public void setMomKitEncd(String momKitEncd) {
        this.momKitEncd = momKitEncd == null ? null : momKitEncd.trim();
    }

    public String getTypNm() {
        return typNm;
    }

    public void setTypNm(String typNm) {
        this.typNm = typNm == null ? null : typNm.trim();
    }
}
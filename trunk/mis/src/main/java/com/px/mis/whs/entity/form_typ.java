package com.px.mis.whs.entity;

//单据类型
public class form_typ {
    private Long formTypId;//单据类型编号

    private String momKitEncd;//母配套件编码

    private String formTypNm;//单据类型名称(组装单、拆卸单)

    public Long getFormTypId() {
        return formTypId;
    }

    public void setFormTypId(Long formTypId) {
        this.formTypId = formTypId;
    }

    public String getMomKitEncd() {
        return momKitEncd;
    }

    public void setMomKitEncd(String momKitEncd) {
        this.momKitEncd = momKitEncd == null ? null : momKitEncd.trim();
    }

    public String getFormTypNm() {
        return formTypNm;
    }

    public void setFormTypNm(String formTypNm) {
        this.formTypNm = formTypNm == null ? null : formTypNm.trim();
    }
}
package com.px.mis.whs.entity;

//����
public class types {
    private Long typId;//���ͱ��

    private String momKitEncd;//ĸ���׼�����

    private String typNm;//�������ƣ��׼���ɢ����

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
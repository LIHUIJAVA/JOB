package com.px.mis.whs.entity;

//��������
public class form_typ {
    private Long formTypId;//�������ͱ��

    private String momKitEncd;//ĸ���׼�����

    private String formTypNm;//������������(��װ������ж��)

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
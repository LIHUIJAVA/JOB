package com.px.mis.whs.entity;

//���������
public class out_into_whs_typ {
    private String outIntoWhsTypId;//��������ͱ��

    private Long ordrNum;//���

    private String formNum;//����

    private String outIntoWhsTypNm;//�������������

    private String srcFormNum;//��Դ���ݺ�

    public String getOutIntoWhsTypId() {
        return outIntoWhsTypId;
    }

    public void setOutIntoWhsTypId(String outIntoWhsTypId) {
        this.outIntoWhsTypId = outIntoWhsTypId == null ? null : outIntoWhsTypId.trim();
    }

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum == null ? null : formNum.trim();
    }

    public String getOutIntoWhsTypNm() {
        return outIntoWhsTypNm;
    }

    public void setOutIntoWhsTypNm(String outIntoWhsTypNm) {
        this.outIntoWhsTypNm = outIntoWhsTypNm == null ? null : outIntoWhsTypNm.trim();
    }

    public String getSrcFormNum() {
        return srcFormNum;
    }

    public void setSrcFormNum(String srcFormNum) {
        this.srcFormNum = srcFormNum == null ? null : srcFormNum.trim();
    }
}
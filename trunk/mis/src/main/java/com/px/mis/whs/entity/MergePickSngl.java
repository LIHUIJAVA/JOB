package com.px.mis.whs.entity;

import java.math.BigDecimal;


//�ϲ������
public class MergePickSngl {

    private Long mergeNum;//�ϲ����������

    private String pickSnglNum;//�������
    private String pickNum;//δ�ϲ��ļ������

    private String whsEncd;//�ֿ����

    private String whsNm;//�ֿ�����

    private String invtyEncd;//�������

    private String invtyNm;//�������

    private String barCd;//������

    private String batNum;//����

    private String prdcDt;//��������

    private String invldtnDt;//ʧЧ����

    private String gdsBitEncd;//��λ����

    private BigDecimal qty;//����

    private String operator;//������
    private String operatorId;//�����˱���

    private Integer isFinshPick;//�Ƿ���
    private String finshPickTm;//���ʱ��


    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getIsFinshPick() {
        return isFinshPick;
    }

    public void setIsFinshPick(Integer isFinshPick) {
        this.isFinshPick = isFinshPick;
    }

    public String getFinshPickTm() {
        return finshPickTm;
    }

    public void setFinshPickTm(String finshPickTm) {
        this.finshPickTm = finshPickTm;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPickNum() {
        return pickNum;
    }

    public void setPickNum(String pickNum) {
        this.pickNum = pickNum;
    }

    public Long getMergeNum() {
        return mergeNum;
    }

    public void setMergeNum(Long mergeNum) {
        this.mergeNum = mergeNum;
    }

    public String getPickSnglNum() {
        return pickSnglNum;
    }

    public void setPickSnglNum(String pickSnglNum) {
        this.pickSnglNum = pickSnglNum;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    public String getInvtyNm() {
        return invtyNm;
    }

    public void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public String getBarCd() {
        return barCd;
    }

    public void setBarCd(String barCd) {
        this.barCd = barCd;
    }

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum;
    }


    public String getPrdcDt() {
        return prdcDt;
    }

    public void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
    }

    public String getInvldtnDt() {
        return invldtnDt;
    }

    public void setInvldtnDt(String invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }


}

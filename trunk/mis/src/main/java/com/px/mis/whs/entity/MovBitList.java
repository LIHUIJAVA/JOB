package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.px.mis.purc.entity.InvtyDoc;

//��λ�嵥
public class MovBitList {

    private Long ordrNum;// ���

    private String oalBit;// ԭʼ��λ

    private BigDecimal oalBitNum;// ԭʼ��λ����

    private String targetBit;// Ŀ���λ

    private BigDecimal targetBitNum;// Ŀ���λ����

    private String invtyEncd;// �������

    private String batNum;// ����

    private String prdcDt;// ��������

    private String whsEncd;// �ֿ����

    private String regnEncd;// ԭ�������

    private String targetRegnEncd;// Ŀ���������

    private String operator;// ������
    private String operatorId;// �����˱���
    private Integer isOporFish;// �Ƿ�������

    // ��λ�嵥
    private WhsDoc whsDoc;
    private Regn regn;
    private InvtyDoc invtyDoc;

    private String whsNm;// �ֿ�����
    private String invtyNm;// �������
    private String spcModel;// ����ͺ�

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public String getInvtyNm() {
        return invtyNm;
    }

    public void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public String getSpcModel() {
        return spcModel;
    }

    public void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    public String getTargetRegnEncd() {
        return targetRegnEncd;
    }

    public void setTargetRegnEncd(String targetRegnEncd) {
        this.targetRegnEncd = targetRegnEncd;
    }

    public WhsDoc getWhsDoc() {
        return whsDoc;
    }

    public void setWhsDoc(WhsDoc whsDoc) {
        this.whsDoc = whsDoc;
    }

    public Regn getRegn() {
        return regn;
    }

    public void setRegn(Regn regn) {
        this.regn = regn;
    }

    public InvtyDoc getInvtyDoc() {
        return invtyDoc;
    }

    public void setInvtyDoc(InvtyDoc invtyDoc) {
        this.invtyDoc = invtyDoc;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getIsOporFish() {
        return isOporFish;
    }

    public void setIsOporFish(Integer isOporFish) {
        this.isOporFish = isOporFish;
    }

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getOalBit() {
        return oalBit;
    }

    public void setOalBit(String oalBit) {
        this.oalBit = oalBit;
    }

    public BigDecimal getOalBitNum() {
        return oalBitNum;
    }

    public void setOalBitNum(BigDecimal oalBitNum) {
        this.oalBitNum = oalBitNum;
    }

    public String getTargetBit() {
        return targetBit;
    }

    public void setTargetBit(String targetBit) {
        this.targetBit = targetBit;
    }

    public BigDecimal getTargetBitNum() {
        return targetBitNum;
    }

    public void setTargetBitNum(BigDecimal targetBitNum) {
        this.targetBitNum = targetBitNum;
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
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

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getRegnEncd() {
        return regnEncd;
    }

    public void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd;
    }

}

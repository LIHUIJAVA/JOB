package com.px.mis.whs.entity;

import java.math.BigDecimal;

//��Ʒ�ṹ�ӱ�
public class ProdStruSubTab {


    private String subEncd;//�Ӽ�����
    private String ordrNumSub;//���
    private String ordrNum;//��Ʒ�ṹ����ʶ
    private String momEncd;//ĸ������
    private String subNm;//�Ӽ�����
    private String subSpc;//�Ӽ����
    private String measrCorp;//������λ
    private BigDecimal bxRule;//���
    private BigDecimal subQty;//�Ӽ�����
    private BigDecimal momQty;//ĸ������
    private String memo;//��ע

    private String smeasrCorpNm;//������λ����
    private String sbaoZhiQiDt;// ����������
    private BigDecimal soptaxRate;// ����˰��
    private String sinvtyCd;// �������
    private BigDecimal srefCost;// �ο��ɱ�
    private String scrspdBarCd;// ��Ӧ������

    public String getSubEncd() {
        return subEncd;
    }

    public void setSubEncd(String subEncd) {
        this.subEncd = subEncd;
    }

    public String getOrdrNumSub() {
        return ordrNumSub;
    }

    public void setOrdrNumSub(String ordrNumSub) {
        this.ordrNumSub = ordrNumSub;
    }

    public String getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(String ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getMomEncd() {
        return momEncd;
    }

    public void setMomEncd(String momEncd) {
        this.momEncd = momEncd;
    }

    public String getSubNm() {
        return subNm;
    }

    public void setSubNm(String subNm) {
        this.subNm = subNm;
    }

    public String getSubSpc() {
        return subSpc;
    }

    public void setSubSpc(String subSpc) {
        this.subSpc = subSpc;
    }

    public String getMeasrCorp() {
        return measrCorp;
    }

    public void setMeasrCorp(String measrCorp) {
        this.measrCorp = measrCorp;
    }

    public BigDecimal getBxRule() {
        return bxRule;
    }

    public void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public BigDecimal getSubQty() {
        return subQty;
    }

    public void setSubQty(BigDecimal subQty) {
        this.subQty = subQty;
    }

    public BigDecimal getMomQty() {
        return momQty;
    }

    public void setMomQty(BigDecimal momQty) {
        this.momQty = momQty;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSmeasrCorpNm() {
        return smeasrCorpNm;
    }

    public void setSmeasrCorpNm(String smeasrCorpNm) {
        this.smeasrCorpNm = smeasrCorpNm;
    }

    public String getSbaoZhiQiDt() {
        return sbaoZhiQiDt;
    }

    public void setSbaoZhiQiDt(String sbaoZhiQiDt) {
        this.sbaoZhiQiDt = sbaoZhiQiDt;
    }

    public BigDecimal getSoptaxRate() {
        return soptaxRate;
    }

    public void setSoptaxRate(BigDecimal soptaxRate) {
        this.soptaxRate = soptaxRate;
    }

    public String getSinvtyCd() {
        return sinvtyCd;
    }

    public void setSinvtyCd(String sinvtyCd) {
        this.sinvtyCd = sinvtyCd;
    }

    public BigDecimal getSrefCost() {
        return srefCost;
    }

    public void setSrefCost(BigDecimal srefCost) {
        this.srefCost = srefCost;
    }

    public String getScrspdBarCd() {
        return scrspdBarCd;
    }

    public void setScrspdBarCd(String scrspdBarCd) {
        this.scrspdBarCd = scrspdBarCd;
    }
}
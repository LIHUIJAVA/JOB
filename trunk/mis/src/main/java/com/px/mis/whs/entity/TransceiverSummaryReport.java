package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

//�շ����ܱ���
public class TransceiverSummaryReport {
    /**
     * �ֿ����
     */
    @JsonProperty("�ֿ����")
    private String whsEncd;// �ֿ����
    /**
     * �������
     */
    @JsonProperty("�������")
    private String invtyEncd;// �����
    /**
     * ����
     */
    @JsonProperty("����")
    private String batNum;// ����

    /**
     * �������
     */
    @JsonProperty("�������")
    private String invtyNm; // �������
    /**
     * �ֿ�����
     */
    @JsonProperty("�ֿ�����")
    private String whsNm; // �ֿ�����
    @JsonProperty("���")
    private String spcModel;// ���
    @JsonProperty("������λ����")
    private String measrCorpNm;// ������λ
    @JsonProperty("�����������")
    private String invtyClsNm;// �����������

    /**
     * �ڳ�
     */
    @JsonProperty("�ڳ�����")
    private BigDecimal qiChuQty;// ��
    @JsonProperty("�ڳ���˰���")
    private BigDecimal qiChuAmt;// ��˰���
    @JsonProperty("�ڳ���˰�ϼ�")
    private BigDecimal qiChuSum;// ��˰�ϼƺ�˰

    /**
     * ����
     */
    @JsonProperty("�������")
    private BigDecimal intoQty;// ��
    @JsonProperty("�����˰���")
    private BigDecimal intoAmt;// ��˰���
    @JsonProperty("����˰�ϼ�")
    private BigDecimal intoSum;// ��˰�ϼƺ�˰

    /**
     * ����
     */
    @JsonProperty("��������")
    private BigDecimal outQty;// ��
    @JsonProperty("������˰���")
    private BigDecimal outAmt;// ��˰���
    @JsonProperty("�����˰�ϼ�")
    private BigDecimal outSum;// ��˰�ϼƺ�˰

    /**
     * ���
     */
    @JsonProperty("�������")
    private BigDecimal jieCunQty;// ��
    @JsonProperty("�����˰���")
    private BigDecimal jieChuAmt;// ��˰���
    @JsonProperty("����˰�ϼ�")
    private BigDecimal jieChuSum;// ��˰�ϼƺ�˰
    @JsonProperty("��������")
    private String prdcDt;// ��������
    @JsonProperty("������")
    private String baoZhiQi;// ������
    @JsonProperty("ʧЧ����")
    private String invldtnDt;// ʧЧ����
    @JsonProperty("���")
    private String bxRule; // ���

    /**
     * @return �ֿ����
     * @date 2019��4��15��
     */
    public final String getWhsEncd() {
        return whsEncd;
    }

    /**
     * @param �ֿ����
     * @date 2019��4��15��
     */
    public final void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    /**
     * @return ���������١����ӿ������ִ�������
     * @date 2019��4��15��
     */
    public final String getInvtyEncd() {
        return invtyEncd;
    }

    /**
     * @param ���������١����ӿ������ִ�������
     * @date 2019��4��15��
     */
    public final void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    /**
     * @return ����
     * @date 2019��4��15��
     */
    public final String getBatNum() {
        return batNum;
    }

    /**
     * @param ����
     * @date 2019��4��15��
     */
    public final void setBatNum(String batNum) {
        this.batNum = batNum;
    }

    /**
     * @return �ڳ�
     * @date 2019��4��15��
     */
    public final BigDecimal getQiChuQty() {
        return qiChuQty;
    }

    /**
     * @param �ڳ�
     * @date 2019��4��15��
     */
    public final void setQiChuQty(BigDecimal qiChuQty) {
        this.qiChuQty = qiChuQty;
    }

    /**
     * @return ���
     * @date 2019��4��15��
     */
    public final BigDecimal getJieCunQty() {
        return jieCunQty;
    }

    /**
     * @param ���
     * @date 2019��4��15��
     */
    public final void setJieCunQty(BigDecimal jieCunQty) {
        this.jieCunQty = jieCunQty;
    }

    /**
     * @return hj�������
     * @date 2019��4��15��
     */
    public final String getInvtyNm() {
        return invtyNm;
    }

    /**
     * @param hj�������
     * @date 2019��4��15��
     */
    public final void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    /**
     * @return �ֿ�����
     * @date 2019��4��15��
     */
    public final String getWhsNm() {
        return whsNm;
    }

    /**
     * @param �ֿ�����
     * @date 2019��4��15��
     */
    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public String getSpcModel() {
        return spcModel;
    }

    public void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    public String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public String getInvtyClsNm() {
        return invtyClsNm;
    }

    public void setInvtyClsNm(String invtyClsNm) {
        this.invtyClsNm = invtyClsNm;
    }

    public BigDecimal getQiChuAmt() {
        return qiChuAmt;
    }

    public void setQiChuAmt(BigDecimal qiChuAmt) {
        this.qiChuAmt = qiChuAmt;
    }

    public BigDecimal getQiChuSum() {
        return qiChuSum;
    }

    public void setQiChuSum(BigDecimal qiChuSum) {
        this.qiChuSum = qiChuSum;
    }

    public BigDecimal getIntoQty() {
        return intoQty;
    }

    public void setIntoQty(BigDecimal intoQty) {
        this.intoQty = intoQty;
    }

    public BigDecimal getIntoAmt() {
        return intoAmt;
    }

    public void setIntoAmt(BigDecimal intoAmt) {
        this.intoAmt = intoAmt;
    }

    public BigDecimal getIntoSum() {
        return intoSum;
    }

    public void setIntoSum(BigDecimal intoSum) {
        this.intoSum = intoSum;
    }

    public BigDecimal getOutQty() {
        return outQty;
    }

    public void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    public BigDecimal getOutAmt() {
        return outAmt;
    }

    public void setOutAmt(BigDecimal outAmt) {
        this.outAmt = outAmt;
    }

    public BigDecimal getOutSum() {
        return outSum;
    }

    public void setOutSum(BigDecimal outSum) {
        this.outSum = outSum;
    }

    public BigDecimal getJieChuAmt() {
        return jieChuAmt;
    }

    public void setJieChuAmt(BigDecimal jieChuAmt) {
        this.jieChuAmt = jieChuAmt;
    }

    public BigDecimal getJieChuSum() {
        return jieChuSum;
    }

    public void setJieChuSum(BigDecimal jieChuSum) {
        this.jieChuSum = jieChuSum;
    }

    public final String getPrdcDt() {
        return prdcDt;
    }

    public final void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
    }

    public final String getBaoZhiQi() {
        return baoZhiQi;
    }

    public final void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi;
    }

    public final String getInvldtnDt() {
        return invldtnDt;
    }

    public final void setInvldtnDt(String invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public final String getBxRule() {
        return bxRule;
    }

    public final void setBxRule(String bxRule) {
        this.bxRule = bxRule;
    }

}
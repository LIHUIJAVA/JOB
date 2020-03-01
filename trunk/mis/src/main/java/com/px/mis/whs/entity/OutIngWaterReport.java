package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * �������ˮ����
 */
public class OutIngWaterReport {
    /**
     * ���� ��������
     */
    @JsonProperty("���ݺ�")
    private String formNum;
    /**
     * ����
     */
    @JsonProperty("��������")
    private String formDt;
    /**
     * ��ע
     */
    @JsonProperty("��ע")
    private String memo;
    /**
     * ������
     */
    @JsonProperty("������")
    private String setupPers;
    /**
     * �����
     */
    @JsonProperty("�����")
    private String chkr;
    /**
     * ���ʱ��
     */
    @JsonProperty("���ʱ��")
    private String chkTm;
    /**
     * ����
     */
    @JsonProperty("����")
    private String batNum;
    /**
     * ����
     */
//	@JsonProperty("����")
    @JsonIgnore
    private BigDecimal qty;
    /**
     * ��˰����<!-- ��˰���� -->;
     */
//	@JsonProperty("��˰���� ")
    @JsonIgnore
    private BigDecimal noTaxUprc;
    /**
     * ����
     */
    @JsonProperty("�������")
    private BigDecimal intoQty;
    /**
     * ��˰����<!-- ��˰���� -->;
     */
    @JsonProperty("�����˰����")
    private BigDecimal intoNoTaxUprc;
    /**
     * ����
     */
    @JsonProperty("��������")
    private BigDecimal outQty;
    /**
     * ��˰����<!-- ��˰���� -->;
     */
    @JsonProperty("������˰����")
    private BigDecimal outNoTaxUprc;
    /**
     * �������������
     */
    @JsonProperty("�������������")
    private String outIntoWhsTypNm;
    /**
     * �ֿ�����
     */
    @JsonProperty("�ֿ�����")
    private String whsNm;
    /**
     * �շ��������
     */
    @JsonProperty("�շ��������")
    private String recvSendCateNm;
    /**
     * ��Ӧ�� //�ͻ�����<!-- �ͻ����� -->
     */
    @JsonProperty("��Ӧ�̿ͻ�����")
    private String custNm;
    /**
     * �������
     */
    @JsonProperty("�������")
    private String invtyEncd;
    /**
     * �������
     */
    @JsonProperty("�������")
    private String invtyNm;
    /**
     * ����ͺ�
     */
    @JsonProperty("����ͺ�")
    private String spcModel;
    /**
     * <!-- ������λ���� -->
     */
    @JsonProperty("������λ����")
    private String measrCorpNm;
    /**
     * ���������id
     */
    @JsonProperty("��������ͱ���")
    private String outIntoWhsTypEncd;

    public final String getOutIntoWhsTypEncd() {
        return outIntoWhsTypEncd;
    }

    public final void setOutIntoWhsTypEncd(String outIntoWhsTypEncd) {
        this.outIntoWhsTypEncd = outIntoWhsTypEncd;
    }

    /**
     * @return ����
     * @date 2019��4��15��
     */
    public final String getFormNum() {
        return formNum;
    }

    /**
     * @param ����
     * @date 2019��4��15��
     */
    public final void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    /**
     * @return ����
     * @date 2019��4��15��
     */
    public final String getFormDt() {
        return formDt;
    }

    /**
     * @param ����
     * @date 2019��4��15��
     */
    public final void setFormDt(String formDt) {
        this.formDt = formDt;
    }

    /**
     * @return ��ע
     * @date 2019��4��15��
     */
    public final String getMemo() {
        return memo;
    }

    /**
     * @param ��ע
     * @date 2019��4��15��
     */
    public final void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @return ������
     * @date 2019��4��15��
     */
    public final String getSetupPers() {
        return setupPers;
    }

    /**
     * @param ������
     * @date 2019��4��15��
     */
    public final void setSetupPers(String setupPers) {
        this.setupPers = setupPers;
    }

    /**
     * @return �����
     * @date 2019��4��15��
     */
    public final String getChkr() {
        return chkr;
    }

    /**
     * @param �����
     * @date 2019��4��15��
     */
    public final void setChkr(String chkr) {
        this.chkr = chkr;
    }

    /**
     * @return ���ʱ��
     * @date 2019��4��15��
     */
    public final String getChkTm() {
        return chkTm;
    }

    /**
     * @param ���ʱ��
     * @date 2019��4��15��
     */
    public final void setChkTm(String chkTm) {
        this.chkTm = chkTm;
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
     * @return ����
     * @date 2019��4��15��
     */
    public final BigDecimal getQty() {
        return qty;
    }

    /**
     * @param ����
     * @date 2019��4��15��
     */
    public final void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * @return ��˰����<! - - ��˰����-->;
     * @date 2019��4��15��
     */
    public final BigDecimal getNoTaxUprc() {
        return noTaxUprc;
    }

    /**
     * @param ��˰����<!--��˰����-->;
     * @date 2019��4��15��
     */
    public final void setNoTaxUprc(BigDecimal noTaxUprc) {
        this.noTaxUprc = noTaxUprc;
    }

    /**
     * @return ����
     * @date 2019��4��15��
     */
    public final BigDecimal getIntoQty() {
        return intoQty;
    }

    /**
     * @param ����
     * @date 2019��4��15��
     */
    public final void setIntoQty(BigDecimal intoQty) {
        this.intoQty = intoQty;
    }

    /**
     * @return ��˰����<! - - ��˰����-->;
     * @date 2019��4��15��
     */
    public final BigDecimal getIntoNoTaxUprc() {
        return intoNoTaxUprc;
    }

    /**
     * @param ��˰����<!--��˰����-->;
     * @date 2019��4��15��
     */
    public final void setIntoNoTaxUprc(BigDecimal intoNoTaxUprc) {
        this.intoNoTaxUprc = intoNoTaxUprc;
    }

    /**
     * @return ����
     * @date 2019��4��15��
     */
    public final BigDecimal getOutQty() {
        return outQty;
    }

    /**
     * @param ����
     * @date 2019��4��15��
     */
    public final void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    /**
     * @return ��˰����<! - - ��˰����-->;
     * @date 2019��4��15��
     */
    public final BigDecimal getOutNoTaxUprc() {
        return outNoTaxUprc;
    }

    /**
     * @param ��˰����<!--��˰����-->;
     * @date 2019��4��15��
     */
    public final void setOutNoTaxUprc(BigDecimal outNoTaxUprc) {
        this.outNoTaxUprc = outNoTaxUprc;
    }

    /**
     * @return �������������
     * @date 2019��4��15��
     */
    public final String getOutIntoWhsTypNm() {
        return outIntoWhsTypNm;
    }

    /**
     * @param �������������
     * @date 2019��4��15��
     */
    public final void setOutIntoWhsTypNm(String outIntoWhsTypNm) {
        this.outIntoWhsTypNm = outIntoWhsTypNm;
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

    /**
     * @return �շ��������
     * @date 2019��4��15��
     */
    public final String getRecvSendCateNm() {
        return recvSendCateNm;
    }

    /**
     * @param �շ��������
     * @date 2019��4��15��
     */
    public final void setRecvSendCateNm(String recvSendCateNm) {
        this.recvSendCateNm = recvSendCateNm;
    }

    /**
     * @return ��Ӧ�̿ͻ�����<! - - �ͻ�����-->
     * @date 2019��4��15��
     */
    public final String getCustNm() {
        return custNm;
    }

    /**
     * @param ��Ӧ�̿ͻ�����<!--�ͻ�����-->
     * @date 2019��4��15��
     */
    public final void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    /**
     * @return �������
     * @date 2019��4��15��
     */
    public final String getInvtyEncd() {
        return invtyEncd;
    }

    /**
     * @param �������
     * @date 2019��4��15��
     */
    public final void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    /**
     * @return �������
     * @date 2019��4��15��
     */
    public final String getInvtyNm() {
        return invtyNm;
    }

    /**
     * @param �������
     * @date 2019��4��15��
     */
    public final void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    /**
     * @return ����ͺ�
     * @date 2019��4��15��
     */
    public final String getSpcModel() {
        return spcModel;
    }

    /**
     * @param ����ͺ�
     * @date 2019��4��15��
     */
    public final void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    /**
     * @return <!--������λ����-->
     * @date 2019��4��15��
     */
    public final String getMeasrCorpNm() {
        return measrCorpNm;
    }

    /**
     * @param <!--������λ����-->
     * @date 2019��4��15��
     */
    public final void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

}
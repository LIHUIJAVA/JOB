package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.px.mis.purc.entity.InvtyDoc;

//�������ӱ�
public class CannibSnglSubTab {
    private Long ordrNum;// ���

    private String formNum;// ���ݺ�

    private String invtyId;// ������
    private String invtyEncd;// ������λ����

    private BigDecimal cannibQty;// ��������

    private BigDecimal recvQty;// ʵ������

    private String batNum;// ����

    private String invldtnDt;// ʧЧ����

    private String baoZhiQi;// ������

    private String prdcDt;// ��������

    private BigDecimal taxRate;// ˰��

    private BigDecimal cntnTaxUprc;// ��˰����

    private BigDecimal unTaxUprc;// δ˰����

    private BigDecimal cntnTaxAmt;// ��˰���

    private BigDecimal unTaxAmt;// δ˰���

    // �������
    private InvtyDoc invtyDoc;

    // �����������ֶΡ�������λ���Ƶ�
    private String invtyNm;// �������
    private String spcModel;// ����ͺ�
    private BigDecimal bxRule;// ���
    private BigDecimal bxQty;// ����
    private String measrCorpNm;// ������λ����

    // �����������ֶΡ�������λ���ơ��ֿ�����
//	private String invtyNm;// �������
//	private String spcModel;// ����ͺ�
    private String invtyCd;// �������
    //	private BigDecimal bxRule;// ���
    private String baoZhiQiDt;// ����������
    //	private BigDecimal iptaxRate;// ����˰��
//	private BigDecimal optaxRate;// ����˰��
//	private BigDecimal highestPurPrice;// ��߽���
//	private BigDecimal loSellPrc;// ����ۼ�
//	private BigDecimal refCost;// �ο��ɱ�
//	private BigDecimal refSellPrc;// �ο��ۼ�
//	private BigDecimal ltstCost;// ���³ɱ�
//	private String measrCorpNm;// ������λ����
//	private String whsNm;// �ֿ�����
    private String crspdBarCd;// ��Ӧ������
    private String projEncd;// ��Ŀ����
    private String projNm;// ��Ŀ����
    private BigDecimal taxAmt;//˰��

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    public String getProjNm() {
        return projNm;
    }

    public void setProjNm(String projNm) {
        this.projNm = projNm;
    }


    public String getProjEncd() {
        return projEncd;
    }

    public void setProjEncd(String projEncd) {
        this.projEncd = projEncd;
    }

    /**
     * @return invtyDoc
     * @date 2019��4��15��
     */
    public final InvtyDoc getInvtyDoc() {
        return invtyDoc;
    }

    /**
     * @param invtyDoc
     * @date 2019��4��15��
     */
    public final void setInvtyDoc(InvtyDoc invtyDoc) {
        this.invtyDoc = invtyDoc;
    }

    /**
     * @return invtyCd
     * @date 2019��4��15��
     */
    public final String getInvtyCd() {
        return invtyCd;
    }

    /**
     * @param invtyCd
     * @date 2019��4��15��
     */
    public final void setInvtyCd(String invtyCd) {
        this.invtyCd = invtyCd;
    }

    /**
     * @return baoZhiQiDt
     * @date 2019��4��15��
     */
    public final String getBaoZhiQiDt() {
        return baoZhiQiDt;
    }

    /**
     * @param baoZhiQiDt
     * @date 2019��4��15��
     */
    public final void setBaoZhiQiDt(String baoZhiQiDt) {
        this.baoZhiQiDt = baoZhiQiDt;
    }

    /**
     * @return crspdBarCd
     * @date 2019��4��15��
     */
    public final String getCrspdBarCd() {
        return crspdBarCd;
    }

    /**
     * @param crspdBarCd
     * @date 2019��4��15��
     */
    public final void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
    }

    public BigDecimal getBxQty() {
        return bxQty;
    }

    public void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
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

    public BigDecimal getBxRule() {
        return bxRule;
    }

    public void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
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

    public String getInvtyId() {
        return invtyId;
    }

    public void setInvtyId(String invtyId) {
        this.invtyId = invtyId == null ? null : invtyId.trim();
    }

    public BigDecimal getCannibQty() {
        return cannibQty;
    }

    public void setCannibQty(BigDecimal cannibQty) {
        this.cannibQty = cannibQty;
    }

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum == null ? null : batNum.trim();
    }

    public String getBaoZhiQi() {
        return baoZhiQi;
    }

    public void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi;
    }

    public String getInvldtnDt() {
        return invldtnDt;
    }

    public void setInvldtnDt(String invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public String getPrdcDt() {
        return prdcDt;
    }

    public void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getCntnTaxUprc() {
        return cntnTaxUprc;
    }

    public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
        this.cntnTaxUprc = cntnTaxUprc;
    }

    public BigDecimal getUnTaxUprc() {
        return unTaxUprc;
    }

    public void setUnTaxUprc(BigDecimal unTaxUprc) {
        this.unTaxUprc = unTaxUprc;
    }

    public BigDecimal getCntnTaxAmt() {
        return cntnTaxAmt;
    }

    public void setCntnTaxAmt(BigDecimal cntnTaxAmt) {
        this.cntnTaxAmt = cntnTaxAmt;
    }

    public BigDecimal getUnTaxAmt() {
        return unTaxAmt;
    }

    public void setUnTaxAmt(BigDecimal unTaxAmt) {
        this.unTaxAmt = unTaxAmt;
    }

    public BigDecimal getRecvQty() {
        return recvQty;
    }

    public void setRecvQty(BigDecimal recvQty) {
        this.recvQty = recvQty;
    }

}
package com.px.mis.whs.entity;

import java.math.BigDecimal;

//��װ��ж���ӱ�
public class AmbDisambSnglubTab {

    private String formNum;// ���ݺ�

    private String subKitEncd;// �����׼�����

    private Long ordrNum;// ���

    private String whsEncd;// �ֿ����

    private BigDecimal subQty;// �Ӽ�����

    private BigDecimal momQty;// ĸ������

    private BigDecimal momSubRatio;// ĸ�ӱ���

    private BigDecimal taxRate;// ˰��

    private BigDecimal scntnTaxUprc;// ��˰����

    private BigDecimal sunTaxUprc;// δ˰����

    private BigDecimal scntnTaxAmt;// ��˰���

    private BigDecimal sunTaxAmt;// δ˰���

    private String sbatNum;// ����

    private String sprdcDt;// ��������

    private String baoZhiQi;// ������

    private String sinvldtnDt;// ʧЧ����

//	private InvtyDoc invtyDoc;

    // �����������ֶΡ�������λ���ơ��ֿ�����
    private String invtyNm;// �������
    private String spcModel;// ����ͺ�
    private String invtyCd;// �������
    private BigDecimal bxRule;// ���
    private String baoZhiQiDt;// ����������
    private BigDecimal iptaxRate;// ����˰��
    private BigDecimal optaxRate;// ����˰��
    private BigDecimal highestPurPrice;// ��߽���
    private BigDecimal loSellPrc;// ����ۼ�
    private BigDecimal refCost;// �ο��ɱ�
    private BigDecimal refSellPrc;// �ο��ۼ�
    private BigDecimal ltstCost;// ���³ɱ�
    private String measrCorpNm;// ������λ����
    private String whsNm;// �ֿ�����
    private String crspdBarCd;// ��Ӧ������
    private BigDecimal sbxQty;// ����
    private String projEncd;// ��Ŀ����
    private String projNm;// ��Ŀ����
    private BigDecimal staxAmt;//˰��

    public BigDecimal getStaxAmt() {
        return staxAmt;
    }

    public void setStaxAmt(BigDecimal staxAmt) {
        this.staxAmt = staxAmt;
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

    public final BigDecimal getSbxQty() {
        return sbxQty;
    }

    public final void setSbxQty(BigDecimal sbxQty) {
        this.sbxQty = sbxQty;
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

    public String getInvtyCd() {
        return invtyCd;
    }

    public void setInvtyCd(String invtyCd) {
        this.invtyCd = invtyCd;
    }

    public BigDecimal getBxRule() {
        return bxRule;
    }

    public void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public String getBaoZhiQiDt() {
        return baoZhiQiDt;
    }

    public void setBaoZhiQiDt(String baoZhiQiDt) {
        this.baoZhiQiDt = baoZhiQiDt;
    }

    public BigDecimal getIptaxRate() {
        return iptaxRate;
    }

    public void setIptaxRate(BigDecimal iptaxRate) {
        this.iptaxRate = iptaxRate;
    }

    public BigDecimal getOptaxRate() {
        return optaxRate;
    }

    public void setOptaxRate(BigDecimal optaxRate) {
        this.optaxRate = optaxRate;
    }

    public BigDecimal getHighestPurPrice() {
        return highestPurPrice;
    }

    public void setHighestPurPrice(BigDecimal highestPurPrice) {
        this.highestPurPrice = highestPurPrice;
    }

    public BigDecimal getLoSellPrc() {
        return loSellPrc;
    }

    public void setLoSellPrc(BigDecimal loSellPrc) {
        this.loSellPrc = loSellPrc;
    }

    public BigDecimal getRefCost() {
        return refCost;
    }

    public void setRefCost(BigDecimal refCost) {
        this.refCost = refCost;
    }

    public BigDecimal getRefSellPrc() {
        return refSellPrc;
    }

    public void setRefSellPrc(BigDecimal refSellPrc) {
        this.refSellPrc = refSellPrc;
    }

    public BigDecimal getLtstCost() {
        return ltstCost;
    }

    public void setLtstCost(BigDecimal ltstCost) {
        this.ltstCost = ltstCost;
    }

    public String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public String getCrspdBarCd() {
        return crspdBarCd;
    }

    public void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
    }

    public String getSprdcDt() {
        return sprdcDt;
    }

    public void setSprdcDt(String sprdcDt) {
        this.sprdcDt = sprdcDt;
    }

    public String getSinvldtnDt() {
        return sinvldtnDt;
    }

    public void setSinvldtnDt(String sinvldtnDt) {
        this.sinvldtnDt = sinvldtnDt;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public String getSubKitEncd() {
        return subKitEncd;
    }

    public void setSubKitEncd(String subKitEncd) {
        this.subKitEncd = subKitEncd == null ? null : subKitEncd.trim();
    }

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd == null ? null : whsEncd.trim();
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

    public BigDecimal getMomSubRatio() {
        return momSubRatio;
    }

    public void setMomSubRatio(BigDecimal momSubRatio) {
        this.momSubRatio = momSubRatio;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getScntnTaxUprc() {
        return scntnTaxUprc;
    }

    public void setScntnTaxUprc(BigDecimal scntnTaxUprc) {
        this.scntnTaxUprc = scntnTaxUprc;
    }

    public BigDecimal getSunTaxUprc() {
        return sunTaxUprc;
    }

    public void setSunTaxUprc(BigDecimal sunTaxUprc) {
        this.sunTaxUprc = sunTaxUprc;
    }

    public BigDecimal getScntnTaxAmt() {
        return scntnTaxAmt;
    }

    public void setScntnTaxAmt(BigDecimal scntnTaxAmt) {
        this.scntnTaxAmt = scntnTaxAmt;
    }

    public BigDecimal getSunTaxAmt() {
        return sunTaxAmt;
    }

    public void setSunTaxAmt(BigDecimal sunTaxAmt) {
        this.sunTaxAmt = sunTaxAmt;
    }

    public String getSbatNum() {
        return sbatNum;
    }

    public void setSbatNum(String sbatNum) {
        this.sbatNum = sbatNum == null ? null : sbatNum.trim();
    }

    public String getBaoZhiQi() {
        return baoZhiQi;
    }

    public void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi == null ? null : baoZhiQi.trim();
    }

}
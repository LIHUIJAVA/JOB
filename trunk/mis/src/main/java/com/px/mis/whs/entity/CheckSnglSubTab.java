package com.px.mis.whs.entity;

import java.math.BigDecimal;
import java.util.List;

import com.px.mis.purc.entity.InvtyDoc;

//�̵㵥�ӱ�
public class CheckSnglSubTab {


    private Long ordrNum;//���

    private String checkFormNum;//�̵㵥��

    private String invtyEncd;//�������

    private String invtyBigClsEncd;//����������

    private String gdsBitEncd;//��λ����

    private String gdsBitNm;//��λ����

    private String barCd;//������

    private String batNum;//����

    private String prdcDt;//��������

    private String baoZhiQi;//������

    private String invldtnDt;//ʧЧ����

    private BigDecimal bookQty;//��������

    private BigDecimal checkQty;//�̵�����

    private BigDecimal prftLossQty;//ӯ������

    private BigDecimal bookAdjustQty;//�����������

    private BigDecimal adjIntoWhsQty;//�����������

    private BigDecimal prftLossRatio;//ӯ������(%)

    private BigDecimal adjOutWhsQty;//������������

    private String reasn;//ԭ��


    //�������
    private InvtyDoc invtyDoc;

    private String invtyClsEncd;//����������
    private String baoZhiQiEarWarn;//������Ԥ������


    // // �����������ֶΡ�������λ���ơ��ֿ�����
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

    //    private List<CheckGdsBit> checkGdsBit;//��λ��λʵ��
    private String projEncd;// ��Ŀ����
    private String projNm;// ��Ŀ����


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


    public String getGdsBitNm() {
        return gdsBitNm;
    }


    public void setGdsBitNm(String gdsBitNm) {
        this.gdsBitNm = gdsBitNm;
    }


    public final String getGdsBitEncd() {
        return gdsBitEncd;
    }


    public final void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd;
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


    public String getInvtyClsEncd() {
        return invtyClsEncd;
    }

    public void setInvtyClsEncd(String invtyClsEncd) {
        this.invtyClsEncd = invtyClsEncd;
    }

    public String getBaoZhiQiEarWarn() {
        return baoZhiQiEarWarn;
    }

    public void setBaoZhiQiEarWarn(String baoZhiQiEarWarn) {
        this.baoZhiQiEarWarn = baoZhiQiEarWarn;
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

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getCheckFormNum() {
        return checkFormNum;
    }

    public void setCheckFormNum(String checkFormNum) {
        this.checkFormNum = checkFormNum == null ? null : checkFormNum.trim();
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd == null ? null : invtyEncd.trim();
    }

    public String getInvtyBigClsEncd() {
        return invtyBigClsEncd;
    }

    public void setInvtyBigClsEncd(String invtyBigClsEncd) {
        this.invtyBigClsEncd = invtyBigClsEncd == null ? null : invtyBigClsEncd.trim();
    }

    public String getBarCd() {
        return barCd;
    }

    public void setBarCd(String barCd) {
        this.barCd = barCd == null ? null : barCd.trim();
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

    public BigDecimal getBookQty() {
        return bookQty;
    }

    public void setBookQty(BigDecimal bookQty) {
        this.bookQty = bookQty;
    }

    public BigDecimal getCheckQty() {
        return checkQty;
    }

    public void setCheckQty(BigDecimal checkQty) {
        this.checkQty = checkQty;
    }

    public BigDecimal getPrftLossQty() {
        return prftLossQty;
    }

    public void setPrftLossQty(BigDecimal prftLossQty) {
        this.prftLossQty = prftLossQty;
    }

    public BigDecimal getBookAdjustQty() {
        return bookAdjustQty;
    }

    public void setBookAdjustQty(BigDecimal bookAdjustQty) {
        this.bookAdjustQty = bookAdjustQty;
    }

    public BigDecimal getAdjIntoWhsQty() {
        return adjIntoWhsQty;
    }

    public void setAdjIntoWhsQty(BigDecimal adjIntoWhsQty) {
        this.adjIntoWhsQty = adjIntoWhsQty;
    }

    public BigDecimal getPrftLossRatio() {
        return prftLossRatio;
    }

    public void setPrftLossRatio(BigDecimal prftLossRatio) {
        this.prftLossRatio = prftLossRatio;
    }

    public BigDecimal getAdjOutWhsQty() {
        return adjOutWhsQty;
    }

    public void setAdjOutWhsQty(BigDecimal adjOutWhsQty) {
        this.adjOutWhsQty = adjOutWhsQty;
    }

    public String getReasn() {
        return reasn;
    }

    public void setReasn(String reasn) {
        this.reasn = reasn == null ? null : reasn.trim();
    }

}
package com.px.mis.whs.entity;

import java.math.BigDecimal;


import com.px.mis.purc.entity.InvtyDoc;

//�̵������ӱ�
public class CheckPrftLossSubTab {
    private Long ordrNum;//���

    private String checkFormNum;//�̵㵥��

    private String invtyEncd;//�������

    private String invtyBigClsEncd;//����������

    private String gdsBitEncd;//��λ����

    private String gdsBitNm;//��λ����


    private String batNum;//����

    private String prdcDt;//��������

    private String baoZhiQi;//������

    private String invldtnDt;//ʧЧ����

    private BigDecimal bookQty;//��������

    private BigDecimal checkQty;//�̵�����

    private BigDecimal prftLossQty;//ӯ������

    private BigDecimal prftLossQtys;//��������

    private BigDecimal bookAdjustQty;//�����������

    private BigDecimal adjIntoWhsQty;//�����������

    private BigDecimal prftLossRatio;//ӯ������(%)

    private BigDecimal adjOutWhsQty;//������������

    private String reasn;//ԭ��

    private BigDecimal taxRate;//˰��

    private BigDecimal cntnTaxUprc;//��˰����

    private BigDecimal unTaxUprc;//δ˰����

    private BigDecimal cntnTaxBookAmt;//��˰������

    private BigDecimal unTaxBookAmt;//δ˰������

    private BigDecimal cntnTaxCheckAmt;//��˰�̵���

    private BigDecimal unTaxCheckAmt;//δ˰�̵���

    private BigDecimal cntnTaxPrftLossAmt;//��˰ӯ�����

    private BigDecimal unTaxPrftLossAmt;//δ˰ӯ�����

    private BigDecimal cntnTaxPrftLossAmts;//��˰������

    private BigDecimal unTaxPrftLossAmts;//δ˰������

    private BigDecimal bookTaxAmt;//����˰��
    private BigDecimal checkTaxAmt;//�̵�˰��
    private BigDecimal prftLossTaxAmt;//ӯ��˰��
    private BigDecimal prftLossTaxAmts;//����˰��
    //    private InvtyDoc invtyDoc;
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
    private String projEncd;// ��Ŀ����
    private String projNm;// ��Ŀ����

    public BigDecimal getBookTaxAmt() {
        return bookTaxAmt;
    }

    public void setBookTaxAmt(BigDecimal bookTaxAmt) {
        this.bookTaxAmt = bookTaxAmt;
    }

    public BigDecimal getCheckTaxAmt() {
        return checkTaxAmt;
    }

    public void setCheckTaxAmt(BigDecimal checkTaxAmt) {
        this.checkTaxAmt = checkTaxAmt;
    }

    public BigDecimal getPrftLossTaxAmt() {
        return prftLossTaxAmt;
    }

    public void setPrftLossTaxAmt(BigDecimal prftLossTaxAmt) {
        this.prftLossTaxAmt = prftLossTaxAmt;
    }

    public BigDecimal getPrftLossTaxAmts() {
        return prftLossTaxAmts;
    }

    public void setPrftLossTaxAmts(BigDecimal prftLossTaxAmts) {
        this.prftLossTaxAmts = prftLossTaxAmts;
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
        this.baoZhiQi = baoZhiQi == null ? null : baoZhiQi.trim();
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

    public BigDecimal getPrftLossQtys() {
        return prftLossQtys;
    }

    public void setPrftLossQtys(BigDecimal prftLossQtys) {
        this.prftLossQtys = prftLossQtys;
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

    public BigDecimal getCntnTaxBookAmt() {
        return cntnTaxBookAmt;
    }

    public void setCntnTaxBookAmt(BigDecimal cntnTaxBookAmt) {
        this.cntnTaxBookAmt = cntnTaxBookAmt;
    }

    public BigDecimal getUnTaxBookAmt() {
        return unTaxBookAmt;
    }

    public void setUnTaxBookAmt(BigDecimal unTaxBookAmt) {
        this.unTaxBookAmt = unTaxBookAmt;
    }

    public BigDecimal getCntnTaxCheckAmt() {
        return cntnTaxCheckAmt;
    }

    public void setCntnTaxCheckAmt(BigDecimal cntnTaxCheckAmt) {
        this.cntnTaxCheckAmt = cntnTaxCheckAmt;
    }

    public BigDecimal getUnTaxCheckAmt() {
        return unTaxCheckAmt;
    }

    public void setUnTaxCheckAmt(BigDecimal unTaxCheckAmt) {
        this.unTaxCheckAmt = unTaxCheckAmt;
    }

    public BigDecimal getCntnTaxPrftLossAmt() {
        return cntnTaxPrftLossAmt;
    }

    public void setCntnTaxPrftLossAmt(BigDecimal cntnTaxPrftLossAmt) {
        this.cntnTaxPrftLossAmt = cntnTaxPrftLossAmt;
    }

    public BigDecimal getUnTaxPrftLossAmt() {
        return unTaxPrftLossAmt;
    }

    public void setUnTaxPrftLossAmt(BigDecimal unTaxPrftLossAmt) {
        this.unTaxPrftLossAmt = unTaxPrftLossAmt;
    }

    public BigDecimal getCntnTaxPrftLossAmts() {
        return cntnTaxPrftLossAmts;
    }

    public void setCntnTaxPrftLossAmts(BigDecimal cntnTaxPrftLossAmts) {
        this.cntnTaxPrftLossAmts = cntnTaxPrftLossAmts;
    }

    public BigDecimal getUnTaxPrftLossAmts() {
        return unTaxPrftLossAmts;
    }

    public void setUnTaxPrftLossAmts(BigDecimal unTaxPrftLossAmts) {
        this.unTaxPrftLossAmts = unTaxPrftLossAmts;
    }


}
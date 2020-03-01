package com.px.mis.whs.entity;

import java.math.BigDecimal;
import java.util.List;

//��װ��ж��
public class AmbDisambSngl {
    private String momKitEncd;// ĸ���׼�����

    private Long ordrNum;// ���

    private String formNum;// ���ݺ�

    private String formDt;// ��������
    private String formDt1;// ��������1
    private String formDt2;// ��������2

    private String formTyp;// ��������(��װ����ж)

    private BigDecimal fee;// ����(�ܵ�)

    private String feeComnt;// ����˵��

    private String memo;// ��ע

    private String typ;// ����(�׼���ɢ��)

    private String whsEncd;// �ֿ����

    private BigDecimal momQty;// ĸ������

    private BigDecimal taxRate;// ˰��

    private BigDecimal mcntnTaxUprc;// ��˰����

    private BigDecimal munTaxUprc;// δ˰����

    private BigDecimal mcntnTaxAmt;// ��˰���

    private BigDecimal munTaxAmt;// δ˰���

    private String mbatNum;// ����

    private String mprdcDt;// ��������

    private String baoZhiQi;// ������

    private String invldtnDt;// ʧЧ����

    private Integer isNtWms;// �Ƿ���WMS�ϴ�

    private Integer isNtChk;// �Ƿ����

    private Integer isNtBookEntry;// �Ƿ����

    private Integer isNtCmplt;// �Ƿ����

    private Integer isNtClos;// �Ƿ�ر�

    private Integer printCnt;// ��ӡ����

    private String setupPers;// ������

    private String setupTm;// ����ʱ��

    private String mdfr;// �޸���

    private String modiTm;// �޸�ʱ��

    private String chkr;// �����

    private String chkTm;// ���ʱ��

    private String bookEntryPers;// ������

    private String bookEntryTm;// ����ʱ��
    private BigDecimal mtaxAmt;//˰��
    private List<AmbDisambSnglubTab> ambSnglubList;

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
    private String formTypEncd;// �������ͱ���
    private String formTypName;//������������
    private BigDecimal bxQty;// ����

    private String mprojEncd;// ��Ŀ����
    private String mprojNm;// ��Ŀ����

    public String getMprojEncd() {
        return mprojEncd;
    }

    public void setMprojEncd(String mprojEncd) {
        this.mprojEncd = mprojEncd;
    }

    public String getMprojNm() {
        return mprojNm;
    }

    public void setMprojNm(String mprojNm) {
        this.mprojNm = mprojNm;
    }

    public BigDecimal getMtaxAmt() {
        return mtaxAmt;
    }

    public void setMtaxAmt(BigDecimal mtaxAmt) {
        this.mtaxAmt = mtaxAmt;
    }

    public final BigDecimal getBxQty() {
        return bxQty;
    }

    public final void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
    }

    public String getFormTypName() {
        return formTypName;
    }

    public void setFormTypName(String formTypName) {
        this.formTypName = formTypName;
    }

    public String getFormTypEncd() {
        return formTypEncd;
    }

    public void setFormTypEncd(String formTypEncd) {
        this.formTypEncd = formTypEncd;
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

    public String getMomKitEncd() {
        return momKitEncd;
    }

    public void setMomKitEncd(String momKitEncd) {
        this.momKitEncd = momKitEncd;
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
        this.formNum = formNum;
    }

    public String getFormDt() {
        return formDt;
    }

    public void setFormDt(String formDt) {
        this.formDt = formDt;
    }

    public String getFormDt1() {
        return formDt1;
    }

    public void setFormDt1(String formDt1) {
        this.formDt1 = formDt1;
    }

    public String getFormDt2() {
        return formDt2;
    }

    public void setFormDt2(String formDt2) {
        this.formDt2 = formDt2;
    }

    public String getFormTyp() {
        return formTyp;
    }

    public void setFormTyp(String formTyp) {
        this.formTyp = formTyp;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getFeeComnt() {
        return feeComnt;
    }

    public void setFeeComnt(String feeComnt) {
        this.feeComnt = feeComnt;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public BigDecimal getMomQty() {
        return momQty;
    }

    public void setMomQty(BigDecimal momQty) {
        this.momQty = momQty;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getMcntnTaxUprc() {
        return mcntnTaxUprc;
    }

    public void setMcntnTaxUprc(BigDecimal mcntnTaxUprc) {
        this.mcntnTaxUprc = mcntnTaxUprc;
    }

    public BigDecimal getMunTaxUprc() {
        return munTaxUprc;
    }

    public void setMunTaxUprc(BigDecimal munTaxUprc) {
        this.munTaxUprc = munTaxUprc;
    }

    public BigDecimal getMcntnTaxAmt() {
        return mcntnTaxAmt;
    }

    public void setMcntnTaxAmt(BigDecimal mcntnTaxAmt) {
        this.mcntnTaxAmt = mcntnTaxAmt;
    }

    public BigDecimal getMunTaxAmt() {
        return munTaxAmt;
    }

    public void setMunTaxAmt(BigDecimal munTaxAmt) {
        this.munTaxAmt = munTaxAmt;
    }

    public String getMbatNum() {
        return mbatNum;
    }

    public void setMbatNum(String mbatNum) {
        this.mbatNum = mbatNum;
    }

    public String getMprdcDt() {
        return mprdcDt;
    }

    public void setMprdcDt(String mprdcDt) {
        this.mprdcDt = mprdcDt;
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

    public Integer getIsNtWms() {
        return isNtWms;
    }

    public void setIsNtWms(Integer isNtWms) {
        this.isNtWms = isNtWms;
    }

    public Integer getIsNtChk() {
        return isNtChk;
    }

    public void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public Integer getIsNtBookEntry() {
        return isNtBookEntry;
    }

    public void setIsNtBookEntry(Integer isNtBookEntry) {
        this.isNtBookEntry = isNtBookEntry;
    }

    public Integer getIsNtCmplt() {
        return isNtCmplt;
    }

    public void setIsNtCmplt(Integer isNtCmplt) {
        this.isNtCmplt = isNtCmplt;
    }

    public Integer getIsNtClos() {
        return isNtClos;
    }

    public void setIsNtClos(Integer isNtClos) {
        this.isNtClos = isNtClos;
    }

    public Integer getPrintCnt() {
        return printCnt;
    }

    public void setPrintCnt(Integer printCnt) {
        this.printCnt = printCnt;
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers;
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }

    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr;
    }

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public String getBookEntryPers() {
        return bookEntryPers;
    }

    public void setBookEntryPers(String bookEntryPers) {
        this.bookEntryPers = bookEntryPers;
    }

    public String getBookEntryTm() {
        return bookEntryTm;
    }

    public void setBookEntryTm(String bookEntryTm) {
        this.bookEntryTm = bookEntryTm;
    }

    public List<AmbDisambSnglubTab> getAmbSnglubList() {
        return ambSnglubList;
    }

    public void setAmbSnglubList(List<AmbDisambSnglubTab> ambSnglubList) {
        this.ambSnglubList = ambSnglubList;
    }

}
package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//��װ��ж��
public class AmbDisambSnglMap {

//	@JsonProperty("ĸ���׼�����") 
//	private String momKitEncd;// ĸ���׼�����
//	private String formNum;// ���ݺ�
//	private String formDt;// ��������
//	private String formTyp;// ��������(��װ����ж)
//	private BigDecimal fee;// ����(�ܵ�)
//	private String feeComnt;// ����˵��
//	private String memo;// ��ע
//	private String typ;// ����(�׼���ɢ��)
//	private String whsEncd;// �ֿ����
//	private BigDecimal momQty;// ĸ������
//	private BigDecimal taxRate;// ˰��
//	private BigDecimal mcntnTaxUprc;// ��˰����
//	private BigDecimal munTaxUprc;// δ˰����
//	private BigDecimal mcntnTaxAmt;// ��˰���
//	private BigDecimal munTaxAmt;// δ˰���
//	private String mbatNum;// ����
//	private String mprdcDt;// ��������
//	private String baoZhiQi;// ������
//	private String invldtnDt;// ʧЧ����
//	private Integer isNtWms;// �Ƿ���WMS�ϴ�
//	private Integer isNtChk;// �Ƿ����
//	private Integer isNtBookEntry;// �Ƿ����
//	private Integer isNtCmplt;// �Ƿ����
//	private Integer isNtClos;// �Ƿ�ر�
//	private Integer printCnt;// ��ӡ����
//	private String setupPers;// ������
//	private String setupTm;// ����ʱ��
//	private String mdfr;// �޸���
//	private String modiTm;// �޸�ʱ��
//	private String chkr;// �����
//	private String chkTm;// ���ʱ��
//	private String bookEntryPers;// ������
//	private String bookEntryTm;// ����ʱ��
//
//	// �����������ֶΡ�������λ���ơ��ֿ�����
//	private String invtyNm;// �������
//	private String spcModel;// ����ͺ�
//	private String invtyCd;// �������
//	private BigDecimal bxRule;// ���
//	private BigDecimal bxQty;// ����
//
//	private String baoZhiQiDt;// ����������
//	private String measrCorpNm;// ������λ����
//	private String whsNm;// �ֿ�����
//	private String crspdBarCd;// ��Ӧ������
//	private String formTypEncd;// �������ͱ���
//	private String formTypName;// ������������
//	// ��
//	private Long ordrNum;// ���
//	private String subKitEncd;// �����׼�����
//	private String swhsEncd;// �ֿ����
//	private BigDecimal subQty;// �Ӽ�����
//	private BigDecimal momSubRatio;// ĸ�ӱ���
//	private BigDecimal staxRate;// ˰��
//	private BigDecimal scntnTaxUprc;// ��˰����
//	private BigDecimal sunTaxUprc;// δ˰����
//	private BigDecimal scntnTaxAmt;// ��˰���
//	private BigDecimal sunTaxAmt;// δ˰���
//	private String sbatNum;// ����
//	private String sprdcDt;// ��������
//	private String sbaoZhiQi;// ������
//	private String sinvldtnDt;// ʧЧ����
//	// �����������ֶΡ�������λ���ơ��ֿ�����
//	private String sinvtyNm;// �������
//	private String sspcModel;// ����ͺ�
//	private String sinvtyCd;// �������
//	private BigDecimal sbxRule;// ���
//	private BigDecimal sbxQty;// ����
//
//	private String sbaoZhiQiDt;// ����������
//	private String smeasrCorpNm;// ������λ����
//	private String swhsNm;// �ֿ�����
//	private String scrspdBarCd;// ��Ӧ������
//	

    @JsonProperty("ĸ������")
    private String momKitEncd;// ĸ���׼�����
    @JsonProperty("���ݺ�")
    private String formNum;// ���ݺ�
    @JsonProperty("��������")
    private String formDt;// ��������
    @JsonProperty("��������(��װ����ж)")
    private String formTyp;// ��������(��װ����ж)
    @JsonProperty("����")
    private BigDecimal fee;// ����(�ܵ�)
    @JsonProperty("����˵��")
    private String feeComnt;// ����˵��
    @JsonProperty("��ע")
    private String memo;// ��ע
    @JsonProperty("����(�׼���ɢ��)")
    private String typ;// ����(�׼���ɢ��)
    @JsonProperty("ĸ���ֿ����")
    private String whsEncd;// �ֿ����
    @JsonProperty("ĸ������")
    private BigDecimal momQty;// ĸ������
    @JsonProperty("ĸ��˰��")
    private BigDecimal taxRate;// ˰��
    @JsonProperty("ĸ����˰����")
    private BigDecimal mcntnTaxUprc;// ��˰����
    @JsonProperty("ĸ��δ˰����")
    private BigDecimal munTaxUprc;// δ˰����
    @JsonProperty("ĸ����˰���")
    private BigDecimal mcntnTaxAmt;// ��˰���
    @JsonProperty("ĸ��δ˰���")
    private BigDecimal munTaxAmt;// δ˰���
    @JsonProperty("ĸ������")
    private String mbatNum;// ����
    @JsonProperty("ĸ����������")
    private String mprdcDt;// ��������
    @JsonProperty("ĸ��������")
    private String baoZhiQi;// ������
    @JsonProperty("ĸ��ʧЧ����")
    private String invldtnDt;// ʧЧ����
    @JsonProperty("ĸ��˰��")
    private BigDecimal mtaxAmt;//˰��
    @JsonProperty("ĸ����Ŀ����")
    private String mprojEncd;// ��Ŀ����
    @JsonProperty("ĸ����Ŀ����")
    private String mprojNm;// ��Ŀ����
    //	@JsonProperty("�Ƿ���WMS�ϴ�")
    @JsonIgnore
    private Integer isNtWms;// �Ƿ���WMS�ϴ�
    @JsonProperty("�Ƿ����")
    private Integer isNtChk;// �Ƿ����
    	@JsonProperty("�Ƿ����")
//    @JsonIgnore
    private Integer isNtBookEntry;// �Ƿ����
    //	@JsonProperty("�Ƿ����")
    @JsonIgnore
    private Integer isNtCmplt;// �Ƿ����
    //	@JsonProperty("�Ƿ�ر�")
    @JsonIgnore
    private Integer isNtClos;// �Ƿ�ر�
    //	@JsonProperty("��ӡ����")
    @JsonIgnore
    private Integer printCnt;// ��ӡ����
    @JsonProperty("������")
    private String setupPers;// ������
    @JsonProperty("����ʱ��")
    private String setupTm;// ����ʱ��
    @JsonProperty("�޸���")
    private String mdfr;// �޸���
    @JsonProperty("�޸�ʱ��")
    private String modiTm;// �޸�ʱ��
    @JsonProperty("�����")
    private String chkr;// �����
    @JsonProperty("���ʱ��")
    private String chkTm;// ���ʱ��
    @JsonProperty("������")
    private String bookEntryPers;// ������
    @JsonProperty("����ʱ��")
    private String bookEntryTm;// ����ʱ��
    @JsonProperty("ĸ���������")
    private String invtyNm;// �������
    @JsonProperty("ĸ������ͺ�")
    private String spcModel;// ����ͺ�
    @JsonProperty("ĸ���������")
    private String invtyCd;// �������
    @JsonProperty("ĸ�����")
    private BigDecimal bxRule;// ���
    @JsonProperty("ĸ������")
    private BigDecimal bxQty;// ����
    @JsonProperty("ĸ������������")
    private String baoZhiQiDt;// ����������
    @JsonProperty("ĸ��������λ����")
    private String measrCorpNm;// ������λ����
    @JsonProperty("ĸ���ֿ�����")
    private String whsNm;// �ֿ�����
    @JsonProperty("ĸ��������")
    private String crspdBarCd;// ��Ӧ������
    @JsonProperty("�������ͱ���")
    private String formTypEncd;// �������ͱ���
    @JsonProperty("������������")
    private String formTypName;// ������������
    //	@JsonProperty("���")
    @JsonIgnore
    private Long ordrNum;// ���
    @JsonProperty("�Ӽ�����")
    private String subKitEncd;// �Ӽ�����
    @JsonProperty("�Ӽ��ֿ����")
    private String swhsEncd;// �ֿ����
    @JsonProperty("�Ӽ�����")
    private BigDecimal subQty;// �Ӽ�����
    @JsonProperty("ĸ�ӱ���")
    private BigDecimal momSubRatio;// ĸ�ӱ���
    @JsonProperty("�Ӽ�˰��")
    private BigDecimal staxRate;// ˰��
    @JsonProperty("�Ӽ���˰����")
    private BigDecimal scntnTaxUprc;// ��˰����
    @JsonProperty("�Ӽ�δ˰����")
    private BigDecimal sunTaxUprc;// δ˰����
    @JsonProperty("�Ӽ���˰���")
    private BigDecimal scntnTaxAmt;// ��˰���
    @JsonProperty("�Ӽ�δ˰���")
    private BigDecimal sunTaxAmt;// δ˰���
    @JsonProperty("�Ӽ�˰��")
    private BigDecimal staxAmt;//˰��
    @JsonProperty("�Ӽ�����")
    private String sbatNum;// ����
    @JsonProperty("�Ӽ���������")
    private String sprdcDt;// ��������
    @JsonProperty("�Ӽ�������")
    private String sbaoZhiQi;// ������
    @JsonProperty("�Ӽ�ʧЧ����")
    private String sinvldtnDt;// ʧЧ����
    @JsonProperty("�Ӽ��������")
    private String sinvtyNm;// �������
    @JsonProperty("�Ӽ�����ͺ�")
    private String sspcModel;// ����ͺ�
    @JsonProperty("�Ӽ��������")
    private String sinvtyCd;// �������
    @JsonProperty("�Ӽ����")
    private BigDecimal sbxRule;// ���
    @JsonProperty("�Ӽ�����")
    private BigDecimal sbxQty;// ����
    @JsonProperty("�Ӽ�����������")
    private String sbaoZhiQiDt;// ����������
    @JsonProperty("�Ӽ�������λ����")
    private String smeasrCorpNm;// ������λ����
    @JsonProperty("�Ӽ��ֿ�����")
    private String swhsNm;// �ֿ�����
    @JsonProperty("�Ӽ�������")
    private String scrspdBarCd;// ��Ӧ������
    @JsonProperty("�Ӽ���Ŀ����")
    private String projEncd;// ��Ŀ����
    @JsonProperty("�Ӽ���Ŀ����")
    private String projNm;// ��Ŀ����

    public BigDecimal getMtaxAmt() {
        return mtaxAmt;
    }

    public void setMtaxAmt(BigDecimal mtaxAmt) {
        this.mtaxAmt = mtaxAmt;
    }

    public BigDecimal getStaxAmt() {
        return staxAmt;
    }

    public void setStaxAmt(BigDecimal staxAmt) {
        this.staxAmt = staxAmt;
    }

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

    public String getProjEncd() {
        return projEncd;
    }

    public void setProjEncd(String projEncd) {
        this.projEncd = projEncd;
    }

    public String getProjNm() {
        return projNm;
    }

    public void setProjNm(String projNm) {
        this.projNm = projNm;
    }

    public final BigDecimal getBxQty() {
        return bxQty;
    }

    public final void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
    }

    public final BigDecimal getSbxQty() {
        return sbxQty;
    }

    public final void setSbxQty(BigDecimal sbxQty) {
        this.sbxQty = sbxQty;
    }

    public final String getMomKitEncd() {
        return momKitEncd;
    }

    public final void setMomKitEncd(String momKitEncd) {
        this.momKitEncd = momKitEncd;
    }

    public final String getFormNum() {
        return formNum;
    }

    public final void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public final String getFormDt() {
        return formDt;
    }

    public final void setFormDt(String formDt) {
        this.formDt = formDt;
    }

    public final String getFormTyp() {
        return formTyp;
    }

    public final void setFormTyp(String formTyp) {
        this.formTyp = formTyp;
    }

    public final BigDecimal getFee() {
        return fee;
    }

    public final void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public final String getFeeComnt() {
        return feeComnt;
    }

    public final void setFeeComnt(String feeComnt) {
        this.feeComnt = feeComnt;
    }

    public final String getMemo() {
        return memo;
    }

    public final void setMemo(String memo) {
        this.memo = memo;
    }

    public final String getTyp() {
        return typ;
    }

    public final void setTyp(String typ) {
        this.typ = typ;
    }

    public final String getWhsEncd() {
        return whsEncd;
    }

    public final void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public final BigDecimal getMomQty() {
        return momQty;
    }

    public final void setMomQty(BigDecimal momQty) {
        this.momQty = momQty;
    }

    public final BigDecimal getTaxRate() {
        return taxRate;
    }

    public final void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public final BigDecimal getMcntnTaxUprc() {
        return mcntnTaxUprc;
    }

    public final void setMcntnTaxUprc(BigDecimal mcntnTaxUprc) {
        this.mcntnTaxUprc = mcntnTaxUprc;
    }

    public final BigDecimal getMunTaxUprc() {
        return munTaxUprc;
    }

    public final void setMunTaxUprc(BigDecimal munTaxUprc) {
        this.munTaxUprc = munTaxUprc;
    }

    public final BigDecimal getMcntnTaxAmt() {
        return mcntnTaxAmt;
    }

    public final void setMcntnTaxAmt(BigDecimal mcntnTaxAmt) {
        this.mcntnTaxAmt = mcntnTaxAmt;
    }

    public final BigDecimal getMunTaxAmt() {
        return munTaxAmt;
    }

    public final void setMunTaxAmt(BigDecimal munTaxAmt) {
        this.munTaxAmt = munTaxAmt;
    }

    public final String getMbatNum() {
        return mbatNum;
    }

    public final void setMbatNum(String mbatNum) {
        this.mbatNum = mbatNum;
    }

    public final String getMprdcDt() {
        return mprdcDt;
    }

    public final void setMprdcDt(String mprdcDt) {
        this.mprdcDt = mprdcDt;
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

    public final Integer getIsNtWms() {
        return isNtWms;
    }

    public final void setIsNtWms(Integer isNtWms) {
        this.isNtWms = isNtWms;
    }

    public final Integer getIsNtChk() {
        return isNtChk;
    }

    public final void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public final Integer getIsNtBookEntry() {
        return isNtBookEntry;
    }

    public final void setIsNtBookEntry(Integer isNtBookEntry) {
        this.isNtBookEntry = isNtBookEntry;
    }

    public final Integer getIsNtCmplt() {
        return isNtCmplt;
    }

    public final void setIsNtCmplt(Integer isNtCmplt) {
        this.isNtCmplt = isNtCmplt;
    }

    public final Integer getIsNtClos() {
        return isNtClos;
    }

    public final void setIsNtClos(Integer isNtClos) {
        this.isNtClos = isNtClos;
    }

    public final Integer getPrintCnt() {
        return printCnt;
    }

    public final void setPrintCnt(Integer printCnt) {
        this.printCnt = printCnt;
    }

    public final String getSetupPers() {
        return setupPers;
    }

    public final void setSetupPers(String setupPers) {
        this.setupPers = setupPers;
    }

    public final String getSetupTm() {
        return setupTm;
    }

    public final void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public final String getMdfr() {
        return mdfr;
    }

    public final void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public final String getModiTm() {
        return modiTm;
    }

    public final void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }

    public final String getChkr() {
        return chkr;
    }

    public final void setChkr(String chkr) {
        this.chkr = chkr;
    }

    public final String getChkTm() {
        return chkTm;
    }

    public final void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public final String getBookEntryPers() {
        return bookEntryPers;
    }

    public final void setBookEntryPers(String bookEntryPers) {
        this.bookEntryPers = bookEntryPers;
    }

    public final String getBookEntryTm() {
        return bookEntryTm;
    }

    public final void setBookEntryTm(String bookEntryTm) {
        this.bookEntryTm = bookEntryTm;
    }

    public final String getInvtyNm() {
        return invtyNm;
    }

    public final void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public final String getSpcModel() {
        return spcModel;
    }

    public final void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    public final String getInvtyCd() {
        return invtyCd;
    }

    public final void setInvtyCd(String invtyCd) {
        this.invtyCd = invtyCd;
    }

    public final BigDecimal getBxRule() {
        return bxRule;
    }

    public final void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public final String getBaoZhiQiDt() {
        return baoZhiQiDt;
    }

    public final void setBaoZhiQiDt(String baoZhiQiDt) {
        this.baoZhiQiDt = baoZhiQiDt;
    }

    public final String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public final void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public final String getWhsNm() {
        return whsNm;
    }

    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public final String getCrspdBarCd() {
        return crspdBarCd;
    }

    public final void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
    }

    public final String getFormTypEncd() {
        return formTypEncd;
    }

    public final void setFormTypEncd(String formTypEncd) {
        this.formTypEncd = formTypEncd;
    }

    public final String getFormTypName() {
        return formTypName;
    }

    public final void setFormTypName(String formTypName) {
        this.formTypName = formTypName;
    }

    public final Long getOrdrNum() {
        return ordrNum;
    }

    public final void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public final String getSubKitEncd() {
        return subKitEncd;
    }

    public final void setSubKitEncd(String subKitEncd) {
        this.subKitEncd = subKitEncd;
    }

    public final String getSwhsEncd() {
        return swhsEncd;
    }

    public final void setSwhsEncd(String swhsEncd) {
        this.swhsEncd = swhsEncd;
    }

    public final BigDecimal getSubQty() {
        return subQty;
    }

    public final void setSubQty(BigDecimal subQty) {
        this.subQty = subQty;
    }

    public final BigDecimal getMomSubRatio() {
        return momSubRatio;
    }

    public final void setMomSubRatio(BigDecimal momSubRatio) {
        this.momSubRatio = momSubRatio;
    }

    public final BigDecimal getStaxRate() {
        return staxRate;
    }

    public final void setStaxRate(BigDecimal staxRate) {
        this.staxRate = staxRate;
    }

    public final BigDecimal getScntnTaxUprc() {
        return scntnTaxUprc;
    }

    public final void setScntnTaxUprc(BigDecimal scntnTaxUprc) {
        this.scntnTaxUprc = scntnTaxUprc;
    }

    public final BigDecimal getSunTaxUprc() {
        return sunTaxUprc;
    }

    public final void setSunTaxUprc(BigDecimal sunTaxUprc) {
        this.sunTaxUprc = sunTaxUprc;
    }

    public final BigDecimal getScntnTaxAmt() {
        return scntnTaxAmt;
    }

    public final void setScntnTaxAmt(BigDecimal scntnTaxAmt) {
        this.scntnTaxAmt = scntnTaxAmt;
    }

    public final BigDecimal getSunTaxAmt() {
        return sunTaxAmt;
    }

    public final void setSunTaxAmt(BigDecimal sunTaxAmt) {
        this.sunTaxAmt = sunTaxAmt;
    }

    public final String getSbatNum() {
        return sbatNum;
    }

    public final void setSbatNum(String sbatNum) {
        this.sbatNum = sbatNum;
    }

    public final String getSprdcDt() {
        return sprdcDt;
    }

    public final void setSprdcDt(String sprdcDt) {
        this.sprdcDt = sprdcDt;
    }

    public final String getSbaoZhiQi() {
        return sbaoZhiQi;
    }

    public final void setSbaoZhiQi(String sbaoZhiQi) {
        this.sbaoZhiQi = sbaoZhiQi;
    }

    public final String getSinvldtnDt() {
        return sinvldtnDt;
    }

    public final void setSinvldtnDt(String sinvldtnDt) {
        this.sinvldtnDt = sinvldtnDt;
    }

    public final String getSinvtyNm() {
        return sinvtyNm;
    }

    public final void setSinvtyNm(String sinvtyNm) {
        this.sinvtyNm = sinvtyNm;
    }

    public final String getSspcModel() {
        return sspcModel;
    }

    public final void setSspcModel(String sspcModel) {
        this.sspcModel = sspcModel;
    }

    public final String getSinvtyCd() {
        return sinvtyCd;
    }

    public final void setSinvtyCd(String sinvtyCd) {
        this.sinvtyCd = sinvtyCd;
    }

    public final BigDecimal getSbxRule() {
        return sbxRule;
    }

    public final void setSbxRule(BigDecimal sbxRule) {
        this.sbxRule = sbxRule;
    }

    public final String getSbaoZhiQiDt() {
        return sbaoZhiQiDt;
    }

    public final void setSbaoZhiQiDt(String sbaoZhiQiDt) {
        this.sbaoZhiQiDt = sbaoZhiQiDt;
    }

    public final String getSmeasrCorpNm() {
        return smeasrCorpNm;
    }

    public final void setSmeasrCorpNm(String smeasrCorpNm) {
        this.smeasrCorpNm = smeasrCorpNm;
    }

    public final String getSwhsNm() {
        return swhsNm;
    }

    public final void setSwhsNm(String swhsNm) {
        this.swhsNm = swhsNm;
    }

    public final String getScrspdBarCd() {
        return scrspdBarCd;
    }

    public final void setScrspdBarCd(String scrspdBarCd) {
        this.scrspdBarCd = scrspdBarCd;
    }

    @Override
    public String toString() {
        return String.format(
                "AmbDisambSnglMap [momKitEncd=%s, formNum=%s, formDt=%s, formTyp=%s, fee=%s, feeComnt=%s, memo=%s, typ=%s, whsEncd=%s, momQty=%s, taxRate=%s, mcntnTaxUprc=%s, munTaxUprc=%s, mcntnTaxAmt=%s, munTaxAmt=%s, mbatNum=%s, mprdcDt=%s, baoZhiQi=%s, invldtnDt=%s, isNtWms=%s, isNtChk=%s, isNtBookEntry=%s, isNtCmplt=%s, isNtClos=%s, printCnt=%s, setupPers=%s, setupTm=%s, mdfr=%s, modiTm=%s, chkr=%s, chkTm=%s, bookEntryPers=%s, bookEntryTm=%s, invtyNm=%s, spcModel=%s, invtyCd=%s, bxRule=%s, baoZhiQiDt=%s, measrCorpNm=%s, whsNm=%s, crspdBarCd=%s, formTypEncd=%s, formTypName=%s, ordrNum=%s, subKitEncd=%s, swhsEncd=%s, subQty=%s, momSubRatio=%s, staxRate=%s, scntnTaxUprc=%s, sunTaxUprc=%s, scntnTaxAmt=%s, sunTaxAmt=%s, sbatNum=%s, sprdcDt=%s, sbaoZhiQi=%s, sinvldtnDt=%s, sinvtyNm=%s, sspcModel=%s, sinvtyCd=%s, sbxRule=%s, sbaoZhiQiDt=%s, smeasrCorpNm=%s, swhsNm=%s, scrspdBarCd=%s]",
                momKitEncd, formNum, formDt, formTyp, fee, feeComnt, memo, typ, whsEncd, momQty, taxRate, mcntnTaxUprc,
                munTaxUprc, mcntnTaxAmt, munTaxAmt, mbatNum, mprdcDt, baoZhiQi, invldtnDt, isNtWms, isNtChk,
                isNtBookEntry, isNtCmplt, isNtClos, printCnt, setupPers, setupTm, mdfr, modiTm, chkr, chkTm,
                bookEntryPers, bookEntryTm, invtyNm, spcModel, invtyCd, bxRule, baoZhiQiDt, measrCorpNm, whsNm,
                crspdBarCd, formTypEncd, formTypName, ordrNum, subKitEncd, swhsEncd, subQty, momSubRatio, staxRate,
                scntnTaxUprc, sunTaxUprc, scntnTaxAmt, sunTaxAmt, sbatNum, sprdcDt, sbaoZhiQi, sinvldtnDt, sinvtyNm,
                sspcModel, sinvtyCd, sbxRule, sbaoZhiQiDt, smeasrCorpNm, swhsNm, scrspdBarCd);
    }

}
package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//�̵㵥
public class CheckSnglMap {

//	private String checkFormNum;// �̵㵥��
//	private String checkDt;// �̵�����
//	private String bookDt;// ��������
//	private String whsEncd;// �ֿ����
//	private String checkBat;// �̵�����
//	private String checkStatus;// �̵�״̬
//	private String memo;// ��ע
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
//	private String operator;// ����Ա
//	private String operatorId;// ����Ա����
//	private String whsNm;// �ֿ���
//	private String formTypEncd;// �������ͱ���
//	private String formTypName;// ������������
////��
//	private Long ordrNum;// ���
//	private String invtyEncd;// �������
//	private String invtyBigClsEncd;// ����������
//	private String barCd;// ������
//	private String batNum;// ����
//	private String prdcDt;// ��������
//	private String baoZhiQi;// ������
//	private String invldtnDt;// ʧЧ����
//	private BigDecimal bookQty;// ��������
//	private BigDecimal checkQty;// �̵�����
//	private BigDecimal prftLossQty;// ӯ������
//	private BigDecimal bookAdjustQty;// �����������
//	private BigDecimal adjIntoWhsQty;// �����������
//	private BigDecimal prftLossRatio;// ӯ������(%)
//	private BigDecimal adjOutWhsQty;// ������������
//	private String reasn;// ԭ��
//
//// // �����������ֶΡ�������λ���ơ��ֿ�����
//	private String invtyNm;// �������
//	private String spcModel;// ����ͺ�
//	private String invtyCd;// �������
//	private BigDecimal bxRule;// ���
////	private BigDecimal bxQty;// ���
//	private String measrCorpNm;// ������λ����
////	private String crspdBarCd;// ��Ӧ������
//	private String measrCorpId;// ������λ����
//	
//    private String  gdsBitEncd;//��λ����
//
//    private String  gdsBitNm;//��λ����

    @JsonProperty("�̵㵥��")
    private String checkFormNum;// �̵㵥��
    @JsonProperty("�̵�����")
    private String checkDt;// �̵�����
    @JsonProperty("��������")
    private String bookDt;// ��������
    @JsonProperty("�ֿ����")
    private String whsEncd;// �ֿ����
    @JsonProperty("�̵�����")
    private String checkBat;// �̵�����
    @JsonProperty("�̵�״̬")
    private String checkStatus;// �̵�״̬
    @JsonProperty("��ע")
    private String memo;// ��ע
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
    //	@JsonProperty("����Ա")
    @JsonIgnore
    private String operator;// ����Ա
    //	@JsonProperty("����Ա����")
    @JsonIgnore
    private String operatorId;// ����Ա����
    @JsonProperty("�ֿ�����")
    private String whsNm;// �ֿ���
    @JsonProperty("�������ͱ���")
    private String formTypEncd;// �������ͱ���
    @JsonProperty("������������")
    private String formTypName;// ������������
    //	@JsonProperty("���")
    @JsonIgnore
    private Long ordrNum;// ���
    @JsonProperty("�������")
    private String invtyEncd;// �������
    @JsonProperty("����������")
    private String invtyBigClsEncd;// ����������
    @JsonProperty("������")
    private String barCd;// ������
    @JsonProperty("����")
    private String batNum;// ����
    @JsonProperty("��������")
    private String prdcDt;// ��������
    @JsonProperty("������")
    private String baoZhiQi;// ������
    @JsonProperty("ʧЧ����")
    private String invldtnDt;// ʧЧ����
    @JsonProperty("��������")
    private BigDecimal bookQty;// ��������
    @JsonProperty("�̵�����")
    private BigDecimal checkQty;// �̵�����
    @JsonProperty("ӯ������")
    private BigDecimal prftLossQty;// ӯ������
    @JsonProperty("�����������")
    private BigDecimal bookAdjustQty;// �����������
    @JsonProperty("�����������")
    private BigDecimal adjIntoWhsQty;// �����������
    @JsonProperty("ӯ������")
    private BigDecimal prftLossRatio;// ӯ������(%)
    @JsonProperty("������������")
    private BigDecimal adjOutWhsQty;// ������������
    @JsonProperty("ԭ��")
    private String reasn;// ԭ��
    @JsonProperty("�������")
    private String invtyNm;// �������
    @JsonProperty("����ͺ�")
    private String spcModel;// ����ͺ�
    @JsonProperty("�������")
    private String invtyCd;// �������
    @JsonProperty("���")
    private BigDecimal bxRule;// ���
    @JsonProperty("������λ����")
    private String measrCorpNm;// ������λ����
    @JsonProperty("������λ����")
    private String measrCorpId;// ������λ����
    @JsonProperty("��λ����")
    private String gdsBitEncd;// ��λ����
    @JsonProperty("��λ����")
    private String gdsBitNm;// ��λ����
    @JsonProperty("��Ŀ����")
    private String projEncd;// ��Ŀ����
    @JsonProperty("��Ŀ����")
    private String projNm;// ��Ŀ����
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

    public final String getMeasrCorpId() {
        return measrCorpId;
    }

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd;
    }

    public String getGdsBitNm() {
        return gdsBitNm;
    }

    public void setGdsBitNm(String gdsBitNm) {
        this.gdsBitNm = gdsBitNm;
    }

    public final void setMeasrCorpId(String measrCorpId) {
        this.measrCorpId = measrCorpId;
    }

    public final String getCheckFormNum() {
        return checkFormNum;
    }

    public final void setCheckFormNum(String checkFormNum) {
        this.checkFormNum = checkFormNum;
    }

    public final String getCheckDt() {
        return checkDt;
    }

    public final void setCheckDt(String checkDt) {
        this.checkDt = checkDt;
    }

    public final String getBookDt() {
        return bookDt;
    }

    public final void setBookDt(String bookDt) {
        this.bookDt = bookDt;
    }

    public final String getWhsEncd() {
        return whsEncd;
    }

    public final void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public final String getCheckBat() {
        return checkBat;
    }

    public final void setCheckBat(String checkBat) {
        this.checkBat = checkBat;
    }

    public final String getCheckStatus() {
        return checkStatus;
    }

    public final void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public final String getMemo() {
        return memo;
    }

    public final void setMemo(String memo) {
        this.memo = memo;
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

    public final String getOperator() {
        return operator;
    }

    public final void setOperator(String operator) {
        this.operator = operator;
    }

    public final String getOperatorId() {
        return operatorId;
    }

    public final void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public final String getWhsNm() {
        return whsNm;
    }

    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
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

    public final String getInvtyEncd() {
        return invtyEncd;
    }

    public final void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    public final String getInvtyBigClsEncd() {
        return invtyBigClsEncd;
    }

    public final void setInvtyBigClsEncd(String invtyBigClsEncd) {
        this.invtyBigClsEncd = invtyBigClsEncd;
    }

    public final String getBarCd() {
        return barCd;
    }

    public final void setBarCd(String barCd) {
        this.barCd = barCd;
    }

    public final String getBatNum() {
        return batNum;
    }

    public final void setBatNum(String batNum) {
        this.batNum = batNum;
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

    public final BigDecimal getBookQty() {
        return bookQty;
    }

    public final void setBookQty(BigDecimal bookQty) {
        this.bookQty = bookQty;
    }

    public final BigDecimal getCheckQty() {
        return checkQty;
    }

    public final void setCheckQty(BigDecimal checkQty) {
        this.checkQty = checkQty;
    }

    public final BigDecimal getPrftLossQty() {
        return prftLossQty;
    }

    public final void setPrftLossQty(BigDecimal prftLossQty) {
        this.prftLossQty = prftLossQty;
    }

    public final BigDecimal getBookAdjustQty() {
        return bookAdjustQty;
    }

    public final void setBookAdjustQty(BigDecimal bookAdjustQty) {
        this.bookAdjustQty = bookAdjustQty;
    }

    public final BigDecimal getAdjIntoWhsQty() {
        return adjIntoWhsQty;
    }

    public final void setAdjIntoWhsQty(BigDecimal adjIntoWhsQty) {
        this.adjIntoWhsQty = adjIntoWhsQty;
    }

    public final BigDecimal getPrftLossRatio() {
        return prftLossRatio;
    }

    public final void setPrftLossRatio(BigDecimal prftLossRatio) {
        this.prftLossRatio = prftLossRatio;
    }

    public final BigDecimal getAdjOutWhsQty() {
        return adjOutWhsQty;
    }

    public final void setAdjOutWhsQty(BigDecimal adjOutWhsQty) {
        this.adjOutWhsQty = adjOutWhsQty;
    }

    public final String getReasn() {
        return reasn;
    }

    public final void setReasn(String reasn) {
        this.reasn = reasn;
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

    public final String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public final void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    @Override
    public String toString() {
        return String.format(
                "CheckSnglMap [checkFormNum=%s, checkDt=%s, bookDt=%s, whsEncd=%s, checkBat=%s, checkStatus=%s, memo=%s, isNtWms=%s, isNtChk=%s, isNtBookEntry=%s, isNtCmplt=%s, isNtClos=%s, printCnt=%s, setupPers=%s, setupTm=%s, mdfr=%s, modiTm=%s, chkr=%s, chkTm=%s, bookEntryPers=%s, bookEntryTm=%s, operator=%s, operatorId=%s, whsNm=%s, formTypEncd=%s, formTypName=%s, ordrNum=%s, invtyEncd=%s, invtyBigClsEncd=%s, barCd=%s, batNum=%s, prdcDt=%s, baoZhiQi=%s, invldtnDt=%s, bookQty=%s, checkQty=%s, prftLossQty=%s, bookAdjustQty=%s, adjIntoWhsQty=%s, prftLossRatio=%s, adjOutWhsQty=%s, reasn=%s, invtyNm=%s, spcModel=%s, invtyCd=%s, bxRule=%s, measrCorpNm=%s]",
                checkFormNum, checkDt, bookDt, whsEncd, checkBat, checkStatus, memo, isNtWms, isNtChk, isNtBookEntry,
                isNtCmplt, isNtClos, printCnt, setupPers, setupTm, mdfr, modiTm, chkr, chkTm, bookEntryPers,
                bookEntryTm, operator, operatorId, whsNm, formTypEncd, formTypName, ordrNum, invtyEncd, invtyBigClsEncd,
                barCd, batNum, prdcDt, baoZhiQi, invldtnDt, bookQty, checkQty, prftLossQty, bookAdjustQty,
                adjIntoWhsQty, prftLossRatio, adjOutWhsQty, reasn, invtyNm, spcModel, invtyCd, bxRule, measrCorpNm);
    }

}
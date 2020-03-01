package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//�̵�����
public class CheckPrftLossMap {
//	private String checkFormNum;// �̵㵥��
//	private String srcFormNum;// ��Դ���ݺ�
//	private String checkDt;// �̵�����
//	private String bookDt;// ��������
//	private String whsEncd;// �ֿ����
//	private String checkBat;// �̵�����
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
//	private String chkTm;// �������
//	private String bookEntryPers;// ������
//	private String bookEntryTm;// ����ʱ��
//	private String whsNm;// �ֿ�����
//	private String formTypEncd;// �������ͱ���
//	private String formTypName;// ������������
//
//	// ��
//
//	private Long ordrNum;// ���
//	private String invtyEncd;// �������
//	private String invtyBigClsEncd;// ����������
//	private String batNum;// ����
//	private String prdcDt;// ��������
//	private String baoZhiQi;// ������
//	private String invldtnDt;// ʧЧ����
//	private BigDecimal bookQty;// ��������
//	private BigDecimal checkQty;// �̵�����
//	private BigDecimal prftLossQty;// ӯ������
//	private BigDecimal prftLossQtys;// ��������
//	private BigDecimal bookAdjustQty;// �����������
//	private BigDecimal adjIntoWhsQty;// �����������
//	private BigDecimal prftLossRatio;// ӯ������(%)
//	private BigDecimal adjOutWhsQty;// ������������
//	private String reasn;// ԭ��
//	private BigDecimal taxRate;// ˰��
//	private BigDecimal cntnTaxUprc;// ��˰����
//	private BigDecimal unTaxUprc;// δ˰����
//	private BigDecimal cntnTaxBookAmt;// ��˰������
//	private BigDecimal unTaxBookAmt;// δ˰������
//	private BigDecimal cntnTaxCheckAmt;// ��˰�̵���
//	private BigDecimal unTaxCheckAmt;// δ˰�̵���
//	private BigDecimal cntnTaxPrftLossAmt;// ��˰ӯ�����
//	private BigDecimal unTaxPrftLossAmt;// δ˰ӯ�����
//	private BigDecimal cntnTaxPrftLossAmts;// ��˰������
//	private BigDecimal unTaxPrftLossAmts;// δ˰������
//	// �����������ֶΡ�������λ���ơ��ֿ�����
//	private String invtyNm;// �������
//	private String spcModel;// ����ͺ�
//	private String invtyCd;// �������
//	private BigDecimal bxRule;// ���
////	private BigDecimal bxQty;// �̵�����
//
//	private String crspdBarCd;// ��Ӧ������
//	private String measrCorpNm;// ������λ����
//	private String measrCorpId;// ������λ����
//	private String gdsBitEncd;// ��λ����
//
//	private String gdsBitNm;// ��λ����

    @JsonProperty("���浥��")
    private String checkFormNum;// �̵㵥��
    @JsonProperty("��Դ���ݺ�")
    private String srcFormNum;// ��Դ���ݺ�
    @JsonProperty("��������")
    private String checkDt;// �̵�����
    @JsonProperty("��������")
    private String bookDt;// ��������
    @JsonProperty("�ֿ����")
    private String whsEncd;// �ֿ����
    @JsonProperty("�̵�����")
    private String checkBat;// �̵�����
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
    @JsonProperty("�������")
    private String chkTm;// �������
    @JsonProperty("������")
    private String bookEntryPers;// ������
    @JsonProperty("����ʱ��")
    private String bookEntryTm;// ����ʱ��
    @JsonProperty("�ֿ�����")
    private String whsNm;// �ֿ�����
    @JsonProperty("�������ͱ���")
    private String formTypEncd;// �������ͱ���
    @JsonProperty("������������")
    private String formTypName;// ������������
    //	@JsonProperty("���")
    @JsonIgnore
    private Long ordrNum;// ���
    @JsonProperty("�������")
    private String invtyEncd;// �������
//    @JsonProperty("����������")
    @JsonIgnore
    private String invtyBigClsEncd;// ����������
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
    @JsonProperty("��������")
    private BigDecimal prftLossQtys;// ��������
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
    @JsonProperty("˰��")
    private BigDecimal taxRate;// ˰��
    @JsonProperty("��˰����")
    private BigDecimal cntnTaxUprc;// ��˰����
    @JsonProperty("δ˰����")
    private BigDecimal unTaxUprc;// δ˰����
    @JsonProperty("��˰������")
    private BigDecimal cntnTaxBookAmt;// ��˰������
    @JsonProperty("δ˰������")
    private BigDecimal unTaxBookAmt;// δ˰������
    @JsonProperty("��˰�̵���")
    private BigDecimal cntnTaxCheckAmt;// ��˰�̵���
    @JsonProperty("δ˰�̵���")
    private BigDecimal unTaxCheckAmt;// δ˰�̵���
    @JsonProperty("��˰ӯ�����")
    private BigDecimal cntnTaxPrftLossAmt;// ��˰ӯ�����
    @JsonProperty("δ˰ӯ�����")
    private BigDecimal unTaxPrftLossAmt;// δ˰ӯ�����
    @JsonProperty("��˰������")
    private BigDecimal cntnTaxPrftLossAmts;// ��˰������
    @JsonProperty("δ˰������")
    private BigDecimal unTaxPrftLossAmts;// δ˰������
    @JsonProperty("����˰��")
    private BigDecimal bookTaxAmt;//����˰��
    @JsonProperty("�̵�˰��")
    private BigDecimal checkTaxAmt;//�̵�˰��
    @JsonProperty("ӯ��˰��")
    private BigDecimal prftLossTaxAmt;//ӯ��˰��
    @JsonProperty("����˰��")
    private BigDecimal prftLossTaxAmts;//����˰��
    @JsonProperty("�������")
    private String invtyNm;// �������
    @JsonProperty("����ͺ�")
    private String spcModel;// ����ͺ�
    @JsonProperty("�������")
    private String invtyCd;// �������
    @JsonProperty("���")
    private BigDecimal bxRule;// ���
    @JsonProperty("��Ӧ������")
    private String crspdBarCd;// ��Ӧ������
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

    public final String getCheckFormNum() {
        return checkFormNum;
    }

    public final void setCheckFormNum(String checkFormNum) {
        this.checkFormNum = checkFormNum;
    }

    public final String getSrcFormNum() {
        return srcFormNum;
    }

    public final void setSrcFormNum(String srcFormNum) {
        this.srcFormNum = srcFormNum;
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

    public final BigDecimal getPrftLossQtys() {
        return prftLossQtys;
    }

    public final void setPrftLossQtys(BigDecimal prftLossQtys) {
        this.prftLossQtys = prftLossQtys;
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

    public final BigDecimal getTaxRate() {
        return taxRate;
    }

    public final void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public final BigDecimal getCntnTaxUprc() {
        return cntnTaxUprc;
    }

    public final void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
        this.cntnTaxUprc = cntnTaxUprc;
    }

    public final BigDecimal getUnTaxUprc() {
        return unTaxUprc;
    }

    public final void setUnTaxUprc(BigDecimal unTaxUprc) {
        this.unTaxUprc = unTaxUprc;
    }

    public final BigDecimal getCntnTaxBookAmt() {
        return cntnTaxBookAmt;
    }

    public final void setCntnTaxBookAmt(BigDecimal cntnTaxBookAmt) {
        this.cntnTaxBookAmt = cntnTaxBookAmt;
    }

    public final BigDecimal getUnTaxBookAmt() {
        return unTaxBookAmt;
    }

    public final void setUnTaxBookAmt(BigDecimal unTaxBookAmt) {
        this.unTaxBookAmt = unTaxBookAmt;
    }

    public final BigDecimal getCntnTaxCheckAmt() {
        return cntnTaxCheckAmt;
    }

    public final void setCntnTaxCheckAmt(BigDecimal cntnTaxCheckAmt) {
        this.cntnTaxCheckAmt = cntnTaxCheckAmt;
    }

    public final BigDecimal getUnTaxCheckAmt() {
        return unTaxCheckAmt;
    }

    public final void setUnTaxCheckAmt(BigDecimal unTaxCheckAmt) {
        this.unTaxCheckAmt = unTaxCheckAmt;
    }

    public final BigDecimal getCntnTaxPrftLossAmt() {
        return cntnTaxPrftLossAmt;
    }

    public final void setCntnTaxPrftLossAmt(BigDecimal cntnTaxPrftLossAmt) {
        this.cntnTaxPrftLossAmt = cntnTaxPrftLossAmt;
    }

    public final BigDecimal getUnTaxPrftLossAmt() {
        return unTaxPrftLossAmt;
    }

    public final void setUnTaxPrftLossAmt(BigDecimal unTaxPrftLossAmt) {
        this.unTaxPrftLossAmt = unTaxPrftLossAmt;
    }

    public final BigDecimal getCntnTaxPrftLossAmts() {
        return cntnTaxPrftLossAmts;
    }

    public final void setCntnTaxPrftLossAmts(BigDecimal cntnTaxPrftLossAmts) {
        this.cntnTaxPrftLossAmts = cntnTaxPrftLossAmts;
    }

    public final BigDecimal getUnTaxPrftLossAmts() {
        return unTaxPrftLossAmts;
    }

    public final void setUnTaxPrftLossAmts(BigDecimal unTaxPrftLossAmts) {
        this.unTaxPrftLossAmts = unTaxPrftLossAmts;
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

    public final String getCrspdBarCd() {
        return crspdBarCd;
    }

    public final void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
    }

    public final String getMeasrCorpId() {
        return measrCorpId;
    }

    public final void setMeasrCorpId(String measrCorpId) {
        this.measrCorpId = measrCorpId;
    }

    @Override
    public String toString() {
        return String.format(
                "CheckPrftLossMap [checkFormNum=%s, srcFormNum=%s, checkDt=%s, bookDt=%s, whsEncd=%s, checkBat=%s, memo=%s, isNtWms=%s, isNtChk=%s, isNtBookEntry=%s, isNtCmplt=%s, isNtClos=%s, printCnt=%s, setupPers=%s, setupTm=%s, mdfr=%s, modiTm=%s, chkr=%s, chkTm=%s, bookEntryPers=%s, bookEntryTm=%s, whsNm=%s, formTypEncd=%s, formTypName=%s, ordrNum=%s, invtyEncd=%s, invtyBigClsEncd=%s, batNum=%s, prdcDt=%s, baoZhiQi=%s, invldtnDt=%s, bookQty=%s, checkQty=%s, prftLossQty=%s, prftLossQtys=%s, bookAdjustQty=%s, adjIntoWhsQty=%s, prftLossRatio=%s, adjOutWhsQty=%s, reasn=%s, taxRate=%s, cntnTaxUprc=%s, unTaxUprc=%s, cntnTaxBookAmt=%s, unTaxBookAmt=%s, cntnTaxCheckAmt=%s, unTaxCheckAmt=%s, cntnTaxPrftLossAmt=%s, unTaxPrftLossAmt=%s, cntnTaxPrftLossAmts=%s, unTaxPrftLossAmts=%s, invtyNm=%s, spcModel=%s, invtyCd=%s, bxRule=%s, crspdBarCd=%s, measrCorpNm=%s, measrCorpId=%s]",
                checkFormNum, srcFormNum, checkDt, bookDt, whsEncd, checkBat, memo, isNtWms, isNtChk, isNtBookEntry,
                isNtCmplt, isNtClos, printCnt, setupPers, setupTm, mdfr, modiTm, chkr, chkTm, bookEntryPers,
                bookEntryTm, whsNm, formTypEncd, formTypName, ordrNum, invtyEncd, invtyBigClsEncd, batNum, prdcDt,
                baoZhiQi, invldtnDt, bookQty, checkQty, prftLossQty, prftLossQtys, bookAdjustQty, adjIntoWhsQty,
                prftLossRatio, adjOutWhsQty, reasn, taxRate, cntnTaxUprc, unTaxUprc, cntnTaxBookAmt, unTaxBookAmt,
                cntnTaxCheckAmt, unTaxCheckAmt, cntnTaxPrftLossAmt, unTaxPrftLossAmt, cntnTaxPrftLossAmts,
                unTaxPrftLossAmts, invtyNm, spcModel, invtyCd, bxRule, crspdBarCd, measrCorpNm, measrCorpId);
    }

}
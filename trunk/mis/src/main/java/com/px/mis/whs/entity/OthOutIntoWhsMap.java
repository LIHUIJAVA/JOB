package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//��������ⵥ
public class OthOutIntoWhsMap {
//	private String formNum;// ����
//	private String formDt;// ��������
//	private String whsEncd;// �ֿ����
//	private String srcFormNum;// ��Դ���ݺ�
//	private String outIntoWhsTyp;// ���������
//	private String outStatus;// ����״̬
//	private String inStatus;// ���״̬
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
//	private String operator;// ������
//	private String operatorId;// �����˱���
//	private String outIntoWhsTypId;// ��������ͱ���
//	private String outIntoWhsTypNm;// �������������
//	private String whsNm;// �ֿ�����
//	private String formTypEncd;// �������ͱ���
//	private String formTypName;// ������������
//	private Integer isNtMakeVouch;// �Ƿ�����ƾ֤
//	private String makVouchPers;// ��ƾ֤��
//	private String makVouchTm;// ��ƾ֤ʱ��
//
//	// ��
//	private Long ordrNum;// ���
//	private String invtyEncd;// �������
//	private BigDecimal qty;// ����
//	private BigDecimal taxRate;// ˰��
//	private BigDecimal cntnTaxUprc;// ��˰����
//	private BigDecimal unTaxUprc;// δ˰����[����(�������δ��뵥��)]
//	private BigDecimal bookEntryUprc;// ���˵���
//	private BigDecimal cntnTaxAmt;// ��˰���
//	private BigDecimal unTaxAmt;// δ˰���
//	private String intlBat;// ��������
//	private String batNum;// ����
//	private String prdcDt;// ��������
//	private String baoZhiQi;// ������
//	private String invldtnDt;// ʧЧ����
//
//	// �����������ֶΡ�������λ���ơ��ֿ�����
//	private String invtyNm;// �������
//	private String spcModel;// ����ͺ�
//	private String invtyCd;// �������
//	private BigDecimal bxRule;// ���
//	private BigDecimal bxQty;// ����
//	private String measrCorpNm;// ������λ����
//	private String crspdBarCd;// ��Ӧ������
//	private String invtyClsEncd;// ����������
//	private String invtyClsNm;// �����������
//	private String measrCorpId;// ������λ����
//	private String memos;// �б�ע

    @JsonProperty("���ݺ�")
    private String formNum;// ����
    @JsonProperty("��������")
    private String formDt;// ��������
    @JsonProperty("�ֿ����")
    private String whsEncd;// �ֿ����
    @JsonProperty("��Դ���ݺ�")
    private String srcFormNum;// ��Դ���ݺ�
//    @JsonProperty("���������")
    @JsonIgnore
    private String outIntoWhsTyp;// ���������
    //	@JsonProperty("����״̬")
    @JsonIgnore
    private String outStatus;// ����״̬
    //	@JsonProperty("���״̬")
    @JsonIgnore
    private String inStatus;// ���״̬
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
//    	@JsonProperty("�Ƿ����")
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
    //	@JsonProperty("������")
    @JsonIgnore
    private String operator;// ������
    //	@JsonProperty("�����˱���")
    @JsonIgnore
    private String operatorId;// �����˱���
    @JsonProperty("��������ͱ���")
    private String outIntoWhsTypId;// ��������ͱ���
    @JsonProperty("�������������")
    private String outIntoWhsTypNm;// �������������
    @JsonProperty("�ֿ�����")
    private String whsNm;// �ֿ�����
    @JsonProperty("�������ͱ���")
    private String formTypEncd;// �������ͱ���
    @JsonProperty("������������")
    private String formTypName;// ������������
    @JsonProperty("�Ƿ�����ƾ֤")
    private Integer isNtMakeVouch;// �Ƿ�����ƾ֤
    @JsonProperty("��ƾ֤��")
    private String makVouchPers;// ��ƾ֤��
    @JsonProperty("��ƾ֤ʱ��")
    private String makVouchTm;// ��ƾ֤ʱ��
    @JsonProperty("�շ�������")
    private String recvSendCateId;// �շ����id
    @JsonProperty("�շ��������")
    private String recvSendCateNm;// �շ��������
    @JsonProperty("����������ֿ����")
    private String  outWhsEncd;
    @JsonProperty("���������ֿ����")
    private String   inWhsEncd;
    @JsonProperty("����������ֿ�����")
    private String   outWhsNm;
    @JsonProperty("���������ֿ�����")
    private String   inWhsNm;

    //	@JsonProperty("���")
    @JsonIgnore
    private Long ordrNum;// ���
    @JsonProperty("�������")
    private String invtyEncd;// �������
    @JsonProperty("����")
    private BigDecimal qty;// ����
    @JsonProperty("˰��")
    private BigDecimal taxRate;// ˰��
    @JsonProperty("��˰����")
    private BigDecimal cntnTaxUprc;// ��˰����
    @JsonProperty("δ˰����")
    private BigDecimal unTaxUprc;// δ˰����[����(�������δ��뵥��)]
    @JsonProperty("���˵���")
    private BigDecimal bookEntryUprc;// ���˵���
    @JsonProperty("��˰���")
    private BigDecimal cntnTaxAmt;// ��˰���
    @JsonProperty("δ˰���")
    private BigDecimal unTaxAmt;// δ˰���
    @JsonProperty("˰��")
    private BigDecimal taxAmt;//˰��
    @JsonProperty("��������")
    private String intlBat;// ��������
    @JsonProperty("����")
    private String batNum;// ����
    @JsonProperty("��������")
    private String prdcDt;// ��������
    @JsonProperty("������")
    private String baoZhiQi;// ������
    @JsonProperty("ʧЧ����")
    private String invldtnDt;// ʧЧ����
    @JsonProperty("�������")
    private String invtyNm;// �������
    @JsonProperty("����ͺ�")
    private String spcModel;// ����ͺ�
    @JsonProperty("�������")
    private String invtyCd;// �������
    @JsonProperty("���")
    private BigDecimal bxRule;// ���
    @JsonProperty("����")
    private BigDecimal bxQty;// ����
    @JsonProperty("������λ����")
    private String measrCorpNm;// ������λ����
    @JsonProperty("��Ӧ������")
    private String crspdBarCd;// ��Ӧ������
    @JsonProperty("����������")
    private String invtyClsEncd;// ����������
    @JsonProperty("�����������")
    private String invtyClsNm;// �����������
    @JsonProperty("������λ����")
    private String measrCorpId;// ������λ����
    @JsonProperty("�ӱ�ע")
    private String memos;// �б�ע
    @JsonProperty("��Ŀ����")
    private String projEncd;// ��Ŀ����
    @JsonProperty("��Ŀ����")
    private String projNm;// ��Ŀ����

    public String getOutWhsEncd() {
        return outWhsEncd;
    }

    public void setOutWhsEncd(String outWhsEncd) {
        this.outWhsEncd = outWhsEncd;
    }

    public String getInWhsEncd() {
        return inWhsEncd;
    }

    public void setInWhsEncd(String inWhsEncd) {
        this.inWhsEncd = inWhsEncd;
    }

    public String getOutWhsNm() {
        return outWhsNm;
    }

    public void setOutWhsNm(String outWhsNm) {
        this.outWhsNm = outWhsNm;
    }

    public String getInWhsNm() {
        return inWhsNm;
    }

    public void setInWhsNm(String inWhsNm) {
        this.inWhsNm = inWhsNm;
    }

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
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

    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }

    public Integer getIsNtMakeVouch() {
        return isNtMakeVouch;
    }

    public void setIsNtMakeVouch(Integer isNtMakeVouch) {
        this.isNtMakeVouch = isNtMakeVouch;
    }

    public String getMakVouchPers() {
        return makVouchPers;
    }

    public void setMakVouchPers(String makVouchPers) {
        this.makVouchPers = makVouchPers;
    }

    public String getMakVouchTm() {
        return makVouchTm;
    }

    public void setMakVouchTm(String makVouchTm) {
        this.makVouchTm = makVouchTm;
    }

    public BigDecimal getBxQty() {
        return bxQty;
    }

    public void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
    }

    public final String getMeasrCorpId() {
        return measrCorpId;
    }

    public final void setMeasrCorpId(String measrCorpId) {
        this.measrCorpId = measrCorpId;
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

    public final String getWhsEncd() {
        return whsEncd;
    }

    public final void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public final String getSrcFormNum() {
        return srcFormNum;
    }

    public final void setSrcFormNum(String srcFormNum) {
        this.srcFormNum = srcFormNum;
    }

    public final String getOutIntoWhsTyp() {
        return outIntoWhsTyp;
    }

    public final void setOutIntoWhsTyp(String outIntoWhsTyp) {
        this.outIntoWhsTyp = outIntoWhsTyp;
    }

    public final String getOutStatus() {
        return outStatus;
    }

    public final void setOutStatus(String outStatus) {
        this.outStatus = outStatus;
    }

    public final String getInStatus() {
        return inStatus;
    }

    public final void setInStatus(String inStatus) {
        this.inStatus = inStatus;
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

    public final String getOutIntoWhsTypId() {
        return outIntoWhsTypId;
    }

    public final void setOutIntoWhsTypId(String outIntoWhsTypId) {
        this.outIntoWhsTypId = outIntoWhsTypId;
    }

    public final String getOutIntoWhsTypNm() {
        return outIntoWhsTypNm;
    }

    public final void setOutIntoWhsTypNm(String outIntoWhsTypNm) {
        this.outIntoWhsTypNm = outIntoWhsTypNm;
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

    public final BigDecimal getQty() {
        return qty;
    }

    public final void setQty(BigDecimal qty) {
        this.qty = qty;
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

    public final BigDecimal getBookEntryUprc() {
        return bookEntryUprc;
    }

    public final void setBookEntryUprc(BigDecimal bookEntryUprc) {
        this.bookEntryUprc = bookEntryUprc;
    }

    public final BigDecimal getCntnTaxAmt() {
        return cntnTaxAmt;
    }

    public final void setCntnTaxAmt(BigDecimal cntnTaxAmt) {
        this.cntnTaxAmt = cntnTaxAmt;
    }

    public final BigDecimal getUnTaxAmt() {
        return unTaxAmt;
    }

    public final void setUnTaxAmt(BigDecimal unTaxAmt) {
        this.unTaxAmt = unTaxAmt;
    }

    public final String getIntlBat() {
        return intlBat;
    }

    public final void setIntlBat(String intlBat) {
        this.intlBat = intlBat;
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

    public final String getInvtyClsEncd() {
        return invtyClsEncd;
    }

    public final void setInvtyClsEncd(String invtyClsEncd) {
        this.invtyClsEncd = invtyClsEncd;
    }

    public final String getInvtyClsNm() {
        return invtyClsNm;
    }

    public final void setInvtyClsNm(String invtyClsNm) {
        this.invtyClsNm = invtyClsNm;
    }

    public String getRecvSendCateId() {
        return recvSendCateId;
    }

    public void setRecvSendCateId(String recvSendCateId) {
        this.recvSendCateId = recvSendCateId;
    }

    public String getRecvSendCateNm() {
        return recvSendCateNm;
    }

    public void setRecvSendCateNm(String recvSendCateNm) {
        this.recvSendCateNm = recvSendCateNm;
    }


}
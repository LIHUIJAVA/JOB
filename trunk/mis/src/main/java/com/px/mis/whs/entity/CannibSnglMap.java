package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//������
public class CannibSnglMap {
//	private String formNum;// ���ݺ�
//	private String formDt;// ��������
//	private String cannibDt;// ��������
//	private String tranOutWhsEncd;// ת���ֿ����
//	private String tranInWhsEncd;// ת��ֿ����
//	private String tranOutWhsEncdName;// ת���ֿ�����
//	private String tranInWhsEncdName;// ת��ֿ�����
//	private String tranOutDeptEncd;// ת�����ű���
//	private String tranInDeptEncd;// ת�벿�ű���
//	private String cannibStatus;// ����״̬
//	private String memo;// ��ע
//	private Integer isNtWms;// �Ƿ���WMS�ϴ�
//	private Integer isNtChk;// �Ƿ����
//	private Integer isNtCmplt;// �Ƿ����
//	private Integer isNtClos;// �Ƿ�ر�
//	private Integer printCnt;// ��ӡ����
//	private String setupPers;// ������
//	private String setupTm;// ����ʱ��
//	private String mdfr;// �޸���
//	private String modiTm;// �޸�ʱ��
//	private String chkr;// �����
//	private String chkTm;// ���ʱ��
//	private String formTypEncd;// �������ͱ���
//	private String formTypName;// ������������
//	private String tranOutDeptNm;// ת����������
//	private String tranInDeptNm;// ת�벿������
//
////��
//
//	private Long ordrNum;// ���
//	private String invtyId;// ������
//	private BigDecimal cannibQty;// ��������
//	private BigDecimal recvQty;// ʵ������
//	private String batNum;// ����
//	private String invldtnDt;// ʧЧ����
//	private String baoZhiQi;// ������
//	private String prdcDt;// ��������
//	private BigDecimal taxRate;// ˰��
//	private BigDecimal cntnTaxUprc;// ��˰����
//	private BigDecimal unTaxUprc;// δ˰����
//	private BigDecimal cntnTaxAmt;// ��˰���
//	private BigDecimal unTaxAmt;// δ˰���
//	// �����������ֶΡ�������λ���Ƶ�
//	private String invtyNm;// �������
//	private String spcModel;// ����ͺ�
//	private BigDecimal bxRule;// ���
//	private BigDecimal bxQty;// ����
//	private String measrCorpNm;// ������λ����
//	private String invtyCd;// �������
//	// �����������ֶΡ�������λ���ơ��ֿ�����
////	private String invtyNm;// �������
////	private String spcModel;// ����ͺ�
////	private BigDecimal bxRule;// ���
////	private String measrCorpNm;// ������λ����
//	private String crspdBarCd;// ��Ӧ������
//	

    @JsonProperty("���ݺ�")
    private String formNum;// ���ݺ�
    @JsonProperty("��������")
    private String formDt;// ��������
//    @JsonProperty("��������")
    @JsonIgnore
    private String cannibDt;// ��������
    @JsonProperty("ת���ֿ����")
    private String tranOutWhsEncd;// ת���ֿ����
    @JsonProperty("ת��ֿ����")
    private String tranInWhsEncd;// ת��ֿ����
    @JsonProperty("ת���ֿ�����")
    private String tranOutWhsEncdName;// ת���ֿ�����
    @JsonProperty("ת��ֿ�����")
    private String tranInWhsEncdName;// ת��ֿ�����
//    @JsonProperty("ת�����ű���")
    @JsonIgnore
    private String tranOutDeptEncd;// ת�����ű���
//    @JsonProperty("ת�벿�ű���")
    @JsonIgnore
    private String tranInDeptEncd;// ת�벿�ű���
//    @JsonProperty("����״̬")
    @JsonIgnore
    private String cannibStatus;// ����״̬
    @JsonProperty("��ע")
    private String memo;// ��ע
    //	@JsonProperty("�Ƿ���WMS�ϴ�")
    @JsonIgnore
    private Integer isNtWms;// �Ƿ���WMS�ϴ�
    @JsonProperty("�Ƿ����")
    private Integer isNtChk;// �Ƿ����
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
    @JsonProperty("�������ͱ���")
    private String formTypEncd;// �������ͱ���
    @JsonProperty("������������")
    private String formTypName;// ������������
//    @JsonProperty("ת����������")
    @JsonIgnore
    private String tranOutDeptNm;// ת����������
//    @JsonProperty("ת�벿������")
    @JsonIgnore
    private String tranInDeptNm;// ת�벿������
    //	@JsonProperty("���")
    @JsonIgnore
    private Long ordrNum;// ���
    @JsonProperty("�������")
    private String invtyId;// ������
    @JsonProperty("��������")
    private BigDecimal cannibQty;// ��������
    //	@JsonProperty("ʵ������")
    @JsonIgnore
    private BigDecimal recvQty;// ʵ������
    @JsonProperty("����")
    private String batNum;// ����
    @JsonProperty("ʧЧ����")
    private String invldtnDt;// ʧЧ����
    @JsonProperty("������")
    private String baoZhiQi;// ������
    @JsonProperty("��������")
    private String prdcDt;// ��������
    @JsonProperty("˰��")
    private BigDecimal taxRate;// ˰��
    @JsonProperty("��˰����")
    private BigDecimal cntnTaxUprc;// ��˰����
    @JsonProperty("δ˰����")
    private BigDecimal unTaxUprc;// δ˰����
    @JsonProperty("��˰���")
    private BigDecimal cntnTaxAmt;// ��˰���
    @JsonProperty("δ˰���")
    private BigDecimal unTaxAmt;// δ˰���
    @JsonProperty("˰��")
    private BigDecimal taxAmt;//˰��
    @JsonProperty("�������")
    private String invtyNm;// �������
    @JsonProperty("����ͺ�")
    private String spcModel;// ����ͺ�
    @JsonProperty("���")
    private BigDecimal bxRule;// ���
    @JsonProperty("����")
    private BigDecimal bxQty;// ����
    @JsonProperty("������λ����")
    private String measrCorpNm;// ������λ����
    @JsonProperty("�������")
    private String invtyCd;// �������
    @JsonProperty("��Ӧ������")
    private String crspdBarCd;// ��Ӧ������
    @JsonProperty("��Ŀ����")
    private String projEncd;// ��Ŀ����

    @JsonProperty("��Ŀ����")
    private String projNm;// ��Ŀ����

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


    public final String getTranOutDeptNm() {
        return tranOutDeptNm;
    }

    public final void setTranOutDeptNm(String tranOutDeptNm) {
        this.tranOutDeptNm = tranOutDeptNm;
    }

    public final String getTranInDeptNm() {
        return tranInDeptNm;
    }

    public final void setTranInDeptNm(String tranInDeptNm) {
        this.tranInDeptNm = tranInDeptNm;
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

    public final String getCannibDt() {
        return cannibDt;
    }

    public final void setCannibDt(String cannibDt) {
        this.cannibDt = cannibDt;
    }

    public final String getTranOutWhsEncd() {
        return tranOutWhsEncd;
    }

    public final void setTranOutWhsEncd(String tranOutWhsEncd) {
        this.tranOutWhsEncd = tranOutWhsEncd;
    }

    public final String getTranInWhsEncd() {
        return tranInWhsEncd;
    }

    public final void setTranInWhsEncd(String tranInWhsEncd) {
        this.tranInWhsEncd = tranInWhsEncd;
    }

    public final String getTranOutWhsEncdName() {
        return tranOutWhsEncdName;
    }

    public final void setTranOutWhsEncdName(String tranOutWhsEncdName) {
        this.tranOutWhsEncdName = tranOutWhsEncdName;
    }

    public final String getTranInWhsEncdName() {
        return tranInWhsEncdName;
    }

    public final void setTranInWhsEncdName(String tranInWhsEncdName) {
        this.tranInWhsEncdName = tranInWhsEncdName;
    }

    public final String getTranOutDeptEncd() {
        return tranOutDeptEncd;
    }

    public final void setTranOutDeptEncd(String tranOutDeptEncd) {
        this.tranOutDeptEncd = tranOutDeptEncd;
    }

    public final String getTranInDeptEncd() {
        return tranInDeptEncd;
    }

    public final void setTranInDeptEncd(String tranInDeptEncd) {
        this.tranInDeptEncd = tranInDeptEncd;
    }

    public final String getCannibStatus() {
        return cannibStatus;
    }

    public final void setCannibStatus(String cannibStatus) {
        this.cannibStatus = cannibStatus;
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

    public final String getInvtyId() {
        return invtyId;
    }

    public final void setInvtyId(String invtyId) {
        this.invtyId = invtyId;
    }

    public final BigDecimal getCannibQty() {
        return cannibQty;
    }

    public final void setCannibQty(BigDecimal cannibQty) {
        this.cannibQty = cannibQty;
    }

    public final BigDecimal getRecvQty() {
        return recvQty;
    }

    public final void setRecvQty(BigDecimal recvQty) {
        this.recvQty = recvQty;
    }

    public final String getBatNum() {
        return batNum;
    }

    public final void setBatNum(String batNum) {
        this.batNum = batNum;
    }

    public final String getInvldtnDt() {
        return invldtnDt;
    }

    public final void setInvldtnDt(String invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public final String getBaoZhiQi() {
        return baoZhiQi;
    }

    public final void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi;
    }

    public final String getPrdcDt() {
        return prdcDt;
    }

    public final void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
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

    public final BigDecimal getBxRule() {
        return bxRule;
    }

    public final void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public final BigDecimal getBxQty() {
        return bxQty;
    }

    public final void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
    }

    public final String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public final void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public final String getInvtyCd() {
        return invtyCd;
    }

    public final void setInvtyCd(String invtyCd) {
        this.invtyCd = invtyCd;
    }

    public final String getCrspdBarCd() {
        return crspdBarCd;
    }

    public final void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
    }

    @Override
    public String toString() {
        return String.format(
                "CannibSnglMap [formNum=%s, formDt=%s, cannibDt=%s, tranOutWhsEncd=%s, tranInWhsEncd=%s, tranOutWhsEncdName=%s, tranInWhsEncdName=%s, tranOutDeptEncd=%s, tranInDeptEncd=%s, cannibStatus=%s, memo=%s, isNtWms=%s, isNtChk=%s, isNtCmplt=%s, isNtClos=%s, printCnt=%s, setupPers=%s, setupTm=%s, mdfr=%s, modiTm=%s, chkr=%s, chkTm=%s, formTypEncd=%s, formTypName=%s, ordrNum=%s, invtyId=%s, cannibQty=%s, recvQty=%s, batNum=%s, invldtnDt=%s, baoZhiQi=%s, prdcDt=%s, taxRate=%s, cntnTaxUprc=%s, unTaxUprc=%s, cntnTaxAmt=%s, unTaxAmt=%s, invtyNm=%s, spcModel=%s, bxRule=%s, bxQty=%s, measrCorpNm=%s, invtyCd=%s, crspdBarCd=%s]",
                formNum, formDt, cannibDt, tranOutWhsEncd, tranInWhsEncd, tranOutWhsEncdName, tranInWhsEncdName,
                tranOutDeptEncd, tranInDeptEncd, cannibStatus, memo, isNtWms, isNtChk, isNtCmplt, isNtClos, printCnt,
                setupPers, setupTm, mdfr, modiTm, chkr, chkTm, formTypEncd, formTypName, ordrNum, invtyId, cannibQty,
                recvQty, batNum, invldtnDt, baoZhiQi, prdcDt, taxRate, cntnTaxUprc, unTaxUprc, cntnTaxAmt, unTaxAmt,
                invtyNm, spcModel, bxRule, bxQty, measrCorpNm, invtyCd, crspdBarCd);
    }

}
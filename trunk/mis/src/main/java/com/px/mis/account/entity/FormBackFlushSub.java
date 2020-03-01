package com.px.mis.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * �س嵥�ӱ�
 * @author 
 */
public class FormBackFlushSub implements Serializable {
    /**
     * ���
     */
    private int ordrNum;

    /**
     * �س嵥�������
     */
    private String backNum;

    /**
     * ���ݺ�
     */
    private String formNum;

    /**
     * �������
     */
    private String invtyEncd;

    /**
     * �������
     */
    private String invtyNm;

    /**
     * �������
     */
    private String invtyCd;

    /**
     * ����ͺ�
     */
    private String spcModel;

    /**
     * ������λ���
     */
    private String measrCorpId;

    /**
     * ������λ����
     */
    private String measrCorpNm;

    /**
     * ���
     */
    private BigDecimal bxRule;

    /**
     * ����������
     */
    private String invtyClsEncd;

    /**
     * �����������
     */
    private String invtyClsNm;

    /**
     * �ֿ���
     */
    private String whsEncd;

    /**
     * �ֿ�����
     */
    private String whsNm;

    /**
     * ��˰����
     */
    private BigDecimal noTaxUprc;

    /**
     * ��˰���
     */
    private BigDecimal noTaxAmt;

    /**
     * ˰��
     */
    private BigDecimal taxAmt;

    /**
     * ��˰����
     */
    private BigDecimal cntnTaxUprc;

    /**
     * ��˰�ϼ�
     */
    private BigDecimal prcTaxSum;

    /**
     * ˰��
     */
    private BigDecimal taxRate;

    /**
     * ����
     */
    private BigDecimal qty;

    /**
     * ����
     */
    private BigDecimal bxQty;

    /**
     * ����
     */
    private String batNum;

    /**
     * ��������
     */
    private String intlBat;

    /**
     * ��������
     */
    private Date prdcDt;

    /**
     * ʧЧ����
     */
    private Date invldtnDt;

    /**
     * ������
     */
    private String baoZhiQi;

    /**
     * ������Ŀ���
     */
    private String subCreditId;

    /**
     * ������Ŀ����
     */
    private String subCreditType;

    /**
     * ������Ŀ����
     */
    private String subCreditNm;

    /**
     * ����-�������
     */
    private BigDecimal subCreditMoney;

    /**
     * ����-�跽���
     */
    private BigDecimal subCreditDebitMoney;

    /**
     * ����-��������
     */
    private BigDecimal subCreditNum;

    /**
     * ����-�跽����
     */
    private BigDecimal subCreditDebitNum;

    /**
     * ������Ŀ����1�� 2��
     */
    private Integer subCreditPath;

    /**
     * �跽��Ŀ���
     */
    private String subDebitId;

    /**
     * �跽��Ŀ����
     */
    private String subDebitType;

    /**
     * �跽��Ŀ����
     */
    private String subDebitNm;

    /**
     * �跽-�跽���
     */
    private BigDecimal subDebitMoney;

    /**
     * �跽-�������
     */
    private BigDecimal subDebitCreditMoney;

    /**
     * �跽-�跽����
     */
    private BigDecimal subDebitNum;

    /**
     * �跽-��������
     */
    private BigDecimal subDebitCreditNum;

    /**
     * �跽��Ŀ���� 1�� 2��
     */
    private Integer subDebitPath;

    private static final long serialVersionUID = 1L;
    
    private SubjectCredit subjectCredit; //����
	private SubjectDebit subjectDebit; //�跽
	
	private Long toOrdrNum;//�ӱ��ʶ

	private BigDecimal unBllgQty;//δ��Ʊ����
    private BigDecimal unBllgUprc;//δ��Ʊ����
    private BigDecimal unBllgAmt;//δ��Ʊ���
    
    private String projEncd;//��Ŀ����
    private String projNm;//��Ŀ����
    
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

	public BigDecimal getUnBllgQty() {
		return unBllgQty;
	}

	public void setUnBllgQty(BigDecimal unBllgQty) {
		this.unBllgQty = unBllgQty;
	}

	public BigDecimal getUnBllgUprc() {
		return unBllgUprc;
	}

	public void setUnBllgUprc(BigDecimal unBllgUprc) {
		this.unBllgUprc = unBllgUprc;
	}

	public BigDecimal getUnBllgAmt() {
		return unBllgAmt;
	}

	public void setUnBllgAmt(BigDecimal unBllgAmt) {
		this.unBllgAmt = unBllgAmt;
	}

	public Long getToOrdrNum() {
		return toOrdrNum;
	}

	public void setToOrdrNum(Long toOrdrNum) {
		this.toOrdrNum = toOrdrNum;
	}

	public int getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(int ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getBackNum() {
        return backNum;
    }

    public void setBackNum(String backNum) {
        this.backNum = backNum;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    public String getInvtyNm() {
        return invtyNm;
    }

    public void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public String getInvtyCd() {
        return invtyCd;
    }

    public void setInvtyCd(String invtyCd) {
        this.invtyCd = invtyCd;
    }

    public String getSpcModel() {
        return spcModel;
    }

    public void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    public String getMeasrCorpId() {
        return measrCorpId;
    }

    public void setMeasrCorpId(String measrCorpId) {
        this.measrCorpId = measrCorpId;
    }

    public String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public BigDecimal getBxRule() {
        return bxRule;
    }

    public void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public String getInvtyClsEncd() {
        return invtyClsEncd;
    }

    public void setInvtyClsEncd(String invtyClsEncd) {
        this.invtyClsEncd = invtyClsEncd;
    }

    public String getInvtyClsNm() {
        return invtyClsNm;
    }

    public void setInvtyClsNm(String invtyClsNm) {
        this.invtyClsNm = invtyClsNm;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public BigDecimal getNoTaxUprc() {
        return noTaxUprc;
    }

    public void setNoTaxUprc(BigDecimal noTaxUprc) {
        this.noTaxUprc = noTaxUprc;
    }

    public BigDecimal getNoTaxAmt() {
        return noTaxAmt;
    }

    public void setNoTaxAmt(BigDecimal noTaxAmt) {
        this.noTaxAmt = noTaxAmt;
    }

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public BigDecimal getCntnTaxUprc() {
        return cntnTaxUprc;
    }

    public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
        this.cntnTaxUprc = cntnTaxUprc;
    }

    public BigDecimal getPrcTaxSum() {
        return prcTaxSum;
    }

    public void setPrcTaxSum(BigDecimal prcTaxSum) {
        this.prcTaxSum = prcTaxSum;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getBxQty() {
        return bxQty;
    }

    public void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
    }

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum;
    }

    public String getIntlBat() {
        return intlBat;
    }

    public void setIntlBat(String intlBat) {
        this.intlBat = intlBat;
    }

    public Date getPrdcDt() {
        return prdcDt;
    }

    public void setPrdcDt(Date prdcDt) {
        this.prdcDt = prdcDt;
    }

    public Date getInvldtnDt() {
        return invldtnDt;
    }

    public void setInvldtnDt(Date invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public String getBaoZhiQi() {
        return baoZhiQi;
    }

    public void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi;
    }

    public String getSubCreditId() {
        return subCreditId;
    }

    public void setSubCreditId(String subCreditId) {
        this.subCreditId = subCreditId;
    }

    public String getSubCreditType() {
        return subCreditType;
    }

    public void setSubCreditType(String subCreditType) {
        this.subCreditType = subCreditType;
    }

    public String getSubCreditNm() {
        return subCreditNm;
    }

    public void setSubCreditNm(String subCreditNm) {
        this.subCreditNm = subCreditNm;
    }

    public BigDecimal getSubCreditMoney() {
        return subCreditMoney;
    }

    public void setSubCreditMoney(BigDecimal subCreditMoney) {
        this.subCreditMoney = subCreditMoney;
    }

    public BigDecimal getSubCreditDebitMoney() {
        return subCreditDebitMoney;
    }

    public void setSubCreditDebitMoney(BigDecimal subCreditDebitMoney) {
        this.subCreditDebitMoney = subCreditDebitMoney;
    }

    public BigDecimal getSubCreditNum() {
        return subCreditNum;
    }

    public void setSubCreditNum(BigDecimal subCreditNum) {
        this.subCreditNum = subCreditNum;
    }

    public BigDecimal getSubCreditDebitNum() {
        return subCreditDebitNum;
    }

    public void setSubCreditDebitNum(BigDecimal subCreditDebitNum) {
        this.subCreditDebitNum = subCreditDebitNum;
    }

    public Integer getSubCreditPath() {
        return subCreditPath;
    }

    public void setSubCreditPath(Integer subCreditPath) {
        this.subCreditPath = subCreditPath;
    }

    public String getSubDebitId() {
        return subDebitId;
    }

    public void setSubDebitId(String subDebitId) {
        this.subDebitId = subDebitId;
    }

    public String getSubDebitType() {
        return subDebitType;
    }

    public void setSubDebitType(String subDebitType) {
        this.subDebitType = subDebitType;
    }

    public String getSubDebitNm() {
        return subDebitNm;
    }

    public void setSubDebitNm(String subDebitNm) {
        this.subDebitNm = subDebitNm;
    }

    public BigDecimal getSubDebitMoney() {
        return subDebitMoney;
    }

    public void setSubDebitMoney(BigDecimal subDebitMoney) {
        this.subDebitMoney = subDebitMoney;
    }

    public BigDecimal getSubDebitCreditMoney() {
        return subDebitCreditMoney;
    }

    public void setSubDebitCreditMoney(BigDecimal subDebitCreditMoney) {
        this.subDebitCreditMoney = subDebitCreditMoney;
    }

    public BigDecimal getSubDebitNum() {
        return subDebitNum;
    }

    public void setSubDebitNum(BigDecimal subDebitNum) {
        this.subDebitNum = subDebitNum;
    }

    public BigDecimal getSubDebitCreditNum() {
        return subDebitCreditNum;
    }

    public void setSubDebitCreditNum(BigDecimal subDebitCreditNum) {
        this.subDebitCreditNum = subDebitCreditNum;
    }

    public Integer getSubDebitPath() {
        return subDebitPath;
    }

    public void setSubDebitPath(Integer subDebitPath) {
        this.subDebitPath = subDebitPath;
    }

	public SubjectCredit getSubjectCredit() {
		return subjectCredit;
	}

	public void setSubjectCredit(SubjectCredit subjectCredit) {
		this.subjectCredit = subjectCredit;
	}

	public SubjectDebit getSubjectDebit() {
		return subjectDebit;
	}

	public void setSubjectDebit(SubjectDebit subjectDebit) {
		this.subjectDebit = subjectDebit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
}
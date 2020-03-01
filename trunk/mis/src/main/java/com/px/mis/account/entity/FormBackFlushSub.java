package com.px.mis.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 回冲单子表
 * @author 
 */
public class FormBackFlushSub implements Serializable {
    /**
     * 序号
     */
    private int ordrNum;

    /**
     * 回冲单主表编码
     */
    private String backNum;

    /**
     * 单据号
     */
    private String formNum;

    /**
     * 存货编码
     */
    private String invtyEncd;

    /**
     * 存货名称
     */
    private String invtyNm;

    /**
     * 存货代码
     */
    private String invtyCd;

    /**
     * 规格型号
     */
    private String spcModel;

    /**
     * 计量单位编号
     */
    private String measrCorpId;

    /**
     * 计量单位名称
     */
    private String measrCorpNm;

    /**
     * 箱规
     */
    private BigDecimal bxRule;

    /**
     * 存货分类编码
     */
    private String invtyClsEncd;

    /**
     * 存货分类名称
     */
    private String invtyClsNm;

    /**
     * 仓库编号
     */
    private String whsEncd;

    /**
     * 仓库名称
     */
    private String whsNm;

    /**
     * 无税单价
     */
    private BigDecimal noTaxUprc;

    /**
     * 无税金额
     */
    private BigDecimal noTaxAmt;

    /**
     * 税额
     */
    private BigDecimal taxAmt;

    /**
     * 含税单价
     */
    private BigDecimal cntnTaxUprc;

    /**
     * 价税合计
     */
    private BigDecimal prcTaxSum;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 箱数
     */
    private BigDecimal bxQty;

    /**
     * 批次
     */
    private String batNum;

    /**
     * 国际批次
     */
    private String intlBat;

    /**
     * 生产日期
     */
    private Date prdcDt;

    /**
     * 失效日期
     */
    private Date invldtnDt;

    /**
     * 保质期
     */
    private String baoZhiQi;

    /**
     * 贷方科目编号
     */
    private String subCreditId;

    /**
     * 贷方科目类型
     */
    private String subCreditType;

    /**
     * 贷方科目名称
     */
    private String subCreditNm;

    /**
     * 贷方-贷方金额
     */
    private BigDecimal subCreditMoney;

    /**
     * 贷方-借方金额
     */
    private BigDecimal subCreditDebitMoney;

    /**
     * 贷方-贷方数量
     */
    private BigDecimal subCreditNum;

    /**
     * 贷方-借方数量
     */
    private BigDecimal subCreditDebitNum;

    /**
     * 贷方科目方向1借 2贷
     */
    private Integer subCreditPath;

    /**
     * 借方科目编号
     */
    private String subDebitId;

    /**
     * 借方科目类型
     */
    private String subDebitType;

    /**
     * 借方科目名称
     */
    private String subDebitNm;

    /**
     * 借方-借方金额
     */
    private BigDecimal subDebitMoney;

    /**
     * 借方-贷方金额
     */
    private BigDecimal subDebitCreditMoney;

    /**
     * 借方-借方数量
     */
    private BigDecimal subDebitNum;

    /**
     * 借方-贷方数量
     */
    private BigDecimal subDebitCreditNum;

    /**
     * 借方科目方向 1借 2贷
     */
    private Integer subDebitPath;

    private static final long serialVersionUID = 1L;
    
    private SubjectCredit subjectCredit; //贷方
	private SubjectDebit subjectDebit; //借方
	
	private Long toOrdrNum;//子表标识

	private BigDecimal unBllgQty;//未开票数量
    private BigDecimal unBllgUprc;//未开票单价
    private BigDecimal unBllgAmt;//未开票金额
    
    private String projEncd;//项目编码
    private String projNm;//项目名称
    
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
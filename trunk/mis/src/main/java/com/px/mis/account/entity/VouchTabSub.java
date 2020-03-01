package com.px.mis.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * vouch_tab_sub
 *  1凭证子表
 * @author 
 */
public class VouchTabSub implements Serializable {
    /**
     * 序号
     */
    private Long ordrNum;

    /**
     * 凭证编码
     */
    private String comnVouchId;

    /**
     * 单据号
     */
    private String formNum;

    /**
     * 单据类型
     */
    private String formTypEncd;

    /**
     * 单据类型名称
     */
    private String formTypName;

    /**
     * 存货编码
     */
    private String invtyEncd;

    /**
     * 存货名称
     */
    private String invtyNm;

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
   
    //存货代码
    private String invtyCd;
    //规格型号 
    private String spcModel;
    //业务类型
    private String bizTypNm;
    //供应商往来
    private String provrId;
    private String provrNm;
    
    //客户往来
    private String custId;
    private String custNm;
    //个人往来---暂无
    
    //项目核算
    private String projEncd;//项目编码
    private String projNm;//项目名称
    private String projClsEncd;//项目大类
    private String projClsNm;//项目大类名称
    
    //部门核算
    private String deptId;//部门编号
    private String deptName;//部门名称
    
    
    
    public String getProvrId() {
		return provrId;
	}

	public void setProvrId(String provrId) {
		this.provrId = provrId;
	}

	public String getProvrNm() {
		return provrNm;
	}

	public void setProvrNm(String provrNm) {
		this.provrNm = provrNm;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustNm() {
		return custNm;
	}

	public void setCustNm(String custNm) {
		this.custNm = custNm;
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

	public String getProjClsEncd() {
		return projClsEncd;
	}

	public void setProjClsEncd(String projClsEncd) {
		this.projClsEncd = projClsEncd;
	}

	public String getProjClsNm() {
		return projClsNm;
	}

	public void setProjClsNm(String projClsNm) {
		this.projClsNm = projClsNm;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getBizTypNm() {
		return bizTypNm;
	}

	public void setBizTypNm(String bizTypNm) {
		this.bizTypNm = bizTypNm;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private static final long serialVersionUID = 1L;

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getComnVouchId() {
        return comnVouchId;
    }

    public void setComnVouchId(String comnVouchId) {
        this.comnVouchId = comnVouchId;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public String getFormTypEncd() {
        return formTypEncd;
    }

    public void setFormTypEncd(String formTypEncd) {
        this.formTypEncd = formTypEncd;
    }

    public String getFormTypName() {
        return formTypName;
    }

    public void setFormTypName(String formTypName) {
        this.formTypName = formTypName;
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

    public VouchTabSub() {}
	public VouchTabSub(String subCreditId, String subCreditType, String subCreditNm, BigDecimal subCreditMoney,
			BigDecimal subCreditDebitMoney, BigDecimal subCreditNum, BigDecimal subCreditDebitNum,
			Integer subCreditPath, String subDebitId, String subDebitType, String subDebitNm, BigDecimal subDebitMoney,
			BigDecimal subDebitCreditMoney, BigDecimal subDebitNum, BigDecimal subDebitCreditNum,
			Integer subDebitPath) {
		super();
		this.subCreditId = subCreditId;
		this.subCreditType = subCreditType;
		this.subCreditNm = subCreditNm;
		this.subCreditMoney = subCreditMoney;
		this.subCreditDebitMoney = subCreditDebitMoney;
		this.subCreditNum = subCreditNum;
		this.subCreditDebitNum = subCreditDebitNum;
		this.subCreditPath = subCreditPath;
		this.subDebitId = subDebitId;
		this.subDebitType = subDebitType;
		this.subDebitNm = subDebitNm;
		this.subDebitMoney = subDebitMoney;
		this.subDebitCreditMoney = subDebitCreditMoney;
		this.subDebitNum = subDebitNum;
		this.subDebitCreditNum = subDebitCreditNum;
		this.subDebitPath = subDebitPath;
	}
	public VouchTabSub(String subDebitId, String subDebitType, String subDebitNm, BigDecimal subDebitMoney,
			BigDecimal subDebitCreditMoney, BigDecimal subDebitNum, BigDecimal subDebitCreditNum,
			Integer subDebitPath) {
		super();
		this.subDebitId = subDebitId;
		this.subDebitType = subDebitType;
		this.subDebitNm = subDebitNm;
		this.subDebitMoney = subDebitMoney;
		this.subDebitCreditMoney = subDebitCreditMoney;
		this.subDebitNum = subDebitNum;
		this.subDebitCreditNum = subDebitCreditNum;
		this.subDebitPath = subDebitPath;
	}
	public VouchTabSub(boolean isCredit,String subCreditId, String subCreditType, String subCreditNm, BigDecimal subCreditMoney,
			BigDecimal subCreditDebitMoney, BigDecimal subCreditNum, BigDecimal subCreditDebitNum,
			Integer subCreditPath) {
		super();
		this.subCreditId = subCreditId;
		this.subCreditType = subCreditType;
		this.subCreditNm = subCreditNm;
		this.subCreditMoney = subCreditMoney;
		this.subCreditDebitMoney = subCreditDebitMoney;
		this.subCreditNum = subCreditNum;
		this.subCreditDebitNum = subCreditDebitNum;
		this.subCreditPath = subCreditPath;
	}
}
package com.px.mis.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * vouch_tab_sub
 *  1ƾ֤�ӱ�
 * @author 
 */
public class VouchTabSub implements Serializable {
    /**
     * ���
     */
    private Long ordrNum;

    /**
     * ƾ֤����
     */
    private String comnVouchId;

    /**
     * ���ݺ�
     */
    private String formNum;

    /**
     * ��������
     */
    private String formTypEncd;

    /**
     * ������������
     */
    private String formTypName;

    /**
     * �������
     */
    private String invtyEncd;

    /**
     * �������
     */
    private String invtyNm;

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
   
    //�������
    private String invtyCd;
    //����ͺ� 
    private String spcModel;
    //ҵ������
    private String bizTypNm;
    //��Ӧ������
    private String provrId;
    private String provrNm;
    
    //�ͻ�����
    private String custId;
    private String custNm;
    //��������---����
    
    //��Ŀ����
    private String projEncd;//��Ŀ����
    private String projNm;//��Ŀ����
    private String projClsEncd;//��Ŀ����
    private String projClsNm;//��Ŀ��������
    
    //���ź���
    private String deptId;//���ű��
    private String deptName;//��������
    
    
    
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
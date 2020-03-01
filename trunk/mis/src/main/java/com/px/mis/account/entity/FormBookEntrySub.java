package com.px.mis.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * form_book_entry_sub
 * @author 
 */
public class FormBookEntrySub implements Serializable {
	public FormBookEntrySub() {}
	
	public FormBookEntrySub(String formNum,Long toOrdrNum, String invtyEncd, String invtyNm, String invtyCd, String spcModel,
			String measrCorpId, String measrCorpNm, BigDecimal bxRule, String invtyClsEncd, String invtyClsNm,
			String whsEncd, String whsNm, BigDecimal noTaxUprc, BigDecimal noTaxAmt, BigDecimal taxAmt,
			BigDecimal cntnTaxUprc, BigDecimal prcTaxSum, BigDecimal taxRate, BigDecimal qty, BigDecimal bxQty,
			String batNum, String intlBat,String prdcDt1, String invldtnDt1, String baoZhiQi,BigDecimal unBllgQty,
		    BigDecimal unBllgUprc,BigDecimal unBllgAmt,int ordrNum,String projEncd,String projNm) {
		super();
		this.formNum = formNum;
		this.toOrdrNum = toOrdrNum;
		this.invtyEncd = invtyEncd;
		this.invtyNm = invtyNm;
		this.invtyCd = invtyCd;
		this.spcModel = spcModel;
		this.measrCorpId = measrCorpId;
		this.measrCorpNm = measrCorpNm;
		this.bxRule = bxRule;
		this.invtyClsEncd = invtyClsEncd;
		this.invtyClsNm = invtyClsNm;
		this.whsEncd = whsEncd;
		this.whsNm = whsNm;
		this.noTaxUprc = noTaxUprc;
		this.noTaxAmt = noTaxAmt;
		this.taxAmt = taxAmt;
		this.cntnTaxUprc = cntnTaxUprc;
		this.prcTaxSum = prcTaxSum;
		this.taxRate = taxRate;
		this.qty = qty;
		this.bxQty = bxQty;
		this.batNum = batNum;
		this.intlBat = intlBat;
		this.prdcDt1 = prdcDt1;
		this.invldtnDt1 = invldtnDt1;
		this.baoZhiQi = baoZhiQi;		
		this.unBllgQty = unBllgQty;
		this.unBllgUprc = unBllgUprc;
		this.unBllgAmt = unBllgAmt;
		this.ordrNum = ordrNum;
		this.projEncd = projEncd;
		this.projNm = projNm;
		
	}
	
	 /**
     * 序号
     */
	@JsonProperty("序号")
    private Integer ordrNum;

    /**
     * 单据号
     */
	@JsonProperty("单据号")
    private String formNum;

    /**
     * 存货编码
     */
	@JsonProperty("存货编码")
    private String invtyEncd;

    /**
     * 存货名称
     */
	@JsonProperty("存货名称")
    private String invtyNm;

    /**
     * 存货代码
     */
	@JsonProperty("存货代码")
    private String invtyCd;

    /**
     * 规格型号
     */
	@JsonProperty("规格型号")
    private String spcModel;

    /**
     * 计量单位编号
     */
	@JsonProperty("计量单位编号")
    private String measrCorpId;

    /**
     * 计量单位名称
     */
	@JsonProperty("计量单位名称")
    private String measrCorpNm;

    /**
     * 箱规
     */
	@JsonProperty("箱规")
    private BigDecimal bxRule;

    /**
     * 存货分类编码
     */
	@JsonProperty("存货分类编码")
    private String invtyClsEncd;

    /**
     * 存货分类名称
     */
	@JsonProperty("存货分类名称")
    private String invtyClsNm;

    /**
     * 仓库编号
     */
	@JsonProperty("仓库编号")
    private String whsEncd;

    /**
     * 仓库名称
     */
	@JsonProperty("仓库名称")
    private String whsNm;

    /**
     * 无税单价
     */
	@JsonProperty("无税单价")
    private BigDecimal noTaxUprc;

    /**
     * 无税金额
     */
	@JsonProperty("无税金额")
    private BigDecimal noTaxAmt;

    /**
     * 税额
     */
	@JsonProperty("税额")
    private BigDecimal taxAmt;

    /**
     * 含税单价
     */
	@JsonProperty("含税单价")
    private BigDecimal cntnTaxUprc;

    /**
     * 价税合计
     */
	@JsonProperty("价税合计")
    private BigDecimal prcTaxSum;

    /**
     * 税率
     */
	@JsonProperty("税率")
    private BigDecimal taxRate;

    /**
     * 数量
     */
	@JsonProperty("数量")
    private BigDecimal qty;

    /**
     * 箱数
     */
	@JsonProperty("箱数")
    private BigDecimal bxQty;

    /**
     * 批次
     */
	@JsonProperty("批次")
    private String batNum;

    /**
     * 国际批次
     */
	@JsonProperty("国际批次")
    private String intlBat;

    /**
     * 生产日期
     */
	@JsonProperty("生产日期")
    private Date prdcDt;

    /**
     * 失效日期
     */
	@JsonProperty("失效日期")
    private Date invldtnDt;

    /**
     * 保质期
     */
	@JsonProperty("保质期")
    private String baoZhiQi;
	@JsonProperty("是否记账")
    private Integer isNtBookOk; //是否记账
	@JsonProperty("记账日期")
    private String bookOkDt;//记账日期
	@JsonProperty("记账人")
    private String bookOkAcc;//记账人 
	@JsonProperty("收发类型编号")
    private String recvSendCateId;//收发类型编号
	@JsonProperty("收发类型名称")
    private String recvSendCateNm;//收发类型名称
	@JsonProperty("凭证编号")
    private String makeVouchId;//凭证编号
	@JsonProperty("凭证摘要")
    private String makeVouchAbst;//凭证摘要
    @JsonUnwrapped
	@JsonProperty("贷方")
	private SubjectCredit subjectCredit; //贷方
    @JsonUnwrapped
	@JsonProperty("借方")
	private SubjectDebit subjectDebit; //借方
	@JsonProperty("贷方名称")
	private String subCreditNm;
//	@JsonProperty("生产日期")
	@JsonIgnore
    private String prdcDt1; //生产日期
//	@JsonProperty("失效日期")
    @JsonIgnore
    private String invldtnDt1; //失效日期
	@JsonIgnore
    private FormBookEntry formBookEntry;
	@JsonProperty("出入库类型")
    private String outIntoWhsTypNm; //出入库类型
	@JsonProperty("业务类型名称")
    private String bizTypNm;
	@JsonProperty("贷方科目编号")
    private String subCreditId;//科目编号
	@JsonProperty("贷方科目类型")
  	private String subCreditType;//科目类型2
	@JsonProperty("贷方金额")
	private BigDecimal subCreditMoney;//贷方金额
	@JsonProperty("贷方-借方金额")
	private BigDecimal subCreditDebitMoney;//贷方-借方金额
	@JsonProperty("贷方数量")
	private BigDecimal subCreditNum;//贷方数量
	@JsonProperty("贷方-借方数量")
	private BigDecimal subCreditDebitNum;//贷方-借方数量
	@JsonProperty("科目方向")
	private Integer subCreditPath;//科目方向 借1贷2
	@JsonProperty("借方科目编号")
    private String subDebitId;//科目编号
	@JsonProperty("借方科目类型")
  	private String subDebitType;//科目类型2
	@JsonProperty("科目名称")
	private String subDebitNm;//科目名称
	@JsonProperty("借方金额")
	private BigDecimal subDebitMoney;//借方金额
	@JsonProperty("借方-贷方金额")
	private BigDecimal subDebitCreditMoney;//借方-贷方金额
	@JsonProperty("借方数量")
	private BigDecimal subDebitNum;//借方数量
	@JsonProperty("借方-贷方数量")
	private BigDecimal subDebitCreditNum;//借方-贷方数量
	@JsonProperty("发出单价")
	private BigDecimal sendUprc; //发出单价
	@JsonProperty("发出金额")
	private BigDecimal sendAmt;  //发出金额
	@JsonProperty("发出数量")
	private BigDecimal sendQty;  //发出数量
	@JsonProperty("收入单价")
	private BigDecimal inUprc; //收入单价
	@JsonProperty("收入金额")
	private BigDecimal inAmt;  //收入金额
	@JsonProperty("收入数量")
	private BigDecimal inQty;  //收入数量
	
	
	@JsonProperty("未开票数量")
	private BigDecimal unBllgQty;//未开票数量
	@JsonProperty("未开票单价")
    private BigDecimal unBllgUprc;//未开票单价
	@JsonProperty("未开票金额")
    private BigDecimal unBllgAmt;//未开票金额
	@JsonProperty("科目方向 借1贷2")
    private Integer subDebitPath;//科目方向 借1贷2
	@JsonProperty("项目编码")
    private String projEncd;//项目编码
	@JsonProperty("项目名称")
    private String projNm;//项目名称
    
	
	private String tyepId; //临时字段
    
    
    public String getTyepId() {
		return tyepId;
	}

	public void setTyepId(String tyepId) {
		this.tyepId = tyepId;
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

	public Integer getSubDebitPath() {
		return subDebitPath;
	}

	public void setSubDebitPath(Integer subDebitPath) {
		this.subDebitPath = subDebitPath;
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

	private static final long serialVersionUID = 1L;
    
	@JsonIgnore
    private Long toOrdrNum;//子表标识
    

	public Long getToOrdrNum() {
		return toOrdrNum;
	}

	public void setToOrdrNum(Long toOrdrNum) {
		this.toOrdrNum = toOrdrNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getInvldtnDt1() {
		return invldtnDt1;
	}

	public void setInvldtnDt1(String invldtnDt1) {
		this.invldtnDt1 = invldtnDt1;
	}

	public Integer getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Integer ordrNum) {
        this.ordrNum = ordrNum;
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
	
	public String getPrdcDt1() {
		return prdcDt1;
	}
	
	public Integer getIsNtBookOk() {
		return isNtBookOk;
	}

	public void setIsNtBookOk(Integer isNtBookOk) {
		this.isNtBookOk = isNtBookOk;
	}

	public void setPrdcDt1(String prdcDt1) {
		this.prdcDt1 = prdcDt1;
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
	
	public String getBookOkDt() {
		return bookOkDt;
	}

	public void setBookOkDt(String bookOkDt) {
		this.bookOkDt = bookOkDt;
	}

	public String getBookOkAcc() {
		return bookOkAcc;
	}

	public void setBookOkAcc(String bookOkAcc) {
		this.bookOkAcc = bookOkAcc;
	}

	public String getSubCreditNm() {
		return subCreditNm;
	}

	public void setSubCreditNm(String subCreditNm) {
		this.subCreditNm = subCreditNm;
	}

	public String getSubDebitNm() {
		return subDebitNm;
	}

	public void setSubDebitNm(String subDebitNm) {
		this.subDebitNm = subDebitNm;
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

	public String getMakeVouchId() {
		return makeVouchId;
	}

	public void setMakeVouchId(String makeVouchId) {
		this.makeVouchId = makeVouchId;
	}

	public String getMakeVouchAbst() {
		return makeVouchAbst;
	}

	public void setMakeVouchAbst(String makeVouchAbst) {
		this.makeVouchAbst = makeVouchAbst;
	}
	
	public FormBookEntry getFormBookEntry() {
		return formBookEntry;
	}

	public void setFormBookEntry(FormBookEntry formBookEntry) {
	}
	
	public String getOutIntoWhsTypNm() {
		return outIntoWhsTypNm;
	}

	public void setOutIntoWhsTypNm(String outIntoWhsTypNm) {
		this.outIntoWhsTypNm = outIntoWhsTypNm;
	}
	
	public String getBizTypNm() {
		return bizTypNm;
	}

	public void setBizTypNm(String bizTypNm) {
		this.bizTypNm = bizTypNm;
	}
	
	public String getSubCreditType() {
		return subCreditType;
	}

	public void setSubCreditType(String subCreditType) {
		this.subCreditType = subCreditType;
	}
	
	public String getSubDebitType() {
		return subDebitType;
	}

	public void setSubDebitType(String subDebitType) {
		this.subDebitType = subDebitType;
	}
	
	public String getSubCreditId() {
		return subCreditId;
	}

	public void setSubCreditId(String subCreditId) {
		this.subCreditId = subCreditId;
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
	
	
	public BigDecimal getSendUprc() {
		return sendUprc;
	}

	public void setSendUprc(BigDecimal sendUprc) {
		this.sendUprc = sendUprc;
	}

	public BigDecimal getSendAmt() {
		return sendAmt;
	}

	public void setSendAmt(BigDecimal sendAmt) {
		this.sendAmt = sendAmt;
	}

	public BigDecimal getSendQty() {
		return sendQty;
	}

	public void setSendQty(BigDecimal sendQty) {
		this.sendQty = sendQty;
	}
	
	
	public BigDecimal getInUprc() {
		return inUprc;
	}

	public void setInUprc(BigDecimal inUprc) {
		this.inUprc = inUprc;
	}

	public BigDecimal getInAmt() {
		return inAmt;
	}

	public void setInAmt(BigDecimal inAmt) {
		this.inAmt = inAmt;
	}

	public BigDecimal getInQty() {
		return inQty;
	}

	public void setInQty(BigDecimal inQty) {
		this.inQty = inQty;
	}

	@Override
	public String toString() {
		return "FormBookEntrySub [ordrNum=" + ordrNum + ", formNum=" + formNum
				+ ", invtyEncd=" + invtyEncd + ", invtyNm=" + invtyNm + ", invtyCd=" + invtyCd + ", spcModel="
				+ spcModel + ", measrCorpId=" + measrCorpId + ", measrCorpNm=" + measrCorpNm + ", bxRule=" + bxRule
				+ ", invtyClsEncd=" + invtyClsEncd + ", invtyClsNm=" + invtyClsNm + ", whsEncd=" + whsEncd + ", whsNm="
				+ whsNm + ", noTaxUprc=" + noTaxUprc + ", noTaxAmt=" + noTaxAmt + ", taxAmt=" + taxAmt
				+ ", cntnTaxUprc=" + cntnTaxUprc + ", prcTaxSum=" + prcTaxSum + ", taxRate=" + taxRate + ", qty=" + qty
				+ ", bxQty=" + bxQty + ", batNum=" + batNum + ", intlBat=" + intlBat + ", prdcDt=" + prdcDt
				+ ", invldtnDt=" + invldtnDt + ", baoZhiQi=" + baoZhiQi + "]";
	}
    
}
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
     * ���
     */
	@JsonProperty("���")
    private Integer ordrNum;

    /**
     * ���ݺ�
     */
	@JsonProperty("���ݺ�")
    private String formNum;

    /**
     * �������
     */
	@JsonProperty("�������")
    private String invtyEncd;

    /**
     * �������
     */
	@JsonProperty("�������")
    private String invtyNm;

    /**
     * �������
     */
	@JsonProperty("�������")
    private String invtyCd;

    /**
     * ����ͺ�
     */
	@JsonProperty("����ͺ�")
    private String spcModel;

    /**
     * ������λ���
     */
	@JsonProperty("������λ���")
    private String measrCorpId;

    /**
     * ������λ����
     */
	@JsonProperty("������λ����")
    private String measrCorpNm;

    /**
     * ���
     */
	@JsonProperty("���")
    private BigDecimal bxRule;

    /**
     * ����������
     */
	@JsonProperty("����������")
    private String invtyClsEncd;

    /**
     * �����������
     */
	@JsonProperty("�����������")
    private String invtyClsNm;

    /**
     * �ֿ���
     */
	@JsonProperty("�ֿ���")
    private String whsEncd;

    /**
     * �ֿ�����
     */
	@JsonProperty("�ֿ�����")
    private String whsNm;

    /**
     * ��˰����
     */
	@JsonProperty("��˰����")
    private BigDecimal noTaxUprc;

    /**
     * ��˰���
     */
	@JsonProperty("��˰���")
    private BigDecimal noTaxAmt;

    /**
     * ˰��
     */
	@JsonProperty("˰��")
    private BigDecimal taxAmt;

    /**
     * ��˰����
     */
	@JsonProperty("��˰����")
    private BigDecimal cntnTaxUprc;

    /**
     * ��˰�ϼ�
     */
	@JsonProperty("��˰�ϼ�")
    private BigDecimal prcTaxSum;

    /**
     * ˰��
     */
	@JsonProperty("˰��")
    private BigDecimal taxRate;

    /**
     * ����
     */
	@JsonProperty("����")
    private BigDecimal qty;

    /**
     * ����
     */
	@JsonProperty("����")
    private BigDecimal bxQty;

    /**
     * ����
     */
	@JsonProperty("����")
    private String batNum;

    /**
     * ��������
     */
	@JsonProperty("��������")
    private String intlBat;

    /**
     * ��������
     */
	@JsonProperty("��������")
    private Date prdcDt;

    /**
     * ʧЧ����
     */
	@JsonProperty("ʧЧ����")
    private Date invldtnDt;

    /**
     * ������
     */
	@JsonProperty("������")
    private String baoZhiQi;
	@JsonProperty("�Ƿ����")
    private Integer isNtBookOk; //�Ƿ����
	@JsonProperty("��������")
    private String bookOkDt;//��������
	@JsonProperty("������")
    private String bookOkAcc;//������ 
	@JsonProperty("�շ����ͱ��")
    private String recvSendCateId;//�շ����ͱ��
	@JsonProperty("�շ���������")
    private String recvSendCateNm;//�շ���������
	@JsonProperty("ƾ֤���")
    private String makeVouchId;//ƾ֤���
	@JsonProperty("ƾ֤ժҪ")
    private String makeVouchAbst;//ƾ֤ժҪ
    @JsonUnwrapped
	@JsonProperty("����")
	private SubjectCredit subjectCredit; //����
    @JsonUnwrapped
	@JsonProperty("�跽")
	private SubjectDebit subjectDebit; //�跽
	@JsonProperty("��������")
	private String subCreditNm;
//	@JsonProperty("��������")
	@JsonIgnore
    private String prdcDt1; //��������
//	@JsonProperty("ʧЧ����")
    @JsonIgnore
    private String invldtnDt1; //ʧЧ����
	@JsonIgnore
    private FormBookEntry formBookEntry;
	@JsonProperty("���������")
    private String outIntoWhsTypNm; //���������
	@JsonProperty("ҵ����������")
    private String bizTypNm;
	@JsonProperty("������Ŀ���")
    private String subCreditId;//��Ŀ���
	@JsonProperty("������Ŀ����")
  	private String subCreditType;//��Ŀ����2
	@JsonProperty("�������")
	private BigDecimal subCreditMoney;//�������
	@JsonProperty("����-�跽���")
	private BigDecimal subCreditDebitMoney;//����-�跽���
	@JsonProperty("��������")
	private BigDecimal subCreditNum;//��������
	@JsonProperty("����-�跽����")
	private BigDecimal subCreditDebitNum;//����-�跽����
	@JsonProperty("��Ŀ����")
	private Integer subCreditPath;//��Ŀ���� ��1��2
	@JsonProperty("�跽��Ŀ���")
    private String subDebitId;//��Ŀ���
	@JsonProperty("�跽��Ŀ����")
  	private String subDebitType;//��Ŀ����2
	@JsonProperty("��Ŀ����")
	private String subDebitNm;//��Ŀ����
	@JsonProperty("�跽���")
	private BigDecimal subDebitMoney;//�跽���
	@JsonProperty("�跽-�������")
	private BigDecimal subDebitCreditMoney;//�跽-�������
	@JsonProperty("�跽����")
	private BigDecimal subDebitNum;//�跽����
	@JsonProperty("�跽-��������")
	private BigDecimal subDebitCreditNum;//�跽-��������
	@JsonProperty("��������")
	private BigDecimal sendUprc; //��������
	@JsonProperty("�������")
	private BigDecimal sendAmt;  //�������
	@JsonProperty("��������")
	private BigDecimal sendQty;  //��������
	@JsonProperty("���뵥��")
	private BigDecimal inUprc; //���뵥��
	@JsonProperty("������")
	private BigDecimal inAmt;  //������
	@JsonProperty("��������")
	private BigDecimal inQty;  //��������
	
	
	@JsonProperty("δ��Ʊ����")
	private BigDecimal unBllgQty;//δ��Ʊ����
	@JsonProperty("δ��Ʊ����")
    private BigDecimal unBllgUprc;//δ��Ʊ����
	@JsonProperty("δ��Ʊ���")
    private BigDecimal unBllgAmt;//δ��Ʊ���
	@JsonProperty("��Ŀ���� ��1��2")
    private Integer subDebitPath;//��Ŀ���� ��1��2
	@JsonProperty("��Ŀ����")
    private String projEncd;//��Ŀ����
	@JsonProperty("��Ŀ����")
    private String projNm;//��Ŀ����
    
	
	private String tyepId; //��ʱ�ֶ�
    
    
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
    private Long toOrdrNum;//�ӱ��ʶ
    

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
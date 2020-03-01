package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

//�ڳ����ʵ����
public class TermBgnBal {
	@JsonProperty("�Ƿ����")
	private Integer isNtBookEntry;// �Ƿ����
	@JsonProperty("�ֿ����")
	private String whsEncd;// �ֿ����
	@JsonProperty("�ֿ�����")
	private String whsNm;// �ֿ�����
	@JsonProperty("�������")
	private String invtyEncd;// �������
	@JsonProperty("�������")
	private String invtyNm;// �������
	@JsonProperty("����ͺ�")
	private String spcModel;// ����ͺ�
	@JsonProperty("������λ")
	private String measrCorpNm;// ������λ����
	@JsonProperty("����")
	private BigDecimal qty;// ����
	@JsonProperty("��˰����")
	private BigDecimal noTaxUprc;// ��˰����
	@JsonProperty("��˰���")
	private BigDecimal noTaxAmt;// ��˰���
	@JsonProperty("����")
	private String batNum;// ����
	@JsonProperty("��������")
	private String prdcDt;// ��������
	@JsonProperty("������")
	private String baoZhiQi;// ������
	@JsonProperty("ʧЧ����")
	private String invldtnDt;// ʧЧ����
	@JsonProperty("�����Ŀ����")
	private String invtySubjId;// �����Ŀ����

	/* ����Ϊ�������Ե��ֶ� */
	@JsonProperty("���")

	private Integer ordrNum;// ���
	@JsonProperty("���ű���")

	private String deptId;// ���ű���
	@JsonProperty("����")

	private BigDecimal bxQty;// ����

	@JsonProperty("˰��")
	private BigDecimal taxAmt;// ˰��

	@JsonProperty("˰��")
	private BigDecimal taxRate;// ˰��

	@JsonProperty("��˰����")
	private BigDecimal cntnTaxUprc;// ��˰����

	@JsonProperty("��˰�ϼ�")
	private BigDecimal prcTaxSum;// ��˰�ϼ�

	@JsonProperty("��������")
	private String intlBat;// ��������

	@JsonProperty("������")
	private String bookEntryPers;// ������

	@JsonProperty("����ʱ��")
	private String bookEntryTm;// ����ʱ��

	@JsonProperty("������")
	private String setupPers;// ������

	@JsonProperty("����ʱ��")
	private String setupTm;// ����ʱ��

	@JsonProperty("�޸���")
	private String modiPers;// �޸���

	@JsonProperty("�޸�ʱ��")
	private String modiTm;// �޸�ʱ��

	@JsonProperty("��Ŀ����")
	private String projEncd;// ��Ŀ����

	@JsonProperty("��ע")
	private String memo;// ��ע

	@JsonProperty("��Ŀ����")
	private String projNm;// ��Ŀ����

	@JsonProperty("���")
	private BigDecimal bxRule;// ���

	@JsonProperty("��������")
	private String deptName;// ��������

	@JsonProperty("��Ŀ����")
	private String subjNm;// ��Ŀ����

	public Integer getIsNtBookEntry() {
		return isNtBookEntry;
	}

	public void setIsNtBookEntry(Integer isNtBookEntry) {
		this.isNtBookEntry = isNtBookEntry;
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

	public String getSpcModel() {
		return spcModel;
	}

	public void setSpcModel(String spcModel) {
		this.spcModel = spcModel;
	}

	public String getMeasrCorpNm() {
		return measrCorpNm;
	}

	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
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

	public String getBatNum() {
		return batNum;
	}

	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}

	public String getPrdcDt() {
		return prdcDt;
	}

	public void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
	}

	public String getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public String getInvldtnDt() {
		return invldtnDt;
	}

	public void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}

	public String getInvtySubjId() {
		return invtySubjId;
	}

	public void setInvtySubjId(String invtySubjId) {
		this.invtySubjId = invtySubjId;
	}
    @JsonIgnore
	public Integer getOrdrNum() {
		return ordrNum;
	}

	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	@JsonIgnore
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	@JsonIgnore
	public BigDecimal getBxQty() {
		return bxQty;
	}

	public void setBxQty(BigDecimal bxQty) {
		this.bxQty = bxQty;
	}
	@JsonIgnore
	public BigDecimal getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}
	@JsonIgnore
	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	@JsonIgnore
	public BigDecimal getCntnTaxUprc() {
		return cntnTaxUprc;
	}

	public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
		this.cntnTaxUprc = cntnTaxUprc;
	}
	@JsonIgnore
	public BigDecimal getPrcTaxSum() {
		return prcTaxSum;
	}

	public void setPrcTaxSum(BigDecimal prcTaxSum) {
		this.prcTaxSum = prcTaxSum;
	}
	@JsonIgnore
	public String getIntlBat() {
		return intlBat;
	}

	public void setIntlBat(String intlBat) {
		this.intlBat = intlBat;
	}
	@JsonIgnore
	public String getBookEntryPers() {
		return bookEntryPers;
	}

	public void setBookEntryPers(String bookEntryPers) {
		this.bookEntryPers = bookEntryPers;
	}
	@JsonIgnore
	public String getBookEntryTm() {
		return bookEntryTm;
	}

	public void setBookEntryTm(String bookEntryTm) {
		this.bookEntryTm = bookEntryTm;
	}
	@JsonIgnore
	public String getSetupPers() {
		return setupPers;
	}

	public void setSetupPers(String setupPers) {
		this.setupPers = setupPers;
	}
	@JsonIgnore
	public String getSetupTm() {
		return setupTm;
	}

	public void setSetupTm(String setupTm) {
		this.setupTm = setupTm;
	}
	@JsonIgnore
	public String getModiPers() {
		return modiPers;
	}

	public void setModiPers(String modiPers) {
		this.modiPers = modiPers;
	}
	@JsonIgnore
	public String getModiTm() {
		return modiTm;
	}

	public void setModiTm(String modiTm) {
		this.modiTm = modiTm;
	}
	@JsonIgnore
	public String getProjEncd() {
		return projEncd;
	}

	public void setProjEncd(String projEncd) {
		this.projEncd = projEncd;
	}
	@JsonIgnore
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	@JsonIgnore
	public String getProjNm() {
		return projNm;
	}

	public void setProjNm(String projNm) {
		this.projNm = projNm;
	}
	@JsonIgnore
	public BigDecimal getBxRule() {
		return bxRule;
	}

	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}
	@JsonIgnore
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	@JsonIgnore
	public String getSubjNm() {
		return subjNm;
	}

	public void setSubjNm(String subjNm) {
		this.subjNm = subjNm;
	}

	

}

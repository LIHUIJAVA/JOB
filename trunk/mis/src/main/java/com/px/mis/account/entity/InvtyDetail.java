package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * ��ϸ��ʵ����
 *
 */
public class InvtyDetail {
	public InvtyDetail() {
		
	}
	@JsonProperty("���ݺ�")
	private int detailId; //��id
	@JsonProperty("��Ŀid")
	private String subId;
	@JsonProperty("��Ŀ����")
	private String subNm;
	@JsonProperty("��Ŀ�跽")
	private String subDebitNm;
	@JsonProperty("��Ŀ����")
	private String subCreditNm;
	@JsonProperty("�������")
	private String invtyNm; //�������
	@JsonProperty("����ͺ�")
	private String spcModel; //����ͺ�
	@JsonProperty("������λ")
    private String measrCorpNm; //������λ
	@JsonProperty("�������")
    private String invtyEncd; //�������
	@JsonProperty("�������")
    private String invtyCd; //�������
	@JsonProperty("����")
    private String batNum; //����
	@JsonProperty("�ֿ����")
    private String whsEncd; //�ֿ����
	@JsonProperty("����������")
    private String invtyClsEncd; //����
	@JsonProperty("�����������")
    private String invtyClsNm; //����
	
    private String level;
	@JsonProperty("�ֿ�����")
    private String whsNm; //�ֿ�����
	@JsonProperty("����")
    private BigDecimal weight;//���� 
	@JsonProperty("���")
    private BigDecimal bxRule;//���
	@JsonProperty("�ڳ��ܶ�")
    private BigDecimal amt;//�ڳ��ܶ�
	@JsonProperty("�ڳ�����")
    private BigDecimal qty;//�ڳ�����
	@JsonProperty("�ڳ�����")
    private BigDecimal uprc; //�ڳ�����
	@JsonProperty("�ɹ����-����")
    private BigDecimal purcQty;//�ɹ����-����
	@JsonProperty("���")
    private BigDecimal purcAmt;//���
	@JsonProperty("�ݹ�-����")
    private BigDecimal purcTempQty;//�ݹ�-����
	@JsonProperty("�ݹ�-���")
    private BigDecimal purcTempAmt;//�ݹ�-���
	@JsonProperty("����������")
    private BigDecimal othInQty;//����������
	@JsonProperty("��������")
    private BigDecimal othInAmt;//�������� 
	@JsonProperty("����������")
    private BigDecimal othOutQty;//����������
	@JsonProperty("���������")
    private BigDecimal othOutAmt;//���������
	@JsonProperty("��˰����")
    private BigDecimal cntnTaxUprc; //��˰����
	@JsonProperty("��˰����")
    private BigDecimal noTaxUprc; //��˰����
	@JsonProperty("��������")
    private BigDecimal tranQty;//��������
	@JsonProperty("�������")
    private BigDecimal tranAmt;//�������
	
	@JsonProperty("��������")
    private BigDecimal sellQty;//��������
	@JsonProperty("��˰�ϼ�")
    private BigDecimal sellAmt;//��˰�ϼ�
	@JsonProperty("��������")
    private BigDecimal sellInAmt;//��������
	@JsonProperty("��������")
    private BigDecimal outQty;//��������
	@JsonProperty("������")
    private BigDecimal outAmt;//������
	@JsonProperty("���۳ɱ�")
     private BigDecimal sellCost;//���۳ɱ�
	@JsonProperty("ë��")
    private BigDecimal gross;//ë��
	@JsonProperty("ë����")
    private String grossRate;//ë����
	@JsonProperty("��ĩ����")
    private BigDecimal finalQty;//��ĩ����
	@JsonProperty("��ĩ���")
    private BigDecimal finalAmt;//��ĩ����
	@JsonProperty("�ͻ�����")
    private String custId;
	@JsonProperty("�ͻ�����")
    private String custNm;
	@JsonProperty("���ű���")
    private String deptId;
	@JsonProperty("��������")
    private String deptName;
	@JsonProperty("������")
    private String accNum;
	@JsonInclude(Include.NON_NULL)
    private List<InvtyDetails> invtyDetailsList;//�ӱ���
    
    
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

	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public int getDetailId() {
		return detailId;
	}

	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}
	
	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getSubNm() {
		return subNm;
	}

	public void setSubNm(String subNm) {
		this.subNm = subNm;
	}

	public String getSubDebitNm() {
		return subDebitNm;
	}

	public void setSubDebitNm(String subDebitNm) {
		this.subDebitNm = subDebitNm;
	}

	public String getSubCreditNm() {
		return subCreditNm;
	}

	public void setSubCreditNm(String subCreditNm) {
		this.subCreditNm = subCreditNm;
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

	public String getInvtyEncd() {
		return invtyEncd;
	}

	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}

	public String getInvtyCd() {
		return invtyCd;
	}

	public void setInvtyCd(String invtyCd) {
		this.invtyCd = invtyCd;
	}

	public String getBatNum() {
		return batNum;
	}

	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}
	
	public List<InvtyDetails> getInvtyDetailsList() {
		return invtyDetailsList;
	}

	public void setInvtyDetailsList(List<InvtyDetails> invtyDetailsList) {
		this.invtyDetailsList = invtyDetailsList;
	}
	
	public String getWhsEncd() {
		return whsEncd;
	}

	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
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
	@JsonIgnore
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getWhsNm() {
		return whsNm;
	}

	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getBxRule() {
		return bxRule;
	}

	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getUprc() {
		return uprc;
	}

	public void setUprc(BigDecimal uprc) {
		this.uprc = uprc;
	}
	
	public BigDecimal getPurcQty() {
		return purcQty;
	}

	public void setPurcQty(BigDecimal purcQty) {
		this.purcQty = purcQty;
	}

	public BigDecimal getPurcAmt() {
		return purcAmt;
	}

	public void setPurcAmt(BigDecimal purcAmt) {
		this.purcAmt = purcAmt;
	}

	public BigDecimal getPurcTempQty() {
		return purcTempQty;
	}

	public void setPurcTempQty(BigDecimal purcTempQty) {
		this.purcTempQty = purcTempQty;
	}

	public BigDecimal getPurcTempAmt() {
		return purcTempAmt;
	}

	public void setPurcTempAmt(BigDecimal purcTempAmt) {
		this.purcTempAmt = purcTempAmt;
	}

	public BigDecimal getOthInQty() {
		return othInQty;
	}

	public void setOthInQty(BigDecimal othInQty) {
		this.othInQty = othInQty;
	}

	public BigDecimal getOthInAmt() {
		return othInAmt;
	}

	public void setOthInAmt(BigDecimal othInAmt) {
		this.othInAmt = othInAmt;
	}

	public BigDecimal getOthOutQty() {
		return othOutQty;
	}

	public void setOthOutQty(BigDecimal othOutQty) {
		this.othOutQty = othOutQty;
	}

	public BigDecimal getOthOutAmt() {
		return othOutAmt;
	}

	public void setOthOutAmt(BigDecimal othOutAmt) {
		this.othOutAmt = othOutAmt;
	}

	public BigDecimal getCntnTaxUprc() {
		return cntnTaxUprc;
	}

	public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
		this.cntnTaxUprc = cntnTaxUprc;
	}

	public BigDecimal getNoTaxUprc() {
		return noTaxUprc;
	}

	public void setNoTaxUprc(BigDecimal noTaxUprc) {
		this.noTaxUprc = noTaxUprc;
	}

	public BigDecimal getTranQty() {
		return tranQty;
	}

	public void setTranQty(BigDecimal tranQty) {
		this.tranQty = tranQty;
	}

	public BigDecimal getTranAmt() {
		return tranAmt;
	}

	public void setTranAmt(BigDecimal tranAmt) {
		this.tranAmt = tranAmt;
	}

	public BigDecimal getSellQty() {
		return sellQty;
	}

	public void setSellQty(BigDecimal sellQty) {
		this.sellQty = sellQty;
	}

	public BigDecimal getSellAmt() {
		return sellAmt;
	}

	public void setSellAmt(BigDecimal sellAmt) {
		this.sellAmt = sellAmt;
	}

	public BigDecimal getSellCost() {
		return sellCost;
	}

	public void setSellCost(BigDecimal sellCost) {
		this.sellCost = sellCost;
	}

	public BigDecimal getGross() {
		return gross;
	}

	public void setGross(BigDecimal gross) {
		this.gross = gross;
	}

	public String getGrossRate() {
		return grossRate;
	}

	public void setGrossRate(String grossRate) {
		this.grossRate = grossRate;
	}

	public BigDecimal getFinalQty() {
		return finalQty;
	}

	public void setFinalQty(BigDecimal finalQty) {
		this.finalQty = finalQty;
	}

	public BigDecimal getFinalAmt() {
		return finalAmt;
	}

	public void setFinalAmt(BigDecimal finalAmt) {
		this.finalAmt = finalAmt;
	}
	
	public BigDecimal getOutQty() {
		return outQty;
	}

	public void setOutQty(BigDecimal outQty) {
		this.outQty = outQty;
	}

	public BigDecimal getOutAmt() {
		return outAmt;
	}

	public void setOutAmt(BigDecimal outAmt) {
		this.outAmt = outAmt;
	}
	
	
	public BigDecimal getSellInAmt() {
		return sellInAmt;
	}

	public void setSellInAmt(BigDecimal sellInAmt) {
		this.sellInAmt = sellInAmt;
	}

	public InvtyDetail( String invtyNm, String spcModel, String measrCorpNm,
			String invtyEncd, String invtyCd, String batNum) {
		super();
		this.invtyNm = invtyNm;
		this.spcModel = spcModel;
		this.measrCorpNm = measrCorpNm;
		this.invtyEncd = invtyEncd;
		this.invtyCd = invtyCd;
		this.batNum = batNum;
	}
    
	public InvtyDetail(String invtyNm, String spcModel, String measrCorpNm, String invtyEncd, String invtyCd,
			   String batNum, String whsEncd, String invtyClsEncd, String invtyClsNm, String whsNm 
			   ) {
			  super();
			  this.invtyNm = invtyNm;
			  this.spcModel = spcModel;
			  this.measrCorpNm = measrCorpNm;
			  this.invtyEncd = invtyEncd;
			  this.invtyCd = invtyCd;
			  this.batNum = batNum;
			  this.whsEncd = whsEncd;
			  this.invtyClsEncd = invtyClsEncd;
			  this.invtyClsNm = invtyClsNm;
			  this.whsNm = whsNm;
			  
			 }
    
}

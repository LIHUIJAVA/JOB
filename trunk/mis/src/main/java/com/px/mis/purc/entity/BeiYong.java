package com.px.mis.purc.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
//���۵�����
public class BeiYong {
	@JsonProperty("����")
    private String formId;//����
	@JsonProperty("����")
    private String formDt;//����
	@JsonProperty("�û����")
    private String accNum;//�û����    
	@JsonProperty("�û�����")
    private String userName;//�û�����    
	@JsonProperty("ҵ�����ͱ��")
    private String bizTypId;//ҵ�����ͱ��
	@JsonProperty("ҵ����������")
    private String bizTypNm;//ҵ����������
	@JsonProperty("�ͻ����")
    private String custId;//�ͻ����
	@JsonProperty("�ͻ�����")
    private String custNm;//�ͻ�����
	@JsonProperty("���ű��")
    private String deptId;//���ű��    
	@JsonProperty("��������")
    private String deptName;//��������
	@JsonProperty("�������ͱ��")
    private String sellTypId;//�������ͱ��
	@JsonProperty("������������")
    private String sellTypNm;//������������
	@JsonProperty("�շ������")
    private String recvSendCateId;//�շ������
	@JsonProperty("�շ��������")
    private String recvSendCateNm;//�շ��������
	@JsonProperty("�ֿ����")
    private String whsEncd;//�ֿ����
	@JsonProperty("�ֿ�����")
    private String whsNm;//�ֿ�����
	@JsonProperty("�������")
    private String invtyEncd;//�������
	@JsonProperty("�������")
    private String invtyNm;//�������
	@JsonProperty("����ͺ�")
    private String spcModel;//����ͺ�
	@JsonProperty("�������")
    private String invtyCd;//�������
	@JsonProperty("������(��Ӧ������)")
    private String crspdBarCd;//������(��Ӧ������)
	@JsonProperty("��������λ����")
    private String measrCorpId;//��������λ����
	@JsonProperty("��������λ����")
    private String measrCorpNm;//��������λ����
	@JsonProperty("����")
    private BigDecimal qty;//����
	@JsonProperty("����")
    private BigDecimal bxQty;//����
	@JsonProperty("���")
    private BigDecimal bxRule;//���
	@JsonProperty("��˰����")
    private BigDecimal cntnTaxUprc;//��˰����
	@JsonProperty("��˰�ϼ�")
    private BigDecimal prcTaxSum;//��˰�ϼ�
	@JsonProperty("��˰����")
    private BigDecimal noTaxUprc;//��˰����
	@JsonProperty("��˰���")
    private BigDecimal noTaxAmt;//��˰���
	@JsonProperty("˰��")
    private BigDecimal taxAmt;//˰��
	@JsonProperty("˰��")
    private BigDecimal taxRate;//˰��
	@JsonProperty("����")
    private String batNum;//����
	@JsonProperty("��������")
    private String intlBat;//��������
	@JsonProperty("�ɱ�")
    private BigDecimal ChengBen;//�ɱ�
	@JsonProperty("ë��")
    private BigDecimal MaoLi;//ë��
	@JsonProperty("��������")
    private String prdcDt;//��������
	@JsonProperty("ʧЧ����")
    private String invldtnDt;//ʧЧ����
	@JsonProperty("������")
    private String baoZhiQi;//������
    
    
    
    public String getInvtyCd() {
		return invtyCd;
	}
	public void setInvtyCd(String invtyCd) {
		this.invtyCd = invtyCd;
	}
	public BigDecimal getBxRule() {
		return bxRule;
	}
	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}
	@JsonIgnore
	public BigDecimal getChengBen() {
		return ChengBen;
	}
	@JsonIgnore
	public void setChengBen(BigDecimal ChengBen) {
		this.ChengBen = ChengBen;
	}
	@JsonIgnore
	public BigDecimal getMaoLi() {
		return MaoLi;
	}
	@JsonIgnore
	public void setMaoLi(BigDecimal MaoLi) {
		this.MaoLi = MaoLi;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFormDt() {
		return formDt;
	}
	public void setFormDt(String formDt) {
		this.formDt = formDt;
	}
	public String getAccNum() {
		return accNum;
	}
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBizTypId() {
		return bizTypId;
	}
	public void setBizTypId(String bizTypId) {
		this.bizTypId = bizTypId;
	}
	public String getBizTypNm() {
		return bizTypNm;
	}
	public void setBizTypNm(String bizTypNm) {
		this.bizTypNm = bizTypNm;
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
	public String getSellTypId() {
		return sellTypId;
	}
	public void setSellTypId(String sellTypId) {
		this.sellTypId = sellTypId;
	}
	public String getSellTypNm() {
		return sellTypNm;
	}
	public void setSellTypNm(String sellTypNm) {
		this.sellTypNm = sellTypNm;
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
	public String getCrspdBarCd() {
		return crspdBarCd;
	}
	public void setCrspdBarCd(String crspdBarCd) {
		this.crspdBarCd = crspdBarCd;
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
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
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
	public String getPrdcDt() {
		return prdcDt;
	}
	public void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
	}
	public String getInvldtnDt() {
		return invldtnDt;
	}
	public void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}
	public String getBaoZhiQi() {
		return baoZhiQi;
	}
	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}
	
}
package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//����ڳ���ʵ����
public class InvtyTermBgnTab {
	private Integer ordrNum;//���
	private String whsEncd;//�ֿ���
	private String invtyEncd;//�������
	private String invtyClsId;//���������
	private String measrCorpId;//������λ���
	private BigDecimal qty;//����
	private BigDecimal uprc;//����
	private BigDecimal amt;//���
	private BigDecimal cntnTaxUprc;//��˰����
	private BigDecimal prxTaxSum;//��˰�ϼ�
	private String batNum;//����
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date prdcDt;//��������
	private String quaGuarPer;//������
	private String invtySubjId;//�����Ŀ���
	private String memo;//��ע
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date invldtnDt;//ʧЧ����
	private Integer isNtBootEntry;//�Ƿ����
	private String deptId;//���ű���
	
	private String bookEntryPers;//������
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date bookEntryTm;//����ʱ��
	private String bookEntryNum;//�����˱��
	
	private String setupPers;//������ �Ƶ��� 
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date setupTm;//����ʱ��
	private String setupPersNum;//�����˱��
	
	private String modiPers;//�޸���
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date modiTm;//�޸�ʱ��
	private String modiPersNum;//�޸��˱��
	
	private String chkPers;//�����
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date chkTm;//�������
	private String chkPersNum;//����˱��
	
	private String whs;//�ֿ�
	private String invtyNm;//�������
	private String spcModel;//����ͺ�
	private BigDecimal bxRule;//���
	private String deptName;//��������
	public InvtyTermBgnTab() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getWhsEncd() {
		return whsEncd;
	}
	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getInvtyClsId() {
		return invtyClsId;
	}
	public void setInvtyClsId(String invtyClsId) {
		this.invtyClsId = invtyClsId;
	}
	public String getMeasrCorpId() {
		return measrCorpId;
	}
	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
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
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public BigDecimal getCntnTaxUprc() {
		return cntnTaxUprc;
	}
	public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
		this.cntnTaxUprc = cntnTaxUprc;
	}
	public BigDecimal getPrxTaxSum() {
		return prxTaxSum;
	}
	public void setPrxTaxSum(BigDecimal prxTaxSum) {
		this.prxTaxSum = prxTaxSum;
	}
	public String getBatNum() {
		return batNum;
	}
	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}
	public Date getPrdcDt() {
		return prdcDt;
	}
	public void setPrdcDt(Date prdcDt) {
		this.prdcDt = prdcDt;
	}
	public String getQuaGuarPer() {
		return quaGuarPer;
	}
	public void setQuaGuarPer(String quaGuarPer) {
		this.quaGuarPer = quaGuarPer;
	}
	public String getInvtySubjId() {
		return invtySubjId;
	}
	public void setInvtySubjId(String invtySubjId) {
		this.invtySubjId = invtySubjId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getInvldtnDt() {
		return invldtnDt;
	}
	public void setInvldtnDt(Date invldtnDt) {
		this.invldtnDt = invldtnDt;
	}
	public Integer getIsNtBootEntry() {
		return isNtBootEntry;
	}
	public void setIsNtBootEntry(Integer isNtBootEntry) {
		this.isNtBootEntry = isNtBootEntry;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getBookEntryPers() {
		return bookEntryPers;
	}
	public void setBookEntryPers(String bookEntryPers) {
		this.bookEntryPers = bookEntryPers;
	}
	public Date getBookEntryTm() {
		return bookEntryTm;
	}
	public void setBookEntryTm(Date bookEntryTm) {
		this.bookEntryTm = bookEntryTm;
	}
	public String getBookEntryNum() {
		return bookEntryNum;
	}
	public void setBookEntryNum(String bookEntryNum) {
		this.bookEntryNum = bookEntryNum;
	}
	public String getSetupPers() {
		return setupPers;
	}
	public void setSetupPers(String setupPers) {
		this.setupPers = setupPers;
	}
	public Date getSetupTm() {
		return setupTm;
	}
	public void setSetupTm(Date setupTm) {
		this.setupTm = setupTm;
	}
	public String getSetupPersNum() {
		return setupPersNum;
	}
	public void setSetupPersNum(String setupPersNum) {
		this.setupPersNum = setupPersNum;
	}
	public String getModiPers() {
		return modiPers;
	}
	public void setModiPers(String modiPers) {
		this.modiPers = modiPers;
	}
	public Date getModiTm() {
		return modiTm;
	}
	public void setModiTm(Date modiTm) {
		this.modiTm = modiTm;
	}
	public String getModiPersNum() {
		return modiPersNum;
	}
	public void setModiPersNum(String modiPersNum) {
		this.modiPersNum = modiPersNum;
	}
	public String getChkPers() {
		return chkPers;
	}
	public void setChkPers(String chkPers) {
		this.chkPers = chkPers;
	}
	public Date getChkTm() {
		return chkTm;
	}
	public void setChkTm(Date chkTm) {
		this.chkTm = chkTm;
	}
	public String getChkPersNum() {
		return chkPersNum;
	}
	public void setChkPersNum(String chkPersNum) {
		this.chkPersNum = chkPersNum;
	}
	public String getWhs() {
		return whs;
	}
	public void setWhs(String whs) {
		this.whs = whs;
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
	public BigDecimal getBxRule() {
		return bxRule;
	}
	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}

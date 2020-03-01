package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//�������ʵ����
public class InvtyGl {
	private Integer ordrNum;//���
	private String invtyEncd;//�������
	private String whsEncd;//�ֿ���
	private String deptId;//���ű��
	private String bizMemId;//ҵ��Ա���
	private BigDecimal incomQty;//��������
	private BigDecimal delvQty;//��������
	private BigDecimal stlQty;//��������
	private BigDecimal minIntoWhsUprc;//��С��ⵥ��
	private BigDecimal maxIntoWhsUprc;//�����ⵥ��
	private Integer isNtChk;//�Ƿ����
	private String chkr;//�����
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date chkDt;//���ʱ��
	private String setupPers;//������
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date setupTm;//����ʱ��
	private String mdfr;//�޸���
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date modiTm;//�޸�ʱ��
	private String acctiPrd;//����ڼ�
	private Integer isNtEndTmDeal;//�Ƿ���ĩ����
	private Integer isNtManlInput;//�Ƿ��ֹ�¼��
	private String memo;//��ע
	
	private String invtyNm;//�������
	private String whsNm;//�ֿ�����
	private String deptNm;//��������
	private String bizMemNm;//ҵ��Ա����
	public InvtyGl() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getWhsEncd() {
		return whsEncd;
	}
	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getBizMemId() {
		return bizMemId;
	}
	public void setBizMemId(String bizMemId) {
		this.bizMemId = bizMemId;
	}
	public BigDecimal getIncomQty() {
		return incomQty;
	}
	public void setIncomQty(BigDecimal incomQty) {
		this.incomQty = incomQty;
	}
	public BigDecimal getDelvQty() {
		return delvQty;
	}
	public void setDelvQty(BigDecimal delvQty) {
		this.delvQty = delvQty;
	}
	public BigDecimal getStlQty() {
		return stlQty;
	}
	public void setStlQty(BigDecimal stlQty) {
		this.stlQty = stlQty;
	}
	public BigDecimal getMinIntoWhsUprc() {
		return minIntoWhsUprc;
	}
	public void setMinIntoWhsUprc(BigDecimal minIntoWhsUprc) {
		this.minIntoWhsUprc = minIntoWhsUprc;
	}
	public BigDecimal getMaxIntoWhsUprc() {
		return maxIntoWhsUprc;
	}
	public void setMaxIntoWhsUprc(BigDecimal maxIntoWhsUprc) {
		this.maxIntoWhsUprc = maxIntoWhsUprc;
	}
	public Integer getIsNtChk() {
		return isNtChk;
	}
	public void setIsNtChk(Integer isNtChk) {
		this.isNtChk = isNtChk;
	}
	public String getChkr() {
		return chkr;
	}
	public void setChkr(String chkr) {
		this.chkr = chkr;
	}
	public Date getChkDt() {
		return chkDt;
	}
	public void setChkDt(Date chkDt) {
		this.chkDt = chkDt;
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
	public String getMdfr() {
		return mdfr;
	}
	public void setMdfr(String mdfr) {
		this.mdfr = mdfr;
	}
	public Date getModiTm() {
		return modiTm;
	}
	public void setModiTm(Date modiTm) {
		this.modiTm = modiTm;
	}
	public String getAcctiPrd() {
		return acctiPrd;
	}
	public void setAcctiPrd(String acctiPrd) {
		this.acctiPrd = acctiPrd;
	}
	public Integer getIsNtEndTmDeal() {
		return isNtEndTmDeal;
	}
	public void setIsNtEndTmDeal(Integer isNtEndTmDeal) {
		this.isNtEndTmDeal = isNtEndTmDeal;
	}
	public Integer getIsNtManlInput() {
		return isNtManlInput;
	}
	public void setIsNtManlInput(Integer isNtManlInput) {
		this.isNtManlInput = isNtManlInput;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getInvtyNm() {
		return invtyNm;
	}
	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}
	public String getWhsNm() {
		return whsNm;
	}
	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getBizMemNm() {
		return bizMemNm;
	}
	public void setBizMemNm(String bizMemNm) {
		this.bizMemNm = bizMemNm;
	}
	
	
}

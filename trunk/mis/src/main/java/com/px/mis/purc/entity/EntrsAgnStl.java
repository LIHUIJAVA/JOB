package com.px.mis.purc.entity;

import java.util.List;
//ί�д������㵥
public class EntrsAgnStl {
    private String stlSnglId;//���㵥����
    private String stlSnglDt;//���㵥����
    private String formTypEncd;//�������ͱ���
    private String sellTypId;//�������ͱ��
    private String bizTypId;//ҵ�����ͱ��
    private String custId;//�ͻ����
    private String accNum;//�û����
    private String userName;//�û�����    
    private String deptId;//���ű��
    private String deptName;//��������
    private String sellOrdrId;//��������
    private String setupPers;//������
    private String setupTm;//����ʱ��  
    private String mdfr;//�޸���
    private String modiTm;//�޸�ʱ��
    private Integer isNtBllg;//�Ƿ�Ʊ
    private Integer isNtChk;//�Ƿ����
    private String chkr;//�����
    private String chkDt;//���ʱ��
    private String invId;//��Ʊ���
    private String custOrdrNum;//�ͻ�������
    private Integer isNtRtnGood;//�Ƿ��˻�
    private String toFormTypId;//��Դ�������ͱ���
    private String custOpnBnk;//�ͻ���������
    private String bkatNum;//�����˺�
    private String dvlprBankId;//����λ���б��
    private String memo;//��ע  
    private String custNm;//�ͻ�����
    private String bizTypNm;//ҵ����������
    private String formTypName;//������������
    private String sellTypNm;//������������
    private String toFormTypEncd;//��Դ�������ͱ���
    private Integer isNtMakeVouch;//�Ƿ�����ƾ֤
    private String makVouchPers;//��ƾ֤��
    private String makVouchTm;//��ƾ֤ʱ��
    
    private List<EntrsAgnStlSub> entrsAgnStlSub;//ί�д������㵥�ӱ�
    
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getCustOrdrNum() {
		return custOrdrNum;
	}

	public void setCustOrdrNum(String custOrdrNum) {
		this.custOrdrNum = custOrdrNum;
	}

	public String getMakVouchPers() {
		return makVouchPers;
	}

	public void setMakVouchPers(String makVouchPers) {
		this.makVouchPers = makVouchPers;
	}

	public String getMakVouchTm() {
		return makVouchTm;
	}

	public void setMakVouchTm(String makVouchTm) {
		this.makVouchTm = makVouchTm;
	}

	public String getToFormTypEncd() {
		return toFormTypEncd;
	}

	public void setToFormTypEncd(String toFormTypEncd) {
		this.toFormTypEncd = toFormTypEncd;
	}

	public Integer getIsNtMakeVouch() {
		return isNtMakeVouch;
	}

	public void setIsNtMakeVouch(Integer isNtMakeVouch) {
		this.isNtMakeVouch = isNtMakeVouch;
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

	public Integer getIsNtRtnGood() {
		return isNtRtnGood;
	}

	public void setIsNtRtnGood(Integer isNtRtnGood) {
		this.isNtRtnGood = isNtRtnGood;
	}

	public String getToFormTypId() {
		return toFormTypId;
	}

	public void setToFormTypId(String toFormTypId) {
		this.toFormTypId = toFormTypId;
	}

	public String getCustNm() {
		return custNm;
	}

	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public String getBizTypNm() {
		return bizTypNm;
	}

	public void setBizTypNm(String bizTypNm) {
		this.bizTypNm = bizTypNm;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}

	public String getStlSnglId() {
		return stlSnglId;
	}

	public void setStlSnglId(String stlSnglId) {
		this.stlSnglId = stlSnglId;
	}

	public String getStlSnglDt() {
		return stlSnglDt;
	}

	public void setStlSnglDt(String stlSnglDt) {
		this.stlSnglDt = stlSnglDt;
	}

	public String getFormTypEncd() {
		return formTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
	}

	public String getBizTypId() {
		return bizTypId;
	}

	public void setBizTypId(String bizTypId) {
		this.bizTypId = bizTypId;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getInvId() {
		return invId;
	}

	public void setInvId(String invId) {
		this.invId = invId;
	}

	public String getSellOrdrId() {
		return sellOrdrId;
	}

	public void setSellOrdrId(String sellOrdrId) {
		this.sellOrdrId = sellOrdrId;
	}

	public String getSetupPers() {
		return setupPers;
	}

	public void setSetupPers(String setupPers) {
		this.setupPers = setupPers;
	}

	public String getSetupTm() {
		return setupTm;
	}

	public void setSetupTm(String setupTm) {
		this.setupTm = setupTm;
	}

	public String getMdfr() {
		return mdfr;
	}

	public void setMdfr(String mdfr) {
		this.mdfr = mdfr;
	}

	public String getModiTm() {
		return modiTm;
	}

	public void setModiTm(String modiTm) {
		this.modiTm = modiTm;
	}

	public Integer getIsNtBllg() {
		return isNtBllg;
	}

	public void setIsNtBllg(Integer isNtBllg) {
		this.isNtBllg = isNtBllg;
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

	public String getChkDt() {
		return chkDt;
	}

	public void setChkDt(String chkDt) {
		this.chkDt = chkDt;
	}

	public String getCustOpnBnk() {
		return custOpnBnk;
	}

	public void setCustOpnBnk(String custOpnBnk) {
		this.custOpnBnk = custOpnBnk;
	}

	public String getBkatNum() {
		return bkatNum;
	}

	public void setBkatNum(String bkatNum) {
		this.bkatNum = bkatNum;
	}

	public String getDvlprBankId() {
		return dvlprBankId;
	}

	public void setDvlprBankId(String dvlprBankId) {
		this.dvlprBankId = dvlprBankId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<EntrsAgnStlSub> getEntrsAgnStlSub() {
		return entrsAgnStlSub;
	}

	public void setEntrsAgnStlSub(List<EntrsAgnStlSub> entrsAgnStlSub) {
		this.entrsAgnStlSub = entrsAgnStlSub;
	}
    
}
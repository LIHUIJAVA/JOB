package com.px.mis.account.entity;

import java.util.List;
//�ɹ���������
public class PursStlSnglMasTab {
	private String stlSnglId;//���㵥��
	private String stlSnglDt;//��������
	private String pursTypId;//�ɹ����ͱ���
	private String pursInvNum;//�ɹ���Ʊ����
	private String provrId;//��Ӧ�̱���
	private String deptId;//���ű���
	private String formTypEncd;//�������ͱ���
	private String accNum;//�û�����
	private String userName;//�û�����
	private String custId;//�ͻ�����
	private String setupPers;//������
	private String setupTm;//����ʱ��
	private String mdfr;//�޸���
	private String modiTm;//�޸�ʱ��
	private Integer isNtChk;//�Ƿ����
	private String chkr;//�����
	private String chkTm;//���ʱ��
	private String memo;//��ע
	
	private List<PursStlSubTab> pursSubList;

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

	public String getPursTypId() {
		return pursTypId;
	}

	public void setPursTypId(String pursTypId) {
		this.pursTypId = pursTypId;
	}

	public String getPursInvNum() {
		return pursInvNum;
	}

	public void setPursInvNum(String pursInvNum) {
		this.pursInvNum = pursInvNum;
	}

	public String getProvrId() {
		return provrId;
	}

	public void setProvrId(String provrId) {
		this.provrId = provrId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getFormTypEncd() {
		return formTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
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

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
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

	public String getChkTm() {
		return chkTm;
	}

	public void setChkTm(String chkTm) {
		this.chkTm = chkTm;
	}

	public List<PursStlSubTab> getPursSubList() {
		return pursSubList;
	}

	public void setPursSubList(List<PursStlSubTab> pursSubList) {
		this.pursSubList = pursSubList;
	}
	
    //������ѯ�ͻ����ơ��û����ơ��������ơ������������ơ��շ�������ơ�ҵ����������
    private String custNm;//�ͻ�����
    private String deptName;//��������
    private String provrNm;//��Ӧ������
    private String pursTypNm;//�ɹ���������
    private String recvSendCateNm;//�շ��������

	public String getCustNm() {
		return custNm;
	}

	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getProvrNm() {
		return provrNm;
	}

	public void setProvrNm(String provrNm) {
		this.provrNm = provrNm;
	}

	public String getPursTypNm() {
		return pursTypNm;
	}

	public void setPursTypNm(String pursTypNm) {
		this.pursTypNm = pursTypNm;
	}

	public String getRecvSendCateNm() {
		return recvSendCateNm;
	}

	public void setRecvSendCateNm(String recvSendCateNm) {
		this.recvSendCateNm = recvSendCateNm;
	}
	
}

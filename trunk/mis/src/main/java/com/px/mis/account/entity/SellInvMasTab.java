package com.px.mis.account.entity;

import java.util.List;
//����ר�÷�Ʊ����
public class SellInvMasTab {
	private String sellInvNum;//���۷�Ʊ��
	private String bllgDt;//��Ʊ����
	private String invTyp;//��Ʊ����
	private String tabHeadTaxRate;//��ͷ˰��
	private String bizTypId;//ҵ�����ͱ��
	private String sellTypId;//�������ͱ��
	private String recvSendCateId;//�շ������
	private String custId;//�ͻ����
	private String deptId;//���ű��
	private String accNum;//ҵ��Ա���
	private String setupPers;//������
	private String setupTm;//��������
	private String mdfr;//�޸���
	private String modiTm;//�޸�ʱ��
	private Integer isNtChk;//�Ƿ����
	private String chkr;//�����
	private String chkTm;//���ʱ��
	private Integer isNtBookEntry;//�Ƿ����
	private String bookEntryPers;//������
	private String bookEntryTm;//����ʱ��
	private String subjEncd;//��Ŀ����
	private String contcr;//�ͻ���ϵ��
	private String bank;//�ͻ�����
	private String acctNum;//�ͻ��˺�
	private String vouchNum;//ƾ֤��
	private String makDocTm;//�Ƶ�ʱ��
	private String makDocPers;//�Ƶ���
	private String sellSnglNum;//���۵���
	private String memo;//��ע
	private List<SellInvSubTab> sellSubList;
	
	private String sellTypNm;//��������
	private String bizTypNm;//ҵ������
	private String deptNm;//����
	private String custNm;//�ͻ�
	private String userName;//�û�����
	private String formTypEncd;//�������ͱ���
	private String toFormTypEncd;//��Ӧ�������ͱ���
	
	public String getFormTypEncd() {
		return formTypEncd;
	}
	
	public String getToFormTypEncd() {
		return toFormTypEncd;
	}

	public void setToFormTypEncd(String toFormTypEncd) {
		this.toFormTypEncd = toFormTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
	}

	public SellInvMasTab() {
	}
	public String getSellInvNum() {
		return sellInvNum;
	}
	public void setSellInvNum(String sellInvNum) {
		this.sellInvNum = sellInvNum;
	}
	
	public String getInvTyp() {
		return invTyp;
	}
	public void setInvTyp(String invTyp) {
		this.invTyp = invTyp;
	}
	public String getTabHeadTaxRate() {
		return tabHeadTaxRate;
	}
	public void setTabHeadTaxRate(String tabHeadTaxRate) {
		this.tabHeadTaxRate = tabHeadTaxRate;
	}
	public String getBizTypId() {
		return bizTypId;
	}
	public void setBizTypId(String bizTypId) {
		this.bizTypId = bizTypId;
	}
	public String getSellTypId() {
		return sellTypId;
	}
	public void setSellTypId(String sellTypId) {
		this.sellTypId = sellTypId;
	}
	public String getRecvSendCateId() {
		return recvSendCateId;
	}
	public void setRecvSendCateId(String recvSendCateId) {
		this.recvSendCateId = recvSendCateId;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getAccNum() {
		return accNum;
	}
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}
	public String getSetupPers() {
		return setupPers;
	}
	public void setSetupPers(String setupPers) {
		this.setupPers = setupPers;
	}
	
	public String getMdfr() {
		return mdfr;
	}
	public void setMdfr(String mdfr) {
		this.mdfr = mdfr;
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
	
	public Integer getIsNtBookEntry() {
		return isNtBookEntry;
	}
	public void setIsNtBookEntry(Integer isNtBookEntry) {
		this.isNtBookEntry = isNtBookEntry;
	}
	public String getBookEntryPers() {
		return bookEntryPers;
	}
	public void setBookEntryPers(String bookEntryPers) {
		this.bookEntryPers = bookEntryPers;
	}
	
	public String getSubjEncd() {
		return subjEncd;
	}
	public void setSubjEncd(String subjEncd) {
		this.subjEncd = subjEncd;
	}
	public String getContcr() {
		return contcr;
	}
	public void setContcr(String contcr) {
		this.contcr = contcr;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getAcctNum() {
		return acctNum;
	}
	public void setAcctNum(String acctNum) {
		this.acctNum = acctNum;
	}
	public String getVouchNum() {
		return vouchNum;
	}
	public void setVouchNum(String vouchNum) {
		this.vouchNum = vouchNum;
	}
	
	public String getMakDocPers() {
		return makDocPers;
	}
	public void setMakDocPers(String makDocPers) {
		this.makDocPers = makDocPers;
	}
	public String getSellSnglNum() {
		return sellSnglNum;
	}
	public void setSellSnglNum(String sellSnglNum) {
		this.sellSnglNum = sellSnglNum;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<SellInvSubTab> getSellSubList() {
		return sellSubList;
	}
	public void setSellSubList(List<SellInvSubTab> sellSubList) {
		this.sellSubList = sellSubList;
	}
	public String getSellTypNm() {
		return sellTypNm;
	}
	public void setSellTypNm(String sellTypNm) {
		this.sellTypNm = sellTypNm;
	}
	public String getBizTypNm() {
		return bizTypNm;
	}
	public void setBizTypNm(String bizTypNm) {
		this.bizTypNm = bizTypNm;
	}
	
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getCustNm() {
		return custNm;
	}
	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}
	public String getBllgDt() {
		return bllgDt;
	}
	public void setBllgDt(String bllgDt) {
		this.bllgDt = bllgDt;
	}
	public String getSetupTm() {
		return setupTm;
	}
	public void setSetupTm(String setupTm) {
		this.setupTm = setupTm;
	}
	public String getModiTm() {
		return modiTm;
	}
	public void setModiTm(String modiTm) {
		this.modiTm = modiTm;
	}
	public String getChkTm() {
		return chkTm;
	}
	public void setChkTm(String chkTm) {
		this.chkTm = chkTm;
	}
	public String getBookEntryTm() {
		return bookEntryTm;
	}
	public void setBookEntryTm(String bookEntryTm) {
		this.bookEntryTm = bookEntryTm;
	}
	public String getMakDocTm() {
		return makDocTm;
	}
	public void setMakDocTm(String makDocTm) {
		this.makDocTm = makDocTm;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
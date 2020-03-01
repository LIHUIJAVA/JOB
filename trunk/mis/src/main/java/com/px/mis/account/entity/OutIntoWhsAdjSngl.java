package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
//��������������ʵ����
public class OutIntoWhsAdjSngl {
	
	@JsonProperty("���ݺ�")
	private String formNum;//���ݺ� ���������������ʶ
	@JsonProperty("��������")
	private String formTm;//��������
	@JsonProperty("�շ�������")
	private String recvSendCateId;//�շ�������
	@JsonProperty("�û�����")
	private String accNum;//�û�����
	@JsonProperty("�û�����")
	private String userName;//�û�����
	@JsonProperty("���ű���")
	private String deptId;//���ű���
	@JsonProperty("�ͻ�����")
	private String custId;//�ͻ�����
	@JsonProperty("��Ӧ�̱���")
	private String provrId;//��Ӧ�̱���
	@JsonProperty("�����")
	private Integer outIntoWhsInd;//������ʶ 0������  1�����
	@JsonProperty("�Ƿ����")
	private Integer isNtBookEntry;//�Ƿ����
	@JsonProperty("������")
	private String bookEntryPers;//������
	@JsonProperty("����ʱ��")
	private String bookEntryTm;//����ʱ��
	@JsonProperty("�޸���")
	private String mdfrPers;//�޸��� ������
	@JsonProperty("�޸�ʱ��")
	private String mdfrTm;//�޸�ʱ��
	@JsonProperty("������")
	private String setupPers;//������ �Ƶ���
	@JsonProperty("����ʱ��")
	private String setupTm;//����ʱ��
	@JsonProperty("��ͷ��ע")
	private String memo;//��ע
	@JsonProperty("�ӱ�ע")
	private String memos;//��ע
	@JsonProperty("�Ƿ��Ƚ��ȳ�")
	private Integer isFifoAdjBan;//�Ƿ��Ƚ��ȳ��������
	@JsonProperty("�Ƿ��Ӧ���ⵥ")
	private Integer isCrspdOutWhsSngl;//�Ƿ��Ӧ���ⵥ
	@JsonProperty("���������ݺ�")
	private String beadjOutIntoWhsMastabInd;//����������������ʶ
	@JsonProperty("�Ƿ�����")
	private Integer isNtSell;//�Ƿ�����
	@JsonProperty("�շ����")
	private String recvSendCateNm;//�շ�������ƣ�
	@JsonProperty("��������")
	private String deptNm;//�������ƣ�
	@JsonProperty("�ͻ�����")
	private String custNm;//�ͻ����ƣ�
	@JsonProperty("��Ӧ��")
	private String provrNm;//��Ӧ�����ƣ�
	@JsonInclude(Include.NON_NULL)
	private List<OutIntoWhsAdjSnglSubTab> outIntoList;
	@JsonProperty("��Դ�������ͱ���")
	private String toFormTypEncd;//��Դ�������ͱ���
	@JsonProperty("�Ƿ�����ƾ֤")
    private Integer isNtMakeVouch;//�Ƿ�����ƾ֤
	@JsonProperty("��ƾ֤��")
    private String makVouchPers;//��ƾ֤��
	@JsonProperty("��ƾ֤ʱ��")
    private String makVouchTm;//��ƾ֤ʱ��
    
	@JsonProperty("�������ͱ���")
    private String formTypEncd;//�������ͱ���
	@JsonProperty("������������")
    private String formTypName;//������������
    
    
	public String getFormTypEncd() {
		return formTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
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

	public String getFormNum() {
		return formNum;
	}

	public void setFormNum(String formNum) {
		this.formNum = formNum;
	}

	public String getFormTm() {
		return formTm;
	}

	public void setFormTm(String formTm) {
		this.formTm = formTm;
	}

	public String getRecvSendCateId() {
		return recvSendCateId;
	}

	public void setRecvSendCateId(String recvSendCateId) {
		this.recvSendCateId = recvSendCateId;
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

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getProvrId() {
		return provrId;
	}

	public void setProvrId(String provrId) {
		this.provrId = provrId;
	}

	public Integer getOutIntoWhsInd() {
		return outIntoWhsInd;
	}

	public void setOutIntoWhsInd(Integer outIntoWhsInd) {
		this.outIntoWhsInd = outIntoWhsInd;
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

	public String getBookEntryTm() {
		return bookEntryTm;
	}

	public void setBookEntryTm(String bookEntryTm) {
		this.bookEntryTm = bookEntryTm;
	}

	public String getMdfrPers() {
		return mdfrPers;
	}

	public void setMdfrPers(String mdfrPers) {
		this.mdfrPers = mdfrPers;
	}

	public String getMdfrTm() {
		return mdfrTm;
	}

	public void setMdfrTm(String mdfrTm) {
		this.mdfrTm = mdfrTm;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getIsFifoAdjBan() {
		return isFifoAdjBan;
	}

	public void setIsFifoAdjBan(Integer isFifoAdjBan) {
		this.isFifoAdjBan = isFifoAdjBan;
	}

	public Integer getIsCrspdOutWhsSngl() {
		return isCrspdOutWhsSngl;
	}

	public void setIsCrspdOutWhsSngl(Integer isCrspdOutWhsSngl) {
		this.isCrspdOutWhsSngl = isCrspdOutWhsSngl;
	}

	public String getBeadjOutIntoWhsMastabInd() {
		return beadjOutIntoWhsMastabInd;
	}

	public void setBeadjOutIntoWhsMastabInd(String beadjOutIntoWhsMastabInd) {
		this.beadjOutIntoWhsMastabInd = beadjOutIntoWhsMastabInd;
	}

	public Integer getIsNtSell() {
		return isNtSell;
	}

	public void setIsNtSell(Integer isNtSell) {
		this.isNtSell = isNtSell;
	}

	public String getRecvSendCateNm() {
		return recvSendCateNm;
	}

	public void setRecvSendCateNm(String recvSendCateNm) {
		this.recvSendCateNm = recvSendCateNm;
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

	public String getProvrNm() {
		return provrNm;
	}

	public void setProvrNm(String provrNm) {
		this.provrNm = provrNm;
	}

	public List<OutIntoWhsAdjSnglSubTab> getOutIntoList() {
		return outIntoList;
	}

	public void setOutIntoList(List<OutIntoWhsAdjSnglSubTab> outIntoList) {
		this.outIntoList = outIntoList;
	}
	
}

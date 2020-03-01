package com.px.mis.account.entity;

//ƾ֤��ʵ����
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(value = { "importNm", "imported", "subjNm", "stlModeId", "importDt","isNtBookOk", "bookOkDt",
		"isNtChk","chkTm", "cntPtySubjNm", "deptName", "custNm", "provrNm", "accNum", "importNm", "imported", "tabNum",
		"ordrNum", "acctYr", "acctiMth", "attachedFormNumbers", "memos", "currencyName", "deptId", "bizMemId", "custId",
		"provrId", "bizMemNm"}, allowSetters = true)
public class VouchTab {

	@JsonProperty("ƾ֤����")
	private String comnVouchId;// ��� --ƾ֤��
	@JsonProperty("ƾ֤�����")
	private String vouchCateWor; // ƾ֤���
	@JsonProperty("ƾ֤˵��")
	private String comnVouchComnt;// ˵��
	@JsonProperty("�Ƶ���")
	private String userName;// �Ƶ���
	@JsonProperty("�Ƶ�ʱ��")
	private String ctime; // ����ʱ�� -�Ƶ�����
	@JsonProperty("�����")
	private String chkr; // �����
	@JsonProperty("������")
	private String bookOkAcc; // ������
	@JsonProperty("ժҪ")
	private String memo;// ժҪ
	@JsonProperty("�跽��Ŀ���")
	private String subjId;// ��Ŀ���
	@JsonProperty("������Ŀ���")
	private String cntPtySubjId;// �Է���Ŀ���
	@JsonProperty("�跽���")
	private BigDecimal debitAmt;// �跽���
	@JsonProperty("�������")
	private BigDecimal crdtAmt;// �������
	@JsonProperty("�跽����")
	private BigDecimal qtyDebit;// �跽����
	@JsonProperty("��������")
	private BigDecimal qtyCrdt;// ��������

	/************* ���º���************* */
	private String importNm; // ������
	private Integer imported;// �Ƿ�������
	private int tabNum; // ƾ֤���
	private Integer ordrNum;// ƾ֤�� -ƾ֤ID
	private int acctYr;// �����
	private int acctiMth;// ����ڼ�
	private String attachedFormNumbers; // ����������
	private String memos;// ����ƾ֤˵�� -��ע2
	// private String bllgEncd;//���㷽ʽ����
	// private String billNum;//Ʊ�ݺ�
	// private String billDt;//Ʊ������
	private String currencyName; // ��������
	// private String rate;//����
	// private String uprc;//����
	// private String oriDebit;//ԭ�ҽ跽
	// private String oriCrdt;//ԭ�Ҵ���
	private String deptId;// ���ű���
	private String bizMemId;// ְԱ����
	private String custId;// �ͻ�����
	private String provrId;// ��Ӧ�̱���
	// private String projectClsEncd;//��Ŀ�������
	// private String projectEncd;//��Ŀ����
	private String bizMemNm;// ҵ��Ա
	/*
	 * private String customize1;//�Զ�����1 private String customize2;//�Զ�����2 private
	 * String customize3;//�Զ�����3 private String customize4;//�Զ�����4 private String
	 * customize5;//�Զ�����5 private String customize6;//�Զ�����6 private String
	 * customize7;//�Զ�����7 private String customize8;//�Զ�����8 private String
	 * customize9;//�Զ�����9 private String customize10;//�Զ�����10 private String
	 * customize11;//�Զ�����11 private String customize12;//�Զ�����12 private String
	 * customize13;//�Զ�����13 private String customize14;//�Զ�����14 private String
	 * customize15;//�Զ�����15 private String customize16;//�Զ�����16 private String
	 * cashFlowProject;//�ֽ�������Ŀ private String cashFlowProjectDebitAmt;//�ֽ������跽���
	 * private String cashFlowProjectCrdtAmt;//�ֽ������������
	 */
	private String subjNm;// ��Ŀ����
	private String stlModeId;// ���㷽ʽ���
	private String importDt; // ����ʱ��
	private Integer isNtBookOk; // �Ƿ�������
	private String bookOkDt; // ����ʱ��
	private Integer isNtChk; // �Ƿ����
	private String chkTm; // �������
	private String cntPtySubjNm;// �Է���Ŀ����
	private String deptName;// ��������
	private String custNm;// �ͻ�����
	private String provrNm;// ��Ӧ������
	private String accNum; // �Ƶ��˺�

	public VouchTab() {
	}

	public String getComnVouchId() {
		return comnVouchId;
	}

	public void setComnVouchId(String comnVouchId) {
		this.comnVouchId = comnVouchId;
	}

	public String getVouchCateWor() {
		return vouchCateWor;
	}

	public void setVouchCateWor(String vouchCateWor) {
		this.vouchCateWor = vouchCateWor;
	}

	public String getComnVouchComnt() {
		return comnVouchComnt;
	}

	public void setComnVouchComnt(String comnVouchComnt) {
		this.comnVouchComnt = comnVouchComnt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getChkr() {
		return chkr;
	}

	public void setChkr(String chkr) {
		this.chkr = chkr;
	}

	public String getBookOkAcc() {
		return bookOkAcc;
	}

	public void setBookOkAcc(String bookOkAcc) {
		this.bookOkAcc = bookOkAcc;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSubjId() {
		return subjId;
	}

	public void setSubjId(String subjId) {
		this.subjId = subjId;
	}

	public String getCntPtySubjId() {
		return cntPtySubjId;
	}

	public void setCntPtySubjId(String cntPtySubjId) {
		this.cntPtySubjId = cntPtySubjId;
	}

	public BigDecimal getDebitAmt() {
		return debitAmt;
	}

	public void setDebitAmt(BigDecimal debitAmt) {
		this.debitAmt = debitAmt;
	}

	public BigDecimal getCrdtAmt() {
		return crdtAmt;
	}

	public void setCrdtAmt(BigDecimal crdtAmt) {
		this.crdtAmt = crdtAmt;
	}

	public BigDecimal getQtyDebit() {
		return qtyDebit;
	}

	public void setQtyDebit(BigDecimal qtyDebit) {
		this.qtyDebit = qtyDebit;
	}

	public BigDecimal getQtyCrdt() {
		return qtyCrdt;
	}

	public void setQtyCrdt(BigDecimal qtyCrdt) {
		this.qtyCrdt = qtyCrdt;
	}

	public String getImportNm() {
		return importNm;
	}

	public void setImportNm(String importNm) {
		this.importNm = importNm;
	}

	public Integer getImported() {
		return imported;
	}

	public void setImported(Integer imported) {
		this.imported = imported;
	}

	public int getTabNum() {
		return tabNum;
	}

	public void setTabNum(int tabNum) {
		this.tabNum = tabNum;
	}

	public Integer getOrdrNum() {
		return ordrNum;
	}

	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}

	public int getAcctYr() {
		return acctYr;
	}

	public void setAcctYr(int acctYr) {
		this.acctYr = acctYr;
	}

	public int getAcctiMth() {
		return acctiMth;
	}

	public void setAcctiMth(int acctiMth) {
		this.acctiMth = acctiMth;
	}

	public String getAttachedFormNumbers() {
		return attachedFormNumbers;
	}

	public void setAttachedFormNumbers(String attachedFormNumbers) {
		this.attachedFormNumbers = attachedFormNumbers;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
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

	public String getBizMemNm() {
		return bizMemNm;
	}

	public void setBizMemNm(String bizMemNm) {
		this.bizMemNm = bizMemNm;
	}

	public String getSubjNm() {
		return subjNm;
	}

	public void setSubjNm(String subjNm) {
		this.subjNm = subjNm;
	}

	public String getStlModeId() {
		return stlModeId;
	}

	public void setStlModeId(String stlModeId) {
		this.stlModeId = stlModeId;
	}

	public String getImportDt() {
		return importDt;
	}

	public void setImportDt(String importDt) {
		this.importDt = importDt;
	}

	public Integer getIsNtBookOk() {
		return isNtBookOk;
	}

	public void setIsNtBookOk(Integer isNtBookOk) {
		this.isNtBookOk = isNtBookOk;
	}

	public String getBookOkDt() {
		return bookOkDt;
	}

	public void setBookOkDt(String bookOkDt) {
		this.bookOkDt = bookOkDt;
	}

	public Integer getIsNtChk() {
		return isNtChk;
	}

	public void setIsNtChk(Integer isNtChk) {
		this.isNtChk = isNtChk;
	}

	public String getChkTm() {
		return chkTm;
	}

	public void setChkTm(String chkTm) {
		this.chkTm = chkTm;
	}

	public String getCntPtySubjNm() {
		return cntPtySubjNm;
	}

	public void setCntPtySubjNm(String cntPtySubjNm) {
		this.cntPtySubjNm = cntPtySubjNm;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

}

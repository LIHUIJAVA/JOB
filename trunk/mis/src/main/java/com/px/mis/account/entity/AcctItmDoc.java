package com.px.mis.account.entity;

//��ƿ�Ŀ������ʵ��
public class AcctItmDoc {
	private String subjId;//��Ŀ���
	private String subjNm;//��Ŀ����
	private String subjTyp;//��Ŀ����
	private String subjCharc;//��Ŀ����
	private Integer encdLvlSub;//���뼶��
	private String subjMnem;//��Ŀ������
	private Integer isNtIndvRecoAccti;//�Ƿ������������
	private Integer isNtCustRecoAccti;//�Ƿ�ͻ���������
	private Integer isNtProvrRecoAccti;//�Ƿ�Ӧ����������
	private Integer isNtDeptAccti;//�Ƿ��ź���
	private Integer isNtEndLvl;//�Ƿ�ĩ��
	private Integer isNtCashSubj;//�Ƿ��ֽ��Ŀ
	private Integer isNtBankSubj;//�Ƿ����п�Ŀ
	private Integer isNtComnCashflowQtySubj;//�Ƿ����ֽ�������Ŀ
	private Integer isNtBkat;//�Ƿ�������
	private Integer isNtDayBookEntry;//�Ƿ��ռ���
	private String memo;//��ע
	private String pId;//����Id
	private Integer balDrct;//����
	private Integer isNtProjAccti;//�Ƿ���Ŀ����
	
	
	public AcctItmDoc() {
	}
	
	public Integer getIsNtProjAccti() {
		return isNtProjAccti;
	}

	public void setIsNtProjAccti(Integer isNtProjAccti) {
		this.isNtProjAccti = isNtProjAccti;
	}

	public Integer getIsNtCashSubj() {
		return isNtCashSubj;
	}

	public void setIsNtCashSubj(Integer isNtCashSubj) {
		this.isNtCashSubj = isNtCashSubj;
	}

	public Integer getIsNtBankSubj() {
		return isNtBankSubj;
	}

	public void setIsNtBankSubj(Integer isNtBankSubj) {
		this.isNtBankSubj = isNtBankSubj;
	}

	public Integer getIsNtBkat() {
		return isNtBkat;
	}

	public void setIsNtBkat(Integer isNtBkat) {
		this.isNtBkat = isNtBkat;
	}

	public Integer getIsNtDayBookEntry() {
		return isNtDayBookEntry;
	}

	public void setIsNtDayBookEntry(Integer isNtDayBookEntry) {
		this.isNtDayBookEntry = isNtDayBookEntry;
	}

	public Integer getBalDrct() {
		return balDrct;
	}

	public void setBalDrct(Integer balDrct) {
		this.balDrct = balDrct;
	}

	public String getSubjId() {
		return subjId;
	}

	public void setSubjId(String subjId) {
		this.subjId = subjId;
	}

	public String getSubjNm() {
		return subjNm;
	}

	public void setSubjNm(String subjNm) {
		this.subjNm = subjNm;
	}

	public String getSubjTyp() {
		return subjTyp;
	}

	public void setSubjTyp(String subjTyp) {
		this.subjTyp = subjTyp;
	}

	public String getSubjCharc() {
		return subjCharc;
	}

	public void setSubjCharc(String subjCharc) {
		this.subjCharc = subjCharc;
	}
    
	public Integer getEncdLvlSub() {
		return encdLvlSub;
	}

	public void setEncdLvlSub(Integer encdLvlSub) {
		this.encdLvlSub = encdLvlSub;
	}

	public String getSubjMnem() {
		return subjMnem;
	}

	public void setSubjMnem(String subjMnem) {
		this.subjMnem = subjMnem;
	}

	public Integer getIsNtIndvRecoAccti() {
		return isNtIndvRecoAccti;
	}

	public void setIsNtIndvRecoAccti(Integer isNtIndvRecoAccti) {
		this.isNtIndvRecoAccti = isNtIndvRecoAccti;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public Integer getIsNtCustRecoAccti() {
		return isNtCustRecoAccti;
	}

	public void setIsNtCustRecoAccti(Integer isNtCustRecoAccti) {
		this.isNtCustRecoAccti = isNtCustRecoAccti;
	}

	public Integer getIsNtProvrRecoAccti() {
		return isNtProvrRecoAccti;
	}

	public void setIsNtProvrRecoAccti(Integer isNtProvrRecoAccti) {
		this.isNtProvrRecoAccti = isNtProvrRecoAccti;
	}

	public Integer getIsNtDeptAccti() {
		return isNtDeptAccti;
	}

	public void setIsNtDeptAccti(Integer isNtDeptAccti) {
		this.isNtDeptAccti = isNtDeptAccti;
	}

	public Integer getIsNtEndLvl() {
		return isNtEndLvl;
	}

	public void setIsNtEndLvl(Integer isNtEndLvl) {
		this.isNtEndLvl = isNtEndLvl;
	}



	public Integer getIsNtComnCashflowQtySubj() {
		return isNtComnCashflowQtySubj;
	}

	public void setIsNtComnCashflowQtySubj(Integer isNtComnCashflowQtySubj) {
		this.isNtComnCashflowQtySubj = isNtComnCashflowQtySubj;
	}
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}

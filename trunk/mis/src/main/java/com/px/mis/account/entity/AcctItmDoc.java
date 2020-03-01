package com.px.mis.account.entity;

//会计科目档案表实体
public class AcctItmDoc {
	private String subjId;//科目编号
	private String subjNm;//科目名称
	private String subjTyp;//科目类型
	private String subjCharc;//科目性质
	private Integer encdLvlSub;//编码级次
	private String subjMnem;//科目助记码
	private Integer isNtIndvRecoAccti;//是否个人往来核算
	private Integer isNtCustRecoAccti;//是否客户往来核算
	private Integer isNtProvrRecoAccti;//是否供应商往来核算
	private Integer isNtDeptAccti;//是否部门核算
	private Integer isNtEndLvl;//是否末级
	private Integer isNtCashSubj;//是否现金科目
	private Integer isNtBankSubj;//是否银行科目
	private Integer isNtComnCashflowQtySubj;//是否常用现金流量科目
	private Integer isNtBkat;//是否银行账
	private Integer isNtDayBookEntry;//是否日记账
	private String memo;//备注
	private String pId;//父级Id
	private Integer balDrct;//余额方向
	private Integer isNtProjAccti;//是否项目核算
	
	
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

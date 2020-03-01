package com.px.mis.account.entity;
//存货核算制单存货科目
public class InvtyAcctiMakDocInvtySubj {
	private Integer ordrNum;//序号
	private String subjId;//科目编号
	private String subjNm;//科目名称
	private Integer isNtComnCashflowSubj;//是否常用现金流量科目
	private Integer isNtProvrRecoAccti;//是否供应商往来核算
	private Integer isNtEndLvl;//是否末级
	private String subjTyp;//科目类型
	private Integer isNtCustRecoAccti;//是否客户往来核算
	private Integer isNtCastSubj;//是否现金科目
	private String subjRecCd;//科目助记码
	private String subjCharc;//科目性质
	private String measrCorpId;//计量单位编号
	private String projBigClsEncd;//项目大类编号
	private Integer isNtProjAccti;//是否项目核算
	private Integer isNtDeptAccti;//是否部门核算
	private String memo;//备注
	
	private String measrCorpNm;//计量单位名称

	public InvtyAcctiMakDocInvtySubj() {
	}

	public Integer getOrdrNum() {
		return ordrNum;
	}

	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
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

	public Integer getIsNtComnCashflowSubj() {
		return isNtComnCashflowSubj;
	}

	public void setIsNtComnCashflowSubj(Integer isNtComnCashflowSubj) {
		this.isNtComnCashflowSubj = isNtComnCashflowSubj;
	}

	public Integer getIsNtProvrRecoAccti() {
		return isNtProvrRecoAccti;
	}

	public void setIsNtProvrRecoAccti(Integer isNtProvrRecoAccti) {
		this.isNtProvrRecoAccti = isNtProvrRecoAccti;
	}

	public Integer getIsNtEndLvl() {
		return isNtEndLvl;
	}

	public void setIsNtEndLvl(Integer isNtEndLvl) {
		this.isNtEndLvl = isNtEndLvl;
	}

	public String getSubjTyp() {
		return subjTyp;
	}

	public void setSubjTyp(String subjTyp) {
		this.subjTyp = subjTyp;
	}

	public Integer getIsNtCustRecoAccti() {
		return isNtCustRecoAccti;
	}

	public void setIsNtCustRecoAccti(Integer isNtCustRecoAccti) {
		this.isNtCustRecoAccti = isNtCustRecoAccti;
	}

	public Integer getIsNtCastSubj() {
		return isNtCastSubj;
	}

	public void setIsNtCastSubj(Integer isNtCastSubj) {
		this.isNtCastSubj = isNtCastSubj;
	}

	public String getSubjRecCd() {
		return subjRecCd;
	}

	public void setSubjRecCd(String subjRecCd) {
		this.subjRecCd = subjRecCd;
	}

	public String getSubjCharc() {
		return subjCharc;
	}

	public void setSubjCharc(String subjCharc) {
		this.subjCharc = subjCharc;
	}

	public String getMeasrCorpId() {
		return measrCorpId;
	}

	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
	}

	public String getProjBigClsEncd() {
		return projBigClsEncd;
	}

	public void setProjBigClsEncd(String projBigClsEncd) {
		this.projBigClsEncd = projBigClsEncd;
	}

	public Integer getIsNtProjAccti() {
		return isNtProjAccti;
	}

	public void setIsNtProjAccti(Integer isNtProjAccti) {
		this.isNtProjAccti = isNtProjAccti;
	}

	public Integer getIsNtDeptAccti() {
		return isNtDeptAccti;
	}

	public void setIsNtDeptAccti(Integer isNtDeptAccti) {
		this.isNtDeptAccti = isNtDeptAccti;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMeasrCorpNm() {
		return measrCorpNm;
	}

	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}
	
}

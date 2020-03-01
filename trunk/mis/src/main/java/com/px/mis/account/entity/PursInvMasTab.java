package com.px.mis.account.entity;

import java.util.List;
//采购专用发票主表
public class PursInvMasTab {
	private String invTyp;//发票类型
	private String tabHeadTaxRate;//表头税率
	private String pursInvNum;//采购发票号
	private String bllgDt;//开票日期
	private String chkr;//审核人
	private Integer isNtChk;//是否审核
	private String chkTm;//审核时间
	private String provrContcr;//供方联系人
	private String provrBankNm;//供方银行名称
	private String setupPers;//创建人
	private String setupTm;//创建日期
	private Integer isNtBookEntry;//是否记账
	private String bookEntryPers;//记账人
	private String bookEntryTm;//记账时间
	private String mdfr;//修改人
	private String modiTm;//修改日期
	private String pursTypId;//采购类型编号
	private String provrId;//供应商编号
	private String deptId;//部门编号
	private String accNum;//业务员编号
	private String pursOrdrId;//采购订单号 
	private String subjId;//科目编码
	private String makDocPers;//制单人
	private String makDocTm;//制单时间
	private String crspdIntoWhsSnglNum;//对应入库单号
	private String vouchNum;//凭证号
	private String memo;//备注
	private List<PursInvSubTab> pursList;
	
	private String pursTypNm;//采购类型名称
	private String provrNm;//供应商名称
	private String userName;//用户名称
	private String deptName;//部门名称
	private String formTypEncd;//单据类型编码

	public String getFormTypEncd() {
		return formTypEncd;
	}
	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
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
	public String getPursInvNum() {
		return pursInvNum;
	}
	public void setPursInvNum(String pursInvNum) {
		this.pursInvNum = pursInvNum;
	}
	public String getBllgDt() {
		return bllgDt;
	}
	public void setBllgDt(String bllgDt) {
		this.bllgDt = bllgDt;
	}
	public String getChkr() {
		return chkr;
	}
	public void setChkr(String chkr) {
		this.chkr = chkr;
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
	public String getProvrContcr() {
		return provrContcr;
	}
	public void setProvrContcr(String provrContcr) {
		this.provrContcr = provrContcr;
	}
	public String getProvrBankNm() {
		return provrBankNm;
	}
	public void setProvrBankNm(String provrBankNm) {
		this.provrBankNm = provrBankNm;
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
	public String getPursTypId() {
		return pursTypId;
	}
	public void setPursTypId(String pursTypId) {
		this.pursTypId = pursTypId;
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
	public String getAccNum() {
		return accNum;
	}
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}
	public String getPursOrdrId() {
		return pursOrdrId;
	}
	public void setPursOrdrId(String pursOrdrId) {
		this.pursOrdrId = pursOrdrId;
	}
	public String getSubjId() {
		return subjId;
	}
	public void setSubjId(String subjId) {
		this.subjId = subjId;
	}
	public String getMakDocPers() {
		return makDocPers;
	}
	public void setMakDocPers(String makDocPers) {
		this.makDocPers = makDocPers;
	}
	public String getMakDocTm() {
		return makDocTm;
	}
	public void setMakDocTm(String makDocTm) {
		this.makDocTm = makDocTm;
	}
	public String getCrspdIntoWhsSnglNum() {
		return crspdIntoWhsSnglNum;
	}
	public void setCrspdIntoWhsSnglNum(String crspdIntoWhsSnglNum) {
		this.crspdIntoWhsSnglNum = crspdIntoWhsSnglNum;
	}
	public String getVouchNum() {
		return vouchNum;
	}
	public void setVouchNum(String vouchNum) {
		this.vouchNum = vouchNum;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<PursInvSubTab> getPursList() {
		return pursList;
	}
	public void setPursList(List<PursInvSubTab> pursList) {
		this.pursList = pursList;
	}
	public String getPursTypNm() {
		return pursTypNm;
	}
	public void setPursTypNm(String pursTypNm) {
		this.pursTypNm = pursTypNm;
	}
	public String getProvrNm() {
		return provrNm;
	}
	public void setProvrNm(String provrNm) {
		this.provrNm = provrNm;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}

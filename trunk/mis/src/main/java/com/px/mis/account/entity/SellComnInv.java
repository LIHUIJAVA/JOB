package com.px.mis.account.entity;

import java.util.List;
//销售普通发票主表
public class SellComnInv {
	private String sellInvNum;//销售发票号
	private String bllgDt;//开票日期
	private String invTyp;//发票类型
	private String tabHeadTaxRate;//表头税率
	private String bizTypId;//业务类型编号
	private String sellTypId;//销售类型编号
	private String recvSendCateId;//收发类别编号
	private String custId;//客户编号
	private String deptId;//部门编号
	private String accNum;//业务员编号
	private String setupPers;//创建人
	private String setupTm;//创建日期
	private String mdfr;//修改人
	private String modiTm;//修改时间
	private Integer isNtChk;//是否审核
	private String chkr;//审核人
	private String chkTm;//审核时间
	private Integer isNtBookEntry;//是否记账
	private String bookEntryPers;//记账人
	private String bookEntryTm;//记账时间
	private String subjEncd;//科目编码
	private String contcr;//客户联系人
	private String bank;//客户银行
	private String acctNum;//客户账号
//	private String vouchNum;//委托代销结算单编码
	private String makDocTm;//制单时间
	private String makDocPers;//制单人
	private String sellSnglNum;//销售单号
	private String memo;//备注
	private List<SellComnInvSub> sellComnInvSubList;
	private String sellTypNm;//销售类型
	private String bizTypNm;//业务类型
	private String deptName;//部门
	private String custNm;//客户
	private String userName;//用户名称
	private String formTypEncd;//单据类型编码
//	private String rtnGoodsId;//退货单主表标识
	private String toFormTypEncd;//来源单据类型编码
    private Integer isNtMakeVouch;//是否生成凭证
    private String makVouchPers;//制凭证人
    private String makVouchTm;//制凭证时间
    private String custOrdrNum;//客户订单号
    private String color;//红蓝字段
    private int isPushed;//是否推送到U8
    

	public int getIsPushed() {
		return isPushed;
	}
	public void setIsPushed(int isPushed) {
		this.isPushed = isPushed;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getCustOrdrNum() {
		return custOrdrNum;
	}
	public void setCustOrdrNum(String custOrdrNum) {
		this.custOrdrNum = custOrdrNum;
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
	
	public String getFormTypEncd() {
		return formTypEncd;
	}
	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
	}
	
	public String getToFormTypEncd() {
		return toFormTypEncd;
	}
	public void setToFormTypEncd(String toFormTypEncd) {
		this.toFormTypEncd = toFormTypEncd;
	}
	public SellComnInv() {
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
//	public String getVouchNum() {
//		return vouchNum;
//	}
//	public void setVouchNum(String vouchNum) {
//		this.vouchNum = vouchNum;
//	}
	
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
	
	public List<SellComnInvSub> getSellComnInvSubList() {
		return sellComnInvSubList;
	}
	public void setSellComnInvSubList(List<SellComnInvSub> sellComnInvSubList) {
		this.sellComnInvSubList = sellComnInvSubList;
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

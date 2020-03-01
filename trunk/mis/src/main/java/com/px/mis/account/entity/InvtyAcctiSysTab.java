package com.px.mis.account.entity;
//存货核算系统表
public class InvtyAcctiSysTab {
	private Integer ordrNum;//序号
	private String acctYr;//会计年
	private String acctMth;//会计月
	private Integer isNtBookEntry;//是否记账
	private Integer isNtEndTmDeal;//是否期末处理
	private Integer isNtMthEndStl;//是否月末结账
	private Integer termBgnIsNtBookEntry;//期初是否记账
	public InvtyAcctiSysTab() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getAcctYr() {
		return acctYr;
	}
	public void setAcctYr(String acctYr) {
		this.acctYr = acctYr;
	}
	public String getAcctMth() {
		return acctMth;
	}
	public void setAcctMth(String acctMth) {
		this.acctMth = acctMth;
	}
	public Integer getIsNtBookEntry() {
		return isNtBookEntry;
	}
	public void setIsNtBookEntry(Integer isNtBookEntry) {
		this.isNtBookEntry = isNtBookEntry;
	}
	public Integer getIsNtEndTmDeal() {
		return isNtEndTmDeal;
	}
	public void setIsNtEndTmDeal(Integer isNtEndTmDeal) {
		this.isNtEndTmDeal = isNtEndTmDeal;
	}
	public Integer getIsNtMthEndStl() {
		return isNtMthEndStl;
	}
	public void setIsNtMthEndStl(Integer isNtMthEndStl) {
		this.isNtMthEndStl = isNtMthEndStl;
	}
	public Integer getTermBgnIsNtBookEntry() {
		return termBgnIsNtBookEntry;
	}
	public void setTermBgnIsNtBookEntry(Integer termBgnIsNtBookEntry) {
		this.termBgnIsNtBookEntry = termBgnIsNtBookEntry;
	}
	
}

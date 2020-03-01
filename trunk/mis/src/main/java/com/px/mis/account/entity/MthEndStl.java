package com.px.mis.account.entity;
/**
 * 月末记账
 *
 */
public class MthEndStl {
	
	private Integer orderNum; //序号
	private String mthBgn; //起始月
	private String mthEnd; //结束月
	private String acctYr; //会计年
	private String acctMth; //会计月
	private Integer isMthEndStl; //是否月末记账
	private String accNum; //结账人
	private String stlDt; //结账时间
	private String oprrDt; //操作时间
	private int isMthSeal; //是否封账
	
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getMthBgn() {
		return mthBgn;
	}
	public void setMthBgn(String mthBgn) {
		this.mthBgn = mthBgn;
	}
	public String getMthEnd() {
		return mthEnd;
	}
	public void setMthEnd(String mthEnd) {
		this.mthEnd = mthEnd;
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
	public Integer getIsMthEndStl() {
		return isMthEndStl;
	}
	public void setIsMthEndStl(Integer isMthEndStl) {
		this.isMthEndStl = isMthEndStl;
	}
	public String getAccNum() {
		return accNum;
	}
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}
	public String getStlDt() {
		return stlDt;
	}
	public void setStlDt(String stlDt) {
		this.stlDt = stlDt;
	}
	public String getOprrDt() {
		return oprrDt;
	}
	public void setOprrDt(String oprrDt) {
		this.oprrDt = oprrDt;
	}
	public int getIsMthSeal() {
		return isMthSeal;
	}
	public void setIsMthSeal(int isMthSeal) {
		this.isMthSeal = isMthSeal;
	}
	
	
	
}

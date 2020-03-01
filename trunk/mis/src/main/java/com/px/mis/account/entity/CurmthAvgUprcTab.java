package com.px.mis.account.entity;

import java.math.BigDecimal;

import com.px.mis.purc.entity.InvtyDoc;
//本月平均单价表
public class CurmthAvgUprcTab {
	private Integer ordrNum;//序号
	private String acctYr;//会计年
	private String acctiMth;//会计月
	private String invtyEncd;//存货编码
	private BigDecimal qty;//数量
	private BigDecimal amt;//金额
	private BigDecimal avgUprc;//平均单价
	
	private InvtyDoc invtydoc;//存货表
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acctYr == null) ? 0 : acctYr.hashCode());
		result = prime * result + ((acctiMth == null) ? 0 : acctiMth.hashCode());
		result = prime * result + ((amt == null) ? 0 : amt.hashCode());
		result = prime * result + ((avgUprc == null) ? 0 : avgUprc.hashCode());
		result = prime * result + ((invtyEncd == null) ? 0 : invtyEncd.hashCode());
		result = prime * result + ((invtydoc == null) ? 0 : invtydoc.hashCode());
		result = prime * result + ((ordrNum == null) ? 0 : ordrNum.hashCode());
		result = prime * result + ((qty == null) ? 0 : qty.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurmthAvgUprcTab other = (CurmthAvgUprcTab) obj;
		if (acctYr == null) {
			if (other.acctYr != null)
				return false;
		} else if (!acctYr.equals(other.acctYr))
			return false;
		if (acctiMth == null) {
			if (other.acctiMth != null)
				return false;
		} else if (!acctiMth.equals(other.acctiMth))
			return false;
		if (amt == null) {
			if (other.amt != null)
				return false;
		} else if (!amt.equals(other.amt))
			return false;
		if (avgUprc == null) {
			if (other.avgUprc != null)
				return false;
		} else if (!avgUprc.equals(other.avgUprc))
			return false;
		if (invtyEncd == null) {
			if (other.invtyEncd != null)
				return false;
		} else if (!invtyEncd.equals(other.invtyEncd))
			return false;
		if (invtydoc == null) {
			if (other.invtydoc != null)
				return false;
		} else if (!invtydoc.equals(other.invtydoc))
			return false;
		if (ordrNum == null) {
			if (other.ordrNum != null)
				return false;
		} else if (!ordrNum.equals(other.ordrNum))
			return false;
		if (qty == null) {
			if (other.qty != null)
				return false;
		} else if (!qty.equals(other.qty))
			return false;
		return true;
	}

	

	public InvtyDoc getInvtydoc() {
		return invtydoc;
	}

	public void setInvtydoc(InvtyDoc invtydoc) {
		this.invtydoc = invtydoc;
	}

	public CurmthAvgUprcTab() {
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

	public String getAcctiMth() {
		return acctiMth;
	}

	public void setAcctiMth(String acctiMth) {
		this.acctiMth = acctiMth;
	}

	public String getInvtyEncd() {
		return invtyEncd;
	}

	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public BigDecimal getAvgUprc() {
		return avgUprc;
	}

	public void setAvgUprc(BigDecimal avgUprc) {
		this.avgUprc = avgUprc;
	}


	
}

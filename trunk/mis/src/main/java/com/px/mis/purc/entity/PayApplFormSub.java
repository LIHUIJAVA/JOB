package com.px.mis.purc.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * pay_appl_form_sub
 * 
 * @author
 */
public class PayApplFormSub implements Serializable {
	/**
	 * 序号
	 */
	private Long ordrNum;

	/**
	 * 付款申请单主表编号
	 */
	private String payApplId;

	/**
	 * 计划到货日期
	 */
	private String expctPayDt;

	/**
	 * 数量
	 */
	private BigDecimal qty;

	/**
	 * 来源单据号
	 */
	private String srcFormNum;

	/**
	 * 原单本次申请金额
	 */
	private BigDecimal orgnlSnglCurrApplAmt;

	/**
	 * 金额
	 */
	private BigDecimal amt;

	/**
	 * 来源子表序号
	 */
	private Long formOrdrNum;

	/**
	 * 实际付款时间
	 */
	private String actlPayTm;

	/**
	 * 行关闭人
	 */
	private String lineClosPers;
	
	private String memo;
	
	private Long toOrdrNum;//来源子表序号

	public Long getToOrdrNum() {
		return toOrdrNum;
	}

	public void setToOrdrNum(Long toOrdrNum) {
		this.toOrdrNum = toOrdrNum;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	private static final long serialVersionUID = 1L;

	public Long getOrdrNum() {
		return ordrNum;
	}

	public void setOrdrNum(Long ordrNum) {
		this.ordrNum = ordrNum;
	}

	public String getPayApplId() {
		return payApplId;
	}

	public void setPayApplId(String payApplId) {
		this.payApplId = payApplId;
	}

	public String getExpctPayDt() {
		return expctPayDt;
	}

	public void setExpctPayDt(String expctPayDt) {
		this.expctPayDt = expctPayDt;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public String getSrcFormNum() {
		return srcFormNum;
	}

	public void setSrcFormNum(String srcFormNum) {
		this.srcFormNum = srcFormNum;
	}

	public BigDecimal getOrgnlSnglCurrApplAmt() {
		return orgnlSnglCurrApplAmt;
	}

	public void setOrgnlSnglCurrApplAmt(BigDecimal orgnlSnglCurrApplAmt) {
		this.orgnlSnglCurrApplAmt = orgnlSnglCurrApplAmt;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public Long getFormOrdrNum() {
		return formOrdrNum;
	}

	public void setFormOrdrNum(Long formOrdrNum) {
		this.formOrdrNum = formOrdrNum;
	}

	public String getActlPayTm() {
		return actlPayTm;
	}

	public void setActlPayTm(String actlPayTm) {
		this.actlPayTm = actlPayTm;
	}

	public String getLineClosPers() {
		return lineClosPers;
	}

	public void setLineClosPers(String lineClosPers) {
		this.lineClosPers = lineClosPers;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		PayApplFormSub other = (PayApplFormSub) that;
		return (this.getOrdrNum() == null ? other.getOrdrNum() == null : this.getOrdrNum().equals(other.getOrdrNum()))
				&& (this.getPayApplId() == null ? other.getPayApplId() == null
						: this.getPayApplId().equals(other.getPayApplId()))
				&& (this.getExpctPayDt() == null ? other.getExpctPayDt() == null
						: this.getExpctPayDt().equals(other.getExpctPayDt()))
				&& (this.getQty() == null ? other.getQty() == null : this.getQty().equals(other.getQty()))
				&& (this.getSrcFormNum() == null ? other.getSrcFormNum() == null
						: this.getSrcFormNum().equals(other.getSrcFormNum()))
				&& (this.getOrgnlSnglCurrApplAmt() == null ? other.getOrgnlSnglCurrApplAmt() == null
						: this.getOrgnlSnglCurrApplAmt().equals(other.getOrgnlSnglCurrApplAmt()))
				&& (this.getAmt() == null ? other.getAmt() == null : this.getAmt().equals(other.getAmt()))
				&& (this.getFormOrdrNum() == null ? other.getFormOrdrNum() == null
						: this.getFormOrdrNum().equals(other.getFormOrdrNum()))
				&& (this.getActlPayTm() == null ? other.getActlPayTm() == null
						: this.getActlPayTm().equals(other.getActlPayTm()))
				&& (this.getLineClosPers() == null ? other.getLineClosPers() == null
						: this.getLineClosPers().equals(other.getLineClosPers()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getOrdrNum() == null) ? 0 : getOrdrNum().hashCode());
		result = prime * result + ((getPayApplId() == null) ? 0 : getPayApplId().hashCode());
		result = prime * result + ((getExpctPayDt() == null) ? 0 : getExpctPayDt().hashCode());
		result = prime * result + ((getQty() == null) ? 0 : getQty().hashCode());
		result = prime * result + ((getSrcFormNum() == null) ? 0 : getSrcFormNum().hashCode());
		result = prime * result + ((getOrgnlSnglCurrApplAmt() == null) ? 0 : getOrgnlSnglCurrApplAmt().hashCode());
		result = prime * result + ((getAmt() == null) ? 0 : getAmt().hashCode());
		result = prime * result + ((getFormOrdrNum() == null) ? 0 : getFormOrdrNum().hashCode());
		result = prime * result + ((getActlPayTm() == null) ? 0 : getActlPayTm().hashCode());
		result = prime * result + ((getLineClosPers() == null) ? 0 : getLineClosPers().hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", ordrNum=").append(ordrNum);
		sb.append(", payApplId=").append(payApplId);
		sb.append(", expctPayDt=").append(expctPayDt);
		sb.append(", qty=").append(qty);
		sb.append(", srcFormNum=").append(srcFormNum);
		sb.append(", orgnlSnglCurrApplAmt=").append(orgnlSnglCurrApplAmt);
		sb.append(", amt=").append(amt);
		sb.append(", formOrdrNum=").append(formOrdrNum);
		sb.append(", actlPayTm=").append(actlPayTm);
		sb.append(", lineClosPers=").append(lineClosPers);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}
package com.px.mis.purc.entity.PayApplFormForU8;

import java.math.BigDecimal;

public class U8PayApplFormSub {
	private String ddcode;// 采购订单编号
	private String dpprepaydate;// 预付日期
	private BigDecimal ipayAmt;// 付款金额
	public String getDdcode() {
		return ddcode;
	}
	public void setDdcode(String ddcode) {
		this.ddcode = ddcode;
	}
	public String getDpprepaydate() {
		return dpprepaydate;
	}
	public void setDpprepaydate(String dpprepaydate) {
		this.dpprepaydate = dpprepaydate;
	}
	public BigDecimal getIpayAmt() {
		return ipayAmt;
	}
	public void setIpayAmt(BigDecimal ipayAmt) {
		this.ipayAmt = ipayAmt;
	}

	

}

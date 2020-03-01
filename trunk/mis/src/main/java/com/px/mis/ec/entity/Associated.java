package com.px.mis.ec.entity;
//一键联查响应给前端的实体；
public class Associated {
	private String name;//单据名称
	private String orderCode;//单据编号
	private String SetupPers;//制单人
	private String SetupTm;//制单日期
	private String Chkr;//审核人
	private String ChkTm;//审核日期
	public Associated() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getSetupPers() {
		return SetupPers;
	}
	public void setSetupPers(String setupPers) {
		SetupPers = setupPers;
	}
	public String getSetupTm() {
		return SetupTm;
	}
	public void setSetupTm(String setupTm) {
		SetupTm = setupTm;
	}
	public String getChkr() {
		return Chkr;
	}
	public void setChkr(String chkr) {
		Chkr = chkr;
	}
	public String getChkTm() {
		return ChkTm;
	}
	public void setChkTm(String chkTm) {
		ChkTm = chkTm;
	}
	@Override
	public String toString() {
		return "Associated [name=" + name + ", orderCode=" + orderCode + ", SetupPers=" + SetupPers + ", SetupTm="
				+ SetupTm + ", Chkr=" + Chkr + ", ChkTm=" + ChkTm + "]";
	}
	
}

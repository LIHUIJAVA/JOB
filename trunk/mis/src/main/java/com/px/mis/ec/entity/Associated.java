package com.px.mis.ec.entity;
//һ��������Ӧ��ǰ�˵�ʵ�壻
public class Associated {
	private String name;//��������
	private String orderCode;//���ݱ��
	private String SetupPers;//�Ƶ���
	private String SetupTm;//�Ƶ�����
	private String Chkr;//�����
	private String ChkTm;//�������
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

package com.px.mis.account.entity;
//单据类型表实体类
public class FormTypTab {
	private Integer ordrNum;//序号
	private String FormTyp;//单据类型
	private String vouchCate;//凭证类别字
	private String recvblPayblInd;//应收应付标志
	private String typCls;//类型分类
	private String memo;//备注
	public FormTypTab() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getFormTyp() {
		return FormTyp;
	}
	public void setFormTyp(String formTyp) {
		FormTyp = formTyp;
	}
	public String getVouchCate() {
		return vouchCate;
	}
	public void setVouchCate(String vouchCate) {
		this.vouchCate = vouchCate;
	}
	public String getRecvblPayblInd() {
		return recvblPayblInd;
	}
	public void setRecvblPayblInd(String recvblPayblInd) {
		this.recvblPayblInd = recvblPayblInd;
	}
	public String getTypCls() {
		return typCls;
	}
	public void setTypCls(String typCls) {
		this.typCls = typCls;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	
}

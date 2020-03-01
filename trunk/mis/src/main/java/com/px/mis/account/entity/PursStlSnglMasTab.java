package com.px.mis.account.entity;

import java.util.List;
//采购结算主表
public class PursStlSnglMasTab {
	private String stlSnglId;//结算单号
	private String stlSnglDt;//结算日期
	private String pursTypId;//采购类型编码
	private String pursInvNum;//采购发票单号
	private String provrId;//供应商编码
	private String deptId;//部门编码
	private String formTypEncd;//单据类型编码
	private String accNum;//用户编码
	private String userName;//用户名称
	private String custId;//客户编码
	private String setupPers;//创建人
	private String setupTm;//创建时间
	private String mdfr;//修改人
	private String modiTm;//修改时间
	private Integer isNtChk;//是否审核
	private String chkr;//审核人
	private String chkTm;//审核时间
	private String memo;//备注
	
	private List<PursStlSubTab> pursSubList;

	public String getStlSnglId() {
		return stlSnglId;
	}

	public void setStlSnglId(String stlSnglId) {
		this.stlSnglId = stlSnglId;
	}

	public String getStlSnglDt() {
		return stlSnglDt;
	}

	public void setStlSnglDt(String stlSnglDt) {
		this.stlSnglDt = stlSnglDt;
	}

	public String getPursTypId() {
		return pursTypId;
	}

	public void setPursTypId(String pursTypId) {
		this.pursTypId = pursTypId;
	}

	public String getPursInvNum() {
		return pursInvNum;
	}

	public void setPursInvNum(String pursInvNum) {
		this.pursInvNum = pursInvNum;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getFormTypEncd() {
		return formTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
	}

	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
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

	public String getChkTm() {
		return chkTm;
	}

	public void setChkTm(String chkTm) {
		this.chkTm = chkTm;
	}

	public List<PursStlSubTab> getPursSubList() {
		return pursSubList;
	}

	public void setPursSubList(List<PursStlSubTab> pursSubList) {
		this.pursSubList = pursSubList;
	}
	
    //关联查询客户名称、用户名称、部门名称、销售类型名称、收发类别名称、业务类型名称
    private String custNm;//客户名称
    private String deptName;//部门名称
    private String provrNm;//供应商名称
    private String pursTypNm;//采购类型名称
    private String recvSendCateNm;//收发类别名称

	public String getCustNm() {
		return custNm;
	}

	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getProvrNm() {
		return provrNm;
	}

	public void setProvrNm(String provrNm) {
		this.provrNm = provrNm;
	}

	public String getPursTypNm() {
		return pursTypNm;
	}

	public void setPursTypNm(String pursTypNm) {
		this.pursTypNm = pursTypNm;
	}

	public String getRecvSendCateNm() {
		return recvSendCateNm;
	}

	public void setRecvSendCateNm(String recvSendCateNm) {
		this.recvSendCateNm = recvSendCateNm;
	}
	
}

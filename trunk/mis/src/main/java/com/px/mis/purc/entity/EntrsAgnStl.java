package com.px.mis.purc.entity;

import java.util.List;
//委托代销结算单
public class EntrsAgnStl {
    private String stlSnglId;//结算单编码
    private String stlSnglDt;//结算单日期
    private String formTypEncd;//单据类型编码
    private String sellTypId;//销售类型编号
    private String bizTypId;//业务类型编号
    private String custId;//客户编号
    private String accNum;//用户编号
    private String userName;//用户名称    
    private String deptId;//部门编号
    private String deptName;//部门名称
    private String sellOrdrId;//发货单号
    private String setupPers;//创建人
    private String setupTm;//创建时间  
    private String mdfr;//修改人
    private String modiTm;//修改时间
    private Integer isNtBllg;//是否开票
    private Integer isNtChk;//是否审核
    private String chkr;//审核人
    private String chkDt;//审核时间
    private String invId;//发票编号
    private String custOrdrNum;//客户订单号
    private Integer isNtRtnGood;//是否退货
    private String toFormTypId;//来源单据类型编码
    private String custOpnBnk;//客户开户银行
    private String bkatNum;//银行账号
    private String dvlprBankId;//本单位银行编号
    private String memo;//备注  
    private String custNm;//客户名称
    private String bizTypNm;//业务类型名称
    private String formTypName;//单据类型名称
    private String sellTypNm;//销售类型名称
    private String toFormTypEncd;//来源单据类型编码
    private Integer isNtMakeVouch;//是否生成凭证
    private String makVouchPers;//制凭证人
    private String makVouchTm;//制凭证时间
    
    private List<EntrsAgnStlSub> entrsAgnStlSub;//委托代销结算单子表
    
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getCustOrdrNum() {
		return custOrdrNum;
	}

	public void setCustOrdrNum(String custOrdrNum) {
		this.custOrdrNum = custOrdrNum;
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

	public String getToFormTypEncd() {
		return toFormTypEncd;
	}

	public void setToFormTypEncd(String toFormTypEncd) {
		this.toFormTypEncd = toFormTypEncd;
	}

	public Integer getIsNtMakeVouch() {
		return isNtMakeVouch;
	}

	public void setIsNtMakeVouch(Integer isNtMakeVouch) {
		this.isNtMakeVouch = isNtMakeVouch;
	}

	public String getSellTypId() {
		return sellTypId;
	}

	public void setSellTypId(String sellTypId) {
		this.sellTypId = sellTypId;
	}

	public String getSellTypNm() {
		return sellTypNm;
	}

	public void setSellTypNm(String sellTypNm) {
		this.sellTypNm = sellTypNm;
	}

	public Integer getIsNtRtnGood() {
		return isNtRtnGood;
	}

	public void setIsNtRtnGood(Integer isNtRtnGood) {
		this.isNtRtnGood = isNtRtnGood;
	}

	public String getToFormTypId() {
		return toFormTypId;
	}

	public void setToFormTypId(String toFormTypId) {
		this.toFormTypId = toFormTypId;
	}

	public String getCustNm() {
		return custNm;
	}

	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public String getBizTypNm() {
		return bizTypNm;
	}

	public void setBizTypNm(String bizTypNm) {
		this.bizTypNm = bizTypNm;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}

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

	public String getFormTypEncd() {
		return formTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
	}

	public String getBizTypId() {
		return bizTypId;
	}

	public void setBizTypId(String bizTypId) {
		this.bizTypId = bizTypId;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getInvId() {
		return invId;
	}

	public void setInvId(String invId) {
		this.invId = invId;
	}

	public String getSellOrdrId() {
		return sellOrdrId;
	}

	public void setSellOrdrId(String sellOrdrId) {
		this.sellOrdrId = sellOrdrId;
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

	public Integer getIsNtBllg() {
		return isNtBllg;
	}

	public void setIsNtBllg(Integer isNtBllg) {
		this.isNtBllg = isNtBllg;
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

	public String getChkDt() {
		return chkDt;
	}

	public void setChkDt(String chkDt) {
		this.chkDt = chkDt;
	}

	public String getCustOpnBnk() {
		return custOpnBnk;
	}

	public void setCustOpnBnk(String custOpnBnk) {
		this.custOpnBnk = custOpnBnk;
	}

	public String getBkatNum() {
		return bkatNum;
	}

	public void setBkatNum(String bkatNum) {
		this.bkatNum = bkatNum;
	}

	public String getDvlprBankId() {
		return dvlprBankId;
	}

	public void setDvlprBankId(String dvlprBankId) {
		this.dvlprBankId = dvlprBankId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<EntrsAgnStlSub> getEntrsAgnStlSub() {
		return entrsAgnStlSub;
	}

	public void setEntrsAgnStlSub(List<EntrsAgnStlSub> entrsAgnStlSub) {
		this.entrsAgnStlSub = entrsAgnStlSub;
	}
    
}
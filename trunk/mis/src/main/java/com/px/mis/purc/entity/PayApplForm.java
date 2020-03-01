package com.px.mis.purc.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * pay_appl_form
 * 
 * @author
 */
public class PayApplForm implements Serializable {
	/**
	 * 订单编号
	 */
	private String payApplId;

	/**
	 * 订单日期
	 */
	private String payApplDt;

	/**
	 * 供应商编号
	 */
	private String provrId;

	/**
	 * 单据类型编码
	 */
	private String formTypEncd;
	/**
	 * 来源单据类型编码
	 */
	private String toFormTypEncd;
	/**
	 * 用户编号
	 */
	private String accNum;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 部门编码
	 */
	private String deptId;

	/**
	 * 供应商订单号
	 */
	private String provrOrdrNum;

	/**
	 * 是否付款
	 */
	private Integer isNtPay;

	/**
	 * 付款人
	 */
	private String payr;

	/**
	 * 结算科目
	 */
	private String stlSubj;

	/**
	 * 预付款余额
	 */
	private BigDecimal prepyMoneyBal;

	/**
	 * 应付款余额
	 */
	private BigDecimal acctPyblBal;

	/**
	 * 是否审核
	 */
	private Integer isNtChk;

	/**
	 * 审核人
	 */
	private String chkr;

	/**
	 * 审核时间
	 */
	private String chkTm;

	/**
	 * 创建人
	 */
	private String setupPers;

	/**
	 * 创建时间
	 */
	private String setupTm;

	/**
	 * 修改人
	 */
	private String mdfr;

	/**
	 * 修改时间
	 */
	private String modiTm;

	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 结算方式
	 */
	private String stlMode;
	/**
	 * 采购订单编码
	 */
	private String pursOrdrId;

	/**
	 * 子表
	 */
	List<PayApplFormSub> list;

	private static final long serialVersionUID = 1L;
	
	//关联查询供应商名称、用户名称、部门名称、采购类型名称
    private String provrNm;//供应商名称
    private String deptName;//部门名称
    private String formTypName;//单据类型名称
    
    
    public String getPursOrdrId() {
		return pursOrdrId;
	}

	public void setPursOrdrId(String pursOrdrId) {
		this.pursOrdrId = pursOrdrId;
	}

	public String getStlMode() {
		return stlMode;
	}

	public void setStlMode(String stlMode) {
		this.stlMode = stlMode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}

	public String getProvrNm() {
		return provrNm;
	}

	public void setProvrNm(String provrNm) {
		this.provrNm = provrNm;
	}

	public String getToFormTypEncd() {
		return toFormTypEncd;
	}

	public void setToFormTypEncd(String toFormTypEncd) {
		this.toFormTypEncd = toFormTypEncd;
	}

	public List<PayApplFormSub> getList() {
		return list;
	}

	public void setList(List<PayApplFormSub> list) {
		this.list = list;
	}

	public String getPayApplId() {
		return payApplId;
	}

	public void setPayApplId(String payApplId) {
		this.payApplId = payApplId;
	}

	public String getPayApplDt() {
		return payApplDt;
	}

	public void setPayApplDt(String payApplDt) {
		this.payApplDt = payApplDt;
	}

	public String getProvrId() {
		return provrId;
	}

	public void setProvrId(String provrId) {
		this.provrId = provrId;
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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getProvrOrdrNum() {
		return provrOrdrNum;
	}

	public void setProvrOrdrNum(String provrOrdrNum) {
		this.provrOrdrNum = provrOrdrNum;
	}

	public Integer getIsNtPay() {
		return isNtPay;
	}

	public void setIsNtPay(Integer isNtPay) {
		this.isNtPay = isNtPay;
	}

	public String getPayr() {
		return payr;
	}

	public void setPayr(String payr) {
		this.payr = payr;
	}

	public String getStlSubj() {
		return stlSubj;
	}

	public void setStlSubj(String stlSubj) {
		this.stlSubj = stlSubj;
	}

	public BigDecimal getPrepyMoneyBal() {
		return prepyMoneyBal;
	}

	public void setPrepyMoneyBal(BigDecimal prepyMoneyBal) {
		this.prepyMoneyBal = prepyMoneyBal;
	}

	public BigDecimal getAcctPyblBal() {
		return acctPyblBal;
	}

	public void setAcctPyblBal(BigDecimal acctPyblBal) {
		this.acctPyblBal = acctPyblBal;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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
		PayApplForm other = (PayApplForm) that;
		return (this.getToFormTypEncd() == null ? other.getToFormTypEncd() == null
				: this.getToFormTypEncd().equals(other.getToFormTypEncd()))
				&& (this.getPayApplId() == null ? other.getPayApplId() == null
						: this.getPayApplId().equals(other.getPayApplId()))
				&& (this.getPayApplDt() == null ? other.getPayApplDt() == null
						: this.getPayApplDt().equals(other.getPayApplDt()))
				&& (this.getProvrId() == null ? other.getProvrId() == null
						: this.getProvrId().equals(other.getProvrId()))
				&& (this.getFormTypEncd() == null ? other.getFormTypEncd() == null
						: this.getFormTypEncd().equals(other.getFormTypEncd()))
				&& (this.getAccNum() == null ? other.getAccNum() == null : this.getAccNum().equals(other.getAccNum()))
				&& (this.getUserName() == null ? other.getUserName() == null
						: this.getUserName().equals(other.getUserName()))
				&& (this.getDeptId() == null ? other.getDeptId() == null : this.getDeptId().equals(other.getDeptId()))
				&& (this.getProvrOrdrNum() == null ? other.getProvrOrdrNum() == null
						: this.getProvrOrdrNum().equals(other.getProvrOrdrNum()))
				&& (this.getIsNtPay() == null ? other.getIsNtPay() == null
						: this.getIsNtPay().equals(other.getIsNtPay()))
				&& (this.getPayr() == null ? other.getPayr() == null : this.getPayr().equals(other.getPayr()))
				&& (this.getStlSubj() == null ? other.getStlSubj() == null
						: this.getStlSubj().equals(other.getStlSubj()))
				&& (this.getPrepyMoneyBal() == null ? other.getPrepyMoneyBal() == null
						: this.getPrepyMoneyBal().equals(other.getPrepyMoneyBal()))
				&& (this.getAcctPyblBal() == null ? other.getAcctPyblBal() == null
						: this.getAcctPyblBal().equals(other.getAcctPyblBal()))
				&& (this.getIsNtChk() == null ? other.getIsNtChk() == null
						: this.getIsNtChk().equals(other.getIsNtChk()))
				&& (this.getChkr() == null ? other.getChkr() == null : this.getChkr().equals(other.getChkr()))
				&& (this.getChkTm() == null ? other.getChkTm() == null : this.getChkTm().equals(other.getChkTm()))
				&& (this.getSetupPers() == null ? other.getSetupPers() == null
						: this.getSetupPers().equals(other.getSetupPers()))
				&& (this.getSetupTm() == null ? other.getSetupTm() == null
						: this.getSetupTm().equals(other.getSetupTm()))
				&& (this.getMdfr() == null ? other.getMdfr() == null : this.getMdfr().equals(other.getMdfr()))
				&& (this.getModiTm() == null ? other.getModiTm() == null : this.getModiTm().equals(other.getModiTm()))
				&& (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getPayApplId() == null) ? 0 : getPayApplId().hashCode());
		result = prime * result + ((getPayApplDt() == null) ? 0 : getPayApplDt().hashCode());
		result = prime * result + ((getProvrId() == null) ? 0 : getProvrId().hashCode());
		result = prime * result + ((getFormTypEncd() == null) ? 0 : getFormTypEncd().hashCode());
		result = prime * result + ((getAccNum() == null) ? 0 : getAccNum().hashCode());
		result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
		result = prime * result + ((getDeptId() == null) ? 0 : getDeptId().hashCode());
		result = prime * result + ((getProvrOrdrNum() == null) ? 0 : getProvrOrdrNum().hashCode());
		result = prime * result + ((getIsNtPay() == null) ? 0 : getIsNtPay().hashCode());
		result = prime * result + ((getPayr() == null) ? 0 : getPayr().hashCode());
		result = prime * result + ((getStlSubj() == null) ? 0 : getStlSubj().hashCode());
		result = prime * result + ((getPrepyMoneyBal() == null) ? 0 : getPrepyMoneyBal().hashCode());
		result = prime * result + ((getAcctPyblBal() == null) ? 0 : getAcctPyblBal().hashCode());
		result = prime * result + ((getIsNtChk() == null) ? 0 : getIsNtChk().hashCode());
		result = prime * result + ((getChkr() == null) ? 0 : getChkr().hashCode());
		result = prime * result + ((getChkTm() == null) ? 0 : getChkTm().hashCode());
		result = prime * result + ((getSetupPers() == null) ? 0 : getSetupPers().hashCode());
		result = prime * result + ((getSetupTm() == null) ? 0 : getSetupTm().hashCode());
		result = prime * result + ((getMdfr() == null) ? 0 : getMdfr().hashCode());
		result = prime * result + ((getModiTm() == null) ? 0 : getModiTm().hashCode());
		result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
		result = prime * result + ((getToFormTypEncd() == null) ? 0 : getToFormTypEncd().hashCode());

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", payApplId=").append(payApplId);
		sb.append(", payApplDt=").append(payApplDt);
		sb.append(", provrId=").append(provrId);
		sb.append(", formTypEncd=").append(formTypEncd);
		sb.append(", accNum=").append(accNum);
		sb.append(", userName=").append(userName);
		sb.append(", deptId=").append(deptId);
		sb.append(", provrOrdrNum=").append(provrOrdrNum);
		sb.append(", isNtPay=").append(isNtPay);
		sb.append(", payr=").append(payr);
		sb.append(", stlSubj=").append(stlSubj);
		sb.append(", prepyMoneyBal=").append(prepyMoneyBal);
		sb.append(", acctPyblBal=").append(acctPyblBal);
		sb.append(", isNtChk=").append(isNtChk);
		sb.append(", chkr=").append(chkr);
		sb.append(", chkTm=").append(chkTm);
		sb.append(", setupPers=").append(setupPers);
		sb.append(", setupTm=").append(setupTm);
		sb.append(", mdfr=").append(mdfr);
		sb.append(", modiTm=").append(modiTm);
		sb.append(", memo=").append(memo);
		sb.append(", toFormTypEncd=").append(toFormTypEncd);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}
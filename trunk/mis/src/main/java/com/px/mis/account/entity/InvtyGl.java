package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//存货总账实体类
public class InvtyGl {
	private Integer ordrNum;//序号
	private String invtyEncd;//存货编码
	private String whsEncd;//仓库编号
	private String deptId;//部门编号
	private String bizMemId;//业务员编号
	private BigDecimal incomQty;//收入数量
	private BigDecimal delvQty;//发出数量
	private BigDecimal stlQty;//结算数量
	private BigDecimal minIntoWhsUprc;//最小入库单价
	private BigDecimal maxIntoWhsUprc;//最大入库单价
	private Integer isNtChk;//是否审核
	private String chkr;//审核人
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date chkDt;//审核时间
	private String setupPers;//创建人
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date setupTm;//创建时间
	private String mdfr;//修改人
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date modiTm;//修改时间
	private String acctiPrd;//会计期间
	private Integer isNtEndTmDeal;//是否期末处理
	private Integer isNtManlInput;//是否手工录入
	private String memo;//备注
	
	private String invtyNm;//存货名称
	private String whsNm;//仓库名称
	private String deptNm;//部门名称
	private String bizMemNm;//业务员名称
	public InvtyGl() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getWhsEncd() {
		return whsEncd;
	}
	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getBizMemId() {
		return bizMemId;
	}
	public void setBizMemId(String bizMemId) {
		this.bizMemId = bizMemId;
	}
	public BigDecimal getIncomQty() {
		return incomQty;
	}
	public void setIncomQty(BigDecimal incomQty) {
		this.incomQty = incomQty;
	}
	public BigDecimal getDelvQty() {
		return delvQty;
	}
	public void setDelvQty(BigDecimal delvQty) {
		this.delvQty = delvQty;
	}
	public BigDecimal getStlQty() {
		return stlQty;
	}
	public void setStlQty(BigDecimal stlQty) {
		this.stlQty = stlQty;
	}
	public BigDecimal getMinIntoWhsUprc() {
		return minIntoWhsUprc;
	}
	public void setMinIntoWhsUprc(BigDecimal minIntoWhsUprc) {
		this.minIntoWhsUprc = minIntoWhsUprc;
	}
	public BigDecimal getMaxIntoWhsUprc() {
		return maxIntoWhsUprc;
	}
	public void setMaxIntoWhsUprc(BigDecimal maxIntoWhsUprc) {
		this.maxIntoWhsUprc = maxIntoWhsUprc;
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
	public Date getChkDt() {
		return chkDt;
	}
	public void setChkDt(Date chkDt) {
		this.chkDt = chkDt;
	}
	public String getSetupPers() {
		return setupPers;
	}
	public void setSetupPers(String setupPers) {
		this.setupPers = setupPers;
	}
	public Date getSetupTm() {
		return setupTm;
	}
	public void setSetupTm(Date setupTm) {
		this.setupTm = setupTm;
	}
	public String getMdfr() {
		return mdfr;
	}
	public void setMdfr(String mdfr) {
		this.mdfr = mdfr;
	}
	public Date getModiTm() {
		return modiTm;
	}
	public void setModiTm(Date modiTm) {
		this.modiTm = modiTm;
	}
	public String getAcctiPrd() {
		return acctiPrd;
	}
	public void setAcctiPrd(String acctiPrd) {
		this.acctiPrd = acctiPrd;
	}
	public Integer getIsNtEndTmDeal() {
		return isNtEndTmDeal;
	}
	public void setIsNtEndTmDeal(Integer isNtEndTmDeal) {
		this.isNtEndTmDeal = isNtEndTmDeal;
	}
	public Integer getIsNtManlInput() {
		return isNtManlInput;
	}
	public void setIsNtManlInput(Integer isNtManlInput) {
		this.isNtManlInput = isNtManlInput;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getInvtyNm() {
		return invtyNm;
	}
	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}
	public String getWhsNm() {
		return whsNm;
	}
	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getBizMemNm() {
		return bizMemNm;
	}
	public void setBizMemNm(String bizMemNm) {
		this.bizMemNm = bizMemNm;
	}
	
	
}

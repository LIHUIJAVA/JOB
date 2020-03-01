package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//库存期初表实体类
public class InvtyTermBgnTab {
	private Integer ordrNum;//序号
	private String whsEncd;//仓库编号
	private String invtyEncd;//存货编码
	private String invtyClsId;//存货分类编号
	private String measrCorpId;//计量单位编号
	private BigDecimal qty;//数量
	private BigDecimal uprc;//单价
	private BigDecimal amt;//金额
	private BigDecimal cntnTaxUprc;//含税单价
	private BigDecimal prxTaxSum;//价税合计
	private String batNum;//批号
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date prdcDt;//生产日期
	private String quaGuarPer;//保质期
	private String invtySubjId;//存货科目编号
	private String memo;//备注
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date invldtnDt;//失效日期
	private Integer isNtBootEntry;//是否记账
	private String deptId;//部门编码
	
	private String bookEntryPers;//记账人
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date bookEntryTm;//记账时间
	private String bookEntryNum;//记账人编号
	
	private String setupPers;//创建人 制单人 
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date setupTm;//创建时间
	private String setupPersNum;//创建人编号
	
	private String modiPers;//修改人
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date modiTm;//修改时间
	private String modiPersNum;//修改人编号
	
	private String chkPers;//审核人
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date chkTm;//审核日期
	private String chkPersNum;//审核人编号
	
	private String whs;//仓库
	private String invtyNm;//存货名称
	private String spcModel;//规格型号
	private BigDecimal bxRule;//箱规
	private String deptName;//部门名称
	public InvtyTermBgnTab() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getWhsEncd() {
		return whsEncd;
	}
	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getInvtyClsId() {
		return invtyClsId;
	}
	public void setInvtyClsId(String invtyClsId) {
		this.invtyClsId = invtyClsId;
	}
	public String getMeasrCorpId() {
		return measrCorpId;
	}
	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public BigDecimal getUprc() {
		return uprc;
	}
	public void setUprc(BigDecimal uprc) {
		this.uprc = uprc;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public BigDecimal getCntnTaxUprc() {
		return cntnTaxUprc;
	}
	public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
		this.cntnTaxUprc = cntnTaxUprc;
	}
	public BigDecimal getPrxTaxSum() {
		return prxTaxSum;
	}
	public void setPrxTaxSum(BigDecimal prxTaxSum) {
		this.prxTaxSum = prxTaxSum;
	}
	public String getBatNum() {
		return batNum;
	}
	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}
	public Date getPrdcDt() {
		return prdcDt;
	}
	public void setPrdcDt(Date prdcDt) {
		this.prdcDt = prdcDt;
	}
	public String getQuaGuarPer() {
		return quaGuarPer;
	}
	public void setQuaGuarPer(String quaGuarPer) {
		this.quaGuarPer = quaGuarPer;
	}
	public String getInvtySubjId() {
		return invtySubjId;
	}
	public void setInvtySubjId(String invtySubjId) {
		this.invtySubjId = invtySubjId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getInvldtnDt() {
		return invldtnDt;
	}
	public void setInvldtnDt(Date invldtnDt) {
		this.invldtnDt = invldtnDt;
	}
	public Integer getIsNtBootEntry() {
		return isNtBootEntry;
	}
	public void setIsNtBootEntry(Integer isNtBootEntry) {
		this.isNtBootEntry = isNtBootEntry;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getBookEntryPers() {
		return bookEntryPers;
	}
	public void setBookEntryPers(String bookEntryPers) {
		this.bookEntryPers = bookEntryPers;
	}
	public Date getBookEntryTm() {
		return bookEntryTm;
	}
	public void setBookEntryTm(Date bookEntryTm) {
		this.bookEntryTm = bookEntryTm;
	}
	public String getBookEntryNum() {
		return bookEntryNum;
	}
	public void setBookEntryNum(String bookEntryNum) {
		this.bookEntryNum = bookEntryNum;
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
	public String getSetupPersNum() {
		return setupPersNum;
	}
	public void setSetupPersNum(String setupPersNum) {
		this.setupPersNum = setupPersNum;
	}
	public String getModiPers() {
		return modiPers;
	}
	public void setModiPers(String modiPers) {
		this.modiPers = modiPers;
	}
	public Date getModiTm() {
		return modiTm;
	}
	public void setModiTm(Date modiTm) {
		this.modiTm = modiTm;
	}
	public String getModiPersNum() {
		return modiPersNum;
	}
	public void setModiPersNum(String modiPersNum) {
		this.modiPersNum = modiPersNum;
	}
	public String getChkPers() {
		return chkPers;
	}
	public void setChkPers(String chkPers) {
		this.chkPers = chkPers;
	}
	public Date getChkTm() {
		return chkTm;
	}
	public void setChkTm(Date chkTm) {
		this.chkTm = chkTm;
	}
	public String getChkPersNum() {
		return chkPersNum;
	}
	public void setChkPersNum(String chkPersNum) {
		this.chkPersNum = chkPersNum;
	}
	public String getWhs() {
		return whs;
	}
	public void setWhs(String whs) {
		this.whs = whs;
	}
	public String getInvtyNm() {
		return invtyNm;
	}
	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}
	public String getSpcModel() {
		return spcModel;
	}
	public void setSpcModel(String spcModel) {
		this.spcModel = spcModel;
	}
	public BigDecimal getBxRule() {
		return bxRule;
	}
	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}

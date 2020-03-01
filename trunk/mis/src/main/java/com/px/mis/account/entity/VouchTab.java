package com.px.mis.account.entity;

//凭证表实体类
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(value = { "importNm", "imported", "subjNm", "stlModeId", "importDt","isNtBookOk", "bookOkDt",
		"isNtChk","chkTm", "cntPtySubjNm", "deptName", "custNm", "provrNm", "accNum", "importNm", "imported", "tabNum",
		"ordrNum", "acctYr", "acctiMth", "attachedFormNumbers", "memos", "currencyName", "deptId", "bizMemId", "custId",
		"provrId", "bizMemNm"}, allowSetters = true)
public class VouchTab {

	@JsonProperty("凭证编码")
	private String comnVouchId;// 序号 --凭证号
	@JsonProperty("凭证类别字")
	private String vouchCateWor; // 凭证类别
	@JsonProperty("凭证说明")
	private String comnVouchComnt;// 说明
	@JsonProperty("制单人")
	private String userName;// 制单人
	@JsonProperty("制单时间")
	private String ctime; // 生成时间 -制单日期
	@JsonProperty("审核人")
	private String chkr; // 审核人
	@JsonProperty("记账人")
	private String bookOkAcc; // 记账人
	@JsonProperty("摘要")
	private String memo;// 摘要
	@JsonProperty("借方科目编号")
	private String subjId;// 科目编号
	@JsonProperty("贷方科目编号")
	private String cntPtySubjId;// 对方科目编号
	@JsonProperty("借方金额")
	private BigDecimal debitAmt;// 借方金额
	@JsonProperty("贷方金额")
	private BigDecimal crdtAmt;// 贷方金额
	@JsonProperty("借方数量")
	private BigDecimal qtyDebit;// 借方数量
	@JsonProperty("贷方数量")
	private BigDecimal qtyCrdt;// 贷方数量

	/************* 以下忽略************* */
	private String importNm; // 导入人
	private Integer imported;// 是否导入总账
	private int tabNum; // 凭证编号
	private Integer ordrNum;// 凭证号 -凭证ID
	private int acctYr;// 会计年
	private int acctiMth;// 会计期间
	private String attachedFormNumbers; // 所附单据数
	private String memos;// 常用凭证说明 -备注2
	// private String bllgEncd;//结算方式编码
	// private String billNum;//票据号
	// private String billDt;//票据日期
	private String currencyName; // 币种名称
	// private String rate;//汇率
	// private String uprc;//单价
	// private String oriDebit;//原币借方
	// private String oriCrdt;//原币贷方
	private String deptId;// 部门编码
	private String bizMemId;// 职员编码
	private String custId;// 客户编码
	private String provrId;// 供应商编码
	// private String projectClsEncd;//项目大类编码
	// private String projectEncd;//项目编码
	private String bizMemNm;// 业务员
	/*
	 * private String customize1;//自定义项1 private String customize2;//自定义项2 private
	 * String customize3;//自定义项3 private String customize4;//自定义项4 private String
	 * customize5;//自定义项5 private String customize6;//自定义项6 private String
	 * customize7;//自定义项7 private String customize8;//自定义项8 private String
	 * customize9;//自定义项9 private String customize10;//自定义项10 private String
	 * customize11;//自定义项11 private String customize12;//自定义项12 private String
	 * customize13;//自定义项13 private String customize14;//自定义项14 private String
	 * customize15;//自定义项15 private String customize16;//自定义项16 private String
	 * cashFlowProject;//现金流量项目 private String cashFlowProjectDebitAmt;//现金流量借方金额
	 * private String cashFlowProjectCrdtAmt;//现金流量贷方金额
	 */
	private String subjNm;// 科目名称
	private String stlModeId;// 结算方式编号
	private String importDt; // 导入时间
	private Integer isNtBookOk; // 是否记账完成
	private String bookOkDt; // 记账时间
	private Integer isNtChk; // 是否审核
	private String chkTm; // 审核日期
	private String cntPtySubjNm;// 对方科目名称
	private String deptName;// 部门名称
	private String custNm;// 客户名称
	private String provrNm;// 供应商名称
	private String accNum; // 制单账号

	public VouchTab() {
	}

	public String getComnVouchId() {
		return comnVouchId;
	}

	public void setComnVouchId(String comnVouchId) {
		this.comnVouchId = comnVouchId;
	}

	public String getVouchCateWor() {
		return vouchCateWor;
	}

	public void setVouchCateWor(String vouchCateWor) {
		this.vouchCateWor = vouchCateWor;
	}

	public String getComnVouchComnt() {
		return comnVouchComnt;
	}

	public void setComnVouchComnt(String comnVouchComnt) {
		this.comnVouchComnt = comnVouchComnt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getChkr() {
		return chkr;
	}

	public void setChkr(String chkr) {
		this.chkr = chkr;
	}

	public String getBookOkAcc() {
		return bookOkAcc;
	}

	public void setBookOkAcc(String bookOkAcc) {
		this.bookOkAcc = bookOkAcc;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSubjId() {
		return subjId;
	}

	public void setSubjId(String subjId) {
		this.subjId = subjId;
	}

	public String getCntPtySubjId() {
		return cntPtySubjId;
	}

	public void setCntPtySubjId(String cntPtySubjId) {
		this.cntPtySubjId = cntPtySubjId;
	}

	public BigDecimal getDebitAmt() {
		return debitAmt;
	}

	public void setDebitAmt(BigDecimal debitAmt) {
		this.debitAmt = debitAmt;
	}

	public BigDecimal getCrdtAmt() {
		return crdtAmt;
	}

	public void setCrdtAmt(BigDecimal crdtAmt) {
		this.crdtAmt = crdtAmt;
	}

	public BigDecimal getQtyDebit() {
		return qtyDebit;
	}

	public void setQtyDebit(BigDecimal qtyDebit) {
		this.qtyDebit = qtyDebit;
	}

	public BigDecimal getQtyCrdt() {
		return qtyCrdt;
	}

	public void setQtyCrdt(BigDecimal qtyCrdt) {
		this.qtyCrdt = qtyCrdt;
	}

	public String getImportNm() {
		return importNm;
	}

	public void setImportNm(String importNm) {
		this.importNm = importNm;
	}

	public Integer getImported() {
		return imported;
	}

	public void setImported(Integer imported) {
		this.imported = imported;
	}

	public int getTabNum() {
		return tabNum;
	}

	public void setTabNum(int tabNum) {
		this.tabNum = tabNum;
	}

	public Integer getOrdrNum() {
		return ordrNum;
	}

	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}

	public int getAcctYr() {
		return acctYr;
	}

	public void setAcctYr(int acctYr) {
		this.acctYr = acctYr;
	}

	public int getAcctiMth() {
		return acctiMth;
	}

	public void setAcctiMth(int acctiMth) {
		this.acctiMth = acctiMth;
	}

	public String getAttachedFormNumbers() {
		return attachedFormNumbers;
	}

	public void setAttachedFormNumbers(String attachedFormNumbers) {
		this.attachedFormNumbers = attachedFormNumbers;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
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

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getProvrId() {
		return provrId;
	}

	public void setProvrId(String provrId) {
		this.provrId = provrId;
	}

	public String getBizMemNm() {
		return bizMemNm;
	}

	public void setBizMemNm(String bizMemNm) {
		this.bizMemNm = bizMemNm;
	}

	public String getSubjNm() {
		return subjNm;
	}

	public void setSubjNm(String subjNm) {
		this.subjNm = subjNm;
	}

	public String getStlModeId() {
		return stlModeId;
	}

	public void setStlModeId(String stlModeId) {
		this.stlModeId = stlModeId;
	}

	public String getImportDt() {
		return importDt;
	}

	public void setImportDt(String importDt) {
		this.importDt = importDt;
	}

	public Integer getIsNtBookOk() {
		return isNtBookOk;
	}

	public void setIsNtBookOk(Integer isNtBookOk) {
		this.isNtBookOk = isNtBookOk;
	}

	public String getBookOkDt() {
		return bookOkDt;
	}

	public void setBookOkDt(String bookOkDt) {
		this.bookOkDt = bookOkDt;
	}

	public Integer getIsNtChk() {
		return isNtChk;
	}

	public void setIsNtChk(Integer isNtChk) {
		this.isNtChk = isNtChk;
	}

	public String getChkTm() {
		return chkTm;
	}

	public void setChkTm(String chkTm) {
		this.chkTm = chkTm;
	}

	public String getCntPtySubjNm() {
		return cntPtySubjNm;
	}

	public void setCntPtySubjNm(String cntPtySubjNm) {
		this.cntPtySubjNm = cntPtySubjNm;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getCustNm() {
		return custNm;
	}

	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public String getProvrNm() {
		return provrNm;
	}

	public void setProvrNm(String provrNm) {
		this.provrNm = provrNm;
	}

	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

}

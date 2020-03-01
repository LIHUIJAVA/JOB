package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//发出商品委托代销明细账
public class DelvMerchdEntrsAgnDtlAcct {
	private Integer ordrNum;//序号
	private String formId;//单据编号
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date formDt;//单据日期
	private String acctiQty;//核算数量
	private String acctiUprc;//核算单价
	private String acctiAmt;//核算金额
	private Integer isNtchk;//是否审核
	private String chkr;//审核人
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date chkTm;//审核日期
	private Integer isNtMakeVouch;//是否生成凭证
	private String recvSendInd;//收发标志
	private String sellTypId;//销售类型编号
	private String crspdSellTypSnglNum;//对应销售单号
	private String invMasTabInd;//发票主表标识
	private String setupPers;//创建人
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date setupTm;//创建时间
	private Integer isNtBookEntry;//是否记账
	private String bookEntryPers;//记账人
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date bookEntryTm;//记账日期
	private String acctiPrd;//会计期间
	private String vouchId;//凭证编号
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date vouchDt;//凭证日期
	private String vouchCate;//凭证类别字
	private String vouchMemo;//凭证摘要
	private String delvMerchdSubjId;//发出商品科目编号
	private String cntPtySubjId;//对方科目编号
	private String formTyp;//单据类型
	private String bozTypId;//业务类型编号
	private String whsId;//仓库编号
	private String deptId;//部门编号
	private String invtyEncd;//存货编码
	private String recvSendCateId;//收发类别编号
	private String custId;//客户编号
	private String invSubTabInd;//发票子表标识
	private String bizMemId;//业务员编号
	private BigDecimal incomQty;//收入数量
	private BigDecimal delvQty;//发出数量
	private BigDecimal incomUprc;//收入单价
	private BigDecimal delvUprc;//发出单价
	private BigDecimal incomAmt;//收入金额
	private BigDecimal delvAmt;//发出金额
	private String batNum;//批号
	private String invenTypeId;//存货一级分类编号
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date prdcDt;//生产日期
	private String quaGuaPer;//保质期
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date invldtnDt;//失效日期
	private String memo;//备注
	public DelvMerchdEntrsAgnDtlAcct() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public Date getFormDt() {
		return formDt;
	}
	public void setFormDt(Date formDt) {
		this.formDt = formDt;
	}
	public String getAcctiQty() {
		return acctiQty;
	}
	public void setAcctiQty(String acctiQty) {
		this.acctiQty = acctiQty;
	}
	public String getAcctiUprc() {
		return acctiUprc;
	}
	public void setAcctiUprc(String acctiUprc) {
		this.acctiUprc = acctiUprc;
	}
	public String getAcctiAmt() {
		return acctiAmt;
	}
	public void setAcctiAmt(String acctiAmt) {
		this.acctiAmt = acctiAmt;
	}
	public Integer getIsNtchk() {
		return isNtchk;
	}
	public void setIsNtchk(Integer isNtchk) {
		this.isNtchk = isNtchk;
	}
	public String getChkr() {
		return chkr;
	}
	public void setChkr(String chkr) {
		this.chkr = chkr;
	}
	public Date getChkTm() {
		return chkTm;
	}
	public void setChkTm(Date chkTm) {
		this.chkTm = chkTm;
	}
	public Integer getIsNtMakeVouch() {
		return isNtMakeVouch;
	}
	public void setIsNtMakeVouch(Integer isNtMakeVouch) {
		this.isNtMakeVouch = isNtMakeVouch;
	}
	public String getRecvSendInd() {
		return recvSendInd;
	}
	public void setRecvSendInd(String recvSendInd) {
		this.recvSendInd = recvSendInd;
	}
	public String getSellTypId() {
		return sellTypId;
	}
	public void setSellTypId(String sellTypId) {
		this.sellTypId = sellTypId;
	}
	public String getCrspdSellTypSnglNum() {
		return crspdSellTypSnglNum;
	}
	public void setCrspdSellTypSnglNum(String crspdSellTypSnglNum) {
		this.crspdSellTypSnglNum = crspdSellTypSnglNum;
	}
	public String getInvMasTabInd() {
		return invMasTabInd;
	}
	public void setInvMasTabInd(String invMasTabInd) {
		this.invMasTabInd = invMasTabInd;
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
	public Integer getIsNtBookEntry() {
		return isNtBookEntry;
	}
	public void setIsNtBookEntry(Integer isNtBookEntry) {
		this.isNtBookEntry = isNtBookEntry;
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
	public String getAcctiPrd() {
		return acctiPrd;
	}
	public void setAcctiPrd(String acctiPrd) {
		this.acctiPrd = acctiPrd;
	}
	public String getVouchId() {
		return vouchId;
	}
	public void setVouchId(String vouchId) {
		this.vouchId = vouchId;
	}
	public Date getVouchDt() {
		return vouchDt;
	}
	public void setVouchDt(Date vouchDt) {
		this.vouchDt = vouchDt;
	}
	public String getVouchCate() {
		return vouchCate;
	}
	public void setVouchCate(String vouchCate) {
		this.vouchCate = vouchCate;
	}
	public String getVouchMemo() {
		return vouchMemo;
	}
	public void setVouchMemo(String vouchMemo) {
		this.vouchMemo = vouchMemo;
	}
	public String getDelvMerchdSubjId() {
		return delvMerchdSubjId;
	}
	public void setDelvMerchdSubjId(String delvMerchdSubjId) {
		this.delvMerchdSubjId = delvMerchdSubjId;
	}
	public String getCntPtySubjId() {
		return cntPtySubjId;
	}
	public void setCntPtySubjId(String cntPtySubjId) {
		this.cntPtySubjId = cntPtySubjId;
	}
	public String getFormTyp() {
		return formTyp;
	}
	public void setFormTyp(String formTyp) {
		this.formTyp = formTyp;
	}
	public String getBozTypId() {
		return bozTypId;
	}
	public void setBozTypId(String bozTypId) {
		this.bozTypId = bozTypId;
	}
	public String getWhsId() {
		return whsId;
	}
	public void setWhsId(String whsId) {
		this.whsId = whsId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getRecvSendCateId() {
		return recvSendCateId;
	}
	public void setRecvSendCateId(String recvSendCateId) {
		this.recvSendCateId = recvSendCateId;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getInvSubTabInd() {
		return invSubTabInd;
	}
	public void setInvSubTabInd(String invSubTabInd) {
		this.invSubTabInd = invSubTabInd;
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
	public BigDecimal getIncomUprc() {
		return incomUprc;
	}
	public void setIncomUprc(BigDecimal incomUprc) {
		this.incomUprc = incomUprc;
	}
	public BigDecimal getDelvUprc() {
		return delvUprc;
	}
	public void setDelvUprc(BigDecimal delvUprc) {
		this.delvUprc = delvUprc;
	}
	public BigDecimal getIncomAmt() {
		return incomAmt;
	}
	public void setIncomAmt(BigDecimal incomAmt) {
		this.incomAmt = incomAmt;
	}
	public BigDecimal getDelvAmt() {
		return delvAmt;
	}
	public void setDelvAmt(BigDecimal delvAmt) {
		this.delvAmt = delvAmt;
	}
	public String getBatNum() {
		return batNum;
	}
	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}
	public String getInvenTypeId() {
		return invenTypeId;
	}
	public void setInvenTypeId(String invenTypeId) {
		this.invenTypeId = invenTypeId;
	}
	public Date getPrdcDt() {
		return prdcDt;
	}
	public void setPrdcDt(Date prdcDt) {
		this.prdcDt = prdcDt;
	}
	public String getQuaGuaPer() {
		return quaGuaPer;
	}
	public void setQuaGuaPer(String quaGuaPer) {
		this.quaGuaPer = quaGuaPer;
	}
	public Date getInvldtnDt() {
		return invldtnDt;
	}
	public void setInvldtnDt(Date invldtnDt) {
		this.invldtnDt = invldtnDt;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
}

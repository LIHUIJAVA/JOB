package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//存货明细账
public class InvtyDtlAcct {
	
	public InvtyDtlAcct(String formId, Date formDt, String recvSendInd, String sellTypId,
			String crspdSellTypSnglNum, String outIntoWhsAdjSnglSubtabInd, String setupPers, Date setupTm,
			Integer isNtChk, String chkr, Date chkTm, Integer isNtBookEntry, String bookEntryPers, Date bookEntryTm,
			String acctiPrd, String vouchId, Date vouchDt, String vouchCate, String vouchMemo, String invtySubjId,
			String cntPtySubjId, String formTyp, String bizTypId, String pursTypId, String recvSendCateId,
			String invtyEncd, String provrId, String custId, String pursOrdrNum, String pursToGdsId,
			String invSubTabInd, String rtnGoodsSnglSubtabInd, String bizMemId, BigDecimal incomQty, BigDecimal delvQty,
			BigDecimal incomUprc, BigDecimal delvUprc, BigDecimal incomAmt, BigDecimal delvAmt, String projId,
			String batNum, Date prdcDt, String quaGuaPer, Date invldtnDt, String sellSnglNum, String sellSnglMasTabId,
			String sellSnglSubTabId, String pursOrdrSubTabId, String memo) {
		super();
		this.formId = formId;
		this.formDt = formDt;
		this.recvSendInd = recvSendInd;
		this.sellTypId = sellTypId;
		this.crspdSellTypSnglNum = crspdSellTypSnglNum;
		this.outIntoWhsAdjSnglSubtabInd = outIntoWhsAdjSnglSubtabInd;
		this.setupPers = setupPers;
		this.setupTm = setupTm;
		this.isNtChk = isNtChk;
		this.chkr = chkr;
		this.chkTm = chkTm;
		this.isNtBookEntry = isNtBookEntry;
		this.bookEntryPers = bookEntryPers;
		this.bookEntryTm = bookEntryTm;
		this.acctiPrd = acctiPrd;
		this.vouchId = vouchId;
		this.vouchDt = vouchDt;
		this.vouchCate = vouchCate;
		this.vouchMemo = vouchMemo;
		this.invtySubjId = invtySubjId;
		this.cntPtySubjId = cntPtySubjId;
		this.formTyp = formTyp;
		this.bizTypId = bizTypId;
		this.pursTypId = pursTypId;
		this.recvSendCateId = recvSendCateId;
		this.invtyEncd = invtyEncd;
		this.provrId = provrId;
		this.custId = custId;
		this.pursOrdrNum = pursOrdrNum;
		this.pursToGdsId = pursToGdsId;
		this.invSubTabInd = invSubTabInd;
		this.rtnGoodsSnglSubtabInd = rtnGoodsSnglSubtabInd;
		this.bizMemId = bizMemId;
		this.incomQty = incomQty;
		this.delvQty = delvQty;
		this.incomUprc = incomUprc;
		this.delvUprc = delvUprc;
		this.incomAmt = incomAmt;
		this.delvAmt = delvAmt;
		this.projId = projId;
		this.batNum = batNum;
		this.prdcDt = prdcDt;
		this.quaGuaPer = quaGuaPer;
		this.invldtnDt = invldtnDt;
		this.sellSnglNum = sellSnglNum;
		this.sellSnglMasTabId = sellSnglMasTabId;
		this.sellSnglSubTabId = sellSnglSubTabId;
		this.pursOrdrSubTabId = pursOrdrSubTabId;
		this.memo = memo;
	}
	
	private Integer ordrNum;//序号
	private String formId;//单据编号
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date formDt;//单据日期
	private String recvSendInd;//收发标识
	private String sellTypId;//销售类型编号
	private String crspdSellTypSnglNum;//对应销售类型单号
	private String outIntoWhsAdjSnglSubtabInd;//出入库调整单子表标识
	private String setupPers;//创建人
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date setupTm;//创建时间
	private Integer isNtChk;//是否审核
	private String chkr;//审核人
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date chkTm;//审核日期
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
	private String invtySubjId;//存货科目编号
	private String cntPtySubjId;//对方科目编号
	private String formTyp;//单据类型
	private String bizTypId;//业务类型编号
	private String pursTypId;//采购类型编号
	private String recvSendCateId;//收发类型编号
	private String invtyEncd;//存货编码
	private String provrId;//供应商编号
	private String custId;//客户编号
	private String pursOrdrNum;//采购订单号
	private String pursToGdsId;//采购到货编号
	private String invSubTabInd;//发票子表标识
	private String rtnGoodsSnglSubtabInd;//退货单子表标识
	private String bizMemId;//业务员编号
	private BigDecimal incomQty;//收入数量
	private BigDecimal delvQty;//发出数量
	private BigDecimal incomUprc;//收入单价
	private BigDecimal delvUprc;//发出单价
	private BigDecimal incomAmt;//收入金额
	private BigDecimal delvAmt;//发出金额
	private String projId;//存货一级分类编码
	private String batNum;//批号
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date prdcDt;//生产日期
	private String quaGuaPer;//保质期
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date invldtnDt;//失效日期
	private String sellSnglNum;//销售单号
	private String sellSnglMasTabId;//销售单主表id
	private String sellSnglSubTabId;//销售单子表id
	private String pursOrdrSubTabId;//采购订单子表id
	private String memo;//备注
	public InvtyDtlAcct() {
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
	public String getOutIntoWhsAdjSnglSubtabInd() {
		return outIntoWhsAdjSnglSubtabInd;
	}
	public void setOutIntoWhsAdjSnglSubtabInd(String outIntoWhsAdjSnglSubtabInd) {
		this.outIntoWhsAdjSnglSubtabInd = outIntoWhsAdjSnglSubtabInd;
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
	public Date getChkTm() {
		return chkTm;
	}
	public void setChkTm(Date chkTm) {
		this.chkTm = chkTm;
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
	public String getVouchCate() {
		return vouchCate;
	}
	public void setVouchCate(String vouchCate) {
		this.vouchCate = vouchCate;
	}
	public Date getVouchDt() {
		return vouchDt;
	}
	public void setVouchDt(Date vouchDt) {
		this.vouchDt = vouchDt;
	}
	public String getVouchMemo() {
		return vouchMemo;
	}
	public void setVouchMemo(String vouchMemo) {
		this.vouchMemo = vouchMemo;
	}
	public String getInvtySubjId() {
		return invtySubjId;
	}
	public void setInvtySubjId(String invtySubjId) {
		this.invtySubjId = invtySubjId;
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
	public String getBizTypId() {
		return bizTypId;
	}
	public void setBizTypId(String bizTypId) {
		this.bizTypId = bizTypId;
	}
	public String getPursTypId() {
		return pursTypId;
	}
	public void setPursTypId(String pursTypId) {
		this.pursTypId = pursTypId;
	}
	public String getRecvSendCateId() {
		return recvSendCateId;
	}
	public void setRecvSendCateId(String recvSendCateId) {
		this.recvSendCateId = recvSendCateId;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getProvrId() {
		return provrId;
	}
	public void setProvrId(String provrId) {
		this.provrId = provrId;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getPursOrdrNum() {
		return pursOrdrNum;
	}
	public void setPursOrdrNum(String pursOrdrNum) {
		this.pursOrdrNum = pursOrdrNum;
	}
	public String getPursToGdsId() {
		return pursToGdsId;
	}
	public void setPursToGdsId(String pursToGdsId) {
		this.pursToGdsId = pursToGdsId;
	}
	public String getInvSubTabInd() {
		return invSubTabInd;
	}
	public void setInvSubTabInd(String invSubTabInd) {
		this.invSubTabInd = invSubTabInd;
	}
	public String getRtnGoodsSnglSubtabInd() {
		return rtnGoodsSnglSubtabInd;
	}
	public void setRtnGoodsSnglSubtabInd(String rtnGoodsSnglSubtabInd) {
		this.rtnGoodsSnglSubtabInd = rtnGoodsSnglSubtabInd;
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
	public String getProjId() {
		return projId;
	}
	public void setProjId(String projId) {
		this.projId = projId;
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
	public String getSellSnglNum() {
		return sellSnglNum;
	}
	public void setSellSnglNum(String sellSnglNum) {
		this.sellSnglNum = sellSnglNum;
	}
	public String getSellSnglMasTabId() {
		return sellSnglMasTabId;
	}
	public void setSellSnglMasTabId(String sellSnglMasTabId) {
		this.sellSnglMasTabId = sellSnglMasTabId;
	}
	public String getSellSnglSubTabId() {
		return sellSnglSubTabId;
	}
	public void setSellSnglSubTabId(String sellSnglSubTabId) {
		this.sellSnglSubTabId = sellSnglSubTabId;
	}
	public String getPursOrdrSubTabId() {
		return pursOrdrSubTabId;
	}
	public void setPursOrdrSubTabId(String pursOrdrSubTabId) {
		this.pursOrdrSubTabId = pursOrdrSubTabId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
}

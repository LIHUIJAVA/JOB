package com.px.mis.account.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
//������Ʒί�д�����ϸ��
public class DelvMerchdEntrsAgnDtlAcct {
	private Integer ordrNum;//���
	private String formId;//���ݱ��
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date formDt;//��������
	private String acctiQty;//��������
	private String acctiUprc;//���㵥��
	private String acctiAmt;//������
	private Integer isNtchk;//�Ƿ����
	private String chkr;//�����
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date chkTm;//�������
	private Integer isNtMakeVouch;//�Ƿ�����ƾ֤
	private String recvSendInd;//�շ���־
	private String sellTypId;//�������ͱ��
	private String crspdSellTypSnglNum;//��Ӧ���۵���
	private String invMasTabInd;//��Ʊ�����ʶ
	private String setupPers;//������
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date setupTm;//����ʱ��
	private Integer isNtBookEntry;//�Ƿ����
	private String bookEntryPers;//������
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date bookEntryTm;//��������
	private String acctiPrd;//����ڼ�
	private String vouchId;//ƾ֤���
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date vouchDt;//ƾ֤����
	private String vouchCate;//ƾ֤�����
	private String vouchMemo;//ƾ֤ժҪ
	private String delvMerchdSubjId;//������Ʒ��Ŀ���
	private String cntPtySubjId;//�Է���Ŀ���
	private String formTyp;//��������
	private String bozTypId;//ҵ�����ͱ��
	private String whsId;//�ֿ���
	private String deptId;//���ű��
	private String invtyEncd;//�������
	private String recvSendCateId;//�շ������
	private String custId;//�ͻ����
	private String invSubTabInd;//��Ʊ�ӱ��ʶ
	private String bizMemId;//ҵ��Ա���
	private BigDecimal incomQty;//��������
	private BigDecimal delvQty;//��������
	private BigDecimal incomUprc;//���뵥��
	private BigDecimal delvUprc;//��������
	private BigDecimal incomAmt;//������
	private BigDecimal delvAmt;//�������
	private String batNum;//����
	private String invenTypeId;//���һ��������
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date prdcDt;//��������
	private String quaGuaPer;//������
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss") 
	private Date invldtnDt;//ʧЧ����
	private String memo;//��ע
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

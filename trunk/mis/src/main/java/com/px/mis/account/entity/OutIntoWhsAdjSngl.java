package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
//出入库调整单主表实体类
public class OutIntoWhsAdjSngl {
	
	@JsonProperty("单据号")
	private String formNum;//单据号 出入库调整单主表标识
	@JsonProperty("单据日期")
	private String formTm;//单据日期
	@JsonProperty("收发类别编码")
	private String recvSendCateId;//收发类别编码
	@JsonProperty("用户编码")
	private String accNum;//用户编码
	@JsonProperty("用户名称")
	private String userName;//用户名称
	@JsonProperty("部门编码")
	private String deptId;//部门编码
	@JsonProperty("客户编码")
	private String custId;//客户编码
	@JsonProperty("供应商编码")
	private String provrId;//供应商编码
	@JsonProperty("出入库")
	private Integer outIntoWhsInd;//出入库标识 0：出库  1：入库
	@JsonProperty("是否记账")
	private Integer isNtBookEntry;//是否记账
	@JsonProperty("记账人")
	private String bookEntryPers;//记账人
	@JsonProperty("记账时间")
	private String bookEntryTm;//记账时间
	@JsonProperty("修改人")
	private String mdfrPers;//修改人 经手人
	@JsonProperty("修改时间")
	private String mdfrTm;//修改时间
	@JsonProperty("创建人")
	private String setupPers;//创建人 制单人
	@JsonProperty("创建时间")
	private String setupTm;//创建时间
	@JsonProperty("表头备注")
	private String memo;//备注
	@JsonProperty("子备注")
	private String memos;//备注
	@JsonProperty("是否先进先出")
	private Integer isFifoAdjBan;//是否先进先出调整结存
	@JsonProperty("是否对应出库单")
	private Integer isCrspdOutWhsSngl;//是否对应出库单
	@JsonProperty("被调整单据号")
	private String beadjOutIntoWhsMastabInd;//被调整出入库主表标识
	@JsonProperty("是否销售")
	private Integer isNtSell;//是否销售
	@JsonProperty("收发类别")
	private String recvSendCateNm;//收发类别名称；
	@JsonProperty("部门名称")
	private String deptNm;//部门名称；
	@JsonProperty("客户名称")
	private String custNm;//客户名称；
	@JsonProperty("供应商")
	private String provrNm;//供应商名称；
	@JsonInclude(Include.NON_NULL)
	private List<OutIntoWhsAdjSnglSubTab> outIntoList;
	@JsonProperty("来源单据类型编码")
	private String toFormTypEncd;//来源单据类型编码
	@JsonProperty("是否生成凭证")
    private Integer isNtMakeVouch;//是否生成凭证
	@JsonProperty("制凭证人")
    private String makVouchPers;//制凭证人
	@JsonProperty("制凭证时间")
    private String makVouchTm;//制凭证时间
    
	@JsonProperty("单据类型编码")
    private String formTypEncd;//单据类型编码
	@JsonProperty("单据类型名称")
    private String formTypName;//单据类型名称
    
    
	public String getFormTypEncd() {
		return formTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
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

	public String getFormNum() {
		return formNum;
	}

	public void setFormNum(String formNum) {
		this.formNum = formNum;
	}

	public String getFormTm() {
		return formTm;
	}

	public void setFormTm(String formTm) {
		this.formTm = formTm;
	}

	public String getRecvSendCateId() {
		return recvSendCateId;
	}

	public void setRecvSendCateId(String recvSendCateId) {
		this.recvSendCateId = recvSendCateId;
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

	public Integer getOutIntoWhsInd() {
		return outIntoWhsInd;
	}

	public void setOutIntoWhsInd(Integer outIntoWhsInd) {
		this.outIntoWhsInd = outIntoWhsInd;
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

	public String getBookEntryTm() {
		return bookEntryTm;
	}

	public void setBookEntryTm(String bookEntryTm) {
		this.bookEntryTm = bookEntryTm;
	}

	public String getMdfrPers() {
		return mdfrPers;
	}

	public void setMdfrPers(String mdfrPers) {
		this.mdfrPers = mdfrPers;
	}

	public String getMdfrTm() {
		return mdfrTm;
	}

	public void setMdfrTm(String mdfrTm) {
		this.mdfrTm = mdfrTm;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getIsFifoAdjBan() {
		return isFifoAdjBan;
	}

	public void setIsFifoAdjBan(Integer isFifoAdjBan) {
		this.isFifoAdjBan = isFifoAdjBan;
	}

	public Integer getIsCrspdOutWhsSngl() {
		return isCrspdOutWhsSngl;
	}

	public void setIsCrspdOutWhsSngl(Integer isCrspdOutWhsSngl) {
		this.isCrspdOutWhsSngl = isCrspdOutWhsSngl;
	}

	public String getBeadjOutIntoWhsMastabInd() {
		return beadjOutIntoWhsMastabInd;
	}

	public void setBeadjOutIntoWhsMastabInd(String beadjOutIntoWhsMastabInd) {
		this.beadjOutIntoWhsMastabInd = beadjOutIntoWhsMastabInd;
	}

	public Integer getIsNtSell() {
		return isNtSell;
	}

	public void setIsNtSell(Integer isNtSell) {
		this.isNtSell = isNtSell;
	}

	public String getRecvSendCateNm() {
		return recvSendCateNm;
	}

	public void setRecvSendCateNm(String recvSendCateNm) {
		this.recvSendCateNm = recvSendCateNm;
	}

	public String getDeptNm() {
		return deptNm;
	}

	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
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

	public List<OutIntoWhsAdjSnglSubTab> getOutIntoList() {
		return outIntoList;
	}

	public void setOutIntoList(List<OutIntoWhsAdjSnglSubTab> outIntoList) {
		this.outIntoList = outIntoList;
	}
	
}

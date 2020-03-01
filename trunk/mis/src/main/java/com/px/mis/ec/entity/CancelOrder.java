package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class CancelOrder {

	private String id;//退款单编号
	private String buyerId;//客户账号
	private String buyerName;//客户姓名
	private String checkTime;//审核时间
	private String applyTime;//申请时间
	private BigDecimal applyRefundSum;//退款金额
	private int status;//审核状态
	private String checkUserName;//审核人
	private String orderId;//订单号
	private String checkRemark;//审核备注
	private String reason;//退款原因
	private int systemId;//退款来源
	private int isAudit;//本地库审核状态
	private String ecId;//电商平台编号
	private String storeId;//店铺编号
	private String downloadTime;//下载时间
	private String auditTime;//审核时间
	private String auditUserId;//审核人id
	private String auditUserName;//审核人姓名
	private String auditHint;//审核提示
	private String storeName;
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getAuditHint() {
		return auditHint;
	}
	public void setAuditHint(String auditHint) {
		this.auditHint = auditHint;
	}
	public String getDownloadTime() {
		return downloadTime;
	}
	public void setDownloadTime(String downloadTime) {
		this.downloadTime = downloadTime;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}
	public String getAuditUserName() {
		return auditUserName;
	}
	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public BigDecimal getApplyRefundSum() {
		return applyRefundSum;
	}
	public void setApplyRefundSum(BigDecimal applyRefundSum) {
		this.applyRefundSum = applyRefundSum;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCheckUserName() {
		return checkUserName;
	}
	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCheckRemark() {
		return checkRemark;
	}
	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getSystemId() {
		return systemId;
	}
	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}
	public int getIsAudit() {
		return isAudit;
	}
	public void setIsAudit(int isAudit) {
		this.isAudit = isAudit;
	}
	public String getEcId() {
		return ecId;
	}
	public void setEcId(String ecId) {
		this.ecId = ecId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	
}

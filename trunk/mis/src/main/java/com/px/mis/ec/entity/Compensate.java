package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class Compensate {

	private String compensateId;//赔付单号
	private int compensateKeyid;//赔付子单号
	private String venderId;//商家编号
	private int type;//赔付类型
	private String orderId;//订单号
	private int orderType;//订单类型
	private String modified;//更新日期
	private String created;//创建日期
	private int compensateType;//赔付金额类型
	private BigDecimal shouldPay;//应赔付金额
	private BigDecimal compensateAmount;//赔付金额
	private String compensateReason;//赔付原因
	private int checkStatus;//商家审核状态
	private int erpCheckStatus;//运营、客服审核状态
	private int canSecondAppeal;//是否可以二次申诉
	private int isAudit;//本地库审核状态
	private String ecId;//电商平台编号
	private String storeId;//店铺编号
	private String downloadTime;
	private String auditTime;
	private String auditUserId;
	private String auditUserName;
	private String storeName;
	private String auditHint;//审核提示
	
	public String getAuditHint() {
		return auditHint;
	}
	public void setAuditHint(String auditHint) {
		this.auditHint = auditHint;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
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
	public String getCompensateId() {
		return compensateId;
	}
	public void setCompensateId(String compensateId) {
		this.compensateId = compensateId;
	}
	public int getCompensateKeyid() {
		return compensateKeyid;
	}
	public void setCompensateKeyid(int compensateKeyid) {
		this.compensateKeyid = compensateKeyid;
	}
	public String getVenderId() {
		return venderId;
	}
	public void setVenderId(String venderId) {
		this.venderId = venderId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public int getCompensateType() {
		return compensateType;
	}
	public void setCompensateType(int compensateType) {
		this.compensateType = compensateType;
	}
	public BigDecimal getShouldPay() {
		return shouldPay;
	}
	public void setShouldPay(BigDecimal shouldPay) {
		this.shouldPay = shouldPay;
	}
	public BigDecimal getCompensateAmount() {
		return compensateAmount;
	}
	public void setCompensateAmount(BigDecimal compensateAmount) {
		this.compensateAmount = compensateAmount;
	}
	public String getCompensateReason() {
		return compensateReason;
	}
	public void setCompensateReason(String compensateReason) {
		this.compensateReason = compensateReason;
	}
	public int getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}
	public int getErpCheckStatus() {
		return erpCheckStatus;
	}
	public void setErpCheckStatus(int erpCheckStatus) {
		this.erpCheckStatus = erpCheckStatus;
	}
	public int getCanSecondAppeal() {
		return canSecondAppeal;
	}
	public void setCanSecondAppeal(int canSecondAppeal) {
		this.canSecondAppeal = canSecondAppeal;
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

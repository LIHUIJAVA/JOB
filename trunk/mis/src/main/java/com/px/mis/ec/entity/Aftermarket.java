package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class Aftermarket {
	
	private int applyId;//申请单号
	private int serviceId;//服务单号
	private String applyTime;//申请时间
	private int customerExpect;//客户期望
	private String customerExpectName;//客户期望名称
	private int serviceStatus;//服务单状态
	private String serviceStatusName;//服务单状态名称
	private String customerPin;//客户账号
	private String customerName;//客户姓名
	private int customerGrade;//用户级别
	private String customerTel;//用户电话
	private String pickwareAddress;//取件地址
	private String orderId;//订单号
	private int orderType;//订单类型
	private String orderTypeName;//订单类型名称
	private BigDecimal actualPayPrice;//实付金额
	private String skuId;//商品编号
	private int wareType;//商品类型
	private String wareTypeName;//商品类型名称
	private String wareName;//商品名称
	private String approvePin;//审核人账号
	private String approveName;//审核人名称
	private String approveTime;//审核时间
	private int approveResult;//审核结果
	private String approveResultName;//审核结果名称
	private String processPin;//处理人账号
	private String processName;//处理人名称
	private String processTime;//处理时间
	private int processResult;//处理结果
	private String processResultName;//处理结果名称
	private int isAudit;//本地库审核状态
	private String ecId;//电商平台编号
	private String storeId;//店铺编号
	private String auditHint;//审核提示 
	private String storeName;//店铺名称
	
	
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
	public int getApplyId() {
		return applyId;
	}
	public void setApplyId(int applyId) {
		this.applyId = applyId;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public int getCustomerExpect() {
		return customerExpect;
	}
	public void setCustomerExpect(int customerExpect) {
		this.customerExpect = customerExpect;
	}
	public String getCustomerExpectName() {
		return customerExpectName;
	}
	public void setCustomerExpectName(String customerExpectName) {
		this.customerExpectName = customerExpectName;
	}
	public int getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(int serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public String getServiceStatusName() {
		return serviceStatusName;
	}
	public void setServiceStatusName(String serviceStatusName) {
		this.serviceStatusName = serviceStatusName;
	}
	public String getCustomerPin() {
		return customerPin;
	}
	public void setCustomerPin(String customerPin) {
		this.customerPin = customerPin;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getCustomerGrade() {
		return customerGrade;
	}
	public void setCustomerGrade(int customerGrade) {
		this.customerGrade = customerGrade;
	}
	public String getCustomerTel() {
		return customerTel;
	}
	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}
	public String getPickwareAddress() {
		return pickwareAddress;
	}
	public void setPickwareAddress(String pickwareAddress) {
		this.pickwareAddress = pickwareAddress;
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
	public String getOrderTypeName() {
		return orderTypeName;
	}
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	public BigDecimal getActualPayPrice() {
		return actualPayPrice;
	}
	public void setActualPayPrice(BigDecimal actualPayPrice) {
		this.actualPayPrice = actualPayPrice;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public int getWareType() {
		return wareType;
	}
	public void setWareType(int wareType) {
		this.wareType = wareType;
	}
	public String getWareTypeName() {
		return wareTypeName;
	}
	public void setWareTypeName(String wareTypeName) {
		this.wareTypeName = wareTypeName;
	}
	public String getWareName() {
		return wareName;
	}
	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	public String getApprovePin() {
		return approvePin;
	}
	public void setApprovePin(String approvePin) {
		this.approvePin = approvePin;
	}
	public String getApproveName() {
		return approveName;
	}
	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}
	public String getApproveTime() {
		return approveTime;
	}
	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}
	public int getApproveResult() {
		return approveResult;
	}
	public void setApproveResult(int approveResult) {
		this.approveResult = approveResult;
	}
	public String getApproveResultName() {
		return approveResultName;
	}
	public void setApproveResultName(String approveResultName) {
		this.approveResultName = approveResultName;
	}
	public String getProcessPin() {
		return processPin;
	}
	public void setProcessPin(String processPin) {
		this.processPin = processPin;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessTime() {
		return processTime;
	}
	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}
	public int getProcessResult() {
		return processResult;
	}
	public void setProcessResult(int processResult) {
		this.processResult = processResult;
	}
	public String getProcessResultName() {
		return processResultName;
	}
	public void setProcessResultName(String processResultName) {
		this.processResultName = processResultName;
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

package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class Aftermarket {
	
	private int applyId;//���뵥��
	private int serviceId;//���񵥺�
	private String applyTime;//����ʱ��
	private int customerExpect;//�ͻ�����
	private String customerExpectName;//�ͻ���������
	private int serviceStatus;//����״̬
	private String serviceStatusName;//����״̬����
	private String customerPin;//�ͻ��˺�
	private String customerName;//�ͻ�����
	private int customerGrade;//�û�����
	private String customerTel;//�û��绰
	private String pickwareAddress;//ȡ����ַ
	private String orderId;//������
	private int orderType;//��������
	private String orderTypeName;//������������
	private BigDecimal actualPayPrice;//ʵ�����
	private String skuId;//��Ʒ���
	private int wareType;//��Ʒ����
	private String wareTypeName;//��Ʒ��������
	private String wareName;//��Ʒ����
	private String approvePin;//������˺�
	private String approveName;//���������
	private String approveTime;//���ʱ��
	private int approveResult;//��˽��
	private String approveResultName;//��˽������
	private String processPin;//�������˺�
	private String processName;//����������
	private String processTime;//����ʱ��
	private int processResult;//������
	private String processResultName;//����������
	private int isAudit;//���ؿ����״̬
	private String ecId;//����ƽ̨���
	private String storeId;//���̱��
	private String auditHint;//�����ʾ 
	private String storeName;//��������
	
	
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

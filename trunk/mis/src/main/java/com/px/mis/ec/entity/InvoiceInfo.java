package com.px.mis.ec.entity;

public class InvoiceInfo {
	private int id;
	private String platId;
	private String shopId;
	private String orderId;
	private String invoiceType;//��Ʊ����
	private String invoiceTitle;//��Ʊ̧ͷ
	private String invoiceContentId;//��Ʊ����
	private String invoiceConsigneeEmail;//��Ʊ��ϵ����
	private String invoiceConsigneePhone;//��Ʊ��ϵ�绰
	private String invoiceCode;//��Ʊ��˰��ʶ���
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlatId() {
		return platId;
	}
	public void setPlatId(String platId) {
		this.platId = platId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getInvoiceTitle() {
		return invoiceTitle;
	}
	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}
	public String getInvoiceContentId() {
		return invoiceContentId;
	}
	public void setInvoiceContentId(String invoiceContentId) {
		this.invoiceContentId = invoiceContentId;
	}
	public String getInvoiceCode() {
		return invoiceCode;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public String getInvoiceConsigneeEmail() {
		return invoiceConsigneeEmail;
	}
	public void setInvoiceConsigneeEmail(String invoiceConsigneeEmail) {
		this.invoiceConsigneeEmail = invoiceConsigneeEmail;
	}
	public String getInvoiceConsigneePhone() {
		return invoiceConsigneePhone;
	}
	public void setInvoiceConsigneePhone(String invoiceConsigneePhone) {
		this.invoiceConsigneePhone = invoiceConsigneePhone;
	}
	

}

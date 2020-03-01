package com.px.mis.ec.dao;

import java.util.List;

import com.px.mis.ec.entity.InvoiceInfo;

public interface InvoiceDao {
	public void insert(List<InvoiceInfo> invoicelist);
	
	public void delete(String orderId);

}

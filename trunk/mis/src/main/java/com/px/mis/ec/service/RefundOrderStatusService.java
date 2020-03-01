package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.RefundOrderStatus;

public interface RefundOrderStatusService {

	public String add(RefundOrderStatus refundOrderStatus);
	
	public String edit(RefundOrderStatus refundOrderStatus);
	
	public String delete(String refStatusId);
	
	public String query(String refStatusId);
	
	public String queryList(Map map);
	
}

package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.OrderStatus;

public interface OrderStatusService {

	public String add(OrderStatus orderStatus);
	
	public String edit(OrderStatus orderStatus);
	
	public String delete(String orderStatusId);
	
	public String query(String orderStatusId);
	
	public String queryList(Map map);
}

package com.px.mis.system.dao;

import com.px.mis.system.entity.OrderNo;

public interface OrderNoDao {

	public void insert(OrderNo orderNo);
	
	public void update(OrderNo orderNo);
	
	public OrderNo select(OrderNo orderNo);
	
}

package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.OrderStatus;

public interface OrderStatusDao  {

	public void insert(OrderStatus orderStatus);
	
	public void update(OrderStatus orderStatus);
	
	public void delete(String orderStatusId);
	
	public OrderStatus select(String orderStatusId);
	
	public List<OrderStatus> selectList(Map map);
	
	public int selectCount(Map map);
	
}

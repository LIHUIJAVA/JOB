package com.px.mis.ec.dao;

import java.util.List;

import com.px.mis.ec.entity.RefundOrders;

public interface RefundOrdersDao  {

	public void insertList(List<RefundOrders> refundOrdersList);
	
	public List<RefundOrders> selectList(String refId);
	
	public void delete(String refId);
	
	public List<RefundOrders> selectListGroupByInvidAndBatch(String refId);
	
}

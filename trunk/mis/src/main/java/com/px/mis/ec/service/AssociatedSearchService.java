package com.px.mis.ec.service;

import java.util.Map;

public interface AssociatedSearchService {
	public String quickSearchByOrderId(String orderId);//关联查询；
	
	public String reverseOperationByOrderId(String orderId);//逆向操作；
	
	public String orderAuditByOrderId(String orderId,String accNum,String loginDate);//订单审核；
	
	public String orderAbandonAuditByOrderId(String orderId,String accNum);//订单弃审；
	
	public String selectList(Map map);
	
}

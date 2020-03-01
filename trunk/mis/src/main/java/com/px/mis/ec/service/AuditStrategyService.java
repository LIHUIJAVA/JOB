package com.px.mis.ec.service;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;

public interface AuditStrategyService {

	public String insert(String requestString);
	
	public String delete(String requestString);
	
	public String update(String requestString);
	
	public String selectList(Map map);
	
	public String findById(String requestString);
	
	public boolean autoAuditCheck(PlatOrder order,List<PlatOrders> orders);
}

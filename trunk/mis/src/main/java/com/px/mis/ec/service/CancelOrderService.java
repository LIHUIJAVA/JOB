package com.px.mis.ec.service;

import java.util.List;

import com.px.mis.ec.entity.CancelOrder;

public interface CancelOrderService {

	public int audit(List<CancelOrder> cancelOrders,String accNum,String storeId);
	
	public String queryList(String jsonBody);
	
	public int download(String storeId,String ecorderId,String accNum,String startDate,String endDate);
	
}

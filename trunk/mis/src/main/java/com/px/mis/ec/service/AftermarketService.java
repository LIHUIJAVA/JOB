package com.px.mis.ec.service;

import java.util.List;

import com.px.mis.ec.entity.Aftermarket;

public interface AftermarketService {

	public int audit(List<Aftermarket> aftermarkets,String accNum,String storeId);
	
	public String queryList(String jsonBody);
	
	public int download(String storeId,String accNum,String orderId,String startDate,String endDate);
	
	public String downloadByStoreId(String storeId);
}

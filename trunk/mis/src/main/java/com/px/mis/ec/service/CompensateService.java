package com.px.mis.ec.service;

import java.util.List;

import com.px.mis.ec.entity.Compensate;

public interface CompensateService {

	public int audit(List<Compensate> compensates,String accNum,String storeId);
	
	public String queryList(String jsonBody);
	
	public int download(String storeId,String ecOrderId,String accNum,String startDate,String endDate);
	
}

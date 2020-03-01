package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.BusinessType;

public interface BusinessTypeService {

	public String add(BusinessType businessType);
	
	public String edit(BusinessType businessType);
	
	public String delete(String brokId);
	
	public String query(String brokId);
	
	public String queryList(Map map);
}

package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.ECPlatform;

public interface ECPlatformService {

	public String add(ECPlatform ecPlatform);
	
	public String edit(ECPlatform ecPlatform);
	
	public String delete(String brokId);
	
	public String query(String brokId);
	
	public String queryList(Map map);
	
	public String queryAll(String jsonBody);
}

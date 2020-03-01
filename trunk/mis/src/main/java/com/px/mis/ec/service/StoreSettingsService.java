package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.StoreSettings;

public interface StoreSettingsService {

	public String edit(StoreSettings storeSetting);
	
	public String queryList(Map map);
}

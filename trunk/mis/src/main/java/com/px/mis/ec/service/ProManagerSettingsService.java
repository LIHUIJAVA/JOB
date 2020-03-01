package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.ProManagerSettings;

public interface ProManagerSettingsService {

	public String edit(ProManagerSettings proManagerSettings);
	
	public String query(String settingId);
}

package com.px.mis.ec.dao;

import com.px.mis.ec.entity.ProManagerSettings;

public interface ProManagerSettingsDao {

	public void update(ProManagerSettings proManagerSettings);
	
	public ProManagerSettings select(String settingId);
}

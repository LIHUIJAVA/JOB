package com.px.mis.ec.dao;

import com.px.mis.ec.entity.SpecialSettings;

public interface SpecialSettingsDao {

	public void update(SpecialSettings specialSettings);
	
	public SpecialSettings select(String settingId);
}

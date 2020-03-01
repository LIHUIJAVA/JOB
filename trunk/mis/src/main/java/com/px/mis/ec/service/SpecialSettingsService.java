package com.px.mis.ec.service;

import com.px.mis.ec.entity.SpecialSettings;

public interface SpecialSettingsService {
	
	public String edit(SpecialSettings specialSettings);

	public String query(String settingId);
	
}

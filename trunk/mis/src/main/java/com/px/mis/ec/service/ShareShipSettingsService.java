package com.px.mis.ec.service;

import com.px.mis.ec.entity.ShareShipSettings;

public interface ShareShipSettingsService {
	
	public String edit(ShareShipSettings shareShipSettings);

	public String query(String settingId);
	
}

package com.px.mis.ec.dao;

import com.px.mis.ec.entity.ShareShipSettings;

public interface ShareShipSettingsDao {

	public void update(ShareShipSettings shareShipSettings);
	
	public ShareShipSettings select(String settingId);
}

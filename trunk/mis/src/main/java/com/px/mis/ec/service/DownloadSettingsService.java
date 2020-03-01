package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.DownloadSettings;

public interface DownloadSettingsService {

	public String add(DownloadSettings downloadSettings);
	
	public String edit(DownloadSettings downloadSettings);
	
	public String selectList(Map map);
}

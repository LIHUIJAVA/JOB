package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.DownloadSettings;

public interface DownloadSettingsDao {

	public void insert(DownloadSettings downloadSettings);
	
	public void update(DownloadSettings downloadSettings);
	
	public List<DownloadSettings> selectList(Map map);
	
	public int selectCount(Map map);
	
	public DownloadSettings selectById(int id);
	
	public void delete(String storeId);
}

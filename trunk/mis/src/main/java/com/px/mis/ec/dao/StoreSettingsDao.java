package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.StoreSettings;


public interface StoreSettingsDao {

	public void insert(StoreSettings storeSetting);
	
	public void update(StoreSettings storeSetting);
	
	public void delete(String storeId);
	
	public StoreSettings select(String storeId);
	
	public List<StoreSettings> selectList(Map map);
	
	public int selectCount(Map map);
	
	public List<Map<String, String>> selectRelevantEC();
	
}

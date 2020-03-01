package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.StoreRecord;

public interface StoreRecordDao  {

	public void insert(StoreRecord storeRecord);
	
	public void update(StoreRecord storeRecord);
	
	public void delete(String storeId);
	
	public StoreRecord select(String storeId);
	
	public List<StoreRecord> selectList(Map map);
	
	public List<StoreRecord> selectAll();
	
	public int selectCount(Map map);
	
	public List<StoreRecord> exportList(Map map);
	
}

package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.ECPlatform;

public interface ECPlatformDao  {

	public void insert(ECPlatform ecPlatform);
	
	public void update(ECPlatform ecPlatform);
	
	public void delete(String ecId);
	
	public ECPlatform select(String ecId);
	
	public List<ECPlatform> selectList(Map map);
	
	public List<Map<String, String>> selectAll();
	
	public int selectCount(Map map);
	
}

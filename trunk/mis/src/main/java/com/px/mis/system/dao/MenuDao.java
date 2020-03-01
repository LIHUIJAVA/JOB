package com.px.mis.system.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.system.entity.Menu;

public interface MenuDao {
	
	public void insert(Menu menu);

	public int delete(String id);
	
	public void update(Menu menu);
	
	public Menu select(String id);
	
	public List<Menu> selectList(Map map);
	
	public int selectCount(Map map);
	
	public List<Menu> selectAll();
}

package com.px.mis.system.service;

import java.util.List;
import java.util.Map;

import com.px.mis.system.entity.Menu;

public interface MenuService {

	public String add(Menu menu);
	
	public String edit(Menu menu);
	
	public String editList(List<Menu> menu);
	
	public String delete(String id);
	
	public String query(String id);
	
	public String queryList(Map map);
	
	public String menuTree(String jsonBody);
	
	public String editMenuTree(String jsonBody);
	
	public String roleMenuList(String roleId);
	
	public String userMenuList(String userId);
}

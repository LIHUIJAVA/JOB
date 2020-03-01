package com.px.mis.system.dao;

import java.util.List;

import com.px.mis.system.entity.Menu;
import com.px.mis.system.entity.RoleMenu;

public interface RoleMenuDao {
	
	public void insert(List<RoleMenu> rmList);
	
	public void delete(String id);
	
	public List<Menu> selectRoleMenu(String id);
	
}

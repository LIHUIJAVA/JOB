package com.px.mis.system.dao;

import java.util.List;

import com.px.mis.system.entity.Menu;
import com.px.mis.system.entity.UserMenu;

public interface UserMenuDao {
	
	public void insert(List<UserMenu> umList);
	
	public void delete(String id);
	
	public List<Menu> selectUserMenu(String id);
	
	public void deleteByRoleId(String roleId);
	
}

package com.px.mis.system.service;

import java.util.List;
import java.util.Map;

import com.px.mis.system.entity.Role;
import com.px.mis.system.entity.RoleMenu;

public interface RoleService {

	public String add(Role role);
	
	public String edit(String role);
	
	public String delete(String id);
	
	public String query(String id);
	
	public String queryList(Map map);
	
	public String permAss(String jsonBody);
	
	public String queryAll();
	
}

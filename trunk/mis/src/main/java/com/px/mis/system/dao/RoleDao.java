package com.px.mis.system.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.system.entity.Role;

public interface RoleDao {

	public void insert(Role role);
	
	public void delete(List<String> id);
	
	public void update(List<Role> role);
	
	public Role select(String id);
	
	public List<Role> selectList(Map map);
	
	public int selectCount(Map map);
	
	public List<Role> selectAll();
	
}

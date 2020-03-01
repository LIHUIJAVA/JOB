package com.px.mis.system.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.dao.RoleDao;
import com.px.mis.system.dao.RoleMenuDao;
import com.px.mis.system.dao.UserMenuDao;
import com.px.mis.system.entity.Menu;
import com.px.mis.system.entity.MisUser;
import com.px.mis.system.entity.Role;
import com.px.mis.system.entity.RoleMenu;
import com.px.mis.system.entity.UserMenu;
import com.px.mis.system.service.RoleService;
import com.px.mis.system.util.MenuTree;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private RoleMenuDao roleMenuDao;
	
	@Autowired
	private MisUserDao misUserDao;
	
	@Autowired
	private UserMenuDao userMenuDao;

	@Override
	public String add(Role role) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (roleDao.select(role.getId()) != null) {
			message = "编号" + role.getId() + "已存在，请重新输入！";
			isSuccess = false;
		} else {
			roleDao.insert(role);
			message = "新增成功！";
			isSuccess = true;
		}

		try {
			resp = BaseJson.returnRespObj("system/role/add", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String edit(String role) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
		List<Role> roleList = new ObjectMapper().readValue(
				BaseJson.getReqBody(role).get("list").toString(), new TypeReference<List<Role>>() {
				});
		if (roleList != null && roleList.size() > 0) {
			message = "批量修改成功！";
			isSuccess = true;
			roleDao.update(roleList);
		} else {
			message = "修改失败！";
			isSuccess = false;
		}

		
			resp = BaseJson.returnRespObj("system/role/edit", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
	 * @return List集合
	 */
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			System.out.println(str[i] + "=====impl 的 str[" + i + "]");
			list.add(str[i]);
		}
		return list;
	}

	@Override
	public String delete(String id) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<String> jjk=getList(id);
		List<String> list = new ArrayList<>();
		for(String ii:jjk) {
			if (roleDao.select(ii) == null) {
				message += "删除失败，编号" + ii + "不存在！";
				isSuccess = false;
			} else {
				list.add(ii);
			}
		}
		if(list.size() == 0) {
			message = "删除失败,未获取到删除数据";
			isSuccess = false;
		} else {
			roleDao.delete(jjk);
			message = "删除成功！";
			isSuccess = true;
		}
		

		try {
			resp = BaseJson.returnRespObj("system/role/del", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String query(String id) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Role role = roleDao.select(id);
		if (role != null) {
			message = "查询成功！";
		} else {
			isSuccess = false;
			message = "编号" + id + "不存在！";
		}

		try {
			resp = BaseJson.returnRespObj("system/role/query", isSuccess, message, role);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String resp = "";
		List<Role> roleList = roleDao.selectList(map);
		int count = roleDao.selectCount(map);
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = roleList.size();
		int pages = count / pageSize + 1;
		try {
			resp = BaseJson.returnRespList("system/role/queryList", true, "查询成功！", count, pageNo, pageSize, listNum,
					pages, roleList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String permAss(String jsonBody) {
		String resp = "";
		try {
			String roleId = BaseJson.getReqBody(jsonBody).get("roleId").asText();
			String menuIdStr = BaseJson.getReqBody(jsonBody).get("menuId").asText();
			String[] menuIds = menuIdStr.split(",");
			List<String> menuIdList = Arrays.asList(menuIds);
			
			Map<String, String> map = new HashMap<>();
			map.put("roleId", roleId);
			List<MisUser> userList = misUserDao.selectAll(map);
			List<UserMenu> userMenuList = new ArrayList<>();

			List<RoleMenu> rmList = new ArrayList<>();
			for (String menuId : menuIdList) {
				RoleMenu roleMenu=new RoleMenu();
				roleMenu.setRoleId(roleId);
				roleMenu.setMenuId(menuId);
				rmList.add(roleMenu);
				for (MisUser user : userList) {
					UserMenu userMenu= new UserMenu();
					userMenu.setUserId(user.getAccNum());
					userMenu.setMenuId(menuId);
					userMenuList.add(userMenu);
				}
			}
			roleMenuDao.delete(roleId);
			if (rmList.size()>0) {
				roleMenuDao.insert(rmList);
			}
			
			userMenuDao.deleteByRoleId(roleId);
			if (userMenuList.size()>0) {
				userMenuDao.insert(userMenuList);
			}
			
			resp = BaseJson.returnRespObj("system/role/permAss", true, "菜单权限分配成功！", null);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return resp;
	}

	@Override
	public String queryAll() {
		String resp = "";
		List<Role> roleList = roleDao.selectAll();
		try {
			resp = BaseJson.returnRespList("system/role/queryAll", true, "查询成功！", roleList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
